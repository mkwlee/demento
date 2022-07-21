/*
 * 
 *      Unused class for now.
 * 
 *  Description: This class is used to control type modals.
 *  
 * Update History: July 20, 2022
*/


package com.company.dementiacare.component;

public class TypeModal {

    String typeName;
    int image;

    public TypeModal(String typeName, int image) {
        this.typeName = typeName;
        this.image = image;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getImage() {
        return image;
    }
}
