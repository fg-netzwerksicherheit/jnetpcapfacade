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

package de.fhffm.jNetPcapFacade.sniffer.processor;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.packet.PcapPacket;

import de.fhffm.jNetPcapFacade.sniffer.data.Packet;

/**
 * 
 * Abstract base class for &quot;forwarder&quot;. This class reads packets from
 * the queue and calls the abstract
 * {@link PacketProcessor#forwardPacket(PcapPacket)} method for each packet
 * read from the queue. Subclasses must implement this method to process the
 * packets further in this method.
 * 
 * @author Ruediger Gad
 * @author Robin Müller-Bady
 * 
 */
public class QueueEater extends Thread {
	private Logger log = LogManager.getLogger(QueueEater.class);
    private BlockingQueue<Packet> queue;
    private List<PacketProcessor> processors = new ArrayList<PacketProcessor>();
    protected boolean running = true;

    public QueueEater(BlockingQueue<Packet> queue) {
        this.queue = queue;
        setDaemon(true);
    }
    
    public void addProcessors(PacketProcessor proc) {
    	log.debug("Added processor: " + proc.getClass().getName());
    	processors.add(proc);
    }

    public void removeProcessors(PacketProcessor proc) {
    	log.debug("Removed processor: " + proc.getClass().getName());
    	processors.remove(proc);
    }
    
    @Override
    public void run() {
        while (running) {
            try {
            	Packet packet = queue.take();
            	for (PacketProcessor proc : processors) {
            		proc.forwardPacket(packet);
            	}
            } catch (InterruptedException e) {
            	log.debug("Interrupted while waiting for queue elements");
            }
        }
    }

    public synchronized void stopQueueEating() throws InterruptedException {
        running = false;
        interrupt();
        join();
    }

}
