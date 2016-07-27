package com.perfecto.Beat;


public class OptimizerPageObject {
//select country button
final static String selectCountry = "//button[@class='pm-select-country-btn ng-scope']";
//*[@type='checkbox']/following-sibling::label[text()='United States']
final static String checkBoxCountry = "//*[@type='checkbox']/following-sibling::label[text()=";
final static String closeButton = "//*[text()='Close']/parent::*";
//country that appears in main page
final static String selectedCountry	= "//selected-country";
//customize prefrences
final static String customizePref = "//*[@id='addData']";
final static String CustomizeClose = "//*[text()='Close']";
final static String FullResultBtn = ".//*[@id='showFullResults']";
final static String FullResultPage = "//*[text()='TEST COVERAGE OPTIMIZER']";
final static String Heroes = ".//*[contains(@class,'pm-heroes-container')]//*[@class='pm-hero-index ng-binding']";




}

