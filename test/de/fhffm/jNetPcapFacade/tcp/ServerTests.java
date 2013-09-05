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

package de.fhffm.jNetPcapFacade.tcp;

import java.io.IOException;

import org.junit.Test;

/**
 * 
 * Tests for &quot;dummy&quot; server.
 * 
 * @author Ruediger Gad
 *
 */
public class ServerTests {

    @Test
    public void createServer() throws IOException, InterruptedException{
        Server server = new Server();
        server.stopServer();
    }
    
    @Test
    public void startStopServer() throws IOException, InterruptedException{
        Server server = new Server();
        
        server.startServer();
        
        server.stopServer();
    }
}
