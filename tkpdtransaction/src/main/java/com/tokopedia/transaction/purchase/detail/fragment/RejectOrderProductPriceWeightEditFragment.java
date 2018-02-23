package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.transaction.purchase.detail.fragment.RejectOrderWeightPriceFragment.FRAGMENT_EDIT_WEIGHT_PRICE_REQUEST_CODE;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class RejectOrderProductPriceWeightEditFragment extends TkpdFragment {

    private static final String EDITABLE_EXTRA = "EDITABLE_EXTRA";
    private static final int SPINNER_MODE_OFFSET = 1;

    public static RejectOrderProductPriceWeightEditFragment createFragment(
            WrongProductPriceWeightEditable editable
    ) {
        RejectOrderProductPriceWeightEditFragment fragment = new RejectOrderProductPriceWeightEditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EDITABLE_EXTRA, editable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        WrongProductPriceWeightEditable editable = getArguments()
                .getParcelable(EDITABLE_EXTRA);
        WrongProductPriceWeightEditable originalEditable = new WrongProductPriceWeightEditable(
                editable
        );
        View view = inflater.inflate(R.layout.order_reject_price_weight_edit_page, container, false);
        ViewGroup mainContainer = view.findViewById(R.id.main_container);
        TextView orderDetailProductName = view.findViewById(R.id.order_detail_product_name);
        TextView orderDetailProductPrice = view.findViewById(R.id.order_detail_product_price);
        ImageView imageView = view.findViewById(R.id.product_image);
        Spinner currencySpinner = view.findViewById(R.id.currency_spinner);
        EditText priceEditText = view.findViewById(R.id.price);
        Spinner weightSpinner = view.findViewById(R.id.weight_spinner);
        EditText weightEditText = view.findViewById(R.id.weight_amount);
        Button rejectOrderConfirmButton = view.findViewById(R.id.reject_order_confirm_button);
        ImageHandler.LoadImage(imageView, editable.getProductImage());
        orderDetailProductName.setText(editable.getProductName());
        orderDetailProductPrice.setText(editable.getProductPrice());
        priceEditText.setText(editable.getProductPriceUnformatted());
        weightEditText.setText(convertWeightToGram(
                editable.getProductWeightUnformatted(), editable.getWeightMode())
        );
        rejectOrderConfirmButton.setOnClickListener(onConfirmButtonClickedListener(
                editable,
                originalEditable,
                priceEditText, weightEditText,
                currencySpinner,
                weightSpinner
        ));
        mainContainer.setOnClickListener(null);
        currencySpinner.setAdapter(SimpleSpinnerAdapter.createAdapter(getActivity(), listOfCurrency()));
        weightSpinner.setAdapter(SimpleSpinnerAdapter.createAdapter(getActivity(), listOfWeight()));
        currencySpinner.setOnItemSelectedListener(onCurrencyChoosen(editable));
        weightSpinner.setOnItemSelectedListener(onWeightChoosen(editable));
        currencySpinner.setSelection(editable.getCurrencyMode() - SPINNER_MODE_OFFSET);
        weightSpinner.setSelection(0);
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }


    private View.OnClickListener onConfirmButtonClickedListener(
            final WrongProductPriceWeightEditable editable,
            final WrongProductPriceWeightEditable originalModel,
            final EditText priceEditText,
            final EditText weightEditText,
            final Spinner priceSpinner,
            final Spinner weightSpinner
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(priceSpinner.getSelectedItemPosition() == 0) {
                    editable.setProductPrice(
                            priceSpinner.getSelectedItem()
                                    + " "
                                    + CurrencyFormatHelper.ConvertToRupiah(
                                            priceEditText.getText().toString())
                                    .replace(",", ".")
                    );
                } else editable.setProductPrice(
                        priceSpinner.getSelectedItem()
                                + " "
                                + CurrencyFormatHelper.ConvertToDollar(
                                        priceEditText.getText().toString())
                );

                editable.setProductWeight(
                        weightSpinner.getSelectedItem()
                                + " "
                                + weightEditText.getText().toString());
                editable.setProductPriceUnformatted(priceEditText.getText().toString());
                editable.setProductWeightUnformatted(weightEditText.getText().toString());
                editable.setWeightMode(
                        priceSpinner.getSelectedItemPosition() + SPINNER_MODE_OFFSET
                );
                editable.setWeightMode(
                        weightSpinner.getSelectedItemPosition() + SPINNER_MODE_OFFSET
                );

                if(editable.equals(originalModel)) {
                    NetworkErrorHelper.showSnackbar(
                            getActivity(),
                            getString(R.string.error_no_change_price_weight)
                    );
                } else {
                    getTargetFragment().onActivityResult(
                            FRAGMENT_EDIT_WEIGHT_PRICE_REQUEST_CODE,
                            Activity.RESULT_OK, new Intent());
                }
            }
        };
    }

    private List<String> listOfCurrency() {
        List<String> currencyList = new ArrayList<>();
        currencyList.add("Rp");
        currencyList.add("$");
        return currencyList;
    }

    private List<String> listOfWeight() {
        List<String> weightList = new ArrayList<>();
        weightList.add("Gram(g)");
        weightList.add("Kg");
        return weightList;
    }

    private AdapterView.OnItemSelectedListener onCurrencyChoosen(
            final WrongProductPriceWeightEditable editable
    ) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                editable.setCurrencyMode(index + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener onWeightChoosen(
            final WrongProductPriceWeightEditable editable
    ) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                editable.setWeightMode(index + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private String convertWeightToGram(String productWeightUnformatted, int currencyMode) {
        if(currencyMode == 2) {
            double productWeightInGram = Double.parseDouble(productWeightUnformatted) * 1000;
            return String.valueOf(productWeightInGram);
        }
        return productWeightUnformatted;
    }

}
