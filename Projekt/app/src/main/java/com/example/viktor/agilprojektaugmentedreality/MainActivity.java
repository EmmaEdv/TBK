package com.example.viktor.agilprojektaugmentedreality;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    /*
    Variables
     */
    public ListView listView;
    private GLSurfaceView mSurfaceView;
    private GLRenderer mGLRenderer = new GLRenderer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final ArrayList<ThumbnailItem> items = generateData();
        final ThumbnailAdapter adapter = new ThumbnailAdapter(this, items);
        listView.setAdapter(adapter);

        /*
        Click listener for item
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Add stuff here for Item selection from the list view

                listView.setItemChecked(position,true);

            }
        });
        // Get the GL Surface View from the activity XML by Id
        mSurfaceView = (GLSurfaceView) findViewById(R.id.surfaceviewclass);
        // Then assign a renderer to the fetched view
        mSurfaceView.setRenderer(mGLRenderer);
    }

    /*
    Generates data for the list.
    Add both thumbnails and description here.
     */
    private ArrayList<ThumbnailItem> generateData(){
        ArrayList<ThumbnailItem> items = new ArrayList<ThumbnailItem>();

        items.add(new ThumbnailItem(R.drawable.rygg_topp, "Stora delar"));
        items.add(new ThumbnailItem(R.drawable.rygg_topp, "Ryggstöd topp"));
        items.add(new ThumbnailItem(R.drawable.rygg_mitt,"Ryggstöd mitten"));
        items.add(new ThumbnailItem(R.drawable.ram, "Ram"));
        items.add(new ThumbnailItem(R.drawable.sits, "Sits"));
        items.add(new ThumbnailItem(R.drawable.rygg_topp, "Små delar"));
        items.add(new ThumbnailItem(R.drawable.skruv,"Skruv"));
        items.add(new ThumbnailItem(R.drawable.plugg, "Plugg"));

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

    @Override
    protected void onResume() {
        super.onResume();
        /*
         * The activity must call the GL surface view's
         * onResume() on activity onResume().
         */
        if (mSurfaceView != null) {
            mSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*
         * The activity must call the GL surface view's
         * onPause() on activity onPause().
         */
        if (mSurfaceView != null) {
            mSurfaceView.onPause();
        }
    }
}