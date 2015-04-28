package com.example.viktor.agilprojektaugmentedreality;

import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

import org.apache.http.impl.conn.Wire;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class CameraActivity extends ARViewActivity {

    /**
     * Reference to loaded metaioman geometry
     */
    private IGeometry sits;
    private IGeometry sida_vanster;
    private IGeometry sida_hoger;
    private IGeometry rygg_top;
    private IGeometry rygg_mid;

    private TrackingValuesVector poses;

    /**
     * Currently loaded tracking configuration file
     */
    File trackingConfigFile;

    private MetaioSDKCallbackHandler mCallbackHandler;

    private int buildStep = 0;

    @Override
    protected int getGUILayout()
    {
        // Attaching layout to the activity
        return R.layout.camera_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.camera_activity);

        mCallbackHandler = new MetaioSDKCallbackHandler();
    }

    public void showStep() {

        mSurfaceView.queueEvent(new Runnable()
        {
            @Override
            public void run() {
        //setVisible(true) for the objects that are included in that step
                switch(buildStep) {
                    case 0:
                        sida_hoger.setVisible(true);
                        sida_vanster.setVisible(true);
                        sits.setVisible(true);
                        rygg_mid.setVisible(true);
                        rygg_top.setVisible(true);
                        break;
                    case 1:
                        sida_hoger.setVisible(false);
                        sida_vanster.setVisible(false);
                        sits.setVisible(false);
                        rygg_mid.setVisible(false);
                        rygg_top.setVisible(false);
                        break;
                }
            }
        });
    System.out.println("i showStep " + buildStep);
    }

    /**
     * nextStep button click, for next step in the building schematics
     */
    //View b = findViewById(R.id.infoBox);
    public void nextStep(View v) {
        if (buildStep < 1) {
            buildStep++;
        }

        showStep();
        findViewById(R.id.topText).setVisibility(View.VISIBLE);

        //findViewById(R.id.infoBox).setVisibility(View.VISIBLE);
        findViewById(R.id.prevButton).setVisibility(View.VISIBLE);
        findViewById(R.id.infoText).setVisibility(View.GONE);

        findViewById(R.id.infoImage).setVisibility(View.VISIBLE);


    }

    /**
     * prevStep button click, for previous step in the building schematics
     */
    public void prevStep(View v) {

        if(buildStep > 0) {
            buildStep--;
        }
        showStep();
        findViewById(R.id.topText).setVisibility(View.INVISIBLE);
        findViewById(R.id.prevButton).setVisibility(View.GONE);


    }


    public void btnCancel(View v)
    {

        findViewById(R.id.infoBox).setVisibility(View.INVISIBLE);

    }

    public void btnHelp(View v)
    {
        findViewById(R.id.infoBox).setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallbackHandler.delete();
        mCallbackHandler = null;
    }

    @Override
    protected void loadContents() {
        try {
            //Copied from AssetsExtracter/doInBackground
            try {
                // Extract all assets and overwrite existing files if debug build
                AssetsManager.extractAllAssets(getApplicationContext(), BuildConfig.DEBUG);
            }
            catch (IOException e) {
                MetaioDebug.log(Log.ERROR, "Error extracting assets: "+e.getMessage());
                MetaioDebug.printStackTrace(Log.ERROR, e);
            }

            //Copied from TutorialTrackingSamples & TutorialInstantSamples
            trackingConfigFile = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/TrackingData_PictureMarker.xml");
            MetaioDebug.log("Tracking Config path = "+trackingConfigFile);

            boolean result = metaioSDK.setTrackingConfiguration(trackingConfigFile);
            MetaioDebug.log("Picture Marker tracking data loaded: " + result);

            // Load all the geometries
            final File sitsModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/sitsCol.obj"); //sitsCol
            final File sidaHogerModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/hogerSidaCol.obj"); //hogerSidaCol
            final File sidaVansterModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/vansterSidaCol.obj"); //vansterSidaCol
            final File ryggToppModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/ryggToppCol.obj"); //ryggToppCol
            final File ryggMidModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/ryggMidCol.obj"); //ryggMidCol

            mSurfaceView.queueEvent(new Runnable()
            {
                @Override
                public void run() {
                    // any code here will be executed in OpenGL thread
                    // always load tracking data, 3D models or billboards here, but never perform any heavy processing or update GUI elements

                    if (sitsModel != null) {
                        sits = metaioSDK.createGeometry(sitsModel);
                        if (sits != null) {
                            // Set geometry properties
                            //sits.setRotation(new Rotation(1.57f, 0.0f, 0.0f));
                            //sits.setTranslation(new Vector3d(-900.0f, 900.0f, -600.0f));
                            sits.setScale(11f);
                            sits.setVisible(false);
                            //sits.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/sitsCol.mtl"));
                            //sits.setTexture("pictureMarker/Assets/sitsCol.mtl");  //Detta borde funka...
                            MetaioDebug.log("Loaded geometry "+sitsModel);
                        }
                        else
                            MetaioDebug.log(Log.ERROR, "Error loading geometry: "+sitsModel);
                    }

                    if (sidaVansterModel != null) {
                        sida_vanster = metaioSDK.createGeometry(sidaVansterModel);

                        if (sida_vanster != null) {
                            // Set geometry properties
                            //sida_vanster.setRotation(new Rotation(1.57f, 0.0f, 0.0f));
                            //sida_vanster.setTranslation(new Vector3d(-900.0f, 900.0f, -600.0f));
                            sida_vanster.setScale(4f);
                            sida_vanster.setVisible(false);
                            //sida_vanster.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/vansterSidaCol.mtl"));
                            //sida_vanster.setTexture("pictureMarker/Assets/vansterSidaCol.mtl");  //Detta borde funka...
                            MetaioDebug.log("Loaded geometry "+sidaVansterModel);
                        }
                        else
                            MetaioDebug.log(Log.ERROR, "Error loading geometry: "+sidaVansterModel);
                    }

                    if (sidaHogerModel != null) {
                        sida_hoger = metaioSDK.createGeometry(sidaHogerModel);
                        if (sida_hoger != null) {
                            // Set geometry properties
                            sida_hoger.setScale(4f);
                            sida_hoger.setVisible(false);
                            //sida_hoger.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/hogerSidaCol.mtl"));
                            //sida_hoger.setTexture("pictureMarker/Assets/hogerSidaCol.mtl");  //Detta borde funka...
                            MetaioDebug.log("Loaded geometry "+sidaHogerModel);
                        }
                        else
                            MetaioDebug.log(Log.ERROR, "Error loading geometry: "+sidaHogerModel);
                    }

                    if (ryggToppModel != null) {
                        rygg_top = metaioSDK.createGeometry(ryggToppModel);
                        if (rygg_top != null) {
                            // Set geometry properties
                            rygg_top.setScale(4f);
                            rygg_top.setVisible(false);
                            //rygg_top.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/ryggTopCol.mtl"));
                            //rygg_top.setTexture("pictureMarker/Assets/ryggTopCol.mtl");  //Detta borde funka...
                            MetaioDebug.log("Loaded geometry "+ryggToppModel);
                        }
                        else
                            MetaioDebug.log(Log.ERROR, "Error loading geometry: "+ryggToppModel);
                    }

                    if (ryggMidModel != null) {
                        rygg_mid = metaioSDK.createGeometry(ryggMidModel);
                        if (rygg_mid != null) {
                            // Set geometry properties
                            rygg_mid.setScale(4f);
                            rygg_mid.setVisible(false);
                            //rygg_mid.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/ryggMidCol.mtl"));
                            //rygg_mid.setTexture("pictureMarker/Assets/ryggMidCol.mtl"); //Detta borde funka...
                            MetaioDebug.log("Loaded geometry "+ryggMidModel);
                        }
                        else
                            MetaioDebug.log(Log.ERROR, "Error loading geometry: "+ryggMidModel);
                    }
                }
            });

        }
        catch (Exception e) {
            MetaioDebug.printStackTrace(Log.ERROR, e);
        }
    }

    @Override
    protected void onGeometryTouched(IGeometry geometry) {

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
    protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
        return mCallbackHandler;
    }

    final class MetaioSDKCallbackHandler extends IMetaioSDKCallback {

        @Override
        public void onSDKReady() {
            // show GUI
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGUIView.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onTrackingEvent(TrackingValuesVector trackingValues) {

            // if we detect any target, we bind the loaded geometry to this target
            if (sida_hoger != null && sida_vanster != null && sits != null && rygg_top != null && rygg_mid != null) {

                for (int i = 0; i < trackingValues.size(); i++) {

                    //TrackingValue is received from TrackingData_PictureMarker.xml

                    if (metaioSDK != null) {
                        // get all detected poses/targets
                        poses = metaioSDK.getTrackingValues();

                        //if we have detected one, attach our metaio man to this coordinate system Id
                        if (poses.size() != 0) {

                            if(buildStep==0) {
                                sits.setVisible(true);
                                rygg_mid.setVisible(true);
                                rygg_top.setVisible(true);
                                sida_hoger.setVisible(true);
                                sida_vanster.setVisible(true);
                            }
                            //Added

                            sida_hoger.setCoordinateSystemID(1);
                            sida_vanster.setCoordinateSystemID(2);
                            sits.setCoordinateSystemID(3);
                            rygg_mid.setCoordinateSystemID(4);
                            rygg_top.setCoordinateSystemID(5);
                        }
                    }
                }
            }
        }
    }
}
