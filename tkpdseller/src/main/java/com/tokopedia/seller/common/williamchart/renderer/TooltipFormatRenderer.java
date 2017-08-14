package com.tokopedia.seller.common.williamchart.renderer;

import android.widget.TextView;

import com.tokopedia.seller.common.williamchart.model.TooltipModel;

import java.util.List;

/**
 * Created by normansyahputa on 8/3/17.
 */

public abstract class TooltipFormatRenderer implements StringFormatRenderer {
    @Override
    public String formatString(String rawString) {
        throw new RuntimeException("don't use this code!!");
    }

    public abstract void formatValue(List<TextView> textViews, TooltipModel tooltipModel);
}
