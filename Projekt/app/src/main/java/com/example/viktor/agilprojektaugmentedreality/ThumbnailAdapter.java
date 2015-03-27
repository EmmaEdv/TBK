package com.example.viktor.agilprojektaugmentedreality;

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

        // 3. Get the two text view from the rowView
        ImageView thumbnailView = (ImageView) rowView.findViewById(R.id.thumbnail);
        TextView titleView = (TextView) rowView.findViewById(R.id.title);

        // 4. Set the text for textView
        thumbnailView.setImageResource(itemsArrayList.get(position).getResource());
        titleView.setText(itemsArrayList.get(position).getTitle());

        // 5. retrn rowView
        return rowView;
    }

}
