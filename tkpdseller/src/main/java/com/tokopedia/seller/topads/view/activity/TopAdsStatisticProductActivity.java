package com.tokopedia.seller.topads.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.model.data.Cell;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticViewListener;
import com.tokopedia.seller.util.ShowCaseDialogFactory;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;

import java.util.ArrayList;
import java.util.List;

public class TopAdsStatisticProductActivity extends TopAdsStatisticActivity {

    private ShowCaseDialog showCaseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getTypeStatistic() {
        return TopAdsNetworkConstant.TYPE_PRODUCT_STAT;
    }

    @Override
    public void updateDataCell(List<Cell> cells) {
        super.updateDataCell(cells);
        startShowCase();
    }

    public void startShowCase() {
        final String showCaseTag = TopAdsStatisticProductActivity.class.getName();
//        if (ShowCasePreference.hasShown(this, showCaseTag)) {
//            return;
//        }
        if (showCaseDialog!= null) {
            return;
        }

        showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar.getHeight() > 0) {
            final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();
            int height = toolbar.getHeight();
            int width = toolbar.getWidth();

            showCaseList.add(
                    new ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_statistics_title_1),
                            getString(R.string.topads_showcase_statistics_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(new int[]{width -(int) (height * 0.9), 0,width, height}));

            showCaseList.add(new ShowCaseObject(
                    tabLayout,
                    getString(R.string.topads_showcase_statistics_title_2),
                    getString(R.string.topads_showcase_statistics_desc_2),
                    ShowCaseContentPosition.UNDEFINED,
                    R.color.tkpd_main_green));

            showCaseDialog.show(TopAdsStatisticProductActivity.this, showCaseTag, showCaseList);

        } else {
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    startShowCase();
                    if (Build.VERSION.SDK_INT < 16) {
                        toolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }


    }
}
