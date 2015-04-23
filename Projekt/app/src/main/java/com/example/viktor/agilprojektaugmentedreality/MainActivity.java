package com.example.viktor.agilprojektaugmentedreality;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends ActionBarActivity {

    /*
    Variables
     */
    public ListView listView;
    // Arrays for "rygg top" object
    protected static float[] ryggTopVerts;
    protected static float[] ryggTopNormals;
    protected static float[] ryggTopCoords;

    // Arrays for "rygg mitten" objects
    protected static float[] ryggMittVerts;
    protected static float[] ryggMittNormals;
    protected static float[] ryggMittCoords;

    // Arrays for "ram" objects
    protected static float[] ramVerts;
    protected static float[] ramNormals;
    protected static float[] ramCoords;

    // Arrays for "sits" objects
    protected static float[] sitsVerts;
    protected static float[] sitsNormals;
    protected static float[] sitsCoords;

    private GLSurfaceView mSurfaceView;
    private GLRenderer mGLRenderer;
    protected static int numObjectVerts;

    //Used for rotation in GLView
    private float mPreviousX;
    private float mPreviousY;
    private float mDensity;
    public static int currentObject = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDensity = getResources().getDisplayMetrics().density;
        super.onCreate(savedInstanceState);

        //Locks the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Load vertex, normal and color lists for all objects
        loadObjectLists();

        // Create our openGL renderer
        mGLRenderer = new GLRenderer();

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


                listView.setItemChecked(position, true);
                // Check which object to render
                switch (position) {
                    case 1:
                        currentObject = 1;
                        numObjectVerts = ryggTopVerts.length;
                        break;
                    case 2:
                        currentObject = 2;
                        numObjectVerts = ryggMittVerts.length;
                        break;
                    case 3:
                        currentObject = 3;
                        numObjectVerts = ramVerts.length;
                        break;
                    case 4:
                        currentObject = 4;
                        numObjectVerts = sitsVerts.length;
                        break;
                    default:
                        currentObject = 1;
                        numObjectVerts = ryggTopVerts.length;
                        break;
                }


            }
        });
        // Get the GL Surface View from the activity XML by Id
        mSurfaceView = (GLSurfaceView) findViewById(R.id.surfaceviewclass);
        mSurfaceView.setEGLContextClientVersion(2);
        //Event listener for touch on the GLView, gets the data needed for proper rotation.
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    float x = event.getX();
                    float y = event.getY();

                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if (mGLRenderer != null) {
                            float deltaX = (x - mPreviousX) / mDensity / 2f;
                            float deltaY = (y - mPreviousY) / mDensity / 2f;

                            mGLRenderer.mDeltaX += deltaX;
                            mGLRenderer.mDeltaY += deltaY;
                        }
                    }

                    mPreviousX = x;
                    mPreviousY = y;

                    return true;
                } else {
                    return false;
                }
            }
        });

        // Then assign a renderer to the fetched view
        mSurfaceView.setRenderer(mGLRenderer);
    }

    /*
    Generates data for the list.
    Add both thumbnails and description here.
     */
    private ArrayList<ThumbnailItem> generateData() {
        ArrayList<ThumbnailItem> items = new ArrayList<ThumbnailItem>();

        items.add(new ThumbnailItem(R.drawable.rygg_topp, "      Stora delar"));
        items.add(new ThumbnailItem(R.drawable.rygg_topp, "    Ryggstöd topp"));
        items.add(new ThumbnailItem(R.drawable.rygg_mitt, "    Ryggstöd mitten"));
        items.add(new ThumbnailItem(R.drawable.ram, "    Ram"));
        items.add(new ThumbnailItem(R.drawable.sits, "    Sits"));
        items.add(new ThumbnailItem(R.drawable.rygg_topp, "      Små delar"));
        items.add(new ThumbnailItem(R.drawable.skruv, "    Skruv"));
        items.add(new ThumbnailItem(R.drawable.plugg, "    Plugg"));

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

        if (item.getItemId() == R.id.Camera) {

            Intent cameraScreen = new Intent(getApplicationContext(),  CameraActivity.class);
            startActivity(cameraScreen);
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

    /**
     * Reads a vertex list from a text file
     */
    public float[] readObjVertecies(int id) throws IOException {

        // Create list for the strings that we read from the text file
        List<String> temps = new ArrayList<String>();
        String string;
        // Open the file that is to be read
        InputStream is = getResources().openRawResource(id);

        try{
            // Create the reader that reades the lines in the text file
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            if(is != null) {
                // Read lines until there are no lines left
                while((string = reader.readLine()) != null) {
                    // Skip the flags for vertecies and normals
                    if(!string.equals("V") && !string.equals("N")) {
                        // Split the line that has been read
                        StringTokenizer st = new StringTokenizer(string);
                        // Go through all the words in a line
                        while (st.hasMoreTokens()) {
                            // Get the word
                            string = st.nextToken();
                            // Remove the "comma"
                            string = string.replace(",", "");
                            // Add the word to pur temporary holder
                            temps.add(string);
                        }
                    }
                }
            }
        } finally {
            try {
                // Close the file
                is.close();
            } catch (Throwable ignore) { }
        }

        // Create an array of floats, to hold our vertecies
        float[] vertecies = new float[temps.size() / 2];
        // Get the number of vertecies, this is needed when we draw,
        // since we need to now how many lines to draw
        numObjectVerts = vertecies.length;

        // Parse all strings from the text file to floats
        for(int i = 0; i < temps.size() / 2; i++)
            vertecies[i] = Float.parseFloat(temps.get(i));

        // And boom, return the vertex list
        return vertecies;
    }

    /**
     * Reads a normal list from a text file
     */
    public float[] readObjNormals(int id) throws IOException {

        // Create list for the strings that we read from the text file
        List<String> temps = new ArrayList<String>();
        String string;
        // Open the file that is to be read
        InputStream is = getResources().openRawResource(id);
        try{
            // Create the reader that reades the lines in the text file
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            if(is != null) {
                // Read lines until there are no lines left
                while((string = reader.readLine()) != null) {
                    // Skip the flags for vertecies and normals
                    if(!string.equals("V") && !string.equals("N")) {
                        // Split the line that has been read
                        StringTokenizer st = new StringTokenizer(string);
                        // Go through all the words in a line
                        while (st.hasMoreTokens()) {
                            // Get the word
                            string = st.nextToken();
                            // Remove the "comma"
                            string = string.replace(",", "");
                            // Add the word to pur temporary holder
                            temps.add(string);
                        }
                    }
                }
            }
        } finally {
            try {
                // Close the file
                is.close();
            } catch (Throwable ignore) { }
        }

        // Create an array of floats, to hold our normals
        float[] normals = new float[temps.size() / 2];
        // Parse all strings from the text file to floats
        for(int i = 0; i < temps.size() / 2; i++)
            normals[i] = Float.parseFloat(temps.get(i + temps.size() / 2));

        // And boom, return the vertex list
        return normals;
    }

    /**
     * Creates a color list with one single color
     */
    public float[] createColorCoords(float color) {

        // Calculate how many color coordinates that we need for the object
        double s = ( numObjectVerts * (4.0 / 3.0));
        final int SIZE = (int)s;

        // Create an array to hold all the color information
        float[] colorCoords = new float[SIZE];

        // Set the color
        for(int i = 0; i < SIZE; i++)
            colorCoords[i] = color;

        return colorCoords;
    }

    /**
     * Loads all objects that we want to use
     */
    void loadObjectLists() {

        // Object 1
        try {
            ryggTopVerts = readObjVertecies(R.raw.rygg_top);
            ryggMittVerts = readObjVertecies(R.raw.rygg_mid);
            ramVerts = readObjVertecies(R.raw.sida);
            sitsVerts = readObjVertecies(R.raw.sits);

            ryggTopNormals = readObjNormals(R.raw.rygg_top);
            ryggMittNormals = readObjNormals(R.raw.rygg_mid);
            ramNormals = readObjNormals(R.raw.sida);
            sitsNormals = readObjNormals(R.raw.sits);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ryggTopNormals = readObjNormals(R.raw.rygg_top);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ryggTopCoords = createColorCoords(1.0f);
        ryggMittCoords = createColorCoords(1.0f);
        ramCoords = createColorCoords(1.0f);
        sitsCoords = createColorCoords(1.0f);
    }
}