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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Optimizer_Main {

    RemoteWebDriver  driver;
    ReportiumClient reportiumClient;

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
    @Test
       public void test(){
        try{
        	System.out.println("-----------------------------------------");
        	System.err.println(Thread.currentThread().getId());
        	System.out.println("-----------------------------------------");
        	//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    		//driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            //START TEST
            reportiumClient.testStart("Optimizer" , new TestContext("Some tag" , "Optimizer")); //Add tags by your choice. 
            reportiumClient.testStep("Navigate to Beat-Optimizer site"); //TEST STEP - Open site and navigate.
         
            driver.get(url);
            Cookie cookie = new Cookie("PM_MARKETO","123");
            driver.manage().addCookie(cookie );
//          ("document.cookie=PM_MARKETO=123");
            driver.get(url);
//            open the geography dialog
            driver.findElementByXPath(OptimizerPageObject.selectCountry).click();
            reportiumClient.testStep("Validate text point"); //TEST STEP - Validate text appear.
            switchToContext(driver, "VISUAL");
            driver.findElementByLinkText("Select Locations");
            switchToContext(driver, "WEBVIEW");
            reportiumClient.testStep("Select country"); //TEST STEP - select country
            selectCountry("Germany");
//            close dialog
            driver.findElement(By.xpath(OptimizerPageObject.closeButton)).click();
//            check the country that was selected appears in main page
            isCountryDisplayed("Germany");
            reportiumClient.testStep("Validate text point-Germany");  
            switchToContext(driver, "VISUAL");
            driver.findElementByLinkText("Germany");
            switchToContext(driver, "WEBVIEW");
            reportiumClient.testStep("Select device type"); //select device type
            SelectDeviceType("Tablet");
            reportiumClient.testStep("Select device OS"); //select device OS
            SelectOS("iOS");
            reportiumClient.testStep("Customize preference"); //customize preference
            driver.findElementByXPath(OptimizerPageObject.customizePref).click();
            selectDevice(1,"6S");
            chooseDevice("6S","Apple iPhone 6S");
            WebElement BtnClose = driver.findElement(By.xpath(OptimizerPageObject.CustomizeClose));
            BtnClose.click();//close customize pref dialog
//            reportiumClient.testStep("heroes list"); //heroes
//            HeroesList();
            reportiumClient.testStep("'Full Picture' button appears on screen"); //customize preference
            WebElement FullRBtn = driver.findElementByXPath(OptimizerPageObject.FullResultBtn);
            isElementVisible(".//*[@id='showFullResults']");
            FullRBtn.click();
            reportiumClient.testStep("Test Coverage Optimizer full report page"); //new page opened with table
            String TableTitle = driver.findElement(By.xpath(OptimizerPageObject.FullResultPage)).getText();
            Assert.assertEquals(TableTitle, "TEST COVERAGE OPTIMIZER");
          
           
            
            
//            //END TEST - Success
            reportiumClient.testStop(TestResultFactory.createSuccess());

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
	
	public  void selectDevice(int row, String device){
		WebElement container = driver.findElementByXPath("//pm-choice["+row+"]");
		WebElement selectDevice = container.findElement(By.xpath(".//*[@class='select2-selection__placeholder']"));
		selectDevice.click();
		WebElement insertDevice = container.findElement(By.xpath("//*[text()='Select an option...']"));
		insertDevice.sendKeys(device);
	

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

 public List<String> HeroesList(){
	 List<WebElement> heroes = driver.findElements(By.xpath(OptimizerPageObject.Heroes));
		List<String> heroesList = new ArrayList<String>();
		for(int i=1; i < (heroes.size()+1); i++) {
		  String deviceName = driver.findElementByXPath(".//*[@id='pmHeroes']//*[@class='pm-hero ng-scope']/*[text()='"+i+"']/following-sibling::label").getText();
		    heroesList.add(deviceName);
		    System.out.println(deviceName);
			}
		return heroesList;
	}
// 
// public static List<String> FullReportlist(){
//		List<String> fullReportlist = new ArrayList<String>();
//		for(int i=0; i < 5; i++) {
//		    String deviceName =driver.findElementByXPath(".//*[contains(@id, '-"+i+"-uiGrid-0005-cell')]/label").getText();
//		    fullReportlist.add(deviceName);
//		    System.out.println(deviceName);
//		    Reporter.log(deviceName);
//	
//			}
//		return fullReportlist;
//		
//		
//	}


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