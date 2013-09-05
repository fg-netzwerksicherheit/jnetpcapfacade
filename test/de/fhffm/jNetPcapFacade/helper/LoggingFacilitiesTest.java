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

package de.fhffm.jNetPcapFacade.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoggingFacilitiesTest {
	private Logger log = LogManager.getLogger(LoggingFacilitiesTest.class);
	@BeforeClass
	public static void init(){
		
	}
	
	@Test
	public void logSomething(){
		/*
		 * Just to see whether log4j is correct included
		 */
		log.info("Info Nummer 1 !");
		log.error("Error Nummer 1 !");
		log.debug("Debug Nummer 1 !");
		log.fatal("Fatal Nummer 1 !");
		log.trace("Trace Nummer 1 !");
		log.warn("Warn Nummer 1 !");
	}

}
