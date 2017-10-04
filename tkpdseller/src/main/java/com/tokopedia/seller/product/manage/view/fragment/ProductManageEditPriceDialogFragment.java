package com.tokopedia.seller.product.manage.view.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.tokopedia.design.text.SpinnerCounterInputView;
import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 10/3/17.
 */

public class ProductManageEditPriceDialogFragment extends DialogFragment {

    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_PRICE = "product_price";
    private static final String PRODUCT_CURRENCY_ID = "product_currency_id";

    public interface ListenerDialogEditPrice{
        void onSubmitEditPrice(String productId, String price, String priceCurrency);
    }

    private String productId;
    private String productPrice;
    private String productCurrencyId;
    private ListenerDialogEditPrice listenerDialogEditPrice;

    public static ProductManageEditPriceDialogFragment createInstance(final String productId, String productPrice, String productCurrencyId){
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID, productId);
        args.putString(PRODUCT_PRICE, productPrice);
        args.putString(PRODUCT_CURRENCY_ID, productCurrencyId);
        ProductManageEditPriceDialogFragment fragment = new ProductManageEditPriceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListenerDialogEditPrice(ListenerDialogEditPrice listenerDialogEditPrice) {
        this.listenerDialogEditPrice = listenerDialogEditPrice;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getArguments().getString(PRODUCT_ID);
        productPrice = getArguments().getString(PRODUCT_PRICE);
        productCurrencyId = getArguments().getString(PRODUCT_CURRENCY_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater li = getActivity().getLayoutInflater();
        View promptsView = li.inflate(R.layout.layout_prompt_change_price_product, null);
        final SpinnerCounterInputView priceInputView = (SpinnerCounterInputView) promptsView.findViewById(R.id.spinner_counter_input_view_price);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(promptsView);
        alertDialog.setTitle(R.string.title_edit_price);
        alertDialog.setPositiveButton(R.string.title_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listenerDialogEditPrice.onSubmitEditPrice(productId, String.valueOf(priceInputView.getCounterValue()), priceInputView.getSpinnerValue());
            }
        });
        alertDialog.setNegativeButton(R.string.title_cancel, null);

        AlertDialog dialog = alertDialog.create();
        return dialog;
    }
}
