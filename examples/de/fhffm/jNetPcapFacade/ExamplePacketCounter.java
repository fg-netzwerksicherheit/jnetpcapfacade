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

package de.fhffm.jNetPcapFacade;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacketHandler;

import de.fhffm.jNetPcapFacade.config.Constants;
import de.fhffm.jNetPcapFacade.sniffer.data.Packet;
import de.fhffm.jNetPcapFacade.sniffer.handler.FullPacketHandler;
import de.fhffm.jNetPcapFacade.sniffer.processor.PacketProcessor;
import de.fhffm.jNetPcapFacade.sniffer.processor.QueueEater;
import de.fhffm.jNetPcapFacade.sniffer.sniffer.PacketSniffer;
import de.fhffm.jNetPcapFacade.sniffer.util.PcapHelper;

/**
 * This Example shows how to create a custom Packet Processor
 * and creates the sniffer manually without StartupHelper
 * @author Denis Hock
 *
 */
public class ExamplePacketCounter {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		//Create a Queue
		BlockingQueue<Packet> queue = new LinkedBlockingQueue<Packet>();
		//Create a Packethandler
        PcapPacketHandler<Object> handler = new FullPacketHandler(queue);
        
        //Start Sniffer on Loopback-Device
        //Not working on Windows 8 (there is no Loopback)
        Pcap pcap = PcapHelper.createAndActivatePcap(Constants.LOOPBACK_DEVICE);
        PacketSniffer sniffer = new PacketSniffer(pcap, handler);
        sniffer.start();
        
        //Create our custom Processor to do something with the packets
        QueueEater forwarder = new QueueEater(queue);
        CustomPacketProcessor processor = new CustomPacketProcessor();
        forwarder.addProcessors(processor);
        forwarder.start();
        
        //Sleep 10 Seconds
        Thread.sleep(10000);

        //Stop everything
        sniffer.stopSniffer();
        forwarder.stopQueueEating();
	}
}

/**
 * You can derive your custom Processor to get Access to
 * Packets
 * @author Denis Hock
 *
 */
class CustomPacketProcessor implements PacketProcessor{
	private int packetcount = 0;
	
	@Override
	public void forwardPacket(Packet packet) {
		packetcount++;
	}

	public int getPacketcount() {
		return packetcount;
	}
	
}
