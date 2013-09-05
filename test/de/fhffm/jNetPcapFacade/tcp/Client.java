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


import java.net.Inet4Address;
import java.net.Socket;

import de.fhffm.jNetPcapFacade.config.TestConstants;

/**
 * 
 * Simple client intended to be used with the test server. The client simply
 * establishes and immediately closes a TCP connection.
 * 
 * @author Ruediger Gad
 * 
 */
public class Client extends Thread {

    private int wait = 0;
    private int count = 1;

    @Override
    public void run() {
        for (int i = 1; i <= count; i++) {
            if (wait > 0) {
                try {
                    sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Socket socket = new Socket(Inet4Address.getLocalHost(),
                        TestConstants.TCP_TEST_PORT);

                if (!socket.isConnected()) {
                	socket.close();
                    throw new RuntimeException("Failed to connect socket!");
                }

                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void startClient() throws InterruptedException {
        start();
        join();
    }

    /**
     * Defer connection setup.
     * 
     * @param Time
     *            to wait in milliseconds.
     */
    public void startClient(int wait) throws InterruptedException {
        this.wait = wait;
        startClient();
    }

    public void startClient(int wait, int count) throws InterruptedException {
        this.count = count;
        startClient(wait);
    }

}
