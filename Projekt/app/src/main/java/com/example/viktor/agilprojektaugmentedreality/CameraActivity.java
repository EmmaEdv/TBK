package com.example.viktor.agilprojektaugmentedreality;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends ARViewActivity {

    /**
     * Reference to loaded metaioman geometry
     */
    private IGeometry mMetaioMan;

    /**
     * Tiger geometry
     */
    private IGeometry mTiger;

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
            case 1:
                mMetaioMan.setVisible(false);
            break;
            case 0:
                mMetaioMan.setVisible(true);

            break;
        }
    System.out.println("i showStep " + buildStep);
    }

    /**
     * nextStep button click, for next step in the building schematics
     */
    public void nextStep(View v) {
        if (buildStep < 1) {
            buildStep++;
        }
            showStep();
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
            final File metaioManModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/metaioman.md2");
            if (metaioManModel != null) {
                mMetaioMan = metaioSDK.createGeometry(metaioManModel);
                if (mMetaioMan != null) {
                    // Set geometry properties
                    mMetaioMan.setScale(4f);
                    mMetaioMan.setVisible(false);
                    MetaioDebug.log("Loaded geometry "+metaioManModel);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+metaioManModel);
            }

            // Load tiger model
            final File tigerModelPath =
                    AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/tiger.md2");
            if (tigerModelPath != null) {
                mTiger = metaioSDK.createGeometry(tigerModelPath);
                if (mTiger != null) {
                    // Set geometry properties
                    mTiger.setScale(20f);
                    mTiger.setVisible(false);
                    MetaioDebug.log("Loaded geometry "+tigerModelPath);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+tigerModelPath);
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
            if (mTiger != null && mMetaioMan != null) {
                for (int i = 0; i < trackingValues.size(); i++) {
                    //TrackingValue is received from TrackingData_PictureMarker.xml
                    final TrackingValues tv = trackingValues.get(i);

                    if (tv.isTrackingState()) {
                        //Cases for each part of the chair (shows tiger/metaioman atm, should show parts of chair)
                        switch (tv.getCoordinateSystemID()) {
                            case 1:
                                if(buildStep==0) {
                                    mTiger.setVisible(true);
                                    mMetaioMan.setVisible(false);
                                    mTiger.setCoordinateSystemID(tv.getCoordinateSystemID());
                                }
                                System.out.println("case1 " + buildStep);
                                break;

                            case 2:
                                if(buildStep==0) {
                                    mMetaioMan.setVisible(true);
                                    mTiger.setVisible(false);
                                    mMetaioMan.setCoordinateSystemID(tv.getCoordinateSystemID());
                                }
                                System.out.println("case2 " + buildStep);
                                    break;

                            case 3:
                                if(buildStep==0) {
                                    mTiger.setVisible(true);
                                    mMetaioMan.setVisible(false);
                                    mTiger.setCoordinateSystemID(tv.getCoordinateSystemID());
                                }
                                System.out.println("case3 " + buildStep);
                                break;

                            case 4:
                                if(buildStep==0) {
                                    mMetaioMan.setVisible(true);
                                    mTiger.setVisible(false);
                                    mMetaioMan.setCoordinateSystemID(tv.getCoordinateSystemID());
                                }
                                System.out.println("case4 " + buildStep);
                                break;

                            case 5:
                                if(buildStep==0) {
                                    mTiger.setVisible(true);
                                    mMetaioMan.setVisible(false);
                                    mTiger.setCoordinateSystemID(tv.getCoordinateSystemID());
                                }
                                System.out.println("case5 " + buildStep);
                                break;

                            case 6:
                                if(buildStep==0) {
                                    mMetaioMan.setVisible(true);
                                    mTiger.setVisible(false);
                                    mMetaioMan.setCoordinateSystemID(tv.getCoordinateSystemID());
                                }
                                System.out.println("case6 " + buildStep);
                                break;

                            default:
                                System.out.println("default " + buildStep);
                                break;
                        }
                    }
                }
            }
        }
    }
}
