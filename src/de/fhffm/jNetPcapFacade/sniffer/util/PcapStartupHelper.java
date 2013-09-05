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

package de.fhffm.jNetPcapFacade.sniffer.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacketHandler;

import de.fhffm.jNetPcapFacade.sniffer.data.Packet;
import de.fhffm.jNetPcapFacade.sniffer.handler.TcpSynFinPacketHandler;
import de.fhffm.jNetPcapFacade.sniffer.processor.PacketProcessor;
import de.fhffm.jNetPcapFacade.sniffer.processor.QueueEater;
import de.fhffm.jNetPcapFacade.sniffer.sniffer.PacketSniffer;
import de.fhffm.jNetPcapFacade.config.Constants;

/**
 * @author Johannes
 * @author Robin Müller-Bady
 *
 */
public class PcapStartupHelper {
	private static Logger log = LogManager.getLogger(PcapStartupHelper.class);
    private BlockingQueue<Packet> queue;
    private PcapPacketHandler<Object> handler;
    private Pcap pcap;
    private PacketSniffer sniffer;
    private QueueEater eater;
    private boolean initialized = false;
    private boolean forwarding = false;
    private String device = Constants.LOOPBACK_DEVICE;
    
    public PcapStartupHelper(String device) {
    	this.init(device);
    }
    
    private void init(String device){
    	NativeLibraryLoader.loadNativeLibs();
    	log.debug("Used device for sniffing: " + device);
    	//Prepare pcap stuff
    	this.device = device;
    	queue = new LinkedBlockingQueue<Packet>();
        handler = new TcpSynFinPacketHandler(queue);
        pcap = PcapHelper.createAndActivatePcap(this.device);
        if(pcap != null){
        	//Create Eater and Sniffer
        	sniffer = new PacketSniffer(pcap, handler);
            eater = new QueueEater(queue);
            this.initialized = true;
        }
    }
    
    /**
     * Starts sniffing and forwarding on specified device
     * @throws InterruptedException
     */
    public void start() throws InterruptedException{
    	this.stop(); 
    	if( this.isInitialized() && this.isForwarding() == false ){
    		sniffer.start();
            eater.start();
            this.forwarding = true;
            log.debug("Started sniffing...");
            log.debug("Started eating...");
    	}
    }
    
    /**
     * Stops sniffing and forwarding
     * @throws InterruptedException
     */
    public void stop() throws InterruptedException{
    	if( this.isForwarding()){
            sniffer.stopSniffer();
            eater.stopQueueEating();
            this.forwarding = false;
            log.debug("Stopped sniffing...");
            log.debug("Stopped eating...");
    	}
    }
    
    /**
     * Adds processor to List of Listeners
     * @param processor the processor to add
     */
    public void addProcessor(PacketProcessor processor){
    	eater.addProcessors(processor);
    }
    
    /**
     * Removes processor from List of Listeners
     * @param processor the processor to remove
     */
    public void removeProcessor(PacketProcessor processor){
    	eater.removeProcessors(processor);
    }

	/**
	 * Initialization-State
	 * @return True if successfully initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Forwarding-State
	 * @return True if currently sniffing and forwarding
	 */
	public boolean isForwarding() {
		return forwarding;
	}

}
