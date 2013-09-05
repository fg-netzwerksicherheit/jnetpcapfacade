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

package de.fhffm.jNetPcapFacade.config;

import java.io.File;

/**
 * 
 * @author Ruediger Gad
 * 
 */
public class TestConstants {
    public static final int TCP_TEST_PORT = 12345;
    public static final int CAPTURE_DELAY = 300;

    public static final String TEST_LOG4J_PROPERTIES = TestConstants.class
            .getPackage().getName().replace(".", File.separator)
            + File.separator + "log4j.properties";
    
    public static final String JMS_TEST_TOPIC = "testtopic";
    
    public static final int WAIT_TIMEOUT = 10000;
}
