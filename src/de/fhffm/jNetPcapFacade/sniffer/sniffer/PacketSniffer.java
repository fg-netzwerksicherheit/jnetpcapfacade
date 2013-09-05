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

package de.fhffm.jNetPcapFacade.sniffer.sniffer;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacketHandler;

import de.fhffm.jNetPcapFacade.sniffer.util.PcapHelper;

/**
 * 
 * The sniffer thread in which the pcap capture loop is run.
 * 
 * @author Ruediger Gad
 * @author Robin Müller-Bady
 * 
 */
public class PacketSniffer extends Thread {
	private Logger log = LogManager.getLogger(PacketSniffer.class);
    private final Pcap pcap;
    private final PcapPacketHandler<Object> handler;

    public PacketSniffer(Pcap pcap, PcapPacketHandler<Object> handler) {
        this.pcap = pcap;
        this.handler = handler;
        // setDaemon(true);
    }

    public synchronized void stopSniffer() {
        if (isAlive()) {
        	log.debug("Sniffer started...");
            pcap.breakloop();
            /*
             * According to the jNetPcap docs the sniffer may process at least
             * one more packet. This causes the sniffer to stay in loop until it
             * got another packet. So we inject one dummy packet to force the
             * sniffer to leave the loop. To make sure that this packet is not
             * filtered we explicitly set a filter that accepts all packets.
             */
            PcapHelper.setFilter(pcap, "");
            pcap.inject(new byte[] { 0 });
            try {
                wait();
            } catch (InterruptedException e) {
            	
            }
            log.debug("Sniffer stopped");
        }
    }

    @Override
    public void run() {
    	log.debug("Starting pcap loop");
        pcap.loop(Pcap.LOOP_INFINITE, handler, null);
        log.debug("Pcap loop terminated. Closing pcap.");
        pcap.close();
        synchronized (this) {
            notify();
        }
    }

}
