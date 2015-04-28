package com.example.viktor.agilprojektaugmentedreality;

import android.net.Uri;
import android.os.Bundle;
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
    private IGeometry sida1;
    private IGeometry sida2;
    private IGeometry rygg_top;
    private IGeometry rygg_mid;
    private TutorialCases TC = new TutorialCases();

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

        //setVisible(true) for the objects that are included in that step
        switch(buildStep){
            case 0:
                sida1.setVisible(true);
                sida2.setVisible(true);
                sits.setVisible(true);
                rygg_mid.setVisible(true);
                rygg_top.setVisible(true);
                TC.case0(this.mGUIView);

            break;
            case 1:
                rygg_mid.setVisible(true);
                rygg_top.setVisible(true);
                sida1.setVisible(false);
                sida2.setVisible(false);
                sits.setVisible(false);
              TC.case1(this.mGUIView);
            break;
            case 2:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                sida1.setVisible(false);
                sida2.setVisible(false);
                sits.setVisible(true);
             TC.case2(this.mGUIView);

            break;
            case 3:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                sida1.setVisible(true);
                sida2.setVisible(false);
                sits.setVisible(false);
              TC.case3(this.mGUIView);
            break;
            case 4:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                sida1.setVisible(false);
                sida2.setVisible(true);
                sits.setVisible(false);
             TC.case4(this.mGUIView);

            break;
            case 5:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                sida1.setVisible(false);
                sida2.setVisible(false);
                sits.setVisible(false);
             TC.case5(this.mGUIView);
            break;
            case 6:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                sida1.setVisible(false);
                sida2.setVisible(false);
                sits.setVisible(false);
               TC.case6(this.mGUIView);
            break;
            case 7:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                sida1.setVisible(false);
                sida2.setVisible(false);
                sits.setVisible(false);
             TC.case7(this.mGUIView);
             break;

        }
    System.out.println("i showStep " + buildStep);
    }

    /**
     * nextStep button click, for next step in the building schematics
     */
    //View b = findViewById(R.id.infoBox);
    public void nextStep(View v) {
        if (buildStep < 7) {
            buildStep++;
        }

        showStep();

        //findViewById(R.id.infoBox).setVisibility(View.VISIBLE);



    }

    /**
     * prevStep button click, for previous step in the building schematics
     */
    public void prevStep(View v) {

        if(buildStep > 0) {
            buildStep--;
        }
        showStep();
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

            // Load all the geometries. First - Model
            // Load metaioman model
            final File sitsModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/sits.obj");
            final File sidaModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/sida.obj");
            final File ryggToppModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/rygg_top.obj");
            final File ryggMidModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/rygg_mid.obj");
            final File sidaModel2 = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/sida.obj");



            if (sitsModel != null) {
                sits = metaioSDK.createGeometry(sitsModel);
                sits.setRotation(new Rotation(1.57f, 0.0f, 0.0f));
                sits.setTranslation(new Vector3d(-900.0f, 900.0f, -600.0f));
                if (sits != null) {
                    // Set geometry properties
                    sits.setScale(11f);
                    sits.setVisible(false);
                    MetaioDebug.log("Loaded geometry "+sitsModel);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+sitsModel);
            }

            if (sidaModel != null) {
                sida1 = metaioSDK.createGeometry(sidaModel);
                sida2 = metaioSDK.createGeometry(sidaModel2);

                if (sida1 != null) {
                    // Set geometry properties
                    sida1.setScale(4f);
                    sida1.setVisible(false);
                    sida2.setScale(4f);
                    sida2.setVisible(false);
                    MetaioDebug.log("Loaded geometry "+sidaModel);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+sidaModel);
            }



            if (ryggToppModel != null) {
                rygg_top = metaioSDK.createGeometry(ryggToppModel);
                if (rygg_top != null) {
                    // Set geometry properties
                    rygg_top.setScale(4f);
                    rygg_top.setVisible(false);
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
                    MetaioDebug.log("Loaded geometry "+ryggMidModel);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+ryggMidModel);
            }

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
            if (sida1 != null && sits != null && rygg_top != null && rygg_mid != null) {

                for (int i = 0; i < trackingValues.size(); i++) {

                    //TrackingValue is received from TrackingData_PictureMarker.xml

                    //bort?
                    //final TrackingValues tv = trackingValues.get(i);

                    if (metaioSDK != null) {
                        // get all detected poses/targets
                        poses = metaioSDK.getTrackingValues();

                        //if we have detected one, attach our metaio man to this coordinate system Id
                        if (poses.size() != 0) {

                            if(buildStep==0) {
                                sida1.setVisible(true);
                                sida2.setVisible(true);
                                rygg_mid.setVisible(true);
                                rygg_top.setVisible(true);
                                sits.setVisible(true);

                            }

                            if(buildStep==1) {
                                rygg_mid.setVisible(true);
                                rygg_top.setVisible(true);
                            }

                            if(buildStep==2) {
                                sits.setVisible(true);
                            }

                            if(buildStep==3) {
                                sida1.setVisible(true);
                            }
                            if(buildStep==4) {
                                sida2.setVisible(true);
                            }


                            //Added

                            sida1.setCoordinateSystemID(1);
                            sida2.setCoordinateSystemID(5);
                            sits.setCoordinateSystemID(2);
                            rygg_mid.setCoordinateSystemID(3);
                            rygg_top.setCoordinateSystemID(4);

                        }
                    }
                }
            }
        }
    }
}
