package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.di.component.AddShipmentAddressComponent;
import com.tokopedia.transaction.checkout.view.di.component.DaggerAddShipmentAddressComponent;
import com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity;

import javax.inject.Inject;

import static com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity.EXTRA_SELECTED_ADDRESS_DATA;
import static com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity.REQUEST_CODE;
import static com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity.RESULT_CODE_ACTION_SELECT_ADDRESS;
import static com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceActivity.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST;

/**
 * Created by kris on 1/25/18. Tokopedia
 */

public class AddShipmentAddressActivity extends BasePresenterActivity {

    private static final String PRODUCT_DATA_EXTRAS = "PRODUCT_DATA_EXTRAS";
    private static final String ADDRESS_DATA_EXTRAS = "ADDRESS_DATA_EXTRAS";
    private static final String MODE_EXTRA = "MODE_EXTRAS";
    public static final String ADDRESS_DATA_RESULT = "ADDRESxS_DATA_RESULT";
    public static final int ADD_MODE = 1;
    public static final int EDIT_MODE = 2;

    private int formMode;

    private EditText quantityField;
    private EditText notesEditText;
    private ViewGroup addressLayout;
    private ViewGroup notesLayout;
    private TextView addressTitle;
    private TextView addressReceiverName;
    private TextView address;
    private TextView saveChangesButton;
    private TextView addAddressErrorTextView;
    private ViewGroup chooseAddressButton;

    @Inject
    IAddShipmentAddressPresenter presenter;

    public static Intent createIntent(Context context,
                                      MultipleAddressAdapterData data,
                                      MultipleAddressItemData addressData,
                                      int mode) {
        Intent intent = new Intent(context, AddShipmentAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_DATA_EXTRAS, data);
        bundle.putParcelable(ADDRESS_DATA_EXTRAS, addressData);
        bundle.putInt(MODE_EXTRA, mode);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        formMode = extras.getInt(MODE_EXTRA);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.add_shipping_address_fragment;
    }

    @Override
    protected void initView() {
        initInjector();
        presenter.initiateData(
                (MultipleAddressAdapterData) getIntent().getExtras()
                        .getParcelable(PRODUCT_DATA_EXTRAS),
                (MultipleAddressItemData) getIntent().getExtras()
                        .getParcelable(ADDRESS_DATA_EXTRAS));
        TextView senderName = findViewById(R.id.sender_name);
        setProductView(presenter.getMultipleAddressAdapterData(), senderName);
        setProductQuantityView(presenter.getMultipleItemData());
        setNotesView(presenter.getMultipleItemData());
        setAddressView(presenter.getMultipleItemData());
        addAddressErrorTextView = findViewById(R.id.add_address_error_warning);
        saveChangesButton = findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(onSaveChangesClickedListener());
        if (formMode == ADD_MODE) showChooseAddressButton();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    private void showChooseAddressButton() {
        addressLayout.setVisibility(View.GONE);
        chooseAddressButton.setVisibility(View.VISIBLE);
        saveChangesButton.setVisibility(View.GONE);
    }

    private void showAddressLayout() {
        addressLayout.setVisibility(View.VISIBLE);
        chooseAddressButton.setVisibility(View.GONE);
        saveChangesButton.setVisibility(View.VISIBLE);
    }

    private void setAddressView(MultipleAddressItemData itemData) {
        addressLayout = findViewById(R.id.address_layout);
        addressTitle = findViewById(R.id.address_title);
        addressReceiverName = findViewById(R.id.address_receiver_name);
        address = findViewById(R.id.address);
        addressTitle.setText(itemData.getAddressTitle());
        addressReceiverName.setText(itemData.getAddressReceiverName());
        address.setText(itemData.getAddressStreet()
                + ", " + itemData.getAddressCityName()
                + ", " + itemData.getAddressProvinceName()
                + ", " + itemData.getRecipientPhoneNumber());
        addressLayout.setOnClickListener(onAddressLayoutClickedListener());
        chooseAddressButton = findViewById(R.id.choose_address_button);
        chooseAddressButton.setOnClickListener(onChooseAddressClickedListener());
    }

    private void updateAddressView(RecipientAddressModel editableAddress) {
        addressTitle.setText(editableAddress.getAddressName());
        addressReceiverName.setText(editableAddress.getRecipientName());
        address.setText(editableAddress.getAddressStreet()
                + ", " + editableAddress.getAddressCityName()
                + ", " + editableAddress.getAddressProvinceName()
                + ", " + editableAddress.getRecipientPhoneNumber());
    }

    private void setNotesView(MultipleAddressItemData itemData) {
        ViewGroup emptyNotesLayout = findViewById(R.id.empty_notes_layout);
        TextView insertNotesButton = findViewById(R.id.insert_notes_button);
        notesLayout = findViewById(R.id.notes_layout);
        notesEditText = findViewById(R.id.notes_edit_text);
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

    private void setProductQuantityView(MultipleAddressItemData itemData) {
        quantityField = findViewById(R.id.quantity_field);
        ImageView decreaseButton = findViewById(R.id.decrease_quantity);
        ImageView increaseButton = findViewById(R.id.increase_quantity);
        quantityField.setText(itemData.getProductQty());
        quantityField.setOnClickListener(onQuantityEditTextClicked());
        quantityField.addTextChangedListener(quantityTextWatcher(
                itemData,
                decreaseButton,
                increaseButton
        ));
        decreaseButton.setOnClickListener(onDecreaseButtonClickedListener(quantityField));
        increaseButton.setOnClickListener(onIncreaseButtonClickedListener(quantityField));
    }

    private void setProductView(MultipleAddressAdapterData productData, TextView senderName) {
        senderName.setText(productData.getSenderName());
        ImageView productImage = findViewById(R.id.product_image);
        ImageHandler.LoadImage(productImage, productData.getProductImageUrl());
        TextView productName = findViewById(R.id.product_name);
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
                openAddressSelectionPage();
            }
        };
    }

