package com.example.viktor.agilprojektaugmentedreality;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    private IGeometry sida1;
    private IGeometry sida2;
    private IGeometry rygg_top;
    private IGeometry rygg_mid;
    private TutorialCases TC = new TutorialCases();

    private TrackingValuesVector poses;

    TextView topText, infoText;

    Button prevButton, nextButton;

    RelativeLayout infoBox;

    ImageButton helpButton;

    boolean helpClick = true;

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

        Typeface font = Typeface.createFromAsset(getAssets(), "Berlin Sans FB.ttf");

        topText = (TextView) mGUIView.findViewById(R.id.topText0);
        infoText = (TextView) mGUIView.findViewById(R.id.infoText);
        prevButton = (Button) mGUIView.findViewById(R.id.prevButton);
        nextButton = (Button) mGUIView.findViewById(R.id.nextButton);
        helpButton = (ImageButton) mGUIView.findViewById(R.id.helpBtn);
        infoBox = (RelativeLayout) mGUIView.findViewById(R.id.infoBox);

        topText.setTypeface(font);
        infoText.setTypeface(font);
        prevButton.setTypeface(font);
        nextButton.setTypeface(font);
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
        //topText.setVisibility(View.VISIBLE);

        //findViewById(R.id.infoBox).setVisibility(View.VISIBLE);
        //prevButton.setVisibility(View.VISIBLE);
        //infoText.setVisibility(View.GONE);
    }

    /**
     * prevStep button click, for previous step in the building schematics
     */
    public void prevStep(View v) {

        if(buildStep > 0) {
            buildStep--;
        }
        showStep();

        topText.setVisibility(View.INVISIBLE);
        prevButton.setVisibility(View.GONE);
    }

    public void btnHelp(View v)
    {
        if(helpClick)
        {
            infoBox.setVisibility(View.INVISIBLE);
            helpButton.setImageResource(R.drawable.wrench_button);
            helpClick = false;
        }
        else
        {
            infoBox.setVisibility(View.VISIBLE);
            helpButton.setImageResource(R.drawable.wrench_button_pressed);
            helpClick = true;
        }
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

        popup.show();

        if(sitsFound){
            item_sits.setTitle("Sits (Found)");
        }
        else
            item_sits.setTitle("Sits (Not found)");

        if(leftSideFound){
            item_right.setTitle("Höger ben (Found)");
        }
        else
            item_right.setTitle("Höger ben (Not found");

        if(ryggMidFound){
            item_ryggmid.setTitle("Rygg mid (Found)");
        }
        else
            item_ryggmid.setTitle("Rygg mid (Not found)");

        if(ryggTopFound){
            item_ryggtop.setTitle("Rygg top (Found)");
        }
        else
            item_ryggtop.setTitle("Rygg top (Not found)");
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
            final File sidaModel2 = AssetsManager.getAssetPathAsFile(getApplicationContext(), "pictureMarker/Assets/sida.obj");

            if (sitsModel != null) {
                sits = metaioSDK.createGeometry(sitsModel);
                sits.setRotation(new Rotation(1.57f, 0.0f, 0.0f));
                sits.setTranslation(new Vector3d(-2300.0f, 2800.0f, -1300.0f));
                sits.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "seatTexture.png"));
                sits.setTransparency(0.5f);

                if (sits != null) {
                    // Set geometry properties
                    sits.setScale(24.0f);
                    sits.setVisible(false);
                    MetaioDebug.log("Loaded geometry " + sitsModel);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+sitsModel);
            }

            if (sidaModel != null) {
                //Specialare, samma model för båda sidorna, kan behövas bytas ut senare...
                sida1 = metaioSDK.createGeometry(sidaModel);
                sida1.setTranslation(new Vector3d(200.0f, -5500.0f, -1000.0f));
                sida1.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "leftSideTexture.png"));
                sida1.setTransparency(0.5f);

                sida2 = metaioSDK.createGeometry(sidaModel2);
                sida2.setRotation(new Rotation(0.0f, 3.14f, 0.0f));
                sida2.setTranslation(new Vector3d(200.0f, -5000.0f, -1000.0f));
                sida2.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "rightSideTexture.png"));
                sida2.setTransparency(0.5f);

                if (sida1 != null) {
                    // Set geometry properties
                    sida1.setScale(23.0f);
                    sida2.setScale(23.0f);

                    sida1.setVisible(false);
                    MetaioDebug.log("Loaded geometry "+sidaModel);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+sidaModel);
            }

            if (ryggToppModel != null) {
                rygg_top = metaioSDK.createGeometry(ryggToppModel);
                rygg_top.setRotation(new Rotation(0.0f, 1.57f, 0.0f));
                rygg_top.setTranslation(new Vector3d(1600.0f, -400.0f, -300.0f));
                rygg_top.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "backTopTexture.png"));
                rygg_top.setTransparency(0.5f);

                if (rygg_top != null) {
                    // Set geometry properties
                    rygg_top.setScale(24.0f);

                    rygg_top.setVisible(false);
                    MetaioDebug.log("Loaded geometry " + ryggToppModel);
                }
                else
                    MetaioDebug.log(Log.ERROR, "Error loading geometry: "+ryggToppModel);
            }

            if (ryggMidModel != null) {
                rygg_mid = metaioSDK.createGeometry(ryggMidModel);
                rygg_mid.setRotation(new Rotation(0.0f, 1.57f, 0.0f));
                rygg_mid.setTranslation(new Vector3d(-100.0f,4000f, -300.0f));
                rygg_mid.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "backMidTexture.png"));
                rygg_mid.setTransparency(0.5f);
                if (rygg_mid != null) {
                    // Set geometry properties
                    rygg_mid.setScale(22.0f);
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
            if (sida1 != null && sits != null && rygg_top != null && rygg_mid != null) {

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

                                        if (sits.getIsRendered()) {
                                            item_sits.setTitle("Sits (Found)");
                                            sitsFound = true;
                                        }
                                        if (sida1.getIsRendered()) {
                                            item_right.setTitle("Höger ben (Found)");
                                            leftSideFound = true;
                                        }

                                        if (rygg_mid.getIsRendered()) {
                                            item_ryggmid.setTitle("Rygg mid (Found)");
                                            ryggMidFound = true;
                                        }
                                        if (rygg_top.getIsRendered()) {
                                            item_ryggtop.setTitle("Rygg top (Found)");
                                            ryggTopFound = true;
                                        }
                                    }
                                }
                            });

                          //  sits.setVisible(true);
                          //  rygg_mid.setVisible(true);
                           // rygg_top.setVisible(true);

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
