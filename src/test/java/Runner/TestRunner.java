package Runner;

import java.io.File;
import org.junit.runner.RunWith;
import com.cucumber.listener.Reporter;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;

// tags are the hooks which specifies which tests need to be run
//glue mentions where exactly my stepdefinition resides
//under features we can specify the features folder or individual feature file
//plugin I have mentioned extent report plugin.
@RunWith(Cucumber.class)
@CucumberOptions(tags = { "@Test" }, glue = { "stepDefinition" }, features = "./src/test/resources/Features", plugin = {
		"com.cucumber.listener.ExtentCucumberFormatter:reports/report.html" })

//This runner class acts as a bridge between Feature file and step definition
public class TestRunner {

	//This writeExtentReport method is run after completion of all the test runs.
	@AfterClass
	public static void writeExtentReport() {
		//loadXMLConfig loads our report.xml file.
		//report.xml mentions some details we want to mention for our tests.We need worry about the entire xml
		Reporter.loadXMLConfig(new File("src/test/resources/Config/report.xml"));

	}

}