    private void setEditButtonVisibility(CharSequence charSequence, MultipleAddressItemData data) {
        if (charSequence.toString().isEmpty()
                || Integer.parseInt(charSequence.toString()) < 1)
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
            } else if (Integer.parseInt(charSequence.toString()) > data.getMaxQuantity()) {
                saveChangesButton.setVisibility(View.GONE);
                //TODO show overquantity Error Message
            } else if (Integer.parseInt(charSequence.toString()) < data.getMinQuantity()) {
                saveChangesButton.setVisibility(View.GONE);
                //TODO show lack quantity Error Message
            } else saveChangesButton.setVisibility(View.VISIBLE);
        }
    }

    private void setQuantityButtonAvailability(CharSequence charSequence,
                                               ImageView decreaseButton,
                                               ImageView increaseButton) {
        if (charSequence.toString().isEmpty() || Integer.parseInt(charSequence.toString()) == 0) {
            decreaseButton.setClickable(false);
            decreaseButton.setEnabled(false);
            increaseButton.setClickable(false);
            increaseButton.setEnabled(false);
        } else if (Integer.parseInt(charSequence.toString()) == 1) {
            decreaseButton.setClickable(false);
            decreaseButton.setEnabled(false);
        } else {
            decreaseButton.setClickable(true);
            decreaseButton.setEnabled(true);
            increaseButton.setClickable(true);
            increaseButton.setEnabled(true);
        }
    }

    private View.OnClickListener onChooseAddressClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddressSelectionPage();
            }
        };
    }

    private void openAddressSelectionPage() {
        Intent intent = CartAddressChoiceActivity.createInstance(this,
                TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private View.OnClickListener onSaveChangesClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO ALTER DATA HERE, ALSO MAKE SOME VIEWS GLOBAL VARIABLE
                if (addressLayout.isShown()) {
                    addAddressErrorTextView.setVisibility(View.GONE);
                    if (formMode == ADD_MODE) {
                        addNewAddressItem();
                    } else {
                        changeAddressData();
                    }
                } else {
                    //TODO Show error here
                    addAddressErrorTextView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private void addNewAddressItem() {
        Intent intent = new Intent();
        MultipleAddressItemData newItemData = presenter.confirmAddData(
                quantityField.getText().toString(),
                checkNotesAvailability(notesLayout.isShown(), notesEditText)
        );
        intent.putExtra(ADDRESS_DATA_RESULT, newItemData);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void changeAddressData() {
        Intent intent = new Intent();
        MultipleAddressItemData editedItemData = presenter.confirmEditData(
                quantityField.getText().toString(),
                checkNotesAvailability(notesLayout.isShown(), notesEditText)
        );
        intent.putExtra(ADDRESS_DATA_RESULT, editedItemData);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void initInjector() {
        AddShipmentAddressComponent component = DaggerAddShipmentAddressComponent.builder().build();
        component.inject(this);
    }

    private View.OnClickListener onQuantityEditTextClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText) view).selectAll();
            }
        };
    }

    private TextWatcher quantityTextWatcher(final MultipleAddressItemData data,
                                            final ImageView decreaseButton,
                                            final ImageView increaseButton) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setQuantityButtonAvailability(charSequence, decreaseButton, increaseButton);
                setEditButtonVisibility(charSequence, data);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_CODE_ACTION_SELECT_ADDRESS:
                    RecipientAddressModel addressModel =
                            data.getParcelableExtra(EXTRA_SELECTED_ADDRESS_DATA);
                    presenter.setEditableModel(addressModel);
                    showAddressLayout();
                    updateAddressView(presenter.getEditableModel());
                    break;

                default:
                    finish();
                    break;
            }
        }
    }

    private String checkNotesAvailability(boolean notesFieldShown, EditText notesEditText) {
        if (notesFieldShown)
            return notesEditText.getText().toString();
        else return "";
    }
}
