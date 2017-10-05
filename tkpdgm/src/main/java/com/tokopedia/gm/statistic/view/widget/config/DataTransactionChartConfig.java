package com.tokopedia.gm.statistic.view.widget.config;

import android.content.Context;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.common.williamchart.config.GrossGraphChartConfig;
import com.tokopedia.seller.common.williamchart.renderer.AxisRenderer;
import com.tokopedia.seller.common.williamchart.renderer.StringFormatRenderer;

/**
 * Created by normansyahputa on 7/7/17.
 */

public class DataTransactionChartConfig extends GrossGraphChartConfig {
    private Context context;

    public DataTransactionChartConfig(Context context) {
        this.context = context;
    }

    @Override
    public AxisRenderer.LabelPosition xLabelPosition() {
        return AxisRenderer.LabelPosition.NONE;
    }

    @Override
    public AxisRenderer.LabelPosition yLabelPosition() {
        return AxisRenderer.LabelPosition.NONE;
    }

    @Override
    public boolean xAxis() {
        return false;
    }

    @Override
    public boolean yAxis() {
        return false;
    }

    @Override
    public boolean xDataGrid() {
        return false;
    }

    @Override
    public StringFormatRenderer yStringFormatRenderer() {
        return new YAxisRenderer();
    }

    @Override
    public int topMargin() {
        return 0;
    }

    @Override
    public int rightMargin() {
        return 0;
    }

    @Override
    public int bottomMargin() {
        int bottomMargin = 5;
        return (int) CommonUtils.DptoPx(context, bottomMargin);
    }
}
