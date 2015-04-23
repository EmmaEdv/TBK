package com.example.viktor.agilprojektaugmentedreality;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by Viktor on 2015-03-24.

    The items in the listview

 */
public class ThumbnailItem {

    private String title;
    private int resource;

    private TextView tv;

    /*
    Empty constructor
     */
    public ThumbnailItem() {}

    /*
    Construtor
     */
    public ThumbnailItem(int resource, String title) {
        super();

        this.resource = resource;
        this.title = title;
    }

    public void setResource(int i){this.resource = i;}
    public void setTitle(String s){this.title = s;}

    public String getTitle() {return this.title;}
    public int getResource() {return this.resource;}
}
