package com.example.viktor.agilprojektaugmentedreality;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    /*
    Variables
     */

    public ListView listView;
    public TextView tV;


    final ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);


        final ThumbnailAdapter adapter = new ThumbnailAdapter(this, generateData());
        listView.setAdapter(adapter);

        tV = (TextView) findViewById(R.id.textview);
        tV.setText("Här ska bilderna visas");
    }

    /*
    Generates data for the list.
    Add both thumbnails and description here.
     */
    private ArrayList<ThumbnailItem> generateData(){

        ArrayList<ThumbnailItem> items = new ArrayList<ThumbnailItem>();
        items.add(new ThumbnailItem(R.drawable.ryggstod_toppen," Ryggstöd topp"));
        items.add(new ThumbnailItem(R.drawable.ryggstod_mitten," Ryggstöd mitten"));
        items.add(new ThumbnailItem(R.drawable.skruv," Jesper"));

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
