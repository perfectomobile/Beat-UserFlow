package com.perfecto.testing.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Utils {
	public static RemoteWebDriver webdriver;
	// Set cloud host and credentials values at runtime via Command Line
	public static String PERFECTO_HOST = "branchtest.perfectomobile.com";
	public static String PERFECTO_USER = "admint";
	public static String PERFECTO_PASSWORD = "admin";
	public static String CLOUD_URL = "https://" + PERFECTO_HOST + "/nexperience/perfectomobile/wd/hub";



	public static RemoteWebDriver getRemoteWebDriver(String platformName, String platformVersion, String browserName,
			String browserVersion, String screenResolution, String deviceName, String persona) throws MalformedURLException {

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("user", PERFECTO_USER);
		capabilities.setCapability("password", PERFECTO_PASSWORD);
		capabilities.setCapability("platformName", platformName);
		capabilities.setCapability("platformVersion", platformVersion);
		capabilities.setCapability("browserName", browserName);
		capabilities.setCapability("browserVersion", browserVersion);

		// Define test name
		capabilities.setCapability("scriptName", "Webinar-SeleniumAcrossDigital");

		if (!screenResolution.isEmpty()) {
			capabilities.setCapability("resolution", screenResolution);
			System.out.println("Creating Remote WebDriver on: " + platformName + " " + platformVersion + ", " + browserName + " " + browserVersion + ", " + screenResolution);
		}
		else {
			if (!platformName.isEmpty())
				System.out.println("Creating Remote WebDriver on: " + platformName + " " + platformVersion);
			else
				System.out.println("Creating Remote WebDriver on: " + browserName);
		}

		// Instantiate platform specific drivers with some platform specific capabilities
		if(capabilities.getCapability("platformName").toString().equals("iOS")) {
			capabilities.setCapability("autoAcceptAlerts", true);
//			capabilities.setCapability("deviceName", deviceName);
//			capabilities.setCapability("model", "");
			capabilities.setCapability("windTunnelPersona", persona);
			webdriver = new IOSDriver(new URL(CLOUD_URL), capabilities);

		} else if (capabilities.getCapability("platformName").toString().equals("Android")){
//			capabilities.setCapability("deviceName", deviceName);
//			capabilities.setCapability("model", "");
			capabilities.setCapability("windTunnelPersona", persona);
			webdriver = new AndroidDriver(new URL(CLOUD_URL), capabilities);
		} else {
			webdriver = new RemoteWebDriver(new URL(CLOUD_URL), capabilities);
		}

		// Define RemoteWebDriver timeouts
		webdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		webdriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

		// Maximize browser window on Desktop
		if (!screenResolution.isEmpty()) {
			webdriver.manage().window().maximize();
		}

		return webdriver;
	}

}