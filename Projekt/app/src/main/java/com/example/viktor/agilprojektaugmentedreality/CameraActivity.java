package com.example.viktor.agilprojektaugmentedreality;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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


    private IGeometry sits;
    private IGeometry rightSide;
    private IGeometry leftSide;
    private IGeometry rygg_top;
    private IGeometry rygg_mid;

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


    //Currently loaded tracking configuration file
    File trackingConfigFile;

    private MetaioSDKCallbackHandler mCallbackHandler;


    private int buildStep = 0;

    // Da popup menu
    PopupMenu popup;

    MenuItem item_sits, item_right, item_left,item_ryggtop, item_ryggmid;

    boolean initiated = false;


    //Bools to check if button in topbar is clicked or not
    boolean helpClick = true;
    boolean listClick = false;

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
    protected int getGUILayout() {
        // Attaching layout to the activity
        return R.layout.camera_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.camera_activity);

        //Locks the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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
        backTText.setTypeface(font);
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
                infoImage.setVisibility(View.INVISIBLE);
                infoText.setText(R.string.stepDone);
                infoText.setVisibility(View.VISIBLE);
             break;

        }

        //System.out.println("i showStep " + buildStep);
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

    public void animate(View v) {

        // Animate();
    }

    public void btnHelp(View v)
    {
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

    public void showinfoBox(){
        infoBox.setVisibility(View.VISIBLE);
        helpButton.setImageResource(R.drawable.wrench_button_pressed);
        helpClick = true;
    }

    public void backBtnClick(View v) {
        v.setSelected(!v.isSelected());
        goBack();
        MenuActivity.resetButtons();
    }

    //List
    public void showPopup(final View v) {
        popup = new PopupMenu(this, v);
        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                v.setSelected(false);
            }
        });

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

        itemLeftSideNF = new SpannableString("Vänster sida (Not found)");
        itemLeftSideNF.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.sidaVanster)), 0, itemLeftSideNF.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        itemLeftSideF = new SpannableString("Vänster sida (Found)");
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
            seatFound.setText(R.string.not_found);
        }
        else {
            item_sits.setTitle(itemSitsTextNF);
            seatFound.setText(R.string.found);
        }

        if(rightSideFound){
            item_right.setTitle(itemRightSideF);
            sideRFound.setText(R.string.not_found);
        }
        else {
            item_right.setTitle(itemRightSideNF);
            sideRFound.setText(R.string.found);
        }

        if(leftSideFound){
            item_left.setTitle(itemLeftSideF);
            sideLFound.setText(R.string.found);
        }
        else {
            item_left.setTitle(itemLeftSideNF);
            sideLFound.setText(R.string.not_found);
        }

        if(ryggMidFound){
            item_ryggmid.setTitle(itemRyggMidF);
            backMFound.setText(R.string.found);
        }
        else {
            item_ryggmid.setTitle(itemRyggMidNF);
            backMFound.setText(R.string.not_found);
        }

        if(ryggTopFound){
            item_ryggtop.setTitle(itemRyggTopF);
            backTFound.setText(R.string.found);
        }
        else {
            item_ryggtop.setTitle(itemRyggTopNF);
            backTFound.setText(R.string.not_found);
        }

    }

    @Override
    public void onBackPressed() {
        goBack();
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
        //findViewById(R.id.infoBox).setVisibility(View.INVISIBLE);
        Log.i("listBtnClick", "isSelected: " + v.isSelected());
        v.setSelected(!v.isSelected());
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
                rightSide = metaioSDK.createGeometry(sidaModel);
                rightSide.setTranslation(new Vector3d(200.0f, -5500.0f, -1000.0f));
                rightSide.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "leftSideTexture.png"));
                rightSide.setTransparency(0.5f);

                leftSide = metaioSDK.createGeometry(sidaModel2);
                leftSide.setRotation(new Rotation(0.0f, 3.14f, 0.0f));
                leftSide.setTranslation(new Vector3d(200.0f, -5000.0f, -1000.0f));
                leftSide.setTexture(AssetsManager.getAssetPathAsFile(getApplicationContext(), "rightSideTexture.png"));
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
            if (rightSide != null && sits != null && rygg_top != null && rygg_mid != null) {

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
                                            seatFound.setText(R.string.found);
                                            sitsFound = true;
                                        }
                                        if (rightSide.getIsRendered()) {
                                            item_right.setTitle(itemRightSideF);
                                            sideRFound.setText(R.string.found);
                                            rightSideFound = true;
                                        }
                                        if (leftSide.getIsRendered()) {
                                            item_left.setTitle(itemLeftSideF);
                                            sideLFound.setText(R.string.found);
                                            leftSideFound = true;
                                        }
                                        if (rygg_mid.getIsRendered()) {
                                            item_ryggmid.setTitle(itemRyggMidF);
                                            backMFound.setText(R.string.found);
                                            ryggMidFound = true;
                                        }
                                        if (rygg_top.getIsRendered()) {
                                            item_ryggtop.setTitle(itemRyggTopF);
                                            backTFound.setText(R.string.found);
                                            ryggTopFound = true;
                                        }
                                    }
                                }
                            });

                          //  sits.setVisible(true);
                          //  rygg_mid.setVisible(true);
                           // rygg_top.setVisible(true);

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
