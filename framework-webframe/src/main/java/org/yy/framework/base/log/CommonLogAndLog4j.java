package org.yy.framework.base.log;

import org.slf4j.bridge.SLF4JBridgeHandler;

public class CommonLogAndLog4j {

//	private static final Log log = LogFactory.getLog(CommonLogAndLog4j.class);
//	private static final Logger log4jLogger = Logger.getLogger(CommonLogAndLog4j.class);
//	private static final java.util.logging.Logger jdkLogger = java.util.logging.Logger
//			.getLogger(CommonLogAndLog4j.class.getName());
//
//	private static final org.slf4j.Logger slf4jLogger = org.slf4j.LoggerFactory.getLogger(CommonLogAndLog4j.class);
//

	public static void installJulToSlf4jBridge() {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

}