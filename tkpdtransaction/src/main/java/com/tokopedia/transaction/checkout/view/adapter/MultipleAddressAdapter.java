package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.view.viewholder.MultipleAddressViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements MultipleAddressItemAdapter.MultipleAddressItemAdapterListener {


    private static final int MULTIPLE_ADDRESS_HEADER_LAYOUT =
            R.layout.multiple_address_step_1_header;
    private static final int MULTIPLE_ADDRESS_ADAPTER_LAYOUT =
            R.layout.multiple_address_adapter;
    private static final int MULTIPLE_ADDRESS_FOOTER_LAYOUT =
            R.layout.multiple_address_footer;

    private List<MultipleAddressAdapterData> addressData;

    private MultipleAddressAdapterListener listener;

    private List<Object> adapterObjectList;

    public MultipleAddressAdapter(List<MultipleAddressAdapterData> addressData,
                                  MultipleAddressAdapterListener listener) {
        this.addressData = addressData;
        this.listener = listener;
        adapterObjectList = new ArrayList<>();
        adapterObjectList.addAll(addressData);
    }

    @Override
    public int getItemViewType(int position) {
        if(adapterObjectList.get(position) instanceof MultipleAddressAdapterData)
            return MULTIPLE_ADDRESS_ADAPTER_LAYOUT;
        else if(position == adapterObjectList.size()) return MULTIPLE_ADDRESS_FOOTER_LAYOUT;
        else
            return super.getItemViewType(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewType == MULTIPLE_ADDRESS_HEADER_LAYOUT)
            return new MultipleAddressHeaderViewHolder(itemView);
        else if (viewType == MULTIPLE_ADDRESS_FOOTER_LAYOUT)
            return new MultipleAddressFooterViewHolder(itemView);
        else return new MultipleAddressViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) ==  MULTIPLE_ADDRESS_ADAPTER_LAYOUT) {
            MultipleAddressViewHolder itemViewHolder = (MultipleAddressViewHolder) holder;
            MultipleAddressAdapterData data = (MultipleAddressAdapterData)
                    adapterObjectList.get(position);
            itemViewHolder.bindAdapterView(data, this, listener, isFirstItem(data));
        } else if (holder instanceof MultipleAddressFooterViewHolder)
            ((MultipleAddressFooterViewHolder) holder).goToCourierPageButton
                    .setOnClickListener(onGoToCourierPageButtonClicked(addressData));

    }

    private boolean isFirstItem(MultipleAddressAdapterData data) {
        return (data.getItemListData().get(0).getCartPosition() == 0);
    }

    @Override
    public int getItemCount() {
        return adapterObjectList.size();
    }

    @Override
    public void onEditItemChoosen(MultipleAddressAdapterData productData, MultipleAddressItemData addressData) {
        listener.onItemChoosen(productData, addressData);
    }

    class MultipleAddressHeaderViewHolder extends RecyclerView.ViewHolder {

        MultipleAddressHeaderViewHolder(View itemView) {
            super(itemView);

        }
    }

    class MultipleAddressFooterViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup goToCourierPageButton;

        MultipleAddressFooterViewHolder(View itemView) {
            super(itemView);

            goToCourierPageButton = itemView.findViewById(R.id.go_to_courier_page_button);


        }
    }

    private View.OnClickListener onGoToCourierPageButtonClicked(
            final List<MultipleAddressAdapterData> listData
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGoToChooseCourier(listData);
            }
        };
    }

    public interface MultipleAddressAdapterListener {

        void onGoToChooseCourier(List<MultipleAddressAdapterData> data);

        void onItemChoosen(MultipleAddressAdapterData productData,
                           MultipleAddressItemData addressData);

        void onAddNewShipmentAddress(int addressPositionToAdd,
                                     MultipleAddressAdapterData data,
                                     MultipleAddressItemData addressData);
    }

    public List<MultipleAddressAdapterData> getAddressData() {
        return addressData;
    }
}
