/**
 * Copyright (c) 2013 JNetPcapFacade
 * 
 * This file is part of JNetPcapFacade.
 * JNetPcapFacade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JNetPcapFacade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JNetPcapFacade. If not, see <http://www.gnu.org/licenses/>.
 */

package de.fhffm.jNetPcapFacade.sniffer.sniffer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Queue;
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
import de.fhffm.jNetPcapFacade.sniffer.handler.FullPacketHandler;
import de.fhffm.jNetPcapFacade.sniffer.handler.HandlerTests;
import de.fhffm.jNetPcapFacade.sniffer.handler.TcpSynFinPacketHandler;
import de.fhffm.jNetPcapFacade.sniffer.sniffer.PacketSniffer;
import de.fhffm.jNetPcapFacade.sniffer.util.NativeLibraryLoader;
import de.fhffm.jNetPcapFacade.sniffer.util.PcapHelper;
import de.fhffm.jNetPcapFacade.tcp.Client;
import de.fhffm.jNetPcapFacade.tcp.Server;

/**
 * 
 * Basic packet sniffer tests. The focus of these tests is on the
 * {@link PacketSniffer} not the handlers. The handlers are tested seperatly in
 * {@link HandlerTests}. Some of the tests here assume that no traffic except
 * the test traffic happens on the loopback interface.
 * 
 * @author Ruediger Gad
 * 
 */
public class PacketSnifferTests {

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
    public void createPacketSniffer() {
        Queue<Packet> queue = new LinkedBlockingQueue<Packet>();
        PcapPacketHandler<Object> handler = new TcpSynFinPacketHandler(queue);

        Pcap pcap = PcapHelper.createAndActivatePcap(Constants.LOOPBACK_DEVICE);
        new PacketSniffer(pcap, handler);
    }

    @Test
    public void startStopPacketSniffer() throws InterruptedException {
        Queue<Packet> queue = new LinkedBlockingQueue<Packet>();
        PcapPacketHandler<Object> handler = new FullPacketHandler(queue);

        Pcap pcap = PcapHelper.createAndActivatePcap(Constants.LOOPBACK_DEVICE);
        PacketSniffer sniffer = new PacketSniffer(pcap, handler);
        sniffer.start();
        sniffer.stopSniffer();
    }

    @Test
    public void tcpSynFinSniffTest() throws InterruptedException {
        Queue<Packet> queue = new LinkedBlockingQueue<Packet>();
        PcapPacketHandler<Object> handler = new TcpSynFinPacketHandler(queue);

        Pcap pcap = PcapHelper.createAndActivatePcap(Constants.LOOPBACK_DEVICE);
        PacketSniffer sniffer = new PacketSniffer(pcap, handler);
        sniffer.start();

        Client client = new Client();
        client.startClient();
        Thread.sleep(TestConstants.CAPTURE_DELAY);

        sniffer.stopSniffer();

        /*
         * 1 TCP Connection ->
         * SYN + SYN/ACK + FIN + FIN/ACK == 4 Packets
         */
        assertEquals(4, queue.size());
    }

}
