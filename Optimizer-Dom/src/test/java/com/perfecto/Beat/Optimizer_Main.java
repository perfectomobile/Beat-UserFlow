package com.perfecto.Beat;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;


import org.openqa.selenium.By;
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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Optimizer_Main {

    RemoteWebDriver driver;
    ReportiumClient reportiumClient;

    //TODO: Set your Perfecto Lab user, password and host.
    //TODO: Set your ESPN email and password.
//    String ESPN_EMAIL           = System.getProperty("np.ESPNuser", "My_Email");
//    String ESPN_PASSWORD        = System.getProperty("np.ESPNpassword", "My_Pass");
    String PERFECTO_HOST        = System.getProperty("np.testHost", "branchtest.perfectomobile.com");
    String PERFECTO_USER        = System.getProperty("np.testUsername", "test_automation@gmail.com");
    String PERFECTO_PASSWORD    = System.getProperty("np.testPassword", "Test_automation");
    private String url = "http://optimizer-beat-test.perfectomobile.com/";
    //TODO: Insert your device capabilities at testng.XML file.
    @Parameters({"platformName" , "model" , "browserName" , "location"})
    @BeforeTest
    public void beforMethod(String platformName, String model, String browserName, String location) throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("user" , PERFECTO_USER);
        capabilities.setCapability("password" , PERFECTO_PASSWORD);
        capabilities.setCapability("platformName" , platformName);
        capabilities.setCapability("model" , model);
        capabilities.setCapability("browserName" , browserName);
        capabilities.setCapability("location" , location);

        driver = new RemoteWebDriver(new URL("https://" + PERFECTO_HOST + "/nexperience/perfectomobile/wd/hub") , capabilities);
        driver.manage().timeouts().implicitlyWait(15 , TimeUnit.SECONDS);
        
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
    @Test(invocationCount = 1)
    public void testGroup(){
        test();
//        test2();
    }

    public void test(){
        try{
            //START TEST
            reportiumClient.testStart("Optimizer" , new TestContext("Some tag" , "Optimizer")); //Add tags by your choice. 
            reportiumClient.testStep("Navigate to Beat-Optimizer site"); //TEST STEP - Open site and navigate.
         
            driver.get(url);
            Cookie cookie = new Cookie("PM_MARKETO","123");
            driver.manage().addCookie(cookie );
//          ("document.cookie=PM_MARKETO=123");
            driver.get(url);
//            open the geography dialog
            driver.findElement(By.xpath(OptimizerPageObject.selectCountry)).click();
//            reportiumClient.testStep("Validate text point"); //TEST STEP - Validate text appear.
//            switchToContext(driver, "VISUAL");
//            driver.findElementByLinkText("Select Locations");
//            switchToContext(driver, "NATIVE");
            reportiumClient.testStep("Select country"); //TEST STEP - select country
            selectCountry("Germany");
//            close dialog
            driver.findElement(By.xpath(OptimizerPageObject.closeButton)).click();
//            check the country that was selected appears in main page
            isCountryDisplayed("Germany");
//            reportiumClient.testStep("Validate text point-Germany");  
//            switchToContext(driver, "VISUAL");
//            driver.findElementByLinkText("Germany");
//            switchToContext(driver, "NATIVE");
            reportiumClient.testStep("Select device type"); //select device type
            SelectDeviceType("Tablet");
            reportiumClient.testStep("Select device OS"); //select device OS
            SelectOS("iOS");
            reportiumClient.testStep("Customize preference"); //customize preference
            driver.findElementByXPath(OptimizerPageObject.customizePref).click();
            selectDevices(0, "AP");
            chooseDevice("6S","Apple iPhone 6S");
            driver.findElement(By.xpath(OptimizerPageObject.CustomizeClose)).click();
            reportiumClient.testStep("'Full Picture' button appears on screen"); //customize preference
            isElementVisible(".//*[@id='showFullResults']");
//          reportiumClient.testStep("Validate text point-BTN full Picture appears on screen");  
//          switchToContext(driver, "VISUAL");
//          driver.findElementByLinkText("See the full Picture");
//          switchToContext(driver, "NATIVE");
            
//            driver.findElement(By.xpath(PageObjects.menuNBA)).click();
//
//            reportiumClient.testStep("Validate text point"); //TEST STEP - Validate text appear.
//            Map<String, Object> textToFind = new HashMap<String, Object>();
//            textToFind.put("content", "SCORES");
//            String TextFindStatus =(String) driver.executeScript("mobile:text:find", textToFind);
//            //Assertion - Validate that "Scores" appear in page.
//            Assert.assertEquals(TextFindStatus , "true");
//
//            reportiumClient.testStep("Search for a team"); //TEST STEP - Search team.
//            driver.findElement(By.xpath(PageObjects.NBATeams)).click();
//            driver.findElement(By.xpath(PageObjects.SelectLakers)).click();
//
//            //END TEST - Success
//            reportiumClient.testStop(TestResultFactory.createSuccess());

        }catch (Throwable t){
            t.printStackTrace();
            reportiumClient.testStop(TestResultFactory.createFailure(t.getMessage() , t));
            Assert.fail(t.getMessage());
        }

    }

    public static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}
    public void selectCountry(String location){
//    	the xpath here defines the input with the specific sibling location
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
    
	public boolean SelectDeviceType(String dtype){
		By type = By.xpath("//device-selector/*[@class='selectionBox']/*[@class='" + dtype + "']");
		System.out.println(type);
		WebElement selectType = driver.findElement(type);
		selectType.click();
		 reportiumClient.testStep("device type selected"); //TEST STEP - deviceType
		return selectType.isSelected();
		
	}
	public boolean SelectOS(String OS){
		By OSType = By.xpath("//os-selector/*[@class='selectionBox "+OS+"']");
		System.out.println(OSType);
		WebElement selectOS = driver.findElement(OSType);
		selectOS.click();
		 reportiumClient.testStep("OS type selected"); //TEST STEP - OSType
		return selectOS.isSelected();
	}
	
	public List<WebElement> selectDevices(int row, String device){
		List<WebElement> selectDevices = driver.findElementsByXPath("//pm-choice");
		selectDevices.size();
		System.out.println(selectDevices.size());
		Assert.assertEquals(selectDevices.size(), 5);
		selectDevices.get(row).click();
		String selected = inputBox(device);
		return selectDevices;
		
	}
	
	public String inputBox(String string){
		
		WebElement search = driver.findElementByXPath("//*[@class='select2-selection__placeholder']");
		String[] split = string.split("");
		for (String string2 : split) {
			search.sendKeys(string2);
		}
	
		List<WebElement> dropDownBox = driver.findElementsByXPath("//*[contains(@class,'select2-results')]/li"); 

	try{
		if(dropDownBox.size()>2){
			System.out.println(dropDownBox.size());
		}
		System.out.println(string);
	}	 catch(Exception e){
		System.out.println("dropbox did not load");
		e.printStackTrace();
	}
			for (int i = 0; i < dropDownBox.size(); i++) {
				System.out.println(dropDownBox.get(i).getText());
				String name = dropDownBox.get(i).getText();
				
				if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(name, string)) {
					System.out.print(name);
				} else {
					System.out.println("the "+ name +" is not compatible and does not contain the " + string);
				} }
				
		return string;
		
	}
	
	public String chooseDevice(String clickDevice, String expected) {
		WebElement chooseDevice = driver.findElement(By.xpath("//*[@class='select2-results']//*[contains(text(),'"+clickDevice +"')]"));
		chooseDevice.click();
		WebElement chosenName = driver.findElement(By.xpath(".//*[@class='selection']//*[contains(@title,'"+clickDevice +"')]"));
		String actualName = chosenName.getText();
		actualName = actualName.substring(1);
		System.out.println(actualName + expected);
		return actualName;
	}
 public boolean isElementVisible(String by) {
		 
		 By xpath = By.xpath(by);
		    try {
		        if (driver.findElement(xpath).isDisplayed()) {
		        	System.out.println("Element is Displayed: " + by);
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

//	public void test2(){
//        try{
//            //NEW TEST
//            reportiumClient.testStart("ESPN login" , new TestContext("ESPN" , "Selenium" , "Login"));//Add tags by your choice
//
//            reportiumClient.testStep("Navigate to ESPN login page"); //TEST STEP - Click login
//            driver.findElement(By.xpath(PageObjects.LoginButton)).click();
//
//            reportiumClient.testStep("Insert login info and press login button"); //TEST STEP - login session
//            driver.switchTo().frame("disneyid-iframe"); // Switch to login frame
//            driver.findElement(By.xpath(PageObjects.Email)).sendKeys(this.ESPN_EMAIL);
//            driver.findElement(By.xpath(PageObjects.password)).sendKeys(this.ESPN_PASSWORD);
//            driver.findElement(By.xpath(PageObjects.submit)).click();
//
//            reportiumClient.testStep("LogOut"); //TEST STEP - logout session
//            driver.findElement(By.xpath(PageObjects.userPlace)).click();
//            driver.findElement(By.xpath(PageObjects.logOut)).click();

//            //END TEST - Success
//            reportiumClient.testStop(TestResultFactory.createSuccess());
//        }catch (Throwable t){
//            t.printStackTrace();
//            reportiumClient.testStop(TestResultFactory.createFailure(t.getMessage() , t));
//            Assert.fail(t.getMessage());
//        }
//    }


    @SuppressWarnings("Since15")
    @AfterTest
    public void afterMethod(){
        try{
            driver.manage().deleteAllCookies(); //Removes cookies after test.
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