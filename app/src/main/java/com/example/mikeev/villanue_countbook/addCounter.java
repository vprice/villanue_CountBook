package com.example.mikeev.villanue_countbook;

/**
 * Created by Mikee V on 2017-09-26.
 */

public interface addCounter {

    //Getters
    public String getName();
    public int getCurrValue();
    public int getInitValue();
    public String getComment();

    //Setters
    public void setName(String name);
    public void setCurrValue(int currValue);
    public void setInitValue(int initValue);
    public void setComment(String comment);
}
