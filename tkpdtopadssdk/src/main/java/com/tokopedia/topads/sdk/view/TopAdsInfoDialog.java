package com.tokopedia.topads.sdk.view;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;

/**
 * @author by errysuprayogi on 4/4/17.
 */

public class TopAdsInfoDialog extends DialogFragment implements View.OnClickListener {

    public static TopAdsInfoDialog newInstance() {

        Bundle args = new Bundle();

        TopAdsInfoDialog fragment = new TopAdsInfoDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView readMoreBtn;
    private TopAdsInfoClickListener infoClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.layout_dialog_info_topads, container, false);
        readMoreBtn = (TextView) rootView.findViewById(R.id.readMore);
        readMoreBtn.setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            infoClickListener = (TopAdsInfoClickListener) context;
        }catch (ClassCastException e){
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.readMore) {
            dismiss();
            String url = "https://www.tokopedia.com/iklan?source=tooltip&medium=android";
            if (infoClickListener != null) {
                infoClickListener.onInfoClicked();
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        }
    }
}
