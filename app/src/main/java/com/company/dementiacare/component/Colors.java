/*
 * 
 *          Color class component
 * 
 *    Description: This class is used to control the colors.
 * 
 *    Update History: July 21, 2022
 *
*/


package com.company.dementiacare.component;

public class Colors {

    // color's name
    private String colorString;

    // constructor
    public Colors(String colorString){
        this.colorString = colorString;
    }

    // get the color's name
    public String getColorString() {
        return colorString;
    }
}
