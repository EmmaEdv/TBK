package com.example.viktor.agilprojektaugmentedreality;

/**
 * Created by Viktor on 2015-03-24.
 */
public class ThumbnailItem {

    private String title;
    private int resource;

    public ThumbnailItem() {}

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
