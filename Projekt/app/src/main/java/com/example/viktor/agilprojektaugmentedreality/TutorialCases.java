package com.example.viktor.agilprojektaugmentedreality;

import android.app.Activity;
import android.view.View;

/**
 * Created by Tim on 2015-04-28.
 */
public class TutorialCases extends Activity{
    public void case0(View v){
        v.findViewById(R.id.topText0).setVisibility(View.VISIBLE);
        v.findViewById(R.id.topText1).setVisibility(View.GONE);
        v.findViewById(R.id.infoText).setVisibility(View.VISIBLE);
        v.findViewById(R.id.prevButton).setVisibility(View.GONE);
        v.findViewById(R.id.nextButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage1).setVisibility(View.GONE);
    }

    public void case1(View v) {
        v.findViewById(R.id.infoText).setVisibility(View.GONE);
        v.findViewById(R.id.prevButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.nextButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage1).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage2).setVisibility(View.GONE);
        v.findViewById(R.id.topText0).setVisibility(View.GONE);
        v.findViewById(R.id.topText1).setVisibility(View.VISIBLE);
        v.findViewById(R.id.topText2).setVisibility(View.GONE);
    }

    public void case2(View v) {
        v.findViewById(R.id.infoText).setVisibility(View.GONE);
        v.findViewById(R.id.prevButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.nextButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage1).setVisibility(View.GONE);
        v.findViewById(R.id.infoImage2).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage3).setVisibility(View.GONE);
        v.findViewById(R.id.topText1).setVisibility(View.GONE);
        v.findViewById(R.id.topText2).setVisibility(View.VISIBLE);
        v.findViewById(R.id.topText3).setVisibility(View.GONE);
    }

    public void case3(View v) {
        v.findViewById(R.id.infoText).setVisibility(View.GONE);
        v.findViewById(R.id.prevButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.nextButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage2).setVisibility(View.GONE);
        v.findViewById(R.id.infoImage3).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage4).setVisibility(View.GONE);
        v.findViewById(R.id.topText2).setVisibility(View.GONE);
        v.findViewById(R.id.topText3).setVisibility(View.VISIBLE);
        v.findViewById(R.id.topText4).setVisibility(View.GONE);
    }

    public void case4(View v) {
        v.findViewById(R.id.infoText).setVisibility(View.GONE);
        v.findViewById(R.id.prevButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.nextButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage3).setVisibility(View.GONE);
        v.findViewById(R.id.infoImage4).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage5).setVisibility(View.GONE);
        v.findViewById(R.id.topText3).setVisibility(View.GONE);
        v.findViewById(R.id.topText4).setVisibility(View.VISIBLE);
        v.findViewById(R.id.topText5).setVisibility(View.GONE);
    }

    public void case5(View v) {
        v.findViewById(R.id.infoText).setVisibility(View.GONE);
        v.findViewById(R.id.prevButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.nextButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage4).setVisibility(View.GONE);
        v.findViewById(R.id.infoImage5).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage6).setVisibility(View.GONE);
        v.findViewById(R.id.topText4).setVisibility(View.GONE);
        v.findViewById(R.id.topText5).setVisibility(View.VISIBLE);
        v.findViewById(R.id.topText6).setVisibility(View.GONE);
    }

    public void case6(View v) {
        v.findViewById(R.id.infoText).setVisibility(View.GONE);
        v.findViewById(R.id.prevButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.nextButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.infoImage5).setVisibility(View.GONE);
        v.findViewById(R.id.infoImage6).setVisibility(View.VISIBLE);
        v.findViewById(R.id.topText5).setVisibility(View.GONE);
        v.findViewById(R.id.topText6).setVisibility(View.VISIBLE);
        v.findViewById(R.id.topText7).setVisibility(View.GONE);
    }

    public void case7(View v) {
        v.findViewById(R.id.infoText).setVisibility(View.GONE);
        v.findViewById(R.id.prevButton).setVisibility(View.VISIBLE);
        v.findViewById(R.id.nextButton).setVisibility(View.GONE);
        v.findViewById(R.id.infoImage6).setVisibility(View.GONE);
        v.findViewById(R.id.topText6).setVisibility(View.GONE);
        v.findViewById(R.id.topText7).setVisibility(View.VISIBLE);
    }
}
