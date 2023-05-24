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

	//this block is executed first than any other thing within the framework when the tests are run.This just says to initialize the log4j2.xml file that is defined under config
	static {
		Configurator.initialize(null, "./src/test/resources/Config/log4j2.xml");
	}

	//This method takes key as parameter of type String and returns a Object
	public static Object getService(String key) throws IOException, ParseException {
		//getConfigJson() function returns the entire jsonobject defined under config.json file
		//By saying getConfigJson().get(key); I am just indicating to get the object by key(MediaService).This will now return  {"getEndPoint": "/api/ottplatform/media"}
		return getConfigJson().get(key);
	}

	//This method takes key as parameter of type String and returns a string
	public static String getValue(String key) throws IOException, ParseException {
		//getConfigJson() function returns the entire jsonobject defined under config.json file
		//By saying getConfigJson().get(key); I am just indicating to get the object by key(BaseUrl).Since this is an object I am typecasting it to string
		return (String) getConfigJson().get(key);
	}
//getconfigJSON method is used to return contents of config.json as a json object 
	public static JSONObject getConfigJson() throws IOException, ParseException {
		//To read the json file I am using FileReader
		//To parse through Json I am using JSONParser.we pass the reader object as an argument to this parser.This is include under try catch block since this should give right stack trace when the test fails.
		try {
			reader = new FileReader("./src/test/resources/Config/Config.json");
			jsonObject = (JSONObject) parser.parse(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//The below line returns entire json defined under config.json
		return jsonObject;
	}
}
