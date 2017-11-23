package com.tokopedia.seller.common.williamchart.tooltip;

import android.content.Context;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.seller.common.williamchart.model.TooltipModel;
import com.tokopedia.seller.common.williamchart.renderer.StringFormatRenderer;


/**
 * Created by zulfikarrahman on 5/18/17.
 */

public class TooltipWithDynamicPointer extends Tooltip {
    private TooltipPointerView tooltipPointerView;
    private TextView titleText;

    private Rect rect;
    private TooltipModel value;

    public TooltipWithDynamicPointer(Context context) {
        super(context);
    }

    public TooltipWithDynamicPointer(Context context, int layoutId) {
        super(context, layoutId);
    }

    public TooltipWithDynamicPointer(Context context, int layoutId, int valueId) {
        super(context, layoutId, valueId);
    }

    public TooltipWithDynamicPointer(Context context, int layoutId, int valueId, int titleId, int pointerId){
        super(context, layoutId, valueId);
        titleText = (TextView) findViewById(titleId);
        tooltipPointerView = (TooltipPointerView) findViewById(pointerId);
    }

    public TooltipWithDynamicPointer(Context context, int layoutId, int valueId, StringFormatRenderer stringFormatRenderer) {
        super(context, layoutId, valueId, stringFormatRenderer);
    }

    /**
     * set the view of pointer for dot chart
     * @param tooltipPointerView pointer of dot chart
     * @return
     */
    public TooltipWithDynamicPointer setPointerView(TooltipPointerView tooltipPointerView){
        if(this.tooltipPointerView != null) {
            removeView(this.tooltipPointerView);
        }
        this.tooltipPointerView = tooltipPointerView;
        addView(tooltipPointerView);
        return this;
    }

    public TooltipPointerView getTooltipPointerView() {
        return tooltipPointerView;
    }

    @Override
    public void prepare(final Rect rect, final TooltipModel value) {
        super.prepare(rect, value);

        if(rect == null){
            return;
        }
        this.rect = rect;
        this.value = value;
        titleText.setText(value.getTitle());
        requestLayout();

    }

    @Override
    public void correctPosition(int left, int top, int right, int bottom) {
        super.correctPosition(left, top, right, bottom);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();

        int arrowMargin = ((rect.left + rect.width()/2) - layoutParams.leftMargin) - (tooltipPointerView.getLayoutWidth() /2);

        ViewGroup.MarginLayoutParams layoutParamsPointer = (ViewGroup.MarginLayoutParams)  tooltipPointerView.getLayoutParams();
        layoutParamsPointer.leftMargin =  arrowMargin;
        tooltipPointerView.setLayoutParams(layoutParamsPointer);
    }
}
