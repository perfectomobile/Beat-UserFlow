package test.java.com.perfecto.beat;

/**
 * Created by ronma on 29-Aug-16.
 */
class OptimizerObject {

    //select country button
    final static String selectCountry = "pm-select-country-label"; // by class

    final static String closeButton = "//*[text()='Close']/parent::*"; // xpath

    final static String FullResultBtn = ".//*[@id='showFullResults']"; // xpath

    final static String FullResultPage = "//*[text()='TEST COVERAGE OPTIMIZER']"; // xpath

    final static String typeSelect = "body > div.pm-content-container.ng-scope.results-page > " +
            "filters-container > div > device-filter > div > div.pm-selection-container.pm-device-type-container > device-selector:nth-child(1) > div > img"; // css

    final static String fullPicButton = "showFullResults"; // by id
}
