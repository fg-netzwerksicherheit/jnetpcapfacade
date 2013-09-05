/**
 * Copyright (c) 2013 JNetPcapFacade
 * 
 * This file is part of JNetPcapFacade.
 * JNetPcapFacade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JNetPcapFacade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JNetPcapFacade. If not, see <http://www.gnu.org/licenses/>.
 */

package de.fhffm.jNetPcapFacade.sniffer.processor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacketHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhffm.jNetPcapFacade.config.Constants;
import de.fhffm.jNetPcapFacade.config.TestConstants;
import de.fhffm.jNetPcapFacade.sniffer.data.Packet;
import de.fhffm.jNetPcapFacade.sniffer.handler.TcpSynFinPacketHandler;
import de.fhffm.jNetPcapFacade.sniffer.processor.QueueEater;
import de.fhffm.jNetPcapFacade.sniffer.processor.StdoutPacketProcessor;
import de.fhffm.jNetPcapFacade.sniffer.sniffer.PacketSniffer;
import de.fhffm.jNetPcapFacade.sniffer.util.NativeLibraryLoader;
import de.fhffm.jNetPcapFacade.sniffer.util.PcapHelper;
import de.fhffm.jNetPcapFacade.tcp.Client;
import de.fhffm.jNetPcapFacade.tcp.Server;

/**
 * 
 * Simple tests for {@link StdoutPacketProcessor}. Note that it is assumed that
 * no further traffic is happening on the loopback interface. Else the
 * assertions might fail.
 * 
 * @author Ruediger Gad
 * 
 */
public class StdoutPacketProcessorTests {

    private Server server;

    @BeforeClass
    public static void init() {
        NativeLibraryLoader.loadNativeLibs();
    }

    @Before
    public void setup() throws IOException {
        server = new Server();
        server.startServer();
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        server.stopServer();
    }

    @Test
    public void startStopStdoutPacketForwarder() throws InterruptedException {
        BlockingQueue<Packet> queue = new LinkedBlockingQueue<Packet>();

        QueueEater forwarder = new QueueEater(queue);
        forwarder.addProcessors(new StdoutPacketProcessor());
        forwarder.start();
        forwarder.stopQueueEating();
    }

    @Test
    public void useStdoutPacketForwarder() throws InterruptedException {
        BlockingQueue<Packet> queue = new LinkedBlockingQueue<Packet>();
        PcapPacketHandler<Object> handler = new TcpSynFinPacketHandler(queue);

        Pcap pcap = PcapHelper.createAndActivatePcap(Constants.LOOPBACK_DEVICE);
        PacketSniffer sniffer = new PacketSniffer(pcap, handler);
        sniffer.start();

        QueueEater forwarder = new QueueEater(queue);
        forwarder.addProcessors(new StdoutPacketProcessor());
        forwarder.start();

        Client client = new Client();
        client.startClient();
        Thread.sleep(TestConstants.CAPTURE_DELAY);

        sniffer.stopSniffer();
        forwarder.stopQueueEating();

        /*
         * TODO:
         * Ursprünglich wurde die queue in den damaligen *Forwarder Klassen abgebaut.
         * Nun geschieht dies im QueueEater und die Verarbeitung geschieht in den *PacketProcessors.
         * Daher sollte dieser Test auch in zwei aufgespalten werden:
         * Einer für den QueueEater und einer für den StdoutPacketProcessor.
         */
        assertEquals(0, queue.size());
    }
}
