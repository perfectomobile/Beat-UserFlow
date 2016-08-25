package com.perfecto.beat;


public class GeicoPageObject {
//Auto Icon
final static String autoIcon = "//*[@id=\"selected_product\"]//*[@class=\"icon icon-vehicle\"]";
//select Insurance Type
final static String insuranceType = "//*[@id=\"insurancetype\"]";
//Motorcycle option
final static String MotorcycleOption = "//*[@id=\"optionMotorcycle\"]";
final static String MotorcycleOption2 = "//*[@class=\"icon icon-motorcycle\"]";
//Motorcycle icon
final static String MotorcycleIcon = "//*[@id=\"selected_product\"]//*[@class=\"icon icon-motorcycle\"]";
// ZIP Code
final static String ZIP = "//*[@id=\"zip\"]";
//Submit button - START QUOTE
final static String startQuote	= "//*[text()=\"START QUOTE\"]";
//Set Radio button - Do you currently have GEICO auto insurance? No
final static String autoInsurance = "//span[text()=\"No\"]";
//Go to Start Quote page, fill in details
final static String firstName= "//*[@id=\"firstName\"]";
final static String lastName = "//*[@id=\"lastName\"]";
final static String street= "//*[@id=\"street\"]";
//Date of birth details
final static String birthMonth = "//*[@id=\"date-monthdob\"]";
final static String birthDay = "//*[@id=\"date-daydob\"]";
final static String birthYear = "//*[@id=\"date-yeardob\"]";
//Submit button
final static String submitContinue = "//*[@id=\"btnSubmit\"]";
//Has CycleError
final static String hasCycleError = "//*[@id=\"hasCycle-error\"]";
//Has Cycle
final static String hasCycle = "//*[@id=\"hasCycle\"]";
//current Insurance
final static String currentInsurance = "//*[text()=\"Current Motorcycle Insurance Company\"]";
//Submit button
final static String submitNext = "//*[@id=\"btnSubmit\"]";
//Location header
final static String locationHeader = "//*[@class=\"location-headline section-header\"]";

}


