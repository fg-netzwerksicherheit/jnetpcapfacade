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

package de.fhffm.jNetPcapFacade.sniffer.handler;

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

import de.fhffm.jNetPcapFacade.config.TestConstants;
import de.fhffm.jNetPcapFacade.sniffer.data.Packet;
import de.fhffm.jNetPcapFacade.sniffer.handler.FullPacketHandler;
import de.fhffm.jNetPcapFacade.sniffer.handler.TcpSynFinPacketHandler;
import de.fhffm.jNetPcapFacade.sniffer.util.NativeLibraryLoader;
import de.fhffm.jNetPcapFacade.sniffer.util.PcapHelper;
import de.fhffm.jNetPcapFacade.tcp.Client;
import de.fhffm.jNetPcapFacade.tcp.Server;

/**
 * 
 * Tests for testing the handlers. Note that it is assumed that no further
 * traffic is happening on the interfaces. Else the assertions might fail.
 * 
 * @author Ruediger Gad
 * 
 */
public class HandlerTests {

    private Server server;
    private Pcap pcap;

    @BeforeClass
    public static void init() {
        NativeLibraryLoader.loadNativeLibs();
    }

    @Before
    public void setup() throws IOException {
        server = new Server();
        server.startServer();

        pcap = PcapHelper.createAndActivatePcap();
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        server.stopServer();
        pcap.close();
    }

    @Test
    public void simpleQueueHandlerTest() throws InterruptedException {
        Queue<Packet> queue = new LinkedBlockingQueue<Packet>();
        PcapPacketHandler<Object> handler = new FullPacketHandler(queue);

        Client client = new Client();
        client.startClient(TestConstants.CAPTURE_DELAY);

        pcap.loop(2, handler, null);

        client.join();

        assertEquals(2, queue.size());
    }

    // @Test
    // public void filterHandlerTest() throws InterruptedException {
    // Queue<PcapPacket> queue = new LinkedBlockingQueue<PcapPacket>();
    // PcapPacketHandler<Object> handler = new QueuePacketHandler(queue);
    //
    // /*
    // * TODO: For some unknown reason the pcap.loop hangs after breakloop
    // * when a filter is set. At least it takes very long for the pcap.loop
    // * call to return after breakloop.
    // */
    // PcapHelper.setFilter(pcap, "tcp[tcpflags] & tcp-fin != 0");
    //
    // Client client = new Client();
    // client.startClient(TestConstants.CAPTURE_DELAY, 2);
    //
    // Thread stopper = new Thread() {
    // @Override
    // public void run() {
    // try {
    // sleep(5 * TestConstants.CAPTURE_DELAY);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // pcap.breakloop();
    // };
    // };
    // stopper.start();
    //
    // pcap.loop(Pcap.LOOP_INFINITE, handler, null);
    //
    // assertEquals(2, queue.size());
    // }

    @Test
    public void tcpSynFinHandlerTest() throws InterruptedException {
        Queue<Packet> queue = new LinkedBlockingQueue<Packet>();
        PcapPacketHandler<Object> handler = new TcpSynFinPacketHandler(queue);

        Client client = new Client();
        client.startClient(TestConstants.CAPTURE_DELAY);

        Thread stopper = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3 * TestConstants.CAPTURE_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pcap.breakloop();
            };
        };
        stopper.start();

        pcap.loop(Pcap.LOOP_INFINITE, handler, null);

        assertEquals(4, queue.size());
    }

}
