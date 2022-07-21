/*
 *      Type class
 * 
 *  Description: This class is used to control the type.
 * 
 *  Update History: July 21, 2022
*/


package com.company.dementiacare.component;

import java.util.ArrayList;

public class Type {

    // type's name
    private String typeString;

    // constructor
    public Type(String typeString) {
        this.typeString = typeString;
    }

    // get the type's name
    public String getDayString() {
        return typeString;
    }
}
