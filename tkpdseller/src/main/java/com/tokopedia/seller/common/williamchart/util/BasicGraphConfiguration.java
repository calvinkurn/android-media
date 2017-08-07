package com.tokopedia.seller.common.williamchart.util;

import com.tokopedia.seller.common.williamchart.renderer.AxisRenderer;
import com.tokopedia.seller.common.williamchart.renderer.StringFormatRenderer;
import com.tokopedia.seller.common.williamchart.view.ChartView;

/**
 * Created by normansyahputa on 7/7/17.
 * <p>
 * this class represent {@link ChartView} configuration.
 */

public interface BasicGraphConfiguration {

    int labelColor();

    int axisColor();

    float gridThickness();

    int gridColor();

    AxisRenderer.LabelPosition xLabelPosition();

    AxisRenderer.LabelPosition yLabelPosition();

    ChartView.GridType gridType();

    boolean xAxis();

    boolean yAxis();

    boolean xDataGrid();

    StringFormatRenderer yStringFormatRenderer();

    int topMargin();

    int rightMargin();

    int bottomMargin();

    float xDistAxisToLabel();

    float yDistAxisToLabel();
}
