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
    private IGeometry sits;
    private IGeometry sida;
    private IGeometry rygg_top;
    private IGeometry rygg_mid;


    /**
     * Tiger geometry
     */
    //private IGeometry mTiger;
    //private IGeometry mTiger2;
    //private IGeometry mTiger3;

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
            final File sitsModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/sits.obj");
            final File sidaModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/sida.obj");
            final File ryggToppModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/rygg_top.obj");
            final File ryggMidModel = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/rygg_mid.obj");


            if (sitsModel != null) {
                sits = metaioSDK.createGeometry(sitsModel);
                if (sits != null) {
                    // Set geometry properties
                    sits.setScale(4f);
                    sits.setVisible(false);
                    MetaioDebug.log("Loaded geometry "+sitsModel);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+sitsModel);
            }

            if (sidaModel != null) {
                sida = metaioSDK.createGeometry(sidaModel);
                if (sida != null) {
                    // Set geometry properties
                    sida.setScale(4f);
                    sida.setVisible(false);
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
            if (sida != null && sits != null && rygg_top != null && rygg_mid != null) {

                for (int i = 0; i < trackingValues.size(); i++) {

                    //TrackingValue is received from TrackingData_PictureMarker.xml

                    //bort?
                    //final TrackingValues tv = trackingValues.get(i);

                    if (metaioSDK != null) {
                        // get all detected poses/targets
                        poses = metaioSDK.getTrackingValues();


                        //Cases for each part of the chair (shows tiger/metaioman atm, should show parts of chair)
                        //if we have detected one, attach our metaio man to this coordinate system Id
                        if (poses.size() != 0) {

                            sits.setVisible(true);
                            sida.setVisible(true);
                            rygg_mid.setVisible(true);
                            rygg_top.setVisible(true);

                            sida.setCoordinateSystemID(1);
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
