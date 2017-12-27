package com.tokopedia.seller.common.williamchart.config;

import android.graphics.Color;

import com.tokopedia.seller.common.williamchart.Tools;
import com.tokopedia.seller.common.williamchart.renderer.AxisRenderer;
import com.tokopedia.seller.common.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.common.williamchart.util.AnimationGraphConfiguration;
import com.tokopedia.seller.common.williamchart.util.TopAdsYAxisRenderer;
import com.tokopedia.seller.common.williamchart.view.ChartView;
import com.tokopedia.seller.common.williamchart.base.BaseWilliamChartConfig;

import static com.tokopedia.seller.common.williamchart.util.TopAdsBaseWilliamChartConfig.WIDTH_TIP;

/**
 * Created by normansyahputa on 7/7/17.
 */

public class GrossGraphChartConfig implements AnimationGraphConfiguration {

    @Override
    public int labelColor() {
        return Color.argb(97, 0, 0, 0);
    }

    @Override
    public int axisColor() {
        return Color.argb(13, 0, 0, 0);
    }

    @Override
    public float gridThickness() {
        return 1f;
    }

    @Override
    public int gridColor() {
        return Color.argb(13, 0, 0, 0);
    }

    @Override
    public AxisRenderer.LabelPosition xLabelPosition() {
        return AxisRenderer.LabelPosition.OUTSIDE;
    }

    @Override
    public AxisRenderer.LabelPosition yLabelPosition() {
        return AxisRenderer.LabelPosition.OUTSIDE;
    }

    @Override
    public ChartView.GridType gridType() {
        return ChartView.GridType.NONE;
    }

    @Override
    public boolean xAxis() {
        return true;
    }

    @Override
    public boolean yAxis() {
        return true;
    }

    @Override
    public boolean xDataGrid() {
        return true;
    }

    @Override
    public StringFormatRenderer yStringFormatRenderer() {
        return new TopAdsYAxisRenderer();
    }

    @Override
    public int topMargin() {
        return (int) Tools.fromDpToPx(WIDTH_TIP);
    }

    @Override
    public int rightMargin() {
        return BaseWilliamChartConfig.DEFAULT;
    }

    @Override
    public int bottomMargin() {
        return BaseWilliamChartConfig.DEFAULT;
    }

    @Override
    public float xDistAxisToLabel() {
        return Tools.fromDpToPx(12f);
    }

    @Override
    public float yDistAxisToLabel() {
        return Tools.fromDpToPx(12f);
    }

    @Override
    public int alpha() {
        return 1;
    }

    @Override
    public int duration() {
        return 500;
    }

    @Override
    public int easingId() {
        return 0;
    }

    @Override
    public float overlapFactor() {
        return 1f;
    }

    @Override
    public float startX() {
        return 0f;
    }

    @Override
    public float startY() {
        return 1f;
    }

    @Override
    public Runnable endAnimation() {
        return new Runnable() {
            @Override
            public void run() {
                // currently empty
            }
        };
    }
}
