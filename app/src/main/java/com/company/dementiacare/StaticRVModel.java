package com.company.dementiacare;

public class StaticRVModel {

    private int image;
    private String text;
    boolean visibility;

    public StaticRVModel(int image, String text) {
        this.image = image;
        this.text = text;
        this.visibility = false;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public boolean isVisible(){
        return visibility;
    }

    public void setVisibility(boolean visibility){
        this.visibility = visibility;
    }
}
