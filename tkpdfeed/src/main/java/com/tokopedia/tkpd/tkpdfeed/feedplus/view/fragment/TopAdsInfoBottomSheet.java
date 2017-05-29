package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.tkpd.tkpdfeed.R;

/**
 * Created by stevenfredian on 5/29/17.
 */

public class TopAdsInfoBottomSheet{

    public static final String TAG = "TAIBS";
    private BottomSheetDialog dialog;
    private View closeButton;

    public TopAdsInfoBottomSheet() {
    }

    public static TopAdsInfoBottomSheet newInstance(Context context){
        TopAdsInfoBottomSheet frag = new TopAdsInfoBottomSheet();
        frag.dialog = new BottomSheetDialog(context);
        frag.dialog.setContentView(R.layout.promoted_info_dialog);
        frag.closeButton = frag.dialog.findViewById(R.id.close_but);
        frag.setView();
        return frag;
    }

    public void setView() {
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
                FrameLayout frameLayout = (FrameLayout)
                        dialog.findViewById(com.tokopedia.core.R.id.design_bottom_sheet);
                if (frameLayout != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(frameLayout);
                    behavior.setHideable(false);
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
    }

    public void show() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
