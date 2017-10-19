package com.tokopedia.seller.common.bottomsheet.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetAdapterBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItem;
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

    public void addSelection(int index, boolean value) {
        mSelectionItems.add(index, value);
    }

    @Override
    public View createView(int titleTextColor, int backgroundDrawable, int backgroundColor, int dividerBackground, int itemTextColor, int itemBackground, int tintColor, int itemLayoutRes, BottomSheetItemClickListener itemClickListener) {
        mItems = remakeAdapterItems();
        return super.createView(titleTextColor, backgroundDrawable, backgroundColor, dividerBackground, itemTextColor, itemBackground, tintColor, itemLayoutRes, itemClickListener);
    }

    private List<BottomSheetItem> remakeAdapterItems() {
        for(int i = 0; i < mItems.size(); i++){
            BottomSheetItem bottomSheetItem = mItems.get(i);
            if(bottomSheetItem instanceof  CheckedBottomSheetMenuItem) {
                if (mSelectionItems.size() > 0) {
                    ((CheckedBottomSheetMenuItem)bottomSheetItem).setChecked(mSelectionItems.get(i));
                    mItems.set(i, bottomSheetItem);
                }
            }
        }
        return mItems;
    }

    @NonNull
    @Override
    protected BottomSheetMenuItem getBottomSheetMenuItem(int itemTextColor, int itemBackground, int tintColor, MenuItem item, int position) {
        CheckedBottomSheetMenuItem checkedBottomSheetMenuItem = new CheckedBottomSheetMenuItem(item, itemTextColor, itemBackground, tintColor);
        if (position != Integer.MIN_VALUE && mSelectionItems.size() > 0) {
            checkedBottomSheetMenuItem.setChecked(mSelectionItems.get(position));
        }
        return checkedBottomSheetMenuItem;
    }

    @NonNull
    @Override
    protected CheckedBottomSheetItemAdapter getBottomSheetItemAdapter(BottomSheetItemClickListener itemClickListener) {
        return new CheckedBottomSheetItemAdapter(
                mItems,
                mMode, itemClickListener);
    }
}
