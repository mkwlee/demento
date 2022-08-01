package com.company.dementiacare;

// modal for the static reminder list
public class StaticRVModel {

    // variables
    private int image;
    private String text;
    boolean visibility;

    // constructor
    public StaticRVModel(int image, String text) {
        this.image = image;
        this.text = text;
        this.visibility = false;
    }


    // get the image
    public int getImage() {
        return image;
    }

    // get the text
    public String getText() {
        return text;
    }

    // get the visibility
    public boolean isVisible(){
        return visibility;
    }

    // set the visibility
    public void setVisibility(boolean visibility){
        this.visibility = visibility;
    }
}
