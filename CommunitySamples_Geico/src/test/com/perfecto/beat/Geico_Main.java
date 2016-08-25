package com.perfecto.beat;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;
import java.util.*;


public class Geico_Main {

    RemoteWebDriver  driver;
    ReportiumClient reportiumClient;

    String PERFECTO_HOST        = System.getProperty("np.testHost", "branchtest.perfectomobile.com");
    String PERFECTO_USER        = System.getProperty("np.testUsername", "test_automation@gmail.com");
    String PERFECTO_PASSWORD    = System.getProperty("np.testPassword", "Test_automation");


    
    private String url = "http://geico.com";
    //TODO: Insert your device capabilities at testng.XML file.
    @Parameters({"platformName" , "model" , "browserName" , "location","platformVersion","browserVersion"})
    @BeforeTest
    public void beforeMethod(String platformName, String model, String browserName, String location, @Optional String platformVersion, @Optional String browserVersion) throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("user" , PERFECTO_USER);
        capabilities.setCapability("password" , PERFECTO_PASSWORD);
        capabilities.setCapability("platformName" , platformName);
        capabilities.setCapability("model" , model);
        capabilities.setCapability("browserName" , browserName);
        capabilities.setCapability("location" , location);
        capabilities.setCapability("platformVersion" , platformVersion);
        capabilities.setCapability("browserVersion" , browserVersion);
        capabilities.setCapability("operabilityRatingScore", 100);
        driver = new RemoteWebDriver(new URL("https://" + PERFECTO_HOST + "/nexperience/perfectomobile/wd/hub") , capabilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        //Create Reportium client.
        reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(
                        new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                        .withProject(new Project("Sample Selenium-Reportium" , "1.0"))
                        .withContextTags("Regression") //Optional
                        .withWebDriver(driver) //Optional
                        .build());
    }

    //Control the number of times each one of the tests runs.
    //Each times generates a new report for each test.
    //TODO: set the number of times to run the test
    @Test (invocationCount = 5, successPercentage = 20)
       public void test(){
        try{
        	System.out.println("-----------------------------------------");
        	System.err.println(Thread.currentThread().getId());
        	System.out.println("-----------------------------------------");

            //START TEST
//
        	String device = driver.getCapabilities().getCapability("platformName").toString();
            reportiumClient.testStart("Geico" , new TestContext("Some tag" , "Geico-"+ device)); //Add tags by your choice. 
            reportiumClient.testStep("Navigate to Geico site"); //TEST STEP - Open site and navigate.
      
            driver.get(url);

        	// Maximize browser window on desktop
            String platform = (String) driver.getCapabilities().getCapability("platformName").toString();
            System.out.println(platform);
            if(platform.equalsIgnoreCase("Windows")){
               driver.manage().window().maximize();
            }

            // Enter form data
            reportiumClient.testStep("Find Insurance Type"); //TEST STEP - find insurance type
            Select type = new Select(driver.findElement(By.id("insurancetype")));
            if(platform.equalsIgnoreCase("Windows")){
                driver.findElementByXPath(GeicoPageObject.MotorcycleOption2).click();
            } else {
                type.selectByVisibleText("Motorcycle");
            }

            reportiumClient.testStep("Find ZIP, click Submit"); //TEST STEP - find ZIP
            driver.findElementByXPath(GeicoPageObject.ZIP).sendKeys("01434");
            driver.findElement(By.id("submitButton")).click();

            isElementVisible(GeicoPageObject.autoInsurance);
            System.out.println(url);

 			reportiumClient.testStep("Set Radio buttons, 1-Yes; 2-No. Set No"); //TEST STEP - Set Radio buttons, 1-Yes; 2-No
 			driver.findElementByXPath(GeicoPageObject.autoInsurance).click();
 			
 			reportiumClient.testStep("Fill in First name, Last Name and Street"); //TEST STEP - Fill in First name, Last Name and Street
 			driver.findElementByXPath(GeicoPageObject.firstName).sendKeys("MyFirstName");
 			driver.findElementByXPath(GeicoPageObject.lastName).sendKeys("MyFamilyName");
 			driver.findElementByXPath(GeicoPageObject.street).sendKeys("My Address");

 			reportiumClient.testStep("Fill in BirthMonth, BirthDay and BirthYear"); //TEST STEP - Fill in BirthMonth, BirthDay and BirthYear
 			driver.findElementByXPath(GeicoPageObject.birthMonth).sendKeys("8");
 			driver.findElementByXPath(GeicoPageObject.birthDay).sendKeys("3");
 			driver.findElementByXPath(GeicoPageObject.birthYear).sendKeys("1981");
 			driver.findElementByXPath(GeicoPageObject.submitContinue).click();

 			reportiumClient.testStep("Find HasCycle-Error"); //TEST STEP - Find HasCycle-Error
 			driver.findElementByXPath(GeicoPageObject.hasCycleError).isDisplayed();

 			reportiumClient.testStep("Select Yes - Do you currently have motorcycle insurance"); //TEST STEP - Select Yes
 			Select hasCycle = new Select(driver.findElementByXPath(GeicoPageObject.hasCycle));
 			hasCycle.selectByIndex(1);

 		 	reportiumClient.testStep("Select current company"); //TEST STEP - Select current company
            Select current = new Select(driver.findElement(By.id("currentInsurance")));
            current.selectByVisibleText("Other");
            driver.findElement(By.id("btnSubmit")).click();

 			reportiumClient.testStep("Is ElementVisible - Location Header"); //TEST STEP - Is ElementVisible - Location Header
 			WebElement  locationHeader = driver.findElementByXPath(GeicoPageObject.locationHeader);
 			isElementVisible(GeicoPageObject.locationHeader);
 			Assert.assertTrue(locationHeader.getText().equals("Motorcycle Details"));
 			//System.out.println("Done geico.com\n");

           //END TEST - Success
            reportiumClient.testStop(TestResultFactory.createSuccess());

        }catch (Throwable t){
            t.printStackTrace();
            reportiumClient.testStop(TestResultFactory.createFailure(t.getMessage() , t));
            Assert.fail(t.getMessage());
        }

    }

    public boolean isElementVisible(String by) {
		 By xpath = By.xpath(by);
		    try {
		        if (driver.findElement(xpath).isDisplayed()) {
		        	//System.out.println("Element is Displayed: " + by);
		        	Reporter.log("Element is Displayed: ");
		            return true;
		        }
		    }
		    catch(Exception e) {
		    	System.out.println("Element is Not Displayed: " + by);
		    	Reporter.log("Element is Not Displayed: " + by);
		        return false;
		    }
		    return false;
	 }



    @AfterTest
    public void afterMethod(){
  	
        try{
            driver.close();
            String platform = (String) driver.getCapabilities().getCapability("platformName").toString();
            if (platform.equalsIgnoreCase("Windows")){
            Map<String, Object> params = new HashMap<String, Object>();
			driver.executeScript("mobile:execution:close", params);
			}
            
            driver.quit();
            String reportURL = reportiumClient.getReportUrl();
            System.out.println(reportURL); //Print URL to console

            //TODO: Enable this couple of lines in order to open the browser with the report at the end of the test.
            //if(Desktop.isDesktopSupported())
              //  Desktop.getDesktop().browse(new URI(reportURL));

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}