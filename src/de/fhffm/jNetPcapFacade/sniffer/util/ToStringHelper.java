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

package de.fhffm.jNetPcapFacade.sniffer.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Helper methods for converting things to string representation. Please note
 * that these methods are only here for convenience and have not been optimized
 * in any way. In fact most implementations are that bruteforce that you do not
 * want to use these in performance critical code paths!
 * 
 * @author Ruediger Gad
 * @author Robin Müller-Bady
 * 
 */
public class ToStringHelper {
	private static Logger log = LogManager.getLogger(ToStringHelper.class);
    public static String IpToString(byte[] addr) {
        try {
            return InetAddress.getByAddress(addr).toString().substring(1);
        } catch (UnknownHostException e) {
            log.error("Error tranforming '" + addr + "' to String.");
        	return "error";
        }
    }
    
    public static String EthernetToString(byte[] addr){
        String ret = "" + String.format("%1$02X", addr[0]);
        for (int i = 1; i < addr.length; i++) {
            ret += ":" + String.format("%1$02X", addr[i]);
        }
        return ret;
    }
}
