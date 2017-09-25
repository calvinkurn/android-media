package com.tokopedia.seller.common.williamchart.base;

import android.animation.PropertyValuesHolder;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.tokopedia.core.util.Pair;
import com.tokopedia.seller.common.williamchart.Tools;
import com.tokopedia.seller.common.williamchart.animation.Animation;
import com.tokopedia.seller.common.williamchart.animation.easing.BaseEasingMethod;
import com.tokopedia.seller.common.williamchart.animation.easing.BounceEase;
import com.tokopedia.seller.common.williamchart.animation.easing.CircEase;
import com.tokopedia.seller.common.williamchart.animation.easing.CubicEase;
import com.tokopedia.seller.common.williamchart.animation.easing.ElasticEase;
import com.tokopedia.seller.common.williamchart.animation.easing.ExpoEase;
import com.tokopedia.seller.common.williamchart.animation.easing.LinearEase;
import com.tokopedia.seller.common.williamchart.animation.easing.QuadEase;
import com.tokopedia.seller.common.williamchart.animation.easing.QuartEase;
import com.tokopedia.seller.common.williamchart.animation.easing.QuintEase;
import com.tokopedia.seller.common.williamchart.animation.easing.SineEase;
import com.tokopedia.seller.common.williamchart.model.LineSet;
import com.tokopedia.seller.common.williamchart.renderer.XRenderer;
import com.tokopedia.seller.common.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.common.williamchart.util.AnimationGraphConfiguration;
import com.tokopedia.seller.common.williamchart.util.BasicGraphConfiguration;
import com.tokopedia.seller.common.williamchart.util.DataSetConfiguration;
import com.tokopedia.seller.common.williamchart.util.TooltipConfiguration;
import com.tokopedia.seller.common.williamchart.view.LineChartView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 7/6/17.
 *
 */
public class BaseWilliamChartConfig {
    public static final String TAG = "BaseWilliamChartConfig";
    public final static int DEFAULT = Integer.MAX_VALUE;
    private List<Pair<BaseWilliamChartModel, DataSetConfiguration>> pairConfigs;
    //    private List<BaseWilliamChartModel> baseWilliamChartModels;
    private BasicGraphConfiguration basicGraphConfiguration;
    private TooltipConfiguration tooltipConfiguration;
    private Drawable dotDrawable;
    private Tooltip tooltip;
    private XRenderer.XRendererListener xRendererListener;
    private int[] overlapOrder;

    public BaseWilliamChartConfig() {
//        baseWilliamChartModels = new ArrayList<>();
        pairConfigs = new ArrayList<>();
    }

    public BaseWilliamChartConfig setBasicGraphConfiguration(BasicGraphConfiguration basicGraphConfiguration) {
        this.basicGraphConfiguration = basicGraphConfiguration;
        return this;
    }

    /**
     * please the data size is singular.
     *
     * @param baseWilliamChartModels
     * @return
     */
    public BaseWilliamChartConfig addBaseWilliamChartModels(BaseWilliamChartModel baseWilliamChartModels, DataSetConfiguration dataSetConfiguration) {
//        this.baseWilliamChartModels.add(baseWilliamChartModels);
        pairConfigs.add(new Pair<>(baseWilliamChartModels, dataSetConfiguration));
        overlapOrder = recalculateOverlapOrder(baseWilliamChartModels);
        return this;
    }

    /**
     * recalculate overlap order for animation
     *
     * @param baseWilliamChartModel non null and full data
     * @return overlap order for animation
     */
    private int[] recalculateOverlapOrder(BaseWilliamChartModel baseWilliamChartModel) {
        int[] overlapOrder = new int[baseWilliamChartModel.getValues().length];
        for (int i = 0; i < overlapOrder.length; i++) {
            overlapOrder[i] = i;
        }
        return overlapOrder;
    }

    public BaseWilliamChartConfig setDotDrawable(Drawable dotDrawable) {
        this.dotDrawable = dotDrawable;
        return this;
    }

    public BaseWilliamChartConfig setTooltip(Tooltip tooltip, TooltipConfiguration tooltipConfiguration) {
        this.tooltipConfiguration = tooltipConfiguration;
        this.tooltip = tooltip;
        return this;
    }

    public BaseWilliamChartConfig setxRendererListener(XRenderer.XRendererListener xRendererListener) {
        this.xRendererListener = xRendererListener;
        return this;
    }

