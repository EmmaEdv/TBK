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
import com.metaio.tools.io.AssetsManager;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends ARViewActivity {

    /**
     * Reference to loaded metaioman geometry
     */
    private IGeometry mMetaioMan;
    private IGeometry mMetaioMan2;
    private IGeometry mMetaioMan3;

    /**
     * Tiger geometry
     */
    private IGeometry mTiger;
    private IGeometry mTiger2;
    private IGeometry mTiger3;

    private TrackingValuesVector poses;

    /**
     * Currently loaded tracking configuration file
     */
    File trackingConfigFile;

    private MetaioSDKCallbackHandler mCallbackHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        mCallbackHandler = new MetaioSDKCallbackHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallbackHandler.delete();
        mCallbackHandler = null;
    }

    @Override
    protected int getGUILayout() {
        return R.layout.empty_layout;
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

            // aDDED
            final File metaioManModel2 = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/metaioman.md2");
            final File metaioManModel3 = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/metaioman.md2");


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

            // Added

            if (metaioManModel2 != null) {
                mMetaioMan2 = metaioSDK.createGeometry(metaioManModel2);
                if (mMetaioMan != null) {
                    // Set geometry properties
                    mMetaioMan2.setScale(4f);
                    mMetaioMan2.setVisible(false);
                    MetaioDebug.log("Loaded geometry "+metaioManModel2);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+metaioManModel2);
            }

            if (metaioManModel3 != null) {
                mMetaioMan3 = metaioSDK.createGeometry(metaioManModel3);
                if (mMetaioMan3 != null) {
                    // Set geometry properties
                    mMetaioMan3.setScale(4f);
                    mMetaioMan3.setVisible(false);
                    MetaioDebug.log("Loaded geometry "+metaioManModel3);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+metaioManModel3);
            }

            // Load tiger model
            final File tigerModelPath =
                    AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/tiger.md2");

            //Added
            final File tigerModelPath2 =
                    AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/tiger.md2");
            final File tigerModelPath3 =
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

            // Added
            if (tigerModelPath2 != null) {
                mTiger2 = metaioSDK.createGeometry(tigerModelPath2);
                if (mTiger2 != null) {
                    // Set geometry properties
                    mTiger2.setScale(20f);
                    mTiger2.setVisible(false);
                    MetaioDebug.log("Loaded geometry "+tigerModelPath2);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+tigerModelPath2);
            }

            if (tigerModelPath3 != null) {
                mTiger3 = metaioSDK.createGeometry(tigerModelPath3);
                if (mTiger3 != null) {
                    // Set geometry properties
                    mTiger3.setScale(20f);
                    mTiger3.setVisible(false);
                    MetaioDebug.log("Loaded geometry "+tigerModelPath3);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+tigerModelPath3);
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
            if (mTiger != null && mMetaioMan != null
                /* ADDED --> */ && mTiger2 != null && mTiger3 != null && mMetaioMan2 != null && mMetaioMan3 != null) {

                for (int i = 0; i < trackingValues.size(); i++) {

                    //TrackingValue is received from TrackingData_PictureMarker.xml
                    final TrackingValues tv = trackingValues.get(i);

                    if (metaioSDK != null) {
                        // get all detected poses/targets
                        poses = metaioSDK.getTrackingValues();


                        //Cases for each part of the chair (shows tiger/metaioman atm, should show parts of chair)
                        //if we have detected one, attach our metaio man to this coordinate system Id
                        if (poses.size() != 0) {

                            mTiger.setVisible(true);
                            mMetaioMan.setVisible(true);

                            //Added

                            mTiger2.setVisible(true);
                            mTiger3.setVisible(true);

                            mMetaioMan2.setVisible(true);
                            mMetaioMan3.setVisible(true);

                            mTiger.setCoordinateSystemID(1);
                            mMetaioMan.setCoordinateSystemID(2);
                            mTiger2.setCoordinateSystemID(3);
                            mMetaioMan2.setCoordinateSystemID(4);
                            mTiger3.setCoordinateSystemID(5);
                            mMetaioMan3.setCoordinateSystemID(6);
                        }
                    }

                }
            }
        }
    }
}
