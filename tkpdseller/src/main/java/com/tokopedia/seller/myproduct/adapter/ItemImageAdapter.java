package com.tokopedia.seller.myproduct.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.myproduct.adapter.ItemImageAndText;
import com.tokopedia.seller.myproduct.fragment.ImageChooserDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sebastianuskh on 8/31/16.
 */

public class ItemImageAdapter extends RecyclerView.Adapter<ItemImageAdapter.ImageViewHolder> {

    List<? extends ItemImageAndText> items;
    ImageChooserDialog.SelectWithImage listener;
    int selectedItem = -1;

    public ItemImageAdapter(List<? extends ItemImageAndText> items, ImageChooserDialog.SelectWithImage listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selector, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.setItem(items.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelected(String selectedCatalog) {
        for (int i = 0; i < items.size(); i++)
            if (items.get(i).getData().second.equals(selectedCatalog)) {
                selectedItem = i;
            }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.image_selected)
        ImageView imageSelected;

        @BindView(R2.id.desc_selected)
        TextView descSelected;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setItem(ItemImageAndText itemImageAndText, final int position, final ImageChooserDialog.SelectWithImage listener) {
            if (itemImageAndText.getData().first != null) {
                imageSelected.setVisibility(View.VISIBLE);
                ImageHandler.LoadImage(imageSelected, itemImageAndText.getData().first);
            } else
                imageSelected.setVisibility(View.INVISIBLE);
            descSelected.setText(itemImageAndText.getData().second);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    listener.itemSelected(position);
                    if (position != 0) {
                        selectedItem = position;
                        notifyDataSetChanged();
                    } else {
                        listener.itemSelected(position);
                    }
                }
            });

            if (selectedItem != -1 && position == selectedItem) {
                itemView.setBackgroundResource(R.color.selector_lv);
            } else {
                itemView.setBackgroundResource(0);
            }
        }
    }
}
