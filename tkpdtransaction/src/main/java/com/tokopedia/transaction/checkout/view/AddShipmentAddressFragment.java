package com.tokopedia.transaction.checkout.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressItemData;

/**
 * Created by kris on 1/25/18. Tokopedia
 */

public class AddShipmentAddressFragment extends TkpdFragment {

    private static final String PRODUCT_DATA_EXTRAS = "PRODUCT_DATA_EXTRAS";
    private static final String ADDRESS_DATA_EXTRAS = "ADDRESS_DATA_EXTRAS";

    public static AddShipmentAddressFragment newInstance(MultipleAddressAdapterData data,
                                                         MultipleAddressItemData addressData) {

        AddShipmentAddressFragment fragment = new AddShipmentAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_DATA_EXTRAS, data);
        bundle.putParcelable(ADDRESS_DATA_EXTRAS, addressData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.add_shipping_address_fragment, container, false);
        MultipleAddressAdapterData productData = getArguments().getParcelable(PRODUCT_DATA_EXTRAS);
        MultipleAddressItemData itemData = getArguments().getParcelable(ADDRESS_DATA_EXTRAS);
        TextView senderName = view.findViewById(R.id.sender_name);
        setProductView(view, productData, senderName);
        setProductQuantityView(view, itemData);
        setAddressView(view, itemData);
        TextView saveChangesButton = view.findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(onSaveChangesClickedListener(itemData));
        return view;
    }

    private void setAddressView(View view, MultipleAddressItemData itemData) {
        ViewGroup addressLayout = setNotesView(view, itemData);
        TextView addressTitle = view.findViewById(R.id.address_title);
        TextView addressReceiverName = view.findViewById(R.id.address_receiver_name);
        TextView address = view.findViewById(R.id.address);
        addressTitle.setText(itemData.getAddressTitle());
        addressReceiverName.setText(itemData.getAddressReceiverName());
        address.setText(itemData.getAddress());
        addressLayout.setOnClickListener(onAddressLayoutClickedListener());
    }

    private ViewGroup setNotesView(View view, MultipleAddressItemData itemData) {
        ViewGroup emptyNotesLayout = view.findViewById(R.id.empty_notes_layout);
        TextView insertNotesButton = view.findViewById(R.id.insert_notes_button);
        ViewGroup notesLayout = view.findViewById(R.id.notes_layout);
        EditText notesEditText = view.findViewById(R.id.notes_edit_text);
        ViewGroup addressLayout = view.findViewById(R.id.address_layout);
        if(itemData.getProductNotes().isEmpty()) {
            emptyNotesLayout.setVisibility(View.VISIBLE);
            insertNotesButton.setOnClickListener(
                    onInsertNotesButtonClickedListener(emptyNotesLayout, notesLayout)
            );
        } else notesLayout.setVisibility(View.VISIBLE);
        return addressLayout;
    }

    private void setProductQuantityView(View view, MultipleAddressItemData itemData) {
        EditText quantityField = view.findViewById(R.id.quantity_field);
        ImageView decreaseButton = view.findViewById(R.id.decrease_quantity);
        ImageView increaseButton = view.findViewById(R.id.increase_quantity);
        quantityField.setText(itemData.getProductQty());
        decreaseButton.setOnClickListener(onDecreaseButtonClickedListener(quantityField));
        increaseButton.setOnClickListener(onIncreaseButtonClickedListener(quantityField));
    }

    private void setProductView(View view, MultipleAddressAdapterData productData, TextView senderName) {
        senderName.setText(productData.getSenderName());
        ImageView productImage = view.findViewById(R.id.product_image);
        ImageHandler.LoadImage(productImage, productData.getProductImageUrl());
        TextView productName = view.findViewById(R.id.product_name);
        productName.setText(productData.getProductName());
    }

    private View.OnClickListener onDecreaseButtonClickedListener(final EditText quantityField) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(quantityField.getText().toString());
                quantityField.setText(String.valueOf(quantity - 1));
            }
        };
    }

    private View.OnClickListener onIncreaseButtonClickedListener(final EditText quantityField) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(quantityField.getText().toString());
                quantityField.setText(String.valueOf(quantity + 1));
            }
        };
    }

    private View.OnClickListener onInsertNotesButtonClickedListener(final ViewGroup emptyNotesLayout,
                                                                    final ViewGroup notesLayout) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyNotesLayout.setVisibility(View.GONE);
                notesLayout.setVisibility(View.VISIBLE);
            }
        };
    }

    private View.OnClickListener onAddressLayoutClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private View.OnClickListener onSaveChangesClickedListener(MultipleAddressItemData itemData) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }
}
