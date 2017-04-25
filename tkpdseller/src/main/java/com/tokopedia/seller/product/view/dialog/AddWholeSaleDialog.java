package com.tokopedia.seller.product.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.wholesale.WholesaleModel;
import com.tokopedia.seller.product.view.widget.CounterInputView;

/**
 * @author normansyahputa on 4/20/17.
 */
public class AddWholeSaleDialog extends DialogFragment {

    public static final String TAG = "AddWholeSaleDialog";
    public static final String KEY_WHOLE_SALE_BASE_VALUE = "KEY_WHOLE_SALE_BASE_VALUE";
    private WholeSaleDialogListener listener;
    private CounterInputView maxWholeSale;
    private CounterInputView minWholeSale;
    private CounterInputView wholesalePrice;
    private WholesaleModel baseValue;

    public static AddWholeSaleDialog newInstance(WholesaleModel wholesaleModel) {
        AddWholeSaleDialog addWholeSaleDialog = new AddWholeSaleDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_WHOLE_SALE_BASE_VALUE, wholesaleModel);
        addWholeSaleDialog.setArguments(bundle);
        return addWholeSaleDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_whole_sale_dialog_layout, container, false);
        minWholeSale = (CounterInputView) view.findViewById(R.id.counter_input_view_minimum_whole_sale);
        maxWholeSale = (CounterInputView) view.findViewById(R.id.counter_input_view_maximum_whole_sale);
        wholesalePrice = (CounterInputView) view.findViewById(R.id.counter_input_view_wholesale_price);
        view.findViewById(R.id.string_picker_dialog_confirm)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addItem(getItem());
                    }
                });
        view.findViewById(R.id.string_picker_dialog_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

        if (savedInstanceState == null && getArguments() != null) {
            baseValue = getArguments().getParcelable(KEY_WHOLE_SALE_BASE_VALUE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WholeSaleDialogListener) {
            listener = (WholeSaleDialogListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + TAG);
        }
    }

    protected void addItem(WholesaleModel object) {
        listener.addWholesaleItem(object);
        dismiss();
    }

    protected WholesaleModel getItem() {
        WholesaleModel wholesaleModel = new WholesaleModel(
                Integer.parseInt(minWholeSale.getEditText().getText().toString()),
                Integer.parseInt(maxWholeSale.getEditText().getText().toString()),
                Double.parseDouble(wholesalePrice.getEditText().getText().toString())
        );
        return wholesaleModel;
    }

    public interface WholeSaleDialogListener {
        void addWholesaleItem(WholesaleModel item);
    }
}
