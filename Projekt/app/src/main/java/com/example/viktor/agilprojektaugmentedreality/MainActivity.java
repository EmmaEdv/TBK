package com.example.viktor.agilprojektaugmentedreality;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public ListView listView;
    public ImageView iV;


    final ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final ThumbnailAdapter adapter = new ThumbnailAdapter(this, generateData());
        listView.setAdapter(adapter);

        final ArrayList<ThumbnailItem> items = generateData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                iV.setImageResource(items.get(position).getResource());
               listView.setItemChecked(position,true);

            }
        });

             iV = (ImageView) findViewById(R.id.imageview);

    }

    private ArrayList<ThumbnailItem> generateData(){

        ArrayList<ThumbnailItem> items = new ArrayList<ThumbnailItem>();
        items.add(new ThumbnailItem(R.drawable.ryggstod_toppen, "Ryggstöd topp"));
        items.add(new ThumbnailItem(R.drawable.ryggstod_mitten,"Ryggstöd mitten"));
        items.add(new ThumbnailItem(R.drawable.skruv,"Skruv"));
        items.add(new ThumbnailItem(R.drawable.sits, "Sits"));

        return items;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
