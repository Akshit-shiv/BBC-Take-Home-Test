package common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.logging.log4j.core.config.Configurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Utilities {

	public static JSONParser parser = new JSONParser();
	public static JSONObject jsonObject;
	public static FileReader reader;

	static {
		Configurator.initialize(null, "./src/test/resources/Config/log4j2.xml");
	}

	public static Object getService(String key) throws IOException, ParseException {
		return getConfigJson().get(key);
	}

	public static String getValue(String key) throws IOException, ParseException {
		return (String) getConfigJson().get(key);
	}

	public static JSONObject getConfigJson() throws IOException, ParseException {
		try {
			reader = new FileReader("./src/test/resources/Config/Config.json");
			jsonObject = (JSONObject) parser.parse(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
