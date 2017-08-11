package com.tokopedia.seller.goldmerchant.statistic.view.builder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;

import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetAdapterBuilder;

/**
 * Created by normansyahputa on 7/17/17.
 */

public class CheckedBottomSheetBuilder extends BottomSheetBuilder {
    public CheckedBottomSheetBuilder(Context context, CoordinatorLayout coordinatorLayout) {
        super(context, coordinatorLayout);
    }

    public CheckedBottomSheetBuilder(Context context) {
        super(context);
    }

    public CheckedBottomSheetBuilder(Context context, @StyleRes int theme) {
        super(context, theme);
    }

    public BottomSheetBuilder addItem(int id, @StringRes int title, @DrawableRes int icon, boolean value) {
        return addItem(id, mContext.getString(title), ContextCompat.getDrawable(mContext, icon), value);
    }

    public BottomSheetBuilder addItem(int id, @StringRes int title, Drawable icon, boolean value) {
        return addItem(id, mContext.getString(title), icon, value);
    }

    public BottomSheetBuilder addItem(int id, String title, @DrawableRes int icon, boolean value) {
        return addItem(id, title, ContextCompat.getDrawable(mContext, icon), value);
    }

    public BottomSheetBuilder addItem(int id, String title, Drawable icon, boolean value) {
        if (mAdapterBuilder != null && mAdapterBuilder instanceof CheckedAdapterBottomSheetBuilder) {
            ((CheckedAdapterBottomSheetBuilder) mAdapterBuilder).addSelection(value);
        }
        mAdapterBuilder.addItem(id, title, icon, mItemTextColor, mItemBackground, mIconTintColor);
        return this;
    }

    @NonNull
    @Override
    protected BottomSheetAdapterBuilder getBottomSheetAdapterBuilder() {
        return new CheckedAdapterBottomSheetBuilder(mContext);
    }
}
