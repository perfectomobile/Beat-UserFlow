package test.java.com.perfecto.beat;


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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
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

public class OptimizerElementFind {

    RemoteWebDriver  driver;
    ReportiumClient reportiumClient;

    String PERFECTO_HOST        = System.getProperty("np.testHost", "branchtest");
    String PERFECTO_USER        = System.getProperty("np.testUsername", "ronma@perfectomobile.com");
    String PERFECTO_PASSWORD    = System.getProperty("np.testPassword", "Welcome2306");

    private String url = "http://optimizer-beat-test.perfectomobile.com/";
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
        driver.manage().timeouts().implicitlyWait(15 , TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
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
        	//START TEST
        	String device = driver.getCapabilities().getCapability("platformName").toString();
            reportiumClient.testStart("Optimizer - Test" , new TestContext("Functions test" , "Optimizer-"+ device)); //Add tags by your choice.

            reportiumClient.testStep("Navigate to Beat-Optimizer site"); //TEST STEP - Open site and navigate.
            System.out.println("Loading optimizer site");
            driver.get(url);

            Cookie cookie = new Cookie("PM_MARKETO","123");
            driver.manage().addCookie(cookie);
            System.out.println("Inject cookie");

            driver.get(url);
            System.out.println("Refresh the page");

            System.out.println("Pressing select country button - by class");
            driver.findElementByClassName(OptimizerObject.selectCountry).click(); // by class

            System.out.println("Platform name:" + driver.getCapabilities().getCapability("platformName").toString());
            reportiumClient.testStep("Select country - by xpath"); //TEST STEP - select country
            System.out.println("Selecting Germany - by xpath");
            selectCountry("Germany");
            driver.findElementByXPath(OptimizerObject.closeButton).click();
            isCountryDisplayed("Germany"); //Check the country that was selected appears in main page

            reportiumClient.testStep("Select device type"); //select device type
            driver.findElementByCssSelector(OptimizerObject.typeSelect).click();
            //SelectDeviceType("Tablet");
            reportiumClient.testStep("Select device OS"); //select device OS
            SelectOS("iOS");

            reportiumClient.testStep("'Full Picture' button appears on screen");
            WebElement FullRBtn = driver.findElementById(OptimizerObject.fullPicButton);
            isElementVisible(OptimizerObject.FullResultBtn);
            FullRBtn.click();
            reportiumClient.testStep("Test Coverage Optimizer full report page"); //new page opened with table
            String TableTitle = driver.findElement(By.xpath(OptimizerObject.FullResultPage)).getText();
            Assert.assertEquals(TableTitle, "TEST COVERAGE OPTIMIZER");
            System.out.println();
            reportiumClient.testStep("Trying to to compare between the titles");
            Assert.assertEquals(driver.getTitle(), "Digital Test Coverage Optimizer by Perfecto Mobile");
            System.out.println("The titles are identical - Digital Test Coverage Optimizer by Perfecto Mobile");
            System.out.println();
            System.out.println();
            System.out.println("-----------------------------------------------------------------------------------------------------------------");
            //END TEST - Success
            reportiumClient.testStop(TestResultFactory.createSuccess());

        }catch (Throwable t){
            t.printStackTrace();
            reportiumClient.testStop(TestResultFactory.createFailure(t.getMessage() , t));
            Assert.fail(t.getMessage());
        }

    }

    public void selectCountry(String location){
    //the xpath here defines the input with the specific sibling location

    	String checkBoxCountry = "//*[@type='checkbox']/following-sibling::label[text()=";
    	String xpath = checkBoxCountry + "'"+location + "']";
    	WebElement select = driver.findElementByXPath(xpath);
    	select.click();

    }

    public boolean isCountryDisplayed(String location){
		By xpath = By.xpath("//selected-country[@country='"+location+"']");
		try{
			if(driver.findElement(xpath).isDisplayed()){
				System.out.println(location+" is Displayed \n");
				Reporter.log(location+" is Displayed \n");
				return true;
			}
		}
		  catch(Exception e) {
		    	System.out.println(location+" is not Displayed");
		    	Reporter.log(location+" is not Displayed");
		       return false;
		    }
		return false;
	}

	public boolean SelectOS(String OS){
		By OSType = By.xpath("//os-selector/*[@class='selectionBox "+OS+"']");
		//System.out.println(OSType);
		WebElement selectOS = driver.findElement(OSType);
		selectOS.click();
		 reportiumClient.testStep("OS type selected"); //TEST STEP - OSType
		return selectOS.isSelected();
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
            driver.manage().deleteAllCookies(); //Removes cookies after test.
            driver.quit();
            String reportURL = reportiumClient.getReportUrl();
            System.out.println(reportURL); //Print URL to console
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}

