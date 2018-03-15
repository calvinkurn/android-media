package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.view.viewholder.MultipleAddressItemViewHolder;

import java.util.List;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressItemAdapter extends RecyclerView.Adapter
        <MultipleAddressItemViewHolder>{

    private List<MultipleAddressItemData> itemDataList;

    private MultipleAddressItemAdapterListener listener;

    private MultipleAddressAdapterData productData;

    public MultipleAddressItemAdapter(MultipleAddressAdapterData productData,
                               List<MultipleAddressItemData> itemDataList,
                               MultipleAddressItemAdapterListener listener) {
        this.itemDataList = itemDataList;
        this.listener = listener;
        this.productData = productData;
    }

    @Override
    public MultipleAddressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multiple_address_item_adapter, parent, false);
        return new MultipleAddressItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MultipleAddressItemViewHolder holder, int position) {
        MultipleAddressItemData itemData = itemDataList.get(position);
        holder.bindItemAdapterAddress(
                itemData,
                itemDataList,
                onEditOrderClickedListener(itemData),
                onDeleteOrderClickedListener(position),
                position);

    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    private View.OnClickListener onEditOrderClickedListener(final MultipleAddressItemData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditItemChoosen(productData, data);
            }
        };
    }

    private View.OnClickListener onDeleteOrderClickedListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemDataList.remove(position);
                notifyDataSetChanged();
            }
        };
    }

    public interface MultipleAddressItemAdapterListener {

        void onEditItemChoosen(MultipleAddressAdapterData productData,
                               MultipleAddressItemData addressData);

    }

}
