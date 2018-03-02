package com.tokopedia.transaction.checkout.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements MultipleAddressItemAdapter.MultipleAddressItemAdapterListener {


    private static final int MULTIPLE_ADDRESS_HEADER_LAYOUT = R.layout.multiple_address_header;
    private static final int MULTIPLE_ADDRESS_ADAPTER_LAYOUT = R.layout.multiple_address_adapter;
    private static final int MULTIPLE_ADDRESS_FOOTER_LAYOUT = R.layout.multiple_address_footer;
    private static final int HEADER_SIZE = 1;
    private static final int FOOTER_SIZE = 1;
    private static final int FIRST_ITEM_POSITION = 1;

    private List<MultipleAddressAdapterData> addressData;

    private MultipleAddressAdapterListener listener;


    public MultipleAddressAdapter(List<MultipleAddressAdapterData> addressData,
                                  MultipleAddressAdapterListener listener) {
        this.addressData = addressData;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return MULTIPLE_ADDRESS_HEADER_LAYOUT;
        else if (position > addressData.size()) return MULTIPLE_ADDRESS_FOOTER_LAYOUT;
        else return MULTIPLE_ADDRESS_ADAPTER_LAYOUT;
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

        if (holder instanceof MultipleAddressViewHolder) {
            MultipleAddressViewHolder itemViewHolder = (MultipleAddressViewHolder) holder;
            MultipleAddressAdapterData data = addressData.get(position - 1);
            itemViewHolder.senderName.setText(data.getSenderName());
            itemViewHolder.productName.setText(data.getProductName());
            itemViewHolder.productPrice.setText(data.getProductPrice());
            ImageHandler.LoadImage(itemViewHolder.productImage, data.getProductImageUrl());
            itemViewHolder.shippingDestinationList
                    .setLayoutManager(new LinearLayoutManager(itemViewHolder.context));
            itemViewHolder.shippingDestinationList.setAdapter(
                    new MultipleAddressItemAdapter(data, data.getItemListData(), this)
            );
            itemViewHolder.addNewShipmentAddressButton.setOnClickListener(
                    onAddAddressClickedListener(data.getItemListData().size(), data, data.getItemListData().get(0))
            );
            if (position == FIRST_ITEM_POSITION) setShowCase(itemViewHolder.context,
                    itemViewHolder.addNewShipmentAddressButton);
        } else if (holder instanceof MultipleAddressFooterViewHolder)
            ((MultipleAddressFooterViewHolder) holder).goToCourierPageButton
                    .setOnClickListener(onGoToCourierPageButtonClicked(addressData));

    }

    @Override
    public int getItemCount() {
        return HEADER_SIZE + addressData.size() + FOOTER_SIZE;
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

    class MultipleAddressViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        private TextView senderName;

        private ImageView productImage;

        private TextView productName;

        private TextView productPrice;

        private RecyclerView shippingDestinationList;

        private ViewGroup addNewShipmentAddressButton;

        MultipleAddressViewHolder(Context context, View itemView) {
            super(itemView);

            this.context = context;

            senderName = itemView.findViewById(R.id.sender_name);

            productImage = itemView.findViewById(R.id.product_image);

            productName = itemView.findViewById(R.id.product_name);

            productPrice = itemView.findViewById(R.id.product_price);

            shippingDestinationList = itemView.findViewById(R.id.shipping_destination_list);

            addNewShipmentAddressButton = itemView
                    .findViewById(R.id.add_new_shipment_address_button);
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

    private View.OnClickListener onAddAddressClickedListener(
            final int latestPositionToAdd,
            final MultipleAddressAdapterData data,
            final MultipleAddressItemData firstItemData) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddNewShipmentAddress(latestPositionToAdd, data, firstItemData);
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

    private void setShowCase(Context context, ViewGroup addAddress) {
        ShowCaseObject showCase = new ShowCaseObject(
                addAddress, "Kirim Barang Sama ke Bebeberapa\n" +
                "Alamat.", "Klik tombol untuk mengirim barang yang sama ke beda alamat.",
                ShowCaseContentPosition.UNDEFINED);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();

        showCaseObjectList.add(showCase);

        ShowCaseDialog showCaseDialog = createShowCaseDialog();

        if (!ShowCasePreference.hasShown(context, CartAddressChoiceFragment.class.getName()))
            showCaseDialog.show(
                    (Activity) context,
                    CartAddressChoiceFragment.class.getName(),
                    showCaseObjectList
            );
    }

    private ShowCaseDialog createShowCaseDialog() {
        return new ShowCaseBuilder()
                .customView(R.layout.show_case_checkout)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.fontvs)
                .finishStringRes(R.string.show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

    public List<MultipleAddressAdapterData> getAddressData() {
        return addressData;
    }
}
