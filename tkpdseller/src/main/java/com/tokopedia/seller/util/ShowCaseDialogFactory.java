package com.tokopedia.seller.util;

import com.tokopedia.seller.R;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;

/**
 * Created by Hendry on 4/13/2017.
 */

public class ShowCaseDialogFactory {
    public static ShowCaseDialog createTkpdShowCase (){
        return new ShowCaseBuilder()
                .textColorRes(android.R.color.white)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .textSizeRes(R.dimen.fontvs)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .prevStringRes(R.string.back)
                .nextStringRes(R.string.next)
                .finishStringRes(R.string.title_done)
                .useCircleIndicator(true)
                .clickable(true)
                .build();
    }
}
