package com.example.viktor.agilprojektaugmentedreality;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.viktor.agilprojektaugmentedreality.ThumbnailItem;

/**
 * Created by Viktor on 2015-03-24.
 */
public class ThumbnailAdapter extends ArrayAdapter<ThumbnailItem>{

    private final Context context;
    private final ArrayList<ThumbnailItem> itemsArrayList;

    public ThumbnailAdapter(Context context, ArrayList<ThumbnailItem> itemsArrayList) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        // 3. Set different title views according to position in the list
        TextView titleView;

        TextView statusView;

        // 3.1 Create typeface for custom fonts
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Berlin Sans FB.ttf");

        if(position == 0 || position == 5) {
            rowView.setBackgroundColor(Color.DKGRAY);

            // 4. Set the textView
            titleView = (TextView) rowView.findViewById(R.id.headtitle);
            statusView = (TextView) rowView.findViewById(R.id.status);
            // 4.1 Set font for Textview
            titleView.setTypeface(font);
        }
        else {
            rowView.setBackgroundColor(Color.WHITE);

            // 4.2 Set the textView
            titleView = (TextView) rowView.findViewById(R.id.title);

            statusView = (TextView) rowView.findViewById(R.id.status);
            statusView.setText(itemsArrayList.get(position).getStatus());
            if(itemsArrayList.get(position).getFound()){
                statusView.setTextColor(Color.parseColor("#4BE38A"));
            }
            // 4.3 Set font for Textview ...again
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
    public void updateStatus(int position){
        TextView tv;
        View v = getView(position, null, null);
        if(v != null){
            tv = (TextView)v.findViewById(R.id.status);
            tv.setText(itemsArrayList.get(position).getStatus());
        }


    }
}
