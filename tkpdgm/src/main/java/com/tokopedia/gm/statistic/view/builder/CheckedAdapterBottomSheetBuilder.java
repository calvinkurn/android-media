package com.tokopedia.gm.statistic.view.builder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.tokopedia.gm.statistic.view.adapter.model.CheckedBottomSheetMenuItem;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetAdapterBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemAdapter;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 7/17/17.
 */
public class CheckedAdapterBottomSheetBuilder extends BottomSheetAdapterBuilder {
    private List<Boolean> mSelectionItems;


    public CheckedAdapterBottomSheetBuilder(Context context) {
        super(context);
        mSelectionItems = new ArrayList<>();
    }

    public void setmSelectionItems(List<Boolean> mSelectionItems) {
        this.mSelectionItems = mSelectionItems;
    }

    public void addSelection(boolean value) {
        mSelectionItems.add(value);
    }

    @NonNull
    @Override
    protected BottomSheetMenuItem getBottomSheetMenuItem(int itemTextColor, int itemBackground, int tintColor, MenuItem item, int position) {
        CheckedBottomSheetMenuItem checkedBottomSheetMenuItem = new CheckedBottomSheetMenuItem(item, itemTextColor, itemBackground, tintColor);
        if (position != Integer.MIN_VALUE)
            checkedBottomSheetMenuItem.setChecked(mSelectionItems.get(position));
        return checkedBottomSheetMenuItem;
    }

    @NonNull
    @Override
    protected BottomSheetItemAdapter getBottomSheetItemAdapter(BottomSheetItemClickListener itemClickListener) {
        return new com.tokopedia.gm.statistic.view.adapter.BottomSheetItemAdapter(
                mItems,
                mMode, itemClickListener );
    }
}
