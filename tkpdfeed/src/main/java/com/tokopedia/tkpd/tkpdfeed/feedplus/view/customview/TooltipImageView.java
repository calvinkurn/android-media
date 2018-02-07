package com.tokopedia.tkpd.tkpdfeed.feedplus.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.tkpdfeed.R;

/**
 * @author by nisie on 10/27/17.
 */

public class TooltipImageView extends FrameLayout {

    private TextView tooltip;
    private ImageView image;

    public TooltipImageView(Context context) {
        super(context);
        init();
    }

    public TooltipImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TooltipImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        removeAllViews();
        View view;
        view = inflate(getContext(), R.layout.tooltip_image_layout, this);
        image = (ImageView) view.findViewById(R.id.image);
        tooltip = (TextView) view.findViewById(R.id.tooltip);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        invalidate();
        requestLayout();
    }

    public void setImageTooltip(String url, String tooltipText, View.OnClickListener clickListener) {
        init();

        ImageHandler.LoadImage(image, url);
        tooltip.setText(tooltipText);
        tooltip.setOnClickListener(clickListener);
    }
}
