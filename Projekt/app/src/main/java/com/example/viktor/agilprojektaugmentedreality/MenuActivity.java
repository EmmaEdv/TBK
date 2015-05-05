package com.example.viktor.agilprojektaugmentedreality;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Created by emmaedv on 21/04/15.
 */
public class MenuActivity extends Activity{
    RelativeLayout layout, header;
    ImageButton listBtn, arBtn;
    TextView listText, arText, headerText;
    Bundle returnDataBundle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Typeface font = Typeface.createFromAsset(getAssets(), "Berlin Sans FB.ttf");

        returnDataBundle = new Bundle();

        //Landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Screen size
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        int dpHeight = outMetrics.heightPixels;
        int dpWidth  = outMetrics.widthPixels;

        double imgProps = (143.0/159.0);
        int imgWidth = dpWidth/4;
        int imgHeight = (int) (imgWidth/imgProps);

        int marginLeft = dpWidth/6;
        int marginTop = 150;

        //Create elements
        layout = new RelativeLayout(this);
        header = new RelativeLayout(this);
        listBtn = new ImageButton(this);
        arBtn = new ImageButton(this);
        listText = new TextView(this);
        arText = new TextView(this);
        headerText = new TextView(this);

        //Set id:s
        listBtn.setId(R.id.listButton);
        arBtn.setId(R.id.arButton);
        listText.setId(R.id.listText);
        arText.setId(R.id.arText);
        headerText.setId(R.id.headerText);

        //Set text and images
        listBtn.setBackgroundResource(R.drawable.list_icon);
        //listBtn.setBackgroundColor(getResources().getColor(R.color.blue));
        listBtn.setAdjustViewBounds(true);

        arBtn.setBackgroundResource(R.drawable.mount_icon);
        arBtn.setAdjustViewBounds(true);

        listText.setText(R.string.listButton);
        arText.setText(R.string.arButton);
        headerText.setText(R.string.app_name);
        listText.setGravity(Gravity.CENTER_HORIZONTAL);
        arText.setGravity(Gravity.CENTER_HORIZONTAL);
        headerText.setGravity(Gravity.CENTER);
        listText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        arText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        headerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        listText.setTypeface(font);
        arText.setTypeface(font);
        headerText.setTypeface(font);

        //Header
        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Math.round(60*getResources().getDisplayMetrics().density));
        headerParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        //Set params of layout
        header.setLayoutParams(headerParams);
        header.setBackgroundColor(getResources().getColor(R.color.custom_gray));

        //Set layout parameters
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
        RelativeLayout.LayoutParams.MATCH_PARENT);
        //Set params of layout
        layout.setLayoutParams(params);
        layout.setBackgroundColor(getResources().getColor(R.color.WHITE));

        //Set params of list button
        RelativeLayout.LayoutParams listParams = new RelativeLayout.LayoutParams(imgWidth, imgHeight);
        listParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        listParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        listParams.setMargins(marginLeft, marginTop, 0, 0);

        //Set params of ar button
        RelativeLayout.LayoutParams arParams = new RelativeLayout.LayoutParams(imgWidth, imgHeight);
        arParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        arParams.addRule(RelativeLayout.RIGHT_OF, listBtn.getId());
        arParams.setMargins(marginLeft,marginTop,0,0);

        //Set params of list text
        RelativeLayout.LayoutParams listTextParams = new RelativeLayout.LayoutParams(imgWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        listTextParams.addRule(RelativeLayout.BELOW, listBtn.getId());
        listTextParams.addRule(RelativeLayout.ALIGN_LEFT, listBtn.getId());
        listTextParams.setMargins(0, marginTop/2, 0, 0);

        //Set params of header text
        RelativeLayout.LayoutParams headerTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        //Set params of ar text
        RelativeLayout.LayoutParams arTextParams = new RelativeLayout.LayoutParams(imgWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        arTextParams.addRule(RelativeLayout.BELOW, arBtn.getId());
        arTextParams.addRule(RelativeLayout.ALIGN_LEFT, arBtn.getId());
        arTextParams.setMargins(0,marginTop/2,0,0);

        //Set params to elements
        header.addView(headerText, headerTextParams);
        layout.addView(header);
        layout.addView(listBtn, listParams);
        layout.addView(arBtn, arParams);
        layout.addView(listText, listTextParams);
        layout.addView(arText, arTextParams);

        setContentView(layout);

        //Set onClick on buttons
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listScreen = new Intent(getApplicationContext(),  MainActivity.class);

                if(returnDataBundle.size() > 0)
                    listScreen.putExtras(returnDataBundle);

                    startActivity(listScreen);

            }
        });

        arBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraScreen = new Intent(getApplicationContext(),  CameraActivity.class);

                if(returnDataBundle.size() > 0)
                    cameraScreen.putExtras(returnDataBundle);

                startActivityForResult(cameraScreen, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            returnDataBundle = data.getExtras();
        }
    }

}
