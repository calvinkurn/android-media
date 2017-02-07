package com.tokopedia.seller.gmstat.utils;

import android.graphics.Color;
import android.graphics.Paint;

import com.tokopedia.seller.gmstat.views.williamchart.chart.Tools;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.Animation;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.BaseEasingMethod;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.BounceEase;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.CircEase;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.CubicEase;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.ElasticEase;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.ExpoEase;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.LinearEase;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.QuadEase;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.QuartEase;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.QuintEase;
import com.tokopedia.seller.gmstat.views.williamchart.chart.animation.easing.SineEase;
import com.tokopedia.seller.gmstat.views.williamchart.chart.model.LineSet;
import com.tokopedia.seller.gmstat.views.williamchart.chart.renderer.AxisRenderer;
import com.tokopedia.seller.gmstat.views.williamchart.chart.view.ChartView;
import com.tokopedia.seller.gmstat.views.williamchart.chart.view.LineChartView;

import java.text.DecimalFormat;

/**
 * Created by normansyahputa on 12/17/16.
 * rename class to DataTransactionChartConfig because it belongs to DataTransaction only.
 */
public class DataTransactionChartConfig {
    private static final int GREEN_COLOR = Color.rgb(66, 181, 73);
    private static final int GREY_COLOR = Color.rgb(189, 189, 189);
    private final Runnable mEndAction = new Runnable() {
        @Override
        public void run() {
            // do nothing
        }
    };
    private String[] mLabels;
    private float[] mValues;
    private boolean mIsLineSmooth = true;
    private int mLineColorId;
    private int mPointColorId;
    private int mGridColorId;
    private float mGridThickness;
    private AxisRenderer.LabelPosition mXLabelPosition;
    private AxisRenderer.LabelPosition mYLabelPosition;
    private int mLabelColorId;
    private int mAxisColorId;
    private ChartView.GridType mGridType = ChartView.GridType.NONE;
    private int mEasingId;
    private int[] mOverlapOrder;
    private float mOverlapFactor;
    private float mStartX;
    private float mStartY;
    private int[] mEqualOrder = {0, 1, 2, 3, 4, 5, 6};

    public DataTransactionChartConfig(String[] mLabels, float[] mValues) {
        if (mLabels == null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");
        if (mValues == null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");

        this.mLabels = mLabels;
        this.mValues = mValues;

        mLineColorId = Color.rgb(66, 181, 73);
        mPointColorId = Color.rgb(255, 255, 255);
        mGridColorId = Color.argb(13, 0, 0, 0);
        mLabelColorId = Color.argb(97, 0, 0, 0);
        mAxisColorId = Color.argb(13, 0, 0, 0);
        mGridThickness = 1f;
        mXLabelPosition = AxisRenderer.LabelPosition.NONE;
        mYLabelPosition = AxisRenderer.LabelPosition.NONE;
        mEasingId = 0;
        mOverlapFactor = 1;
        mOverlapOrder = mEqualOrder;
        mStartX = 0f;
        mStartY = 1f;
    }

    public void setmLabels(String[] mLabels) {
        this.mLabels = mLabels;
    }

    public void setmValues(float[] mValues) {
        this.mValues = mValues;

        if (mValues.length != mEqualOrder.length) {
            mEqualOrder = new int[mValues.length];
            for (int i = 0; i < mEqualOrder.length; i++) {
                mEqualOrder[i] = i;
            }
        }
        mOverlapOrder = mEqualOrder;
    }

    public ChartView buildLineChart(LineChartView chart, int bottomMargin, boolean emptyState) {

        if (emptyState) {
            mLineColorId = GREY_COLOR;
        } else {
            mLineColorId = GREEN_COLOR;
        }

        chart.reset();
        chart.resetYRndr();

        LineSet dataset = new LineSet(mLabels, mValues);

        float mLineThickness = 3;
        dataset.setSmooth(LineSet.SMOOTH_QUAD)
                .setThickness(Tools.fromDpToPx(mLineThickness))
                .setColor(mLineColorId);

        float mPointsSize = 0;
        dataset.setDotsRadius(Tools.fromDpToPx(mPointsSize)).setDotsColor(mPointColorId);
        chart.addData(dataset);
        chart.setTopMargin(0);
        chart.setRightMargin(0);
        chart.setBottomMargin(bottomMargin);

        return chart;
    }

    public void buildChart(ChartView chart) {

        Paint mGridPaint = new Paint();
        mGridPaint.setColor(mGridColorId);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setAntiAlias(true);
        mGridPaint.setStrokeWidth(Tools.fromDpToPx(mGridThickness));

        chart.setXAxis(false)
                .setXLabels(mXLabelPosition)
                .setYAxis(false)
                .setYLabels(mYLabelPosition)
                .setLabelsColor(mLabelColorId)
                .setAxisColor(mAxisColorId);

        if (mGridType != null) chart.setGrid(mGridType, mGridPaint);

        String mLabelFormat = "";
        chart.setLabelsFormat(new DecimalFormat("#" + mLabelFormat));

        chart.show(buildAnimation());
    }

    private Animation buildAnimation() {

        BaseEasingMethod mEasing;
        switch (mEasingId) {
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

        int mAlpha = 1;
        int mDuration = 500;
        return new Animation(mDuration).setAlpha(mAlpha)
                .setEasing(mEasing)
                .setOverlap(mOverlapFactor, mOverlapOrder)
                .setStartPoint(mStartX, mStartY)
                .setEndAction(mEndAction);
    }
}