    private LineChartView buildLineChart(LineChartView chart) {
        if (pairConfigs == null || pairConfigs.isEmpty())
            throw new RuntimeException("please pass the data !! BaseWilliamChartConfig");

        chart.clearData();
        chart.reset();
        chart.resetYRndr();

        for (Pair<BaseWilliamChartModel, DataSetConfiguration> pairConfig : pairConfigs) {
            BaseWilliamChartModel baseWilliamChartModel = pairConfig.getModel1();
            DataSetConfiguration dataSetConfiguration = pairConfig.getModel2();

            LineSet dataset = new LineSet();
            dataset.setmPointVisible(dataSetConfiguration.isVisible());

            for (int i = 0; i < baseWilliamChartModel.size(); i++) {
                dataset.addPoint(
                        baseWilliamChartModel.getLabels()[i],
                        baseWilliamChartModel.getValues()[i]);
            }

            dataset.setSmooth(LineSet.SMOOTH_QUAD)
                    .setThickness(Tools.fromDpToPx(dataSetConfiguration.lineThickness()))
                    .setColor(dataSetConfiguration.lineColor());

            if (dataSetConfiguration.isVisible()) {
                dataset.setDotsRadius(Tools.fromDpToPx(dataSetConfiguration.pointsSize()))
                        .setDotsColor(dataSetConfiguration.pointColor());
            }
            chart.addData(dataset);
        }

        return chart;
    }

    public BaseWilliamChartConfig reset() {
        pairConfigs.clear();
        return this;
    }

    public void buildChart(LineChartView chart) {

        chart = buildLineChart(chart);

        // Tooltip
        if (tooltip != null || tooltipConfiguration != null) {
            Tooltip mTip = tooltip;

            mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
            mTip.setDimensions((int) Tools.fromDpToPx(tooltipConfiguration.width()), (int) Tools.fromDpToPx(tooltipConfiguration.height()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

                mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                        PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

                mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                        PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                        PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

                mTip.setPivotX(Tools.fromDpToPx(65) / 2);
                mTip.setPivotY(Tools.fromDpToPx(25));
            }

            chart.setTooltips(mTip);
        }
        if (basicGraphConfiguration.yStringFormatRenderer() != null) {
            chart.putYRndrStringFormatter(basicGraphConfiguration.yStringFormatRenderer());
        }
        if (dotDrawable != null) {
            chart.setDrawable(dotDrawable);
        }

        Paint mGridPaint = new Paint();
        mGridPaint.setColor(basicGraphConfiguration.gridColor());
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setAntiAlias(true);
        mGridPaint.setStrokeWidth(Tools.fromDpToPx(basicGraphConfiguration.gridThickness()));

        chart.setXAxis(basicGraphConfiguration.xAxis())
                .setXLabels(basicGraphConfiguration.xLabelPosition())
                .setYAxis(basicGraphConfiguration.yAxis())
                .setYLabels(basicGraphConfiguration.yLabelPosition())
                .setLabelsColor(basicGraphConfiguration.labelColor())
                .setAxisColor(basicGraphConfiguration.axisColor())
                .setxDataGrid(basicGraphConfiguration.xDataGrid());

        if (xRendererListener != null)
            chart.setXRendererListener(xRendererListener);

        if (basicGraphConfiguration.topMargin() != DEFAULT) {
            chart.setTopMargin(basicGraphConfiguration.topMargin());
        }

        if (basicGraphConfiguration.rightMargin() != DEFAULT) {
            chart.setRightMargin(basicGraphConfiguration.rightMargin());
        }

        if (basicGraphConfiguration.bottomMargin() != DEFAULT) {
            chart.setBottomMargin(basicGraphConfiguration.bottomMargin());
        }

        if (basicGraphConfiguration.gridType() != null)
            chart.setGrid(basicGraphConfiguration.gridType(), mGridPaint);

        chart.setXAxisLabelSpacing(basicGraphConfiguration.xDistAxisToLabel());
        chart.setYAxisLabelSpacing(basicGraphConfiguration.yDistAxisToLabel());

        String mLabelFormat = "";
        chart.setLabelsFormat(new DecimalFormat("#" + mLabelFormat));

        if (basicGraphConfiguration instanceof AnimationGraphConfiguration) {
            chart.show(buildAnimation((AnimationGraphConfiguration) basicGraphConfiguration));
        } else {
            chart.show();
        }

    }

    private Animation buildAnimation(AnimationGraphConfiguration animationGraphConfiguration) {

        BaseEasingMethod mEasing;
        switch (animationGraphConfiguration.easingId()) {
            case 0:
                mEasing = new CubicEase();
                break;
            case 1:
                mEasing = new QuartEase();
                break;
            case 2:
                mEasing = new QuintEase();
                break;
            case 3:
                mEasing = new BounceEase();
                break;
            case 4:
                mEasing = new ElasticEase();
                break;
            case 5:
                mEasing = new ExpoEase();
                break;
            case 6:
                mEasing = new CircEase();
                break;
            case 7:
                mEasing = new QuadEase();
                break;
            case 8:
                mEasing = new SineEase();
                break;
            case 9:
                mEasing = new LinearEase();
                break;
            default:
                mEasing = new CubicEase();
        }
        return new Animation(animationGraphConfiguration.duration())
                .setAlpha(animationGraphConfiguration.alpha())
                .setEasing(mEasing)
                .setOverlap(animationGraphConfiguration.overlapFactor(), overlapOrder)
                .setStartPoint(animationGraphConfiguration.startX(), animationGraphConfiguration.startY())
                .setEndAction(animationGraphConfiguration.endAnimation());
    }
}
