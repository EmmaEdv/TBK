package com.example.viktor.agilprojektaugmentedreality;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

public class MainActivity extends Activity {

    /*
    Variables
     */
    public ListView listView;
    protected static float[] cubeVerts;
    protected static float[] cubeNormals;
    protected static float[] cubeColorCoords;
    private GLSurfaceView mSurfaceView;
    private GLRenderer mGLRenderer;
    protected static int numCubeVerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        try {
            cubeVerts = readObjVertecies();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            cubeNormals = readObjNormals();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cubeColorCoords = createColorCoords(1.0f);

/*
        Log.d("VERTS SIZE: ", Integer.toString(cubeVerts.length));
        Log.d("NORMALS SIZE: ", Integer.toString(cubeNormals.length));
        Log.d("COLORS SIZE: ", Integer.toString(cubeColorCoords.length));
        Log.d("NUMVERTS: ", Integer.toString(numCubeVerts));
*/
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

                // Add stuff here for Item selection from the list view

                listView.setItemChecked(position,true);

            }
        });
        // Get the GL Surface View from the activity XML by Id
        mSurfaceView = (GLSurfaceView) findViewById(R.id.surfaceviewclass);
        mSurfaceView.setEGLContextClientVersion(2);
        // Then assign a renderer to the fetched view
        mSurfaceView.setRenderer(mGLRenderer);




       /* for(int i = 0; i < cubeVerts.length; i++) {
            Log.d("VERT:", Float.toString(cubeVerts[i]));
        }*/

/*
        try {
            reader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /*try {
            Float[] verts = reader();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /*
    Generates data for the list.
    Add both thumbnails and description here.
     */
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

    public float[] readObjVertecies() throws IOException {

        List<String> temps = new ArrayList<String>();
        String string;
        InputStream is = getResources().openRawResource(R.raw.epic_parsed_list);
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            if(is != null) {
                while((string = reader.readLine()) != null) {

                    if(!string.equals("V") && !string.equals("N")) {
                        StringTokenizer st = new StringTokenizer(string);
                        while (st.hasMoreTokens()) {

                            string = st.nextToken();
                            string = string.replace(",", "");
                            temps.add(string);
                        }
                    }
                }
            }
        } finally {
            try {
                is.close();
            } catch (Throwable ignore) { }
        }

        float[] vertecies = new float[temps.size() / 2];
        numCubeVerts = vertecies.length;

        for(int i = 0; i < temps.size() / 2; i++)
            vertecies[i] = Float.parseFloat(temps.get(i));

        return vertecies;
    }

    public float[] readObjNormals() throws IOException {

        List<String> temps = new ArrayList<String>();
        String string;
        InputStream is = getResources().openRawResource(R.raw.epic_parsed_list);
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            if(is != null) {
                while((string = reader.readLine()) != null) {

                    if(!string.equals("V") && !string.equals("N")) {
                        StringTokenizer st = new StringTokenizer(string);
                        while (st.hasMoreTokens()) {
                            string = st.nextToken();
                            string = string.replace(",", "");
                            temps.add(string);
                        }
                    }
                }
            }
        } finally {
            try {
                is.close();
            } catch (Throwable ignore) { }
        }

        float[] normals = new float[temps.size() / 2];
        for(int i = 0; i < temps.size() / 2; i++)
            normals[i] = Float.parseFloat(temps.get(i + temps.size() / 2));

        return normals;
    }

    public float[] createColorCoords(float color) {

        double s = ( numCubeVerts * (4.0 / 3.0));
        final int SIZE = (int)s;
        Log.d("SIZE: ", Integer.toString(SIZE));
        float[] colorCoords = new float[SIZE];

        for(int i = 0; i < SIZE; i++)
            colorCoords[i] = color;

        return colorCoords;
    }
}