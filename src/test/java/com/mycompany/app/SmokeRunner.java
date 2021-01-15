package com.mycompany.app;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
		plugin = {"pretty","json:target/cucumberSmoke.json"},
		tags = {"@smoke"},
		features = "src/test/resources/com/mycompany/app/"
		)
public class SmokeRunner {

}
