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

package de.fhffm.jNetPcapFacade.helper;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhffm.jNetPcapFacade.config.Constants;
import de.fhffm.jNetPcapFacade.config.TestConstants;
import de.fhffm.jNetPcapFacade.sniffer.processor.StdoutPacketProcessor;
import de.fhffm.jNetPcapFacade.sniffer.util.PcapStartupHelper;
import de.fhffm.jNetPcapFacade.tcp.Client;
import de.fhffm.jNetPcapFacade.tcp.Server;

public class PcapHelperTests {

    private Server server;

    @BeforeClass
    public static void init() {

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
    public void startStopPcapStartupHelper() throws InterruptedException {
    	PcapStartupHelper helper = new PcapStartupHelper(Constants.LOOPBACK_DEVICE);
    	helper.addProcessor(new StdoutPacketProcessor() );
    	helper.start();
        Thread.sleep(TestConstants.CAPTURE_DELAY);
        helper.stop();
    }

    @Test
    public void usePcapStartupHelper() throws InterruptedException {
    	
    	PcapStartupHelper helper = new PcapStartupHelper(Constants.LOOPBACK_DEVICE);
    	ProcessorMockup mock = new ProcessorMockup();
    	helper.addProcessor(mock);
    	helper.start();

        Client client = new Client();
        client.startClient();
        Thread.sleep(TestConstants.CAPTURE_DELAY);

        helper.stop();
        
        assertFalse(mock.packetCount == 0);
        
    }
}
