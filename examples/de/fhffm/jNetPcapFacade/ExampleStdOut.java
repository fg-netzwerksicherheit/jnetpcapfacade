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

import de.fhffm.jNetPcapFacade.sniffer.processor.StdoutPacketProcessor;
import de.fhffm.jNetPcapFacade.sniffer.util.PcapHelper;
import de.fhffm.jNetPcapFacade.sniffer.util.PcapStartupHelper;

/**
 * Shows the most simple way to start and stop the sniffer
 * @author Denis
 *
 */
public class ExampleStdOut {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		//List all devices
		String[] alldevices = PcapHelper.listDevices().split("\n");
		String device = alldevices[alldevices.length-1];
		System.out.println("Used Interface: " + device);
		//Create the StartupHelper to start the sniffer
		PcapStartupHelper sniffer = new PcapStartupHelper(device);
		//The stdoutProcessor will print IP and Mac-Addresses
		//!Derive your own PacketProcessor to do something else!
		sniffer.addProcessor(new StdoutPacketProcessor());
		sniffer.start();
		Thread.sleep(10000);
		sniffer.stop();
	}

}
