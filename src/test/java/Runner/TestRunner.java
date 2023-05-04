package Runner;

import java.io.File;
import org.junit.runner.RunWith;
import com.cucumber.listener.Reporter;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;

@RunWith(Cucumber.class)
@CucumberOptions(tags = { "@e2e" }, glue = { "stepDefinition" }, features = "./src/test/resources/Features", plugin = {
		"com.cucumber.listener.ExtentCucumberFormatter:reports/report.html" })

public class TestRunner {

	@AfterClass
	public static void writeExtentReport() {
		Reporter.loadXMLConfig(new File("src/test/resources/Config/report.xml"));

	}

}
