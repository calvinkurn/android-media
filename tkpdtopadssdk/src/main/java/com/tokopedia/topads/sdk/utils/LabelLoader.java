package com.tokopedia.topads.sdk.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.Label;
import com.tokopedia.topads.sdk.view.FlowLayout;

import java.util.List;

/**
 * @author by errysuprayogi on 3/30/17.
 */

public class LabelLoader {

    public static void initLabel(Context context, FlowLayout container, List<Label> labels){
        container.removeAllViews();
        for(Label label : labels){
            View view = LayoutInflater.from(context).inflate(R.layout.layout_label, null);
            TextView labelText = (TextView) view.findViewById(R.id.label);
            labelText.setText(label.getTitle());
            if (!label.getColor().toLowerCase().equals("#ffffff")) {
                labelText.setBackgroundResource(R.drawable.bg_label);
                labelText.setTextColor(ContextCompat.getColor(context, R.color.white));
                ColorStateList tint = ColorStateList.valueOf(Color.parseColor(label.getColor()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    labelText.setBackgroundTintList(tint);
                } else {
                    ViewCompat.setBackgroundTintList(labelText, tint);
                }
            }
            container.addView(view);
        }
    }
}
