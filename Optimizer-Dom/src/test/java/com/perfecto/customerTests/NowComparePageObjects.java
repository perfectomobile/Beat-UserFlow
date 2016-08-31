package test.java.com.perfecto.customerTests;

import org.openqa.selenium.By;

public class NowComparePageObjects {

    final static String iconVehicle = "//*[@id=\"selected_product\"]//*[@class=\"icon icon-vehicle\"]";

    final static String healthInsurance = "//*[ancestor::*[@id='dq-middle'] and @title='Health-Insurance']";

    final static String navbarToggle = "//*[ancestor::*[@id='dq-middle'] and @class='navbar-toggle']";

    final static String compareExpatHealthInsurance = "//*[ancestor::*[@id='dq-middle'] and text()='Compare Expat Health Insurance']";

    final static String iconNumber = "//button[ancestor::*[@id='preqoute_form'] and parent::*[@class='icon-number']]";

    final static String fPersonOption6 = "//*[preceding-sibling::*[@id='ui-multiselect-fPerson-option-6'] and text()='6']";

    final static String iconLocation = "//button[ancestor::*[@id='preqoute_form'] and parent::*[@class='icon-location']]";

    final static String unitedKingdom = "//*[preceding-sibling::*[@id='ui-multiselect-fLocation-option-231'] and text()='United Kingdom']";

    final static String iconRequirements = "//button[ancestor::*[@id='preqoute_form'] and parent::*[@class='icon-requirements']]";

    final static String dentalCover = "//*[@id='ui-multiselect-cover-list-option-2' and following-sibling::*[text()='Dental Cover']]";

    final static String maternityCover = "//*[@id='ui-multiselect-cover-list-option-3' and following-sibling::*[text()='Maternity Cover']]";

    final static String getQuotes = "//*[parent::*[@id='preqoute_form'] and text()='GET QUOTES']";

    // About You Page
    final static String yourDetails = "//*[ancestor::*[@id='dq-page-holder'] and text()='Your Details']";

}


