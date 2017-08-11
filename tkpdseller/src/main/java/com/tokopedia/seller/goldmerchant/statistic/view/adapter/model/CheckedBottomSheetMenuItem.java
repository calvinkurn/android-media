package com.tokopedia.seller.goldmerchant.statistic.view.adapter.model;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.MenuItem;

import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetMenuItem;

/**
 * @author normansyahputa on 7/17/17.
 */

public class CheckedBottomSheetMenuItem extends BottomSheetMenuItem {
    private boolean isChecked;

    public CheckedBottomSheetMenuItem(MenuItem item, @ColorInt int textColor, @DrawableRes int background, @ColorInt int tintColor) {
        super(item, textColor, background, tintColor);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
