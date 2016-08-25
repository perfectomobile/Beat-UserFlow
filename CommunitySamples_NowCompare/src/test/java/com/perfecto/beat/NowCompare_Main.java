package com.perfecto.beat;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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


public class NowCompare_Main {

    RemoteWebDriver  driver;
    ReportiumClient reportiumClient;

    String PERFECTO_HOST        = System.getProperty("np.testHost", "branchtest.perfectomobile.com");
    String PERFECTO_USER        = System.getProperty("np.testUsername", "test_automation@gmail.com");
    String PERFECTO_PASSWORD    = System.getProperty("np.testPassword", "Test_automation");



    private String url = "https://www.nowcompare.com/";
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
    @Test (invocationCount = 1, successPercentage = 20)
    public void test(){
        try{
            System.out.println("-----------------------------------------");
            System.err.println(Thread.currentThread().getId());
            System.out.println("-----------------------------------------");

            //START TEST
//
            WebElement element = null;
            WebDriverWait wait = new WebDriverWait(driver, 15);

            String device = driver.getCapabilities().getCapability("platformName").toString();
            reportiumClient.testStart("NowCompare" , new TestContext("Some tag" , "NowCompare -"+ device)); //Add tags by your choice.
            reportiumClient.testStep("Navigate to NowCompare site"); //TEST STEP - Open site and navigate.

            driver.get(url);

            // Maximize browser window on desktop
            String platform = (String) driver.getCapabilities().getCapability("platformName").toString();
            System.out.println(platform);
            if(platform.equalsIgnoreCase("Windows")){
                driver.manage().window().maximize();
            }


            reportiumClient.testStep("Verify visibility"); //TEST STEP - verify visibility
            if(platform.equalsIgnoreCase("Windows")){
                //In desktop select Trade directly from top menu - just verify visibility and...
                try {
                    wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath(NowComparePageObjects.healthInsurance)));
                } catch (TimeoutException t) {
                    System.out.println("Could not find element");
                }
            }  else {
                //Verify visibility of hamburger menu
                try {
                    wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath(NowComparePageObjects.navbarToggle)));
                } catch (TimeoutException t) {
                    System.out.println("Could not find element");
                }

                //then open hamburger menu and take screenshot of menu...
                element = driver.findElementByXPath(NowComparePageObjects.navbarToggle);
                element.click();

                try {
                    wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath(NowComparePageObjects.healthInsurance)));
                } catch (TimeoutException t) {
                    System.out.println("Could not find element");
                }

            }

            reportiumClient.testStep("Find Health Insurance element"); //TEST STEP - Find Health Insurance element
            element = driver.findElementByXPath(NowComparePageObjects.healthInsurance);
            element.click();
            System.out.println("Clicked +Health");

            reportiumClient.testStep("Verify visibility of Health insurance page"); //TEST STEP - Verify visibility of Health insurance page
            try {
                wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath(NowComparePageObjects.compareExpatHealthInsurance)));
            } catch (TimeoutException t) {
                System.out.println("Could not find element");
            }

            reportiumClient.testStep("Fill in the quote request form"); //TEST STEP - Fill in the quote request form
            element = driver.findElementByXPath(NowComparePageObjects.iconNumber);
            element.click();
            element = driver.findElementByXPath(NowComparePageObjects.fPersonOption6);
            element.click();
            element = driver.findElementByXPath(NowComparePageObjects.iconLocation);
            element.click();
            element = driver.findElementByXPath(NowComparePageObjects.unitedKingdom);
            element.click();
            element = driver.findElementByXPath(NowComparePageObjects.iconRequirements);
            element.click();
            element = driver.findElementByXPath(NowComparePageObjects.dentalCover);
            if ((! element.isSelected()) && element.isEnabled())
                element.click();
            element = driver.findElementByXPath(NowComparePageObjects.maternityCover);
            if ((! element.isSelected()) && element.isEnabled())
                element.click();
            element = driver.findElementByXPath(NowComparePageObjects.getQuotes);
            element.click();

            reportiumClient.testStep("Verify visibility of insured people page"); //TEST STEP - Verify visibility of insured people page
            try {
                wait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath(NowComparePageObjects.yourDetails)));
            } catch (TimeoutException t) {
                System.out.println("Could not find element");
            }

            WebElement  yourDetails = driver.findElementByXPath(NowComparePageObjects.yourDetails);
            isElementVisible(NowComparePageObjects.yourDetails);
            Assert.assertTrue(yourDetails.getText().equals("Your Details"));



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