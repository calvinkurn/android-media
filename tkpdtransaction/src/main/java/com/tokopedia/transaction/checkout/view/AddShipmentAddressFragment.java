package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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

import static com.tokopedia.transaction.checkout.view.MultipleAddressFragment.ADD_SHIPMENT_ADDRESS_REQUEST_CODE;
import static com.tokopedia.transaction.checkout.view.MultipleAddressFragment.EDIT_SHIPMENT_ADDRESS_REQUEST_CODE;

/**
 * Created by kris on 1/25/18. Tokopedia
 */

public class AddShipmentAddressFragment extends TkpdFragment {

    private static final String PRODUCT_DATA_EXTRAS = "PRODUCT_DATA_EXTRAS";
    private static final String ADDRESS_DATA_EXTRAS = "ADDRESS_DATA_EXTRAS";
    private static final String MODE_EXTRA = "MODE_EXTRAS";
    public static final int ADD_MODE = 1;
    public static final int EDIT_MODE = 2;

    private EditText quantityField;
    private EditText notesEditText;
    private ViewGroup addressLayout;
    private ViewGroup notesLayout;
    private TextView addressTitle;
    private TextView addressReceiverName;
    private TextView address;
    private TextView saveChangesButton;

    public static AddShipmentAddressFragment newInstance(MultipleAddressAdapterData data,
                                                         MultipleAddressItemData addressData,
                                                         int mode) {

        AddShipmentAddressFragment fragment = new AddShipmentAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_DATA_EXTRAS, data);
        bundle.putParcelable(ADDRESS_DATA_EXTRAS, addressData);
        bundle.putInt(MODE_EXTRA, mode);
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
        setNotesView(view, itemData);
        setAddressView(view, itemData);
        saveChangesButton = view.findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(onSaveChangesClickedListener(itemData));
        if (getArguments().getInt(MODE_EXTRA) == ADD_MODE) showChooseAddressButton(view);
        return view;
    }

    private void showChooseAddressButton(View view) {
        ViewGroup chooseAddressButton = view.findViewById(R.id.choose_address_button);
        chooseAddressButton.setOnClickListener(onChooseAddressClickedListener());
        addressLayout.setVisibility(View.GONE);
        chooseAddressButton.setVisibility(View.VISIBLE);
        saveChangesButton.setVisibility(View.GONE);
    }

    private void setAddressView(View view, MultipleAddressItemData itemData) {
        addressLayout = view.findViewById(R.id.address_layout);
        addressTitle = view.findViewById(R.id.address_title);
        addressReceiverName = view.findViewById(R.id.address_receiver_name);
        address = view.findViewById(R.id.address);
        addressTitle.setText(itemData.getAddressTitle());
        addressReceiverName.setText(itemData.getAddressReceiverName());
        address.setText(itemData.getAddress());
        addressLayout.setOnClickListener(onAddressLayoutClickedListener());
    }

    private void setNotesView(View view, MultipleAddressItemData itemData) {
        ViewGroup emptyNotesLayout = view.findViewById(R.id.empty_notes_layout);
        TextView insertNotesButton = view.findViewById(R.id.insert_notes_button);
        notesLayout = view.findViewById(R.id.notes_layout);
        notesEditText = view.findViewById(R.id.notes_edit_text);
        if (itemData.getProductNotes().isEmpty()) {
            emptyNotesLayout.setVisibility(View.VISIBLE);
            insertNotesButton.setOnClickListener(
                    onInsertNotesButtonClickedListener(emptyNotesLayout, notesLayout)
            );
        } else {
            notesLayout.setVisibility(View.VISIBLE);
            notesEditText.setText(itemData.getProductNotes());
        }
    }

    private void setProductQuantityView(View view, MultipleAddressItemData itemData) {
        quantityField = view.findViewById(R.id.quantity_field);
        ImageView decreaseButton = view.findViewById(R.id.decrease_quantity);
        ImageView increaseButton = view.findViewById(R.id.increase_quantity);
        quantityField.setText(itemData.getProductQty());
        quantityField.addTextChangedListener(quantityTextWatcher());
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

    private TextWatcher quantityTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().isEmpty())
                    quantityField.setText("1");
                else {
                    if (!addressLayout.isShown()) {
                        saveChangesButton.setVisibility(View.GONE);
                    } else if (Integer.parseInt(charSequence.toString()) > 10000) {
                        saveChangesButton.setVisibility(View.GONE);
                        //TODO show error message
                    } else if (Integer.parseInt(charSequence.toString()) < 1) {
                        saveChangesButton.setVisibility(View.GONE);
                        //TODO show error message
                    } else saveChangesButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private View.OnClickListener onChooseAddressClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private View.OnClickListener onSaveChangesClickedListener(final MultipleAddressItemData itemData) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO ALTER DATA HERE, ALSO MAKE SOME VIEWS GLOBAL VARIABLE
                if (addressLayout.isShown()) {
                    if(getArguments().getInt(MODE_EXTRA) == ADD_MODE) {
                        addNewAddressItem(itemData);
                    } else {
                        insertDataToModel(itemData);
                        getTargetFragment().onActivityResult(
                                EDIT_SHIPMENT_ADDRESS_REQUEST_CODE,
                                Activity.RESULT_OK,
                                new Intent()
                        );
                    }
                } else {
                    //TODO Show error here
                }
            }
        };
    }

    private void addNewAddressItem(MultipleAddressItemData itemData) {
        MultipleAddressItemData newAddressData = new MultipleAddressItemData();
        newAddressData.setProductWeight(itemData.getProductWeight());
        insertDataToModel(newAddressData);
        MultipleAddressAdapterData productData = getArguments()
                .getParcelable(PRODUCT_DATA_EXTRAS);
        productData.getItemListData().add(newAddressData);
        getTargetFragment().onActivityResult(
                ADD_SHIPMENT_ADDRESS_REQUEST_CODE,
                Activity.RESULT_OK,
                new Intent()
        );
    }

    private void insertDataToModel(MultipleAddressItemData itemData) {
        itemData.setProductQty(quantityField.getText().toString());
        itemData.setAddressTitle(addressTitle.getText().toString());
        itemData.setAddressReceiverName(addressReceiverName.getText().toString());
        itemData.setAddress(address.getText().toString());
        itemData.setCartId("0");
        if (notesLayout.isShown())
            itemData.setProductNotes(notesEditText.getText().toString());
    }
}
