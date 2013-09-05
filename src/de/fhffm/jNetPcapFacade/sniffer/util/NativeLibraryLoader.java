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

import java.io.File;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.fhffm.jNetPcapFacade.config.Constants;

/**
 * 
 * Loads native jnetpcap libraries. Currently only Linux and Windows (both x86
 * and x86_64) are supported.
 * 
 * @author Ruediger Gad
 * @author Robin Müller-Bady
 */
public class NativeLibraryLoader {
	private static Logger log = LogManager.getLogger(NativeLibraryLoader.class);
    private static final String ARCH = System.getProperty(Constants.SYSTEM_PROPERTY_ARCH).toLowerCase();
    private static final String OS = new StringTokenizer(System.getProperty(Constants.SYSTEM_PROPERTY_OS)).nextToken().toLowerCase();
    private static final String LIB_PREFIX = OS.equals(Constants.OS_VALUE_WINDOWS) ? Constants.PREFIX_LIBRARAY_WINDOWS : Constants.PREFIX_LIBRARAY_LINUX;
    private static final String LIB_SUFFIX = OS.equals(Constants.OS_VALUE_WINDOWS) ? Constants.SUFFIX_LIBRARAY_WINDOWS : Constants.SUFFIX_LIBRARAY_LINUX;
    private static final String CURRENT_JAR_DIR = new File(NativeLibraryLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
    private static boolean loaded = false;

    public static void loadNativeLibs() {
        if (!loaded) {
        	String version080 = buildStringForVersion(Constants.PCAP_LIB_080);
        	System.load(version080);
        	log.debug("Loaded native library: " + version080);
        	String version100 = buildStringForVersion(Constants.PCAP_LIB_100);
            System.load(version100);
        	log.debug("Loaded native library: " + version100);
            loaded = true;
        }
    }
    
    private static String buildStringForVersion(String version){
    	/*
    	 * Example: 
    	 * /home/robin/workspace_git/jNetPcapFacade/jnetpcap/native/linux/amd64/libjnetpcap.so
    	 * -> {/home/robin/workspace_git/jNetPcapFacade} / {jnetpcap/native} / {linux} / {amd64} / {lib} {jnetpcap} {.so}
    	 */
    	StringBuilder sb = new StringBuilder();
    	sb.append(CURRENT_JAR_DIR).append(File.separator);
    	sb.append(Constants.NATIVE_JNETPCAP_PATH).append(File.separator);
    	sb.append(OS).append(File.separator);
    	sb.append(ARCH).append(File.separator);
    	sb.append(LIB_PREFIX);
    	sb.append(version);
    	sb.append(LIB_SUFFIX);
    	return sb.toString();
    }
}
