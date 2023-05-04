package common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

	private static Logger logger = LogManager.getLogger(Log.class);

	public static void endTestCase(String scenarioName, String status) {
		logger.info("Execution of scenario: " + scenarioName + " has " + status);
		logger.info(
				"-------------------------------------------------------------------------------------------------------------------");
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void warn(String message) {
		logger.warn(message);
	}

	public static void error(String message) {
		logger.error(message);
	}

	public static void fatal(String message) {
		logger.fatal(message);
	}

	public static void debug(String message) {
		logger.debug(message);
	}
}
