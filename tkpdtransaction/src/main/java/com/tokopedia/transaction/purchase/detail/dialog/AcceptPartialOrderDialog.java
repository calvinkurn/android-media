package com.tokopedia.transaction.purchase.detail.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.detail.dialog.PartialDialogModels;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailItemData;

import java.util.ArrayList;

/**
 * Created by kris on 1/2/18. Tokopedia
 */

public class AcceptPartialOrderDialog extends DialogFragment{
    //TODO KALO ADA WAKTU, RAPIHIN!!! KODINGAN LAMA SUCKS


    private static final String ORDER_DATA_EXTRAS = "ORDER_DATA_EXTRAS";
    private PartialDialogListener listener;


    public static AcceptPartialOrderDialog createDialog(OrderDetailData data) {
        AcceptPartialOrderDialog dialog = new AcceptPartialOrderDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDER_DATA_EXTRAS, data);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PartialDialogListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (PartialDialogListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final OrderDetailData data = getArguments().getParcelable(ORDER_DATA_EXTRAS);
        final int numberOfProducts = data.getItemList().size();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_partial_order, container, false);
        final TextView Invoice = view.findViewById(R.id.invoice_text);
        final TextView BuyerName = view.findViewById(R.id.buyer_name);
        final TextView TotalInvoice = view.findViewById(R.id.total_invoice);
        final TextView ConfirmButton = view.findViewById(R.id.confirm_button);
        final EditText Remark = view.findViewById(R.id.remark);
        Invoice.setText(getActivity().getString(R.string.title_invoice_number) + data.getInvoiceNumber());
        TotalInvoice.setText(getActivity().getString(R.string.title_grand_total) + " : " + data.getTotalPayment());
        BuyerName.setText(data.getBuyerName());
        LinearLayout ProductList = view.findViewById(R.id.product_list);
        final EditText[] ProdQty = new EditText[numberOfProducts];
        final LinearLayout[] ProductView = new LinearLayout[numberOfProducts];
        LinearLayout.LayoutParams viewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams prodParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        prodParam.gravity = Gravity.CENTER_VERTICAL;
        viewParam.gravity = Gravity.CENTER_VERTICAL;
        TextView[] ProdNameList = new TextView[numberOfProducts];
        for(int i=0; i<numberOfProducts; i++){
            OrderDetailItemData product = (data.getItemList().get(i));
            ProdNameList[i] = new TextView(getActivity());
            ProductView[i] = new LinearLayout(getActivity());
            ProductView[i].setOrientation(LinearLayout.HORIZONTAL);
            ProdNameList[i].setLayoutParams(prodParam);
            ProductView[i].setLayoutParams(viewParam);
            if(i != numberOfProducts - 1) {
                ProdNameList[i].setText(MethodChecker.fromHtml(product.getItemName() + " </br> "));
            } else {
                ProdNameList[i].setText(MethodChecker.fromHtml(product.getItemName()));
            }
            ProdNameList[i].setGravity(Gravity.CENTER_VERTICAL);
            ProdQty[i] = new EditText(getActivity());
            ProdQty[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            ProdQty[i].setText(product.getItemQuantity());
            ProductView[i].addView(ProdQty[i]);
            ProductView[i].addView(ProdNameList[i]);
            ProductList.addView(ProductView[i]);
        }

        ConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Remark.setError(null);
                String generatedPartialOrder = "";
                ArrayList<PartialDialogModels> newDialogPartialDetails = new ArrayList<PartialDialogModels>();
                for(int i=0; i<numberOfProducts; i++) {
                    PartialDialogModels partialdetails = new PartialDialogModels();
                    partialdetails.setItemDesc(data.getItemList().get(i).getOrderDetailId());
                    partialdetails.setItemQty(ProdQty[i].getText().toString());
                    newDialogPartialDetails.add(partialdetails);
                }
                generatedPartialOrder = partialDetailGenerator(newDialogPartialDetails);
                if(Remark.length() > 0){
                    listener.onAcceptPartialOrderCreated(data.getOrderId(), Remark.getText().toString(), generatedPartialOrder);
                    dismiss();
                }
                else
                    Remark.setError(getActivity().getString(R.string.error_field_required));
            }
        });
        return view;
    }

    private String partialDetailGenerator(ArrayList<PartialDialogModels> partialdetails){
        String variable = "%s", nextItem = "*~*", acceptCount="~";
        String join = variable+acceptCount+variable;
        String join2 = nextItem+join;
        String result = "";
        for(int i=0;i<partialdetails.size();i++){
            String formatter = null;
            if(i == 0){
                formatter = join;
            }else{
                formatter = join2;
            }

            result += String.format(formatter, partialdetails.get(i).getItemDesc(), partialdetails.get(i).getItemQty());
        }
        return result;
    }

    public interface PartialDialogListener{
        void onAcceptPartialOrderCreated(String orderId, String remark, String param);
    }

}
