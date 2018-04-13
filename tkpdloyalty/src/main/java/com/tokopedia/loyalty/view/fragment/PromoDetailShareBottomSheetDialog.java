package com.tokopedia.loyalty.view.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;

/**
 * @author Aghny A. Putra on 27/03/18
 */

public class PromoDetailShareBottomSheetDialog extends BottomSheetDialog {



    public PromoDetailShareBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public PromoDetailShareBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected PromoDetailShareBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
