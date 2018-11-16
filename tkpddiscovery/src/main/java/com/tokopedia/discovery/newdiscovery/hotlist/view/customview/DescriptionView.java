package com.tokopedia.discovery.newdiscovery.hotlist.view.customview;

import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.discovery.R;

/**
 * Created by errysuprayogi on 5/15/18.
 */

public class DescriptionView extends BottomSheets {

    private String desc;
    private TextView descTxt;

    @Override
    public int getLayoutResourceId() {
        return R.layout.layout_hotlist_description;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        if (getBottomSheetBehavior() != null)
            getBottomSheetBehavior().setPeekHeight(screenHeight/2);
    }


    @Override
    public void initView(View view) {
        descTxt = view.findViewById(R.id.desc_txt);
        if(desc!=null)
            descTxt.setText(desc);
    }

    @Override
    protected String title() {
        return getString(R.string.description_page_title);
    }

    public void setDescTxt(String text){
        desc = text;
    }
}
