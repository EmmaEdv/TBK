package com.example.viktor.agilprojektaugmentedreality;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector3d;
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

    private TrackingValuesVector poses;

    /**
     * Currently loaded tracking configuration file
     */
    File trackingConfigFile;

    private MetaioSDKCallbackHandler mCallbackHandler;

    private int buildStep = 0;

    // Da popup menu
    PopupMenu popup;

    MenuItem item_sits;
    MenuItem item_right;
    MenuItem item_left;
    MenuItem item_ryggtop;
    MenuItem item_ryggmid;

    boolean initiated = false;

    boolean sitsFound = false;
    boolean leftSideFound = false;
    boolean rightSideFound = false;
    boolean ryggMidFound = false;
    boolean ryggTopFound = false;

    // En lång radda spannable strings
    // Behövs för att ändra färg på menu items
    SpannableString itemSitsTextF;
    SpannableString itemSitsTextNF;

    SpannableString itemRightSideF;
    SpannableString itemRightSideNF;

    SpannableString itemLeftSideF;
    SpannableString itemLeftSideNF;

    SpannableString itemRyggMidF;
    SpannableString itemRyggMidNF;

    SpannableString itemRyggTopF;
    SpannableString itemRyggTopNF;

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
                sida.setVisible(false);
            break;
            case 0:
                sida.setVisible(true);

            break;
        }

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


    public void btnCancel(View v) {
        findViewById(R.id.infoBox).setVisibility(View.INVISIBLE);
    }

    public void btnHelp(View v) {
        findViewById(R.id.infoBox).setVisibility(View.VISIBLE);
    }

    //List
    public void showPopup(View v) {

        popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_cameralist, popup.getMenu());

        item_sits = popup.getMenu().getItem(0);
        item_right = popup.getMenu().getItem(1);
        item_left = popup.getMenu().getItem(2);
        item_ryggtop = popup.getMenu().getItem(3);
        item_ryggmid = popup.getMenu().getItem(4);

        itemSitsTextNF = new SpannableString("Sits (Not found)");
        itemSitsTextNF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.sits)), 0, itemSitsTextNF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemSitsTextF = new SpannableString("Sits (Found)");
        itemSitsTextF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.sits)), 0, itemSitsTextF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemRightSideNF = new SpannableString("Höger sida (Not found)");
        itemRightSideNF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.sidaHoger)), 0, itemRightSideNF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemRightSideF = new SpannableString("Höger sida (Found)");
        itemRightSideF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.sidaHoger)), 0, itemRightSideF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemLeftSideNF = new SpannableString("Höger sida (Not found)");
        itemLeftSideNF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.sidaVanster)), 0, itemLeftSideNF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemLeftSideF = new SpannableString("Höger sida (Found)");
        itemLeftSideF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.sidaVanster)), 0, itemLeftSideF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemRyggMidNF  = new SpannableString("Rygg mitt (Not found)");
        itemRyggMidNF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.ryggMid)), 0, itemRyggMidNF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemRyggMidF  = new SpannableString("Rygg mitt (Found)");
        itemRyggMidF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.ryggMid)), 0, itemRyggMidF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemRyggTopNF  = new SpannableString("Rygg topp (Not found)");
        itemRyggTopNF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.ryggTop)), 0, itemRyggTopNF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemRyggTopF  = new SpannableString("Rygg topp (Found)");
        itemRyggTopF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.ryggTop)), 0, itemRyggTopF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        popup.show();

        if(sitsFound){
            item_sits.setTitle(itemSitsTextF);
        }
        else
            item_sits.setTitle(itemSitsTextNF);

        if(leftSideFound){
            item_right.setTitle(itemRightSideF);
        }
        else
            item_right.setTitle(itemRightSideNF);

        if(ryggMidFound){
            item_ryggmid.setTitle(itemRyggMidF);
        }
        else
            item_ryggmid.setTitle(itemRyggMidNF);

        if(ryggTopFound){
            item_ryggtop.setTitle(itemRyggTopF);
        }
        else
            item_ryggtop.setTitle(itemRyggTopNF);


    }

    public void listBtnClick(View v) {
        //findViewById(R.id.infoBox).setVisibility(View.INVISIBLE);
        initiated = true;
        showPopup(v);
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


            if (sitsModel != null) {
                sits = metaioSDK.createGeometry(sitsModel);
                sits.setRotation(new Rotation(1.57f, 0.0f, 0.0f));
                sits.setTranslation(new Vector3d(-900.0f, 900.0f, -600.0f));

                if (sits != null) {
                    // Set geometry properties
                    sits.setScale(11f);
                    sits.setVisible(false);
                    MetaioDebug.log("Loaded geometry " + sitsModel);
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
                    MetaioDebug.log("Loaded geometry " + sidaModel);
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
                    MetaioDebug.log("Loaded geometry " + ryggToppModel);
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
                    MetaioDebug.log("Loaded geometry " + ryggMidModel);
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

                    if (metaioSDK != null) {
                        // get all detected poses/targets
                        poses = metaioSDK.getTrackingValues();

                        //if we have detected one, attach our metaio man to this coordinate system Id
                        if (poses.size() != 0) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    // If popuplist has been created
                                    if(initiated) {

                                        // Tillåter oss att ändra listan dynamiskt
                                        if (sits.getIsRendered()) {
                                            item_sits.setTitle(itemSitsTextF);
                                            sitsFound = true;
                                        }
                                        if (sida.getIsRendered()) {
                                            item_right.setTitle(itemRightSideF);
                                            leftSideFound = true;
                                        }

                                        if (rygg_mid.getIsRendered()) {
                                            item_ryggmid.setTitle(itemRyggMidF);
                                            ryggMidFound = true;
                                        }
                                        if (rygg_top.getIsRendered()) {
                                            item_ryggtop.setTitle(itemRyggTopF);
                                            ryggTopFound = true;
                                        }
                                    }
                                }
                            });

                            sits.setVisible(true);
                            rygg_mid.setVisible(true);
                            rygg_top.setVisible(true);


                            if(buildStep==0) {
                                sida.setVisible(true);
                            }
                            //Added

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
