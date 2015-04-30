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
    private boolean found;

    private TextView tv;

    /*
    Empty constructor
     */
    public ThumbnailItem() {}

    /*
    Construtor
     */

    public ThumbnailItem(String title){
        super();

        found = false;
        this.title = title;
    }


    public ThumbnailItem(int resource, String title, boolean status) {
        super();

        this.resource = resource;
        this.title = title;
        this.found = status;
    }

    public void setResource(int i){this.resource = i;}
    public void setTitle(String s){this.title = s;}
    public void setFound(boolean b){this.found = b;}

    public String getTitle() {return this.title;}
    public int getResource() {return this.resource;}
    public boolean getFound(){return this.found;}

    //returns a string to display if item has been found by metaio.
    //room for a more general solution!
    public String getStatus(){

        int nFound = found ? 1 : 0 ;
        int nTotal = 1;
        String res = "(" + nFound + "/" + nTotal + ")";

        return res;
    }
}
