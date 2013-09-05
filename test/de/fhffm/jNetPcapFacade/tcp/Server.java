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
import java.net.ServerSocket;
import java.net.Socket;

import de.fhffm.jNetPcapFacade.config.TestConstants;

/**
 * 
 * Simple test server which simply accepts new connections and closes these
 * immediately.
 * 
 * @author Ruediger Gad
 * 
 */
public class Server extends Thread {

    private ServerSocket serverSocket;
    private boolean running = true;

    public Server() throws IOException {
        serverSocket = new ServerSocket(TestConstants.TCP_TEST_PORT);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket s = serverSocket.accept();
                s.close();
            } catch (IOException e) {
                System.out.println("Server socket was closed.");
            }
        }
    }

    public void stopServer() throws IOException, InterruptedException {
        running = false;
        serverSocket.close();
        join();
    }

    public void startServer() {
        start();

        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
