package com.tokopedia.seller.common.williamchart.util;

/**
 * Created by normansyahputa on 7/7/17.
 * <p>
 * this class represent {@link com.tokopedia.seller.common.williamchart.view.ChartView} configuration
 * with animation capability.
 */

public interface AnimationGraphConfiguration extends BasicGraphConfiguration {

    int alpha();

    int duration();

    int easingId();

    float overlapFactor();

    float startX();

    float startY();

    Runnable endAnimation();
}
