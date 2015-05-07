package com.example.viktor.agilprojektaugmentedreality;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Viktor on 2015-03-24.
 */
public class ThumbnailAdapter extends ArrayAdapter<ThumbnailItem>{

    private final Context context;
    private final ArrayList<ThumbnailItem> itemsArrayList;

    public ThumbnailAdapter(Context context, ArrayList<ThumbnailItem> itemsArrayList) {

        super(context, R.layout.thumbnail_item, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.thumbnail_item, parent, false);

        // 3. set views for structuring different elements in the rows

        //view for displaying the title
        TextView titleView;

        //view for displaying the has-been-found-by-metaio status
        TextView statusView;

        // 3.1 Create typeface for custom fonts
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Berlin Sans FB.ttf");

        //for now, the headeres are hard coded
        if(position == 0 || position == 6) {
            rowView.setBackgroundColor(Color.DKGRAY);

            // 4. Set the textView
            titleView = (TextView) rowView.findViewById(R.id.headtitle);

            // 4.1 Set font for Textview
            titleView.setTypeface(font);
        }
        else {
            // 4.2 Set the textView
            titleView = (TextView) rowView.findViewById(R.id.title);
            //and the statusview
            statusView = (TextView) rowView.findViewById(R.id.status);

            //set the statusviews text content. For smaller parts, put no text
            statusView.setText(  position > 6 ? "" : itemsArrayList.get(position).getStatus());

            //if the status is FOUND set the text to green. not great solution..
            if(itemsArrayList.get(position).getFound()){
                statusView.setTextColor(Color.parseColor("#4BE38A"));
            }
            // 4.3 Set font for Textview and StatusView
            titleView.setTypeface(font);
            statusView.setTypeface(font);

            // 5. Set image
            ImageView thumbnailView = (ImageView) rowView.findViewById(R.id.thumbnail);
            thumbnailView.setImageResource(itemsArrayList.get(position).getResource());
        }

        //set the text for the title view. from the list in MainActivity
        titleView.setText(itemsArrayList.get(position).getTitle());

        // 6. Return rowView
        return rowView;
    }

    //method for uptating the found property of each item.
    public void updateStatus(int position, boolean found){
        TextView tv;
        itemsArrayList.get(position).setFound(found);
        View v = getView(position, null, null);
        if(v != null){
            tv = (TextView)v.findViewById(R.id.status);
            tv.setText(itemsArrayList.get(position).getStatus());
        }
    }
}
