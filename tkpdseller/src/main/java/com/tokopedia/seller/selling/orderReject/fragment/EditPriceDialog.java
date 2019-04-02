package com.tokopedia.seller.selling.orderReject.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.core2.R;

import com.tokopedia.core.myproduct.utils.VerificationUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.Pair;
import com.tokopedia.seller.selling.model.orderShipping.OrderProduct;
import com.tokopedia.seller.selling.orderReject.adapter.ProductListAdapter;
import com.tokopedia.seller.selling.orderReject.model.ModelEditPrice;

import org.parceler.Parcels;


/**
 * Created by Erry on 6/6/2016.
 */
public class EditPriceDialog extends DialogFragment {


    TextView titleTxt;
    CheckBox checkBox;
    Spinner priceSpiner;
    Spinner weightSpiner;
    EditText price;
    EditText weight;

    public static final String TITLE = "title";
    OrderProduct orderProduct;
    OnChangePrice onChangePrice;
    int position;
    private boolean isStockChange;

    public static EditPriceDialog newInstance(OrderProduct orderProduct, int position, boolean isStockChange) {

        Bundle args = new Bundle();
        args.putString(TITLE, orderProduct.getProductName());
        args.putParcelable(ProductListAdapter.ORDER_PRODUCT, Parcels.wrap(orderProduct));
        args.putInt(ProductListAdapter.POSITION, position);
        args.putBoolean(ProductListAdapter.STOCK_CHANGE_CONDITION, isStockChange);
        EditPriceDialog fragment = new EditPriceDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListenerChangePrice(OnChangePrice onChangePrice){
        this.onChangePrice = onChangePrice;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reject_order_edit_price, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.title_edit), null);
        builder.setNegativeButton(getString(R.string.title_cancel_res), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        titleTxt = (TextView) view.findViewById(R.id.title);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        priceSpiner = (Spinner) view.findViewById(R.id.priceSpiner);
        weightSpiner = (Spinner) view.findViewById(R.id.weightSpiner);
        price = (EditText) view.findViewById(R.id.price);
        weight = (EditText) view.findViewById(R.id.weight);

        final AlertDialog d = builder.create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button posButton = d.getButton(DialogInterface.BUTTON_POSITIVE);
                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(validateForm()) {
                            ModelEditPrice modelEditPrice = new ModelEditPrice();
                            modelEditPrice.setProduct_id(orderProduct.getProductId().toString());
                            modelEditPrice.setProduct_price(price.getText().toString());
                            modelEditPrice.setProduct_price_currency(String.valueOf(priceSpiner.getSelectedItemPosition() + 1));
                            modelEditPrice.setProduct_weight_value(weight.getText().toString());
                            modelEditPrice.setProduct_weight(String.valueOf(weightSpiner.getSelectedItemPosition() + 1));
                            onChangePrice.OnChangePrice(modelEditPrice, position, checkBox.isChecked());
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        return d;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String title = getArguments().getString(TITLE);
        orderProduct = Parcels.unwrap(getArguments().getParcelable(ProductListAdapter.ORDER_PRODUCT));
        position = getArguments().getInt(ProductListAdapter.POSITION);
        isStockChange = getArguments().getBoolean(ProductListAdapter.STOCK_CHANGE_CONDITION, false);
        checkBox.setChecked(isStockChange);
        titleTxt.setText(MethodChecker.fromHtml("Nama produk: <b>"+title+"<b>"));
        price.setText(orderProduct.getProductNormalPrice());
        weight.setText(orderProduct.getProductWeight());

        initSpinner();

        switch (orderProduct.getProductPriceCurrency()){
            case "1":
                priceSpiner.setSelection(0);
                break;
            case "2":
                priceSpiner.setSelection(1);
                break;
        }
        switch (orderProduct.getProductWeightUnit()){
            case "1":
                weightSpiner.setSelection(0);
                break;
            case "2":
                weightSpiner.setSelection(1);
                break;
        }

        priceSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                price.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        weightSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weight.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinner() {
        String[] arrayWeight = getActivity().getResources().getStringArray(R.array.weight);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayWeight); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpiner.setAdapter(spinnerArrayAdapter);

        if(orderProduct.getShopIsGold() == 1){
            String[] arrayPrice = getActivity().getResources().getStringArray(R.array.priceList);
            ArrayAdapter<String> spinnerArrayAdapterPrice = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayPrice); //selected item will look like a spinner set from XML
            spinnerArrayAdapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            priceSpiner.setAdapter(spinnerArrayAdapterPrice);
        }else{
            String[] arrayPrice = getActivity().getResources().getStringArray(R.array.priceListNonGm);
            ArrayAdapter<String> spinnerArrayAdapterPrice = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayPrice); //selected item will look like a spinner set from XML
            spinnerArrayAdapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            priceSpiner.setAdapter(spinnerArrayAdapterPrice);
        }
    }

    private boolean validateForm() {
        Pair<Boolean, String> validateweight =  VerificationUtils.validateMinimumWeight(getActivity(), weightSpiner.getSelectedItem().toString(), weight.getText().toString());
        Pair<Boolean, String> validatePrice =  VerificationUtils.validatePrice(getActivity(), priceSpiner.getSelectedItem().toString(), price.getText().toString());

        if(!validatePrice.getModel1() || !validateweight.getModel1()){
            if(!validatePrice.getModel1()){
                price.setError(validatePrice.getModel2());
            }else{
                price.setError(null);
            }
            if(!validateweight.getModel1()){
                weight.setError(validateweight.getModel2());
            }else{
                weight.setError(null);
            }
            return false;
        }else{
            price.setError(null);
            weight.setError(null);
            return true;
        }
    }

    public interface OnChangePrice{
        void OnChangePrice(ModelEditPrice modelEditPrice, int position, boolean isStockChange);
    }
}
