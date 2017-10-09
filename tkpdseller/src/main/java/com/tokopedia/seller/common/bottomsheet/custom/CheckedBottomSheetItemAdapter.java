package com.tokopedia.seller.common.bottomsheet.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItem;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemAdapter;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;

import java.util.List;

/**
 * @author normansyahputa on 7/17/17.
 */
public class CheckedBottomSheetItemAdapter extends BottomSheetItemAdapter {
    public static final int TYPE_CHECKED_ITEM = 3;

    public CheckedBottomSheetItemAdapter(List<BottomSheetItem> items, int mode, BottomSheetItemClickListener listener) {
        super(items, mode, listener);
    }

    @Override
    public int getItemViewType(int position) {
        BottomSheetItem item = mItems.get(position);
        if (item instanceof CheckedBottomSheetMenuItem) {
            return TYPE_CHECKED_ITEM;
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mMode == BottomSheetBuilder.MODE_LIST) {
            if (viewType == TYPE_CHECKED_ITEM) {
                return new CheckedItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_checked_bottomsheetbuilder_list_adapter, parent, false));
            }
        }

        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BottomSheetItem item = mItems.get(position);

        if (item instanceof CheckedBottomSheetMenuItem) {
            if (holder.getItemViewType() == TYPE_CHECKED_ITEM) {
                ((CheckedItemViewHolder) holder).setData((CheckedBottomSheetMenuItem) item);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    public class CheckedItemViewHolder extends ItemViewHolder {

        private final ImageView checkedImage;

        public CheckedItemViewHolder(View itemView) {
            super(itemView);
            checkedImage = (ImageView) itemView.findViewById(R.id.checked_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
        }

        public void setData(CheckedBottomSheetMenuItem item) {
            super.setData(item);

            if (item.isChecked()) {
                checkedImage.setVisibility(View.VISIBLE);
            } else {
                checkedImage.setVisibility(View.GONE);
            }
        }
    }
}
