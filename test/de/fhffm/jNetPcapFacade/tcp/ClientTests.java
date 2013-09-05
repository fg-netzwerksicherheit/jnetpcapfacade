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

package de.fhffm.jNetPcapFacade.tcp;

import java.io.IOException;

import org.junit.Test;

/**
 * 
 * Test the simple &quot;dummy&quot; client.
 * 
 * @author Ruediger Gad
 *
 */
public class ClientTests {

    @Test
    public void connectClient() throws IOException, InterruptedException{
        Server server = new Server();
        server.startServer();
        
        Client client = new Client();
        client.startClient();
        
        server.stopServer();
    }
}
