package com.example.viktor.agilprojektaugmentedreality;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by emmaedv on 21/04/15.
 */
public class MenuActivity extends ActionBarActivity{
    RelativeLayout layout;
    ImageButton listBtn, arBtn;
    TextView listText, arText;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        /*setContentView(R.layout.menu_activity);
        listBtn = (ImageButton) findViewById(R.id.listButton);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked on ListButton", "");
            }
        });

        arBtn = (ImageButton) findViewById(R.id.arButton);
        arBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked on ARButton", "");
            }
        });*/

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
        int marginTop = 100;

        //Create elements
        layout = new RelativeLayout(this);
        listBtn = new ImageButton(this);
        arBtn = new ImageButton(this);
        listText = new TextView(this);
        arText = new TextView(this);

        //Set id:s
        listBtn.setId(R.id.listBtn);
        arBtn.setId(R.id.arBtn);
        listText.setId(R.id.listTxt);
        arText.setId(R.id.arTxt);

        //Set text and images
        listBtn.setBackgroundResource(R.drawable.list_icon);
        //listBtn.setBackgroundColor(getResources().getColor(R.color.blue));
        listBtn.setAdjustViewBounds(true);

        arBtn.setBackgroundResource(R.drawable.mount_icon);
        //arBtn.setBackgroundColor(getResources().getColor(R.color.blue));
        arBtn.setAdjustViewBounds(true);

        listText.setText(R.string.listButton);
        arText.setText(R.string.arButton);
        listText.setBackgroundColor(getResources().getColor(R.color.blue));
        arText.setBackgroundColor(getResources().getColor(R.color.blue));
        //TODO: Centrera, Ändra textstorlek, Sätt font

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
        listTextParams.setMargins(0, marginTop, 0, 0);

        //Set params of ar text
        RelativeLayout.LayoutParams arTextParams = new RelativeLayout.LayoutParams(imgWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        arTextParams.addRule(RelativeLayout.BELOW, arBtn.getId());
        arTextParams.addRule(RelativeLayout.ALIGN_LEFT, arBtn.getId());
        arTextParams.setMargins(0,marginTop,0,0);

        //Set params to elements
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
                startActivity(listScreen);
            }
        });

        arBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraScreen = new Intent(getApplicationContext(),  CameraActivity.class);
                startActivity(cameraScreen);
            }
        });
    }

}
