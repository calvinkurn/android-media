package com.tokopedia.seller.selling.orderReject.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core2.R;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.selling.model.orderShipping.OrderProduct;
import com.tokopedia.seller.selling.orderReject.adapter.ProductListAdapter;
import com.tokopedia.seller.selling.orderReject.model.ModelEditDescription;

import org.parceler.Parcels;


/**
 * Created by Erry on 6/6/2016.
 */
public class EditVarianDialog extends DialogFragment {

    EditText descTxt;
    TextView titleTxt;
    CheckBox checkBox;

    public static final String TITLE = "title";

    OrderProduct orderProduct;
    int position;
    ListenerOnChangeDesc listenerOnChangeDesc;
    private boolean isStockChange;

    public static EditVarianDialog newInstance(OrderProduct orderProduct, int position, boolean isStockChange) {

        Bundle args = new Bundle();
        args.putString(TITLE, orderProduct.getProductName());
        args.putParcelable(ProductListAdapter.ORDER_PRODUCT, Parcels.wrap(orderProduct));
        args.putInt(ProductListAdapter.POSITION, position);
        args.putBoolean(ProductListAdapter.STOCK_CHANGE_CONDITION, isStockChange);
        EditVarianDialog fragment = new EditVarianDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListenerOnChangeDesc(ListenerOnChangeDesc listenerOnChangeDesc){
        this.listenerOnChangeDesc = listenerOnChangeDesc;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reject_order_edit_varian, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.title_edit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (validateForm()){
                    ModelEditDescription modelEditDescription = new ModelEditDescription();
                    modelEditDescription.setProduct_id(orderProduct.getProductId().toString());
                    modelEditDescription.setProduct_description(descTxt.getText().toString());
                    listenerOnChangeDesc.onChangeDesc(modelEditDescription, position, checkBox.isChecked());
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.title_cancel_res), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        descTxt = (EditText) view.findViewById(R.id.description);
        titleTxt = (TextView) view.findViewById(R.id.title);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        Dialog dialog = builder.create();
        return dialog;
    }

    private boolean validateForm() {
        if (descTxt.getText().toString().length() > 2000){
            descTxt.setError(getString(R.string.desc_should_less_2000));
            return false;
        }else{
            descTxt.setError(null);
            return true;
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String title = getArguments().getString(TITLE);
        orderProduct = Parcels.unwrap(getArguments().getParcelable(ProductListAdapter.ORDER_PRODUCT));
        position = getArguments().getInt(ProductListAdapter.POSITION);
        isStockChange = getArguments().getBoolean(ProductListAdapter.STOCK_CHANGE_CONDITION, false);
        titleTxt.setText(MethodChecker.fromHtml("Nama produk: <b>"+title+"<b>"));
        if(CommonUtils.checkNullForZeroJson(orderProduct.getProductDescription())) {
            descTxt.setText(removeHtmlTag(orderProduct.getProductDescription()));
        }
        checkBox.setChecked(isStockChange);
    }

    private String removeHtmlTag(String s){
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(s);
        replace(stringBuilder, '\n', "<br/>");
        String parseString = MethodChecker.fromHtml(stringBuilder.toString()).toString();
        return parseString;
    }

    private static void replace(SpannableStringBuilder b, char oldChar, String newStr) {
        for (int i = b.length() - 1; i >= 0; i--) {
            if (b.charAt(i) == oldChar) {
                b.replace(i, i + 1, newStr);
            }
        }
    }

    public interface ListenerOnChangeDesc{
        void onChangeDesc(ModelEditDescription modelEditDescription, int position, boolean isStockChange);
    }
}
