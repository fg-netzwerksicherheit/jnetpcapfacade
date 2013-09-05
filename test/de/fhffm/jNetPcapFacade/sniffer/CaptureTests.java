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

package de.fhffm.jNetPcapFacade.sniffer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhffm.jNetPcapFacade.config.Constants;
import de.fhffm.jNetPcapFacade.config.TestConstants;
import de.fhffm.jNetPcapFacade.sniffer.util.NativeLibraryLoader;
import de.fhffm.jNetPcapFacade.sniffer.util.PcapHelper;
import de.fhffm.jNetPcapFacade.tcp.Client;
import de.fhffm.jNetPcapFacade.tcp.Server;

/**
 * 
 * Basic capture tests. The primary goal of these tests is to verify that our
 * jnetpcap packet capture and helper classes/methods are working.
 * 
 * @author Ruediger Gad
 * 
 */
public class CaptureTests {
    private Server server;
    private Pcap pcap;
    private Tcp tcp = new Tcp();

    @BeforeClass
    public static void init() {
    	NativeLibraryLoader.loadNativeLibs();
    }

    @Before
    public void setup() throws IOException {
        server = new Server();
        server.startServer();

        pcap = PcapHelper.createAndActivatePcap(Constants.LOOPBACK_DEVICE);
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        server.stopServer();
        pcap.close();
    }

    @Test
    public void captureTest() throws InterruptedException {
        Client client = new Client();
        client.startClient(TestConstants.CAPTURE_DELAY);

        /*
         * Break the loop on the first packet we receive.
         */
        int ret = pcap.loop(Pcap.LOOP_INFINITE,
                new PcapPacketHandler<Object>() {
                    @Override
                    public void nextPacket(PcapPacket arg0, Object arg1) {
                        pcap.breakloop();
                    }
                }, null);

        /*
         * As we break the loop we check the return value if this was correctly
         * done.
         */
        assertEquals(Pcap.ERROR_BREAK, ret);
    }

    @Test
    public void captureTcpSyn() throws InterruptedException {
        Client client = new Client();
        client.startClient(TestConstants.CAPTURE_DELAY);

        /*
         * Break the loop when we receive a TCP packet with the SYN flag set.
         */
        int ret = pcap.loop(Pcap.LOOP_INFINITE,
                new PcapPacketHandler<Object>() {
                    @Override
                    public void nextPacket(PcapPacket p, Object arg1) {
                        if (p.hasHeader(tcp)
                                && tcp.destination() == TestConstants.TCP_TEST_PORT
                                && tcp.flags_SYN()) {
                            pcap.breakloop();
                        }
                    }
                }, null);

        /*
         * As we break the loop we check the return value if this was correctly
         * done.
         */
        assertEquals(Pcap.ERROR_BREAK, ret);
    }

}
