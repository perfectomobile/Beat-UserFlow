package com.perfecto.testing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.exception.ReportiumException;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import com.perfecto.testing.utils.Utils;

public class WebScenarioParallel {
	RemoteWebDriver driver;
	ReportiumClient reportiumClient;

	// Create Remote WebDriver based on testng.xml configuration
	@Parameters({"platformName", "deviceName", "platformVersion", "browserName", "browserVersion", "screenResolution", "persona"})
	@BeforeTest
	public void beforeTest(String platformName, String deviceName, String platformVersion, String browserName, String browserVersion,
						   String screenResolution, String persona) throws MalformedURLException {
			driver = Utils.getRemoteWebDriver(platformName, platformVersion, browserName, browserVersion, screenResolution, deviceName, persona);
		reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(
				new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
						.withProject(new Project("Sample Selenium-Reportium" , "1.0"))
						.withContextTags("Regression") //Optional
						.withWebDriver(driver) //Optional
						.build());

	}

	// Test Method, navigate to google and perform search

	// Test Method, navigate to Pokemon and download native app for iOS/Android
	@Test
	public void pokemonGOTest() throws MalformedURLException {
		reportiumClient.testStart("Pokemon-Go test" , new TestContext("Some tag" , "Pokemon-go")); //Add tags by your choice.
		reportiumClient.testStep("Step 1 - Navigate to the Pokemon Go Site"); //TEST STEP - Open site and navigate.

		System.out.println("Step 1 - Navigate to the Pokemon Go Site");
		String siteURL = "http://www.pokemongo.com/en-us/";
		driver.get(siteURL);
		reportiumClient.testStep("Verify - Pokemon Go Site is displayed");
		System.out.println("Verify - Pokemon Go Site is displayed");
		Assert.assertTrue(driver.getCurrentUrl().equals(siteURL), "Expected to be on site " + siteURL + " but actually on site " + driver.getCurrentUrl());
		Assert.assertTrue(driver.findElement(By.xpath("//*[@class='header--logo']")).isDisplayed(), "Pokemon Go logo not displayed on the page");
		reportiumClient.testStep("Check device platform");
		if (driver.getCapabilities().getCapability("platformName").toString().equals("Windows")) {
			System.out.println("Nothing further to do for Windows Web test");
		} else {
			reportiumClient.testStep("Step 2 - Click on the App or Play store logo in mobile webview");
			System.out.println("Step 2 - Click on the App or Play store logo in mobile webview");
			if (driver.getCapabilities().getCapability("platformName").toString().equals("iOS")) {
				driver.findElement(By.xpath("//*[contains(@class, 'apple btn-appstore-toggle-by-platform visible')]")).click();
			} else if (driver.getCapabilities().getCapability("platformName").toString().equals("Android")) {
				driver.findElement(By.xpath("//*[contains(@class, 'google btn-appstore-toggle-by-platform visible')]")).click();
			}

			System.out.println("Modal window warning is presented");
			Assert.assertTrue(driver.findElement(By.xpath("//*[@class='modal--content']//*[contains(text(), " +
					"'You are about to leave a site operated by')]")).isDisplayed(), "Modal window not displayed after clicking to go to Store");
			reportiumClient.testStep("Clicking Continue to proceed to the store");
			System.out.println("Clicking Continue to proceed to the store");
			if (driver.getCapabilities().getCapability("platformName").toString().equals("iOS")) {
				jsClick(driver.findElement(By.xpath("//*[contains(@class, \"btn-primary\")]")));
				clickHomeButton();
				System.out.println("opening app store");
				openApp("App Store");
			} else {
				jsClick(driver.findElement(By.xpath("//*[contains(text(), \"Continue\")]")));
				clickHomeButton();
				suspend(2000);
			}

			reportiumClient.testStep("Step 3 - Download and Wait for app to be installed");
			System.out.println("Step 3 - Download and Wait for app to be installed");
			//app already installed

			reportiumClient.testStep("Step 4 - Launch the App");
			System.out.println("Step 4 - Launch the App");
			openApp("Pokémon GO");
			suspend(10000);

			reportiumClient.testStep("Step 5 - Accept any device permissions popups");
			System.out.println("Step 5 - Accept any device permissions popups");
			if (driver.getCapabilities().getCapability("platformName").toString().equals("iOS")) {
				//set capability to auto accept alerts so no need to do it here
			} else if (driver.getCapabilities().getCapability("platformName").toString().equals("Android")) {
				//android phone pre-6.0 doesn't ask for permissions. Try to accept with OCR command
				clickButtonText("Allow");
			}

		}
		System.out.println("Done. Pokemon Go!");
		reportiumClient.testStop(TestResultFactory.createSuccess());
	}

	@AfterTest
	public void afterTest() throws IOException {
		driver.close();
		System.out.println(driver.getCapabilities().getCapability("platformName").toString() + " report URL: "
				+ driver.getCapabilities().getCapability("windTunnelReportUrl").toString());
		driver.quit();
		String reportURL = reportiumClient.getReportUrl();
		System.out.println(reportURL); //Print URL to console
	}


	public void jsClick(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	protected void clickHomeButton() {
		String command = "mobile:presskey";
		Map<String, Object> params = new HashMap<>();
		params.put("keySequence", "HOME");
		try {
			driver.executeScript(command, params);
		} catch (WebDriverException e) { }
	}

	protected void openApp(String appName) {
		String command = "mobile:application:open";
		Map<String, Object> params = new HashMap<>();
		params.put("name", appName);
		try{
			driver.executeScript(command, params);
		} catch (WebDriverException e) { }
	}

	protected void clickButtonText(String buttonText) {
		String command = "mobile:button-text:click";
		Map<String, Object> params = new HashMap<>();
		params.put("label", buttonText);
		try{
			driver.executeScript(command, params);
		} catch (WebDriverException e) { }
	}

	protected void suspend(int millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {

		}
	}
}
