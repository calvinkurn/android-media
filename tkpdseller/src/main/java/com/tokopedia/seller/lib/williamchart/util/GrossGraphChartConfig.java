package com.tokopedia.seller.lib.williamchart.util;

import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.tokopedia.seller.lib.williamchart.Tools;
import com.tokopedia.seller.lib.williamchart.animation.Animation;
import com.tokopedia.seller.lib.williamchart.animation.easing.BaseEasingMethod;
import com.tokopedia.seller.lib.williamchart.animation.easing.BounceEase;
import com.tokopedia.seller.lib.williamchart.animation.easing.CircEase;
import com.tokopedia.seller.lib.williamchart.animation.easing.CubicEase;
import com.tokopedia.seller.lib.williamchart.animation.easing.ElasticEase;
import com.tokopedia.seller.lib.williamchart.animation.easing.ExpoEase;
import com.tokopedia.seller.lib.williamchart.animation.easing.LinearEase;
import com.tokopedia.seller.lib.williamchart.animation.easing.QuadEase;
import com.tokopedia.seller.lib.williamchart.animation.easing.QuartEase;
import com.tokopedia.seller.lib.williamchart.animation.easing.QuintEase;
import com.tokopedia.seller.lib.williamchart.animation.easing.SineEase;
import com.tokopedia.seller.lib.williamchart.model.LineSet;
import com.tokopedia.seller.lib.williamchart.renderer.AxisRenderer;
import com.tokopedia.seller.lib.williamchart.renderer.XRenderer;
import com.tokopedia.seller.lib.williamchart.tooltip.Tooltip;
import com.tokopedia.seller.lib.williamchart.view.ChartView;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.text.DecimalFormat;

/**
 * Created by normansyahputa on 11/24/16.
 * rename class to gross graph chart config because it belongs to gross graph.
 */
public class GrossGraphChartConfig {
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
    private final Runnable mEndAction = new Runnable() {
        @Override
        public void run() {

//            mPlayBtn.setEnabled(true);
        }
    };
    private Drawable dotDrawable;
    private Tooltip tooltip;
    private XRenderer.XRendererListener xRendererListener;

    public GrossGraphChartConfig setDotDrawable(Drawable dotDrawable) {
        this.dotDrawable = dotDrawable;
        return this;
    }

    public GrossGraphChartConfig setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public GrossGraphChartConfig(String[] mLabels, float[] mValues){
        if(mLabels == null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");
        if(mValues==null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");

        this.mLabels = mLabels;
        this.mValues = mValues;

        mLineColorId = Color.rgb(66,181,73);
        mPointColorId = Color.rgb(255,255,255);
        mGridColorId = Color.argb(13, 0,0,0);
        mLabelColorId = Color.argb(97, 0,0,0);
        mAxisColorId = Color.argb(13, 0,0,0);
        mGridThickness = 1f;
        mXLabelPosition = AxisRenderer.LabelPosition.OUTSIDE;
        mYLabelPosition = AxisRenderer.LabelPosition.OUTSIDE;
        mEasingId = 0;
        mOverlapFactor = 1;
//        int[] mEqualOrder = new
//        mOverlapOrder = mEqualOrder;
        mStartX = 0f;
        mStartY = 1f;
    }

    private int[] mEqualOrder = {0, 1, 2, 3, 4, 5, 6};

    public GrossGraphChartConfig setmLabels(String[] mLabels) {
        this.mLabels = mLabels;
        return this;
    }

    public GrossGraphChartConfig setmValues(float[] mValues, XRenderer.XRendererListener xRendererListener) {
        this.mValues = mValues;
        this.xRendererListener = xRendererListener;

        if(mValues.length != mEqualOrder.length)
        {
            mEqualOrder = new int[mValues.length];
            for(int i=0;i<mEqualOrder.length;i++){
                mEqualOrder[i] = i;
            }
        }
        mOverlapOrder = mEqualOrder;
        return this;
    }

    public LineChartView buildLineChart(LineChartView chart) {

        chart.reset();
        chart.resetYRndr();

        LineSet dataset = new LineSet(mLabels, mValues);

//        if (mIsLineDashed) dataset.setDashed(mLineDashType);
        float mLineThickness = 3;
        dataset.setSmooth(LineSet.SMOOTH_QUAD)
                .setThickness(Tools.fromDpToPx(mLineThickness))
                .setColor(mLineColorId);

        float mPointsSize = 4;
        dataset.setDotsRadius(Tools.fromDpToPx(mPointsSize)).setDotsColor(mPointColorId);
        chart.addData(dataset);

        return chart;
    }

    public void buildChart(LineChartView chart) {

        // Tooltip
        Tooltip mTip = tooltip;

//        ((TextView) mTip.findViewById(R.id.value)).setTypeface(
//                Typeface.createFromAsset(chart.getContext().getAssets(), "OpenSans-Semibold.ttf"));

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        mTip.setDimensions((int) Tools.fromDpToPx(80), (int) Tools.fromDpToPx(30));

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
        chart.putYRndrStringFormatter(new YAxisRenderer());
        chart.setDrawable(dotDrawable);

        Paint mGridPaint = new Paint();
        mGridPaint.setColor(mGridColorId);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setAntiAlias(true);
        mGridPaint.setStrokeWidth(Tools.fromDpToPx(mGridThickness));
//        if (mIsGridDashed) mGridPaint.setPathEffect(new DashPathEffect(mGridDashType, 0));

        chart.setXAxis(true)
                .setXLabels(mXLabelPosition)
                .setYAxis(true)
                .setYLabels(mYLabelPosition)
                .setLabelsColor(mLabelColorId)
                .setAxisColor(mAxisColorId)
                .setxDataGrid(true)
                .setXRendererListener(xRendererListener);

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
