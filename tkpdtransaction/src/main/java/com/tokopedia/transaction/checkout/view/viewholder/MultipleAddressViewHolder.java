package com.tokopedia.transaction.checkout.view.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressAdapter;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressItemAdapter;
import com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceFragment;

import java.util.ArrayList;

/**
 * Created by kris on 3/14/18. Tokopedia
 */

public class MultipleAddressViewHolder extends RecyclerView.ViewHolder{

    private Context context;

    private TextView senderName;

    private ImageView productImage;

    private TextView productName;

    private TextView productPrice;

    private RecyclerView shippingDestinationList;

    private ViewGroup addNewShipmentAddressButton;

    public MultipleAddressViewHolder(Context context, View itemView) {
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


    public void bindAdapterView(
            MultipleAddressAdapterData data,
            MultipleAddressItemAdapter.MultipleAddressItemAdapterListener listener,
            MultipleAddressAdapter.MultipleAddressAdapterListener addressListener,
            boolean firstItemPosition
    ) {
        senderName.setText(data.getSenderName());
        productName.setText(data.getProductName());
        productPrice.setText(data.getProductPrice());
        ImageHandler.LoadImage(productImage, data.getProductImageUrl());
        shippingDestinationList
                .setLayoutManager(new LinearLayoutManager(context));
        shippingDestinationList.setAdapter(
                new MultipleAddressItemAdapter(data, data.getItemListData(), listener)
        );
        addNewShipmentAddressButton.setOnClickListener(
                onAddAddressClickedListener(
                        data.getItemListData().size(),
                        data,
                        data.getItemListData().get(0),
                        addressListener
                )
        );
        if (firstItemPosition) setShowCase(
                context,
                addNewShipmentAddressButton
        );
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

    private View.OnClickListener onAddAddressClickedListener(
            final int latestPositionToAdd,
            final MultipleAddressAdapterData data,
            final MultipleAddressItemData firstItemData,
            final MultipleAddressAdapter.MultipleAddressAdapterListener addressListener) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressListener.onAddNewShipmentAddress(latestPositionToAdd, data, firstItemData);
            }
        };
    }

}
