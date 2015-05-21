package com.example.viktor.agilprojektaugmentedreality;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.Camera;
import com.metaio.sdk.jni.CameraVector;
import com.metaio.sdk.jni.ELIGHT_TYPE;
import com.metaio.sdk.jni.GestureHandler;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.ILight;
import com.metaio.sdk.jni.IMetaioSDK;
import com.metaio.sdk.jni.IMetaioSDKAndroid;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.sdk.jni.Vector2di;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

import junit.framework.Test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class CameraActivity extends ARViewActivity {

    private IGeometry sits;
    private IGeometry rightSide;
    private IGeometry leftSide;
    private IGeometry rygg_top;
    private IGeometry rygg_mid;

    private IGeometry stepOne;
    private IGeometry stepThree;
    private IGeometry stepTwo;
    private IGeometry stepFour;
    private IGeometry stepFive;
    private IGeometry stepSix;
    private ILight mDirectionalLight;



    private TrackingValuesVector poses;

    TextView topText, infoText, sideRText, sideLText, seatText, backMText, backTText,
            sideRFound, sideLFound, seatFound, backMFound, backTFound;
    Button prevButton, nextButton, animateButton;
    RelativeLayout infoBox, popupList;
    ImageButton helpButton, listButton, arrowButton;
    ImageView infoImage;

    // Set the booleans for "lilla listan"
    // Get the value from intent, default is false
    boolean sitsFound;
    boolean leftSideFound;
    boolean rightSideFound;
    boolean ryggMidFound;
    boolean ryggTopFound;

    boolean checkCamera = true;

    float currentYrotation;
    float currentXrotation;
    float currentZrotation;



    //Currently loaded tracking configuration file

    File trackingConfigFile;

    private MetaioSDKCallbackHandler mCallbackHandler;

    private int buildStep = 0;

    //Bools to check if button in topbar is clicked or not
    boolean helpClick = true;
    boolean listClick = false;

    //stepOne.setRotation(new Rotation(-0.785f, -0.3925f, 1.57f));


    private float mPreviousX =-0.785f;
    private float mPreviousY =-0.3925f;
    private float mDensity;



    @Override
    protected int getGUILayout() {
        // Attaching layout to the activity
        return R.layout.camera_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Locks the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
       mDensity= getResources().getDisplayMetrics().density;
        mCallbackHandler = new MetaioSDKCallbackHandler();



        Typeface font = Typeface.createFromAsset(getAssets(), "Berlin Sans FB.ttf");

        topText = (TextView) mGUIView.findViewById(R.id.topText);
        infoText = (TextView) mGUIView.findViewById(R.id.infoText);
        prevButton = (Button) mGUIView.findViewById(R.id.prevButton);
        nextButton = (Button) mGUIView.findViewById(R.id.nextButton);
        animateButton = (Button) mGUIView.findViewById(R.id.goAnimate);
        helpButton = (ImageButton) mGUIView.findViewById(R.id.helpBtn);
        listButton = (ImageButton) mGUIView.findViewById(R.id.listBtn);
        arrowButton = (ImageButton) mGUIView.findViewById(R.id.arrowBtn);
        infoBox = (RelativeLayout) mGUIView.findViewById(R.id.infoBox);
        infoImage = (ImageView) mGUIView.findViewById(R.id.infoImage);

        popupList = (RelativeLayout) mGUIView.findViewById(R.id.popupList);
        sideLText = (TextView) mGUIView.findViewById(R.id.sideL_text);
        sideRText = (TextView) mGUIView.findViewById(R.id.sideR_text);
        seatText = (TextView) mGUIView.findViewById(R.id.seat_text);
        backMText = (TextView) mGUIView.findViewById(R.id.backM_text);
        backTText = (TextView) mGUIView.findViewById(R.id.backT_text);
        sideLFound = (TextView) mGUIView.findViewById(R.id.sideL_found);
        sideRFound = (TextView) mGUIView.findViewById(R.id.sideR_found);
        seatFound = (TextView) mGUIView.findViewById(R.id.seat_found);
        backMFound = (TextView) mGUIView.findViewById(R.id.backM_found);
        backTFound = (TextView) mGUIView.findViewById(R.id.backT_found);

        topText.setTypeface(font);
        infoText.setTypeface(font);
        prevButton.setTypeface(font);
        nextButton.setTypeface(font);

        animateButton.setTypeface(font);
        sideLText.setTypeface(font);
        sideRText.setTypeface(font);
        seatText.setTypeface(font);
        backTText.setTypeface(font);
        backMText.setTypeface(font);
        sideLFound.setTypeface(font);
        sideRFound.setTypeface(font);
        seatFound.setTypeface(font);
        backTFound.setTypeface(font);
        backMFound.setTypeface(font);

        // Set the booleans for "lilla listan"
        // Get the value from intent, default is false
        sitsFound = getIntent().getBooleanExtra("foundSits", false);
        leftSideFound = getIntent().getBooleanExtra("foundLeftSide", false);
        rightSideFound = getIntent().getBooleanExtra("foundRightSide", false);
        ryggMidFound = getIntent().getBooleanExtra("foundRyggMid", false);
        ryggTopFound = getIntent().getBooleanExtra("foundRyggTop", false);


    }


    public void showStep() {
        //setVisible(true) for the objects that are included in that step
        switch(buildStep){
            case 0:
                rightSide.setVisible(true);
                leftSide.setVisible(true);
                sits.setVisible(true);
                rygg_mid.setVisible(true);
                rygg_top.setVisible(true);
                prevButton.setVisibility(View.GONE);
                animateButton.setVisibility(View.GONE);
                topText.setText(R.string.step_zero);
                infoImage.setVisibility(View.INVISIBLE);
                infoText.setText(R.string.stepStart);
                infoText.setVisibility(View.VISIBLE);
            break;
            case 1:
                rygg_mid.setVisible(true);
                rygg_top.setVisible(true);
                rightSide.setVisible(false);
                leftSide.setVisible(false);
                sits.setVisible(false);
                prevButton.setVisibility(View.VISIBLE);
                animateButton.setVisibility(View.VISIBLE);
                topText.setText(R.string.step_one);
                infoText.setVisibility(View.INVISIBLE);
                infoImage.setVisibility(View.VISIBLE);
                infoImage.setImageResource(R.drawable.step1_color);

                break;
            case 2:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                rightSide.setVisible(false);
                leftSide.setVisible(false);
                sits.setVisible(true);
                topText.setText(R.string.step_two);
                infoImage.setImageResource(R.drawable.step2_color);
                break;
            case 3:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                rightSide.setVisible(true);
                leftSide.setVisible(false);
                sits.setVisible(false);
                topText.setText(R.string.step_three);
                infoImage.setImageResource(R.drawable.step3_color);
            break;
            case 4:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                rightSide.setVisible(false);
                leftSide.setVisible(true);
                sits.setVisible(false);
                topText.setText(R.string.step_four);
                infoImage.setImageResource(R.drawable.step4_color);
            break;
            case 5:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                rightSide.setVisible(false);
                leftSide.setVisible(false);
                sits.setVisible(false);
                topText.setText(R.string.step_five);
                infoImage.setImageResource(R.drawable.step5_color);
            break;
            case 6:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                rightSide.setVisible(false);
                leftSide.setVisible(false);
                sits.setVisible(false);
                nextButton.setVisibility(View.VISIBLE);
                topText.setText(R.string.step_six);
                animateButton.setVisibility(View.VISIBLE);
                infoImage.setImageResource(R.drawable.step6_color);
                infoText.setVisibility(View.INVISIBLE);
                infoImage.setVisibility(View.VISIBLE);
            break;
            case 7:
                rygg_mid.setVisible(false);
                rygg_top.setVisible(false);
                rightSide.setVisible(false);
                leftSide.setVisible(false);
                sits.setVisible(false);
                nextButton.setVisibility(View.GONE);
                topText.setText(R.string.step_seven);
                animateButton.setVisibility(View.GONE);
                infoImage.setVisibility(View.INVISIBLE);
                infoText.setText(R.string.stepDone);
                infoText.setVisibility(View.VISIBLE);
             break;
        }
    }

    /**
     * nextStep button click, for next step in the building schematics
     */
    public void nextStep(View v) {

        if (buildStep < 7)
            buildStep++;


        showStep();

        showinfoBox();
        topText.setVisibility(View.VISIBLE);
    }

    /**
     * prevStep button click, for previous step in the building schematics
     */
    public void prevStep(View v) {
        if(buildStep > 0) {
            buildStep--;
        }
        showStep();
        showinfoBox();
    }

    //Starts the camera after animation is done
    public void lightenCamera(){
        metaioSDK.startCamera();
        metaioSDK.resumeTracking();
        metaioSDK.setSeeThrough(false);
        prevButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        infoBox.setVisibility(View.VISIBLE);
        helpButton.setImageResource(R.drawable.wrench_button_pressed);
        helpClick = true;
        popupList.setVisibility(View.GONE);
        listClick = false;
        animateButton.setText(R.string.goAnimate);
    }

    //Darkens the camera before animation
    //infoBox is set to invisible, make sure you also handle the showInfoBox() function in prev and nextStep()
    public void darkenCamera() {
        metaioSDK.pauseTracking();
        metaioSDK.stopCamera();
        metaioSDK.setSeeThroughColor(150,150,150,255);
        metaioSDK.setSeeThrough(true);
        prevButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        infoBox.setVisibility(View.INVISIBLE);
        helpButton.setImageResource(R.drawable.wrench_button);
        helpClick = false;
        popupList.setVisibility(View.GONE);
        listClick = false;
        animateButton.setText(R.string.stopAnimate);
        createLight();
    }

    //Controls the button for darkening/lightening camera and animation
    public void animate(View v) {

        // Animate();
        if (checkCamera) {
            darkenCamera();
            startAnimating();
            animateButton.setVisibility(View.GONE);
            checkCamera = false;
        } /*else {
            lightenCamera();
            stopAnimating();
            animateButton.setVisibility(View.VISIBLE);
            checkCamera = true;
        }*/
    }

  //Creates light for the animation in the tutorial
    private void createLight(){
        metaioSDK.setAmbientLight(new Vector3d(0.05f));
        mDirectionalLight = metaioSDK.createLight();
        mDirectionalLight.setType(ELIGHT_TYPE.ELIGHT_TYPE_DIRECTIONAL);
        mDirectionalLight.setAmbientColor(new Vector3d(0.3f, 0.3f, 0.3f));
        mDirectionalLight.setDiffuseColor(new Vector3d(0.8f, 0.8f, 0.8f));
        mDirectionalLight.setCoordinateSystemID(0);
        mDirectionalLight.setEnabled(false);

    }
    //Controls what gets animated at a certain step in the tutorial

    private void startAnimating(){
        mDirectionalLight.setEnabled(true);
        switch(buildStep){
            case 1:
                currentYrotation =  0.785f;
                currentXrotation = -0.3925f;
                currentZrotation = 1.57f;
                stepOne.setCoordinateSystemID(0);
                stepOne.setDynamicLightingEnabled(true);
                stepOne.setScale(2.0f);
                stepOne.setTranslation(new Vector3d(0, 0, -10000));
                stepOne.setRotation(new Rotation(-0.3925f,-0.785f, 1.57f));
                stepOne.setDynamicLightingEnabled(true);
                stepOne.setVisible(true);
                stepOne.setAnimationSpeed(15);
                stepOne.startAnimation("Scene", true);
                break;

            case 2:
                currentYrotation = 2.0f;
                currentXrotation =  -1.0f;
                currentZrotation = -0.3f;
                stepTwo.setCoordinateSystemID(0);
                stepTwo.setScale(10.0f);
                stepTwo.setTranslation(new Vector3d(1000, 0, -12000));
                stepTwo.setRotation(new Rotation(2.0f, -1.0f, -0.3f));
                stepTwo.setDynamicLightingEnabled(true);
                stepTwo.setVisible(true);
                stepTwo.setAnimationSpeed(15);
                stepTwo.startAnimation("Scene", true);
                break;

            case 3:
                currentYrotation = 0.0f;
                currentXrotation = 1.57f;
                currentZrotation = 0.0f;
                stepThree.setCoordinateSystemID(0);
                stepThree.setDynamicLightingEnabled(true);
                stepThree.setScale(6.0f);
                stepThree.setTranslation(new Vector3d(0, -1200, -10000));
                stepThree.setRotation(new Rotation(0.0f, 1.57f, 0.0f));
                stepThree.setDynamicLightingEnabled(true);
                stepThree.setVisible(true);
                stepThree.setAnimationSpeed(15);
                stepThree.startAnimation("Scene", true);
                break;

            case 4:
                currentYrotation = -1.7f;
                currentXrotation = 2.2f;
                currentZrotation = 0.0f;
                stepFour.setCoordinateSystemID(0);
                stepFour.setDynamicLightingEnabled(true);
                stepFour.setScale(6.0f);
                stepFour.setTranslation(new Vector3d(900, 800, -8000));
                stepFour.setRotation(new Rotation(-1.7f, 2.2f, 0.0f));
                stepFour.setDynamicLightingEnabled(true);
                stepFour.setVisible(true);
                stepFour.setAnimationSpeed(15);
                stepFour.startAnimation("Scene", true);
                break;

            case 5:
                currentYrotation = -1.7f;
                currentXrotation = 2.2f;
                currentZrotation = 0.0f;
                stepFive.setCoordinateSystemID(0);
                stepFive.setDynamicLightingEnabled(true);
                stepFive.setScale(6.0f);
                stepFive.setTranslation(new Vector3d(900, 800, -8000));
                stepFive.setRotation(new Rotation(-1.7f, 2.2f, 0.0f));
                stepFive.setDynamicLightingEnabled(true);
                stepFive.setVisible(true);
                stepFive.setAnimationSpeed(15);
                stepFive.startAnimation("Scene", true);
                break;

            case 6:
                currentYrotation = 1.9f;
                currentXrotation = -1.1f;
                currentZrotation = 0.0f;
                stepSix.setCoordinateSystemID(0);
                stepSix.setDynamicLightingEnabled(true);
                stepSix.setScale(6.0f);
                stepSix.setTranslation(new Vector3d(900, -700, -8000));
                stepSix.setRotation(new Rotation(1.9f, -1.1f, 0.0f));
                stepSix.setDynamicLightingEnabled(true);
                stepSix.setVisible(true);
                stepSix.setAnimationSpeed(15);
                stepSix.startAnimation("Scene", true);
                break;
        }
    }

    //Stops the animation at a certain step
    private void stopAnimating(){
        mDirectionalLight.setEnabled(false);

        switch(buildStep){
            case 1:
                stepOne.setVisible(false);
                stepOne.stopAnimation();
                break;

            case 2:
                stepTwo.setVisible(false);
                stepTwo.stopAnimation();
                break;

            case 3:
                stepThree.setVisible(false);
                stepThree.stopAnimation();
                break;

            case 4:
                stepFour.setVisible(false);
                stepFour.stopAnimation();
                break;

            case 5:
                stepFive.setVisible(false);
                stepFive.stopAnimation();
                break;

            case 6:
                stepSix.setVisible(false);
                stepSix.stopAnimation();
                break;
        }
    }

    public void btnHelp(View v) {
        if(helpClick){
            infoBox.setVisibility(View.INVISIBLE);
            helpButton.setImageResource(R.drawable.wrench_button);
            helpClick = false;
        }
        else {
            infoBox.setVisibility(View.VISIBLE);
            helpButton.setImageResource(R.drawable.wrench_button_pressed);
            helpClick = true;
        }
    }

    public void showinfoBox() {
        infoBox.setVisibility(View.VISIBLE);
        helpButton.setImageResource(R.drawable.wrench_button_pressed);
        helpClick = true;
    }

    /*
    * If the animate is active then the backBtnBlick should bring the user back to the original metaio view
    * */
    public void backBtnClick(View v) {
        if(checkCamera) {
            v.setSelected(!v.isSelected());
            goBack();
            MenuActivity.resetButtons();
        }
        else {
            lightenCamera();
            stopAnimating();
            animateButton.setVisibility(View.VISIBLE);
            checkCamera = true;
        }
    }

    /*
    * Changed behaviour for the standard back button, it should act as the backBtnClick do
    * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // do something on back.

            if(checkCamera) {
                goBack();
                MenuActivity.resetButtons();
            }
            else {
                lightenCamera();
                stopAnimating();
                animateButton.setVisibility(View.VISIBLE);
                checkCamera = true;
            }

        }
        return true;
    }

    //List
    public void showPopup(final View v) {
        if(sitsFound) seatFound.setText(R.string.found);
        else seatFound.setText(R.string.not_found);

        if(rightSideFound) sideRFound.setText(R.string.found);
        else sideRFound.setText(R.string.not_found);

        if(leftSideFound) sideLFound.setText(R.string.found);
        else sideLFound.setText(R.string.not_found);

        if(ryggMidFound) backMFound.setText(R.string.found);
        else backMFound.setText(R.string.not_found);

        if(ryggTopFound) backTFound.setText(R.string.found);
        else backTFound.setText(R.string.not_found);

        popupList.setVisibility(View.VISIBLE);
    }

    public void hidePopup(final View v){
        popupList.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed(){
        goBack();
        MenuActivity.resetButtons();
    }

    public void goBack(){
        // Create intent and with it send a bundle
        // populated with data if we found parts
        Intent returnIntent = new Intent();

        returnIntent.putExtra("foundSits", sitsFound);
        returnIntent.putExtra("foundRightSide", rightSideFound);
        returnIntent.putExtra("foundLeftSide", leftSideFound);
        returnIntent.putExtra("foundRyggMid", ryggMidFound);
        returnIntent.putExtra("foundRyggTop", ryggTopFound);

        setResult(0,returnIntent);
        finish();
    }

    public void listBtnClick(View v) {
        v.setSelected(!v.isSelected());
        Log.i("listBtnClick", "listClick: " +listClick);

        if(listClick){
            hidePopup(v);
            listClick = false;
        }
        else {
            showPopup(v);
            listClick = true;
        }
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


            final File stepOneFile = AssetsManager.getAssetPathAsFile(getApplicationContext(), "step1_9.zip");
            final File stepTwoFile = AssetsManager.getAssetPathAsFile(getApplicationContext(), "step2.zip");
            final File stepThreeFile = AssetsManager.getAssetPathAsFile(getApplicationContext(), "steg3_fixad.zip");
            final File stepFourFile = AssetsManager.getAssetPathAsFile(getApplicationContext(), "steg_4.zip");
            final File stepFiveFile = AssetsManager.getAssetPathAsFile(getApplicationContext(), "steg_5.zip");
            final File stepSixFile = AssetsManager.getAssetPathAsFile(getApplicationContext(), "steg_6.zip");

            stepOne =   metaioSDK.createGeometry(stepOneFile);
            stepTwo = metaioSDK.createGeometry(stepTwoFile);
            stepThree =   metaioSDK.createGeometry(stepThreeFile);
            stepFour =   metaioSDK.createGeometry(stepFourFile);
            stepFive =   metaioSDK.createGeometry(stepFiveFile);
            stepSix =   metaioSDK.createGeometry(stepSixFile);


            stepOne.setVisible(false);
            stepTwo.setVisible(false);
            stepThree.setVisible(false);
            stepFour.setVisible(false);
            stepFive.setVisible(false);
            stepSix.setVisible(false);

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
                rightSide = metaioSDK.createGeometry(sidaModel);
                rightSide.setTranslation(new Vector3d(200.0f, -5500.0f, -1000.0f));
                rightSide.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "rightSideTexture.png"));
                rightSide.setTransparency(0.5f);

                leftSide = metaioSDK.createGeometry(sidaModel2);
                leftSide.setRotation(new Rotation(0.0f, 3.14f, 0.0f));
                leftSide.setTranslation(new Vector3d(200.0f, -5000.0f, -1000.0f));
                leftSide.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "leftSideTexture.png"));
                leftSide.setTransparency(0.5f);

                if (rightSide != null) {
                    // Set geometry properties
                    rightSide.setScale(23.0f);
                    leftSide.setScale(23.0f);

                    rightSide.setVisible(false);

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
                rygg_mid.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "ryggMidUVWMap.png")); //backMidTexture.png
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
    public boolean onTouch (View v, MotionEvent event){

        if (event != null) {
            float x = event.getX();
            float y = event.getY();

            float deltaX = (x - mPreviousX) / mDensity / 2f;
            float deltaY = (y - mPreviousY) / mDensity / 2f;
            if(Math.abs(deltaX) < 20 && Math.abs(deltaY) < 20) {

                    currentYrotation += deltaY / 100;
                    currentXrotation += deltaX / 100;

                if (stepOne.isVisible()){
                    stepOne.setRotation(new Rotation(currentXrotation, -currentYrotation, currentZrotation));
            }
                else if (stepTwo.isVisible()){
                    stepTwo.setRotation(new Rotation(currentYrotation, currentXrotation, currentZrotation));

                }
                else if(stepThree.isVisible())
                {
                    stepThree.setRotation(new Rotation(currentYrotation, currentXrotation, currentZrotation));

                }
                else if(stepFour.isVisible())
                {
                    stepFour.setRotation(new Rotation(currentYrotation, currentXrotation, currentZrotation));

                }
                else if(stepFive.isVisible())
                {
                    stepFive.setRotation(new Rotation(currentYrotation, currentXrotation, currentZrotation));

                }
                else if(stepSix.isVisible())
                {
                    stepSix.setRotation(new Rotation(currentYrotation, currentXrotation, currentZrotation));

                }

            }


            mPreviousX = x;
            mPreviousY = y;

            return true;
        } else {
            return false;
        }
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


    //Sets camera resolution and picks camera.
    @Override
    protected void startCamera()
    {
        final CameraVector cameras = metaioSDK.getCameraList();


        if (!cameras.isEmpty())
        {
            Camera camera = cameras.get(0);

            // Try to choose the front back camera
            for (int i = 0; i < cameras.size(); i++)
            {

                if (cameras.get(i).getFacing() == Camera.FACE_FRONT)
                {
                     camera = cameras.get(i);

                }

                if (cameras.get(i).getFacing() == Camera.FACE_BACK)
                {
                    camera = cameras.get(i);
                    break;
                }
            }
            camera.setResolution(new Vector2di(1920,1440));
            camera.setDownsample(1);
            metaioSDK.startCamera(camera);
        }
        else
        {
            MetaioDebug.log(Log.WARN, "No camera found on the device!");
        }
    }

    //Auto focus, old code but it works.
    @Override
    public void onSurfaceChanged(int width, int height)
    {
        android.hardware.Camera camera = IMetaioSDKAndroid.getCamera(this);
        android.hardware.Camera.Parameters params = camera.getParameters();
        params.setFocusMode("continuous-picture");
        camera.setParameters(params);
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

            if (rightSide != null && sits != null && rygg_top != null && rygg_mid != null) {

                for (int i = 0; i < trackingValues.size(); i++) {

                    //TrackingValue is received from TrackingData_PictureMarker.xml
                    if (metaioSDK != null) {
                        // get all detected poses/targets
                        poses = metaioSDK.getTrackingValues();
                        //CameraShutter sound when an item is found
                        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.camerashutter);
                        //if we have detected one, attach our metaio man to this coordinate system Id
                        if (poses.size() != 0) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // If popuplist has been created
                                    // CameraShutter sound when an item is found
                                    // Tillåter oss att ändra listan dynamiskt
                                    if (sits.getIsRendered()) {
                                        Log.i("OnTrackingEvent", "Hittar rygg topp");
                                        seatFound.setText(R.string.found);

                                        //CameraShutter sound when an item is found
                                        if(!sitsFound)
                                        {
                                            sitsFound = true;
                                            mp.start();
                                        }
                                    }
                                    if (rightSide.getIsRendered()) {
                                        Log.i("OnTrackingEvent","Hittar rygg topp");
                                        sideRFound.setText(R.string.found);

                                        if(!rightSideFound)
                                        {
                                            rightSideFound = true;
                                            mp.start();
                                        }
                                    }
                                    if (leftSide.getIsRendered()) {
                                        Log.i("OnTrackingEvent", "Hittar rygg topp");
                                        sideLFound.setText(R.string.found);

                                        if(!leftSideFound)
                                        {
                                            leftSideFound = true;
                                            mp.start();
                                        }
                                    }
                                    if (rygg_mid.getIsRendered()) {
                                        Log.i("OnTrackingEvent","Hittar rygg topp");
                                        backMFound.setText(R.string.found);

                                        if(!ryggMidFound)
                                        {
                                            ryggMidFound = true;
                                            mp.start();
                                        }
                                    }
                                    if (rygg_top.getIsRendered()) {
                                        Log.i("OnTrackingEvent","Hittar rygg topp");
                                        backTFound.setText(R.string.found);

                                        if(!ryggTopFound)
                                        {
                                            ryggTopFound = true;
                                            mp.start();
                                        }
                                    }
                                }
                            });

                            if(buildStep==0) {
                                rightSide.setVisible(true);
                                leftSide.setVisible(true);
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
                                rightSide.setVisible(true);
                            }
                            if(buildStep==4) {
                                leftSide.setVisible(true);
                            }

                            //Added
                            rightSide.setCoordinateSystemID(1);
                            leftSide.setCoordinateSystemID(5);
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
