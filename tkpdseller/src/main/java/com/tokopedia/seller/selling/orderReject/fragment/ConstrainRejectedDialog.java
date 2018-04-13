package com.tokopedia.seller.selling.orderReject.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.selling.orderReject.ConfirmRejectOrderActivity;
import com.tokopedia.seller.selling.orderReject.adapter.ProductListAdapter;

/**
 * Created by Erry on 6/6/2016.
 */
public class ConstrainRejectedDialog extends DialogFragment {

    TextView confirmBtn;
    EditText reasonTxt;
    TextView titleTxt;

    OnConfirmReject onConfirmReject;
    ProductListAdapter.Type type;

    public static ConstrainRejectedDialog newInstance(String reason, ProductListAdapter.Type type) {

        Bundle args = new Bundle();
        args.putString(ConfirmRejectOrderActivity.REASON, reason);
        args.putSerializable(ConfirmRejectOrderActivity.TYPE, type);
        ConstrainRejectedDialog fragment = new ConstrainRejectedDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnConfirmReject(OnConfirmReject onConfirmReject){
        this.onConfirmReject = onConfirmReject;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_reject_order_reason, container, false);
        confirmBtn = (TextView) view.findViewById(R.id.confirm_button);
        reasonTxt = (EditText) view.findViewById(R.id.reason);
        titleTxt = (TextView) view.findViewById(R.id.title);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String title = getArguments().getString(ConfirmRejectOrderActivity.REASON);
        type = (ProductListAdapter.Type) getArguments().getSerializable(ConfirmRejectOrderActivity.TYPE);
        titleTxt.setText(MethodChecker.fromHtml("Alasan Penolakan: <b>"+title+"<b>"));
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()) {
                    onConfirmReject.OnConfirmRejectOrder(reasonTxt.getText().toString(), type);
                    getDialog().dismiss();
                }
            }
        });
    }

    public boolean validateForm(){
        if(reasonTxt.getText().toString().isEmpty()){
            reasonTxt.setError(getString(R.string.desc_should_not_empty));
            return false;
        }else if (reasonTxt.getText().toString().length() > 490){
            reasonTxt.setError(getString(R.string.desc_should_less_490));
            return false;
        }else{
            reasonTxt.setError(null);
            return true;
        }
    }

    public interface OnConfirmReject{
        void OnConfirmRejectOrder(String reason, ProductListAdapter.Type type);
    }
}
