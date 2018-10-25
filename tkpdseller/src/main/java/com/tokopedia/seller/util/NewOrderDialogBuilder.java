package com.tokopedia.seller.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.selling.model.partialDetails;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core2.R;
import com.tokopedia.seller.selling.model.orderShipping.OrderProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 2/16/2015.
 */
public class NewOrderDialogBuilder {

    public interface OnAcceptListener{
        void onAccept();
    }

    public interface OnPartialListener{
        void onAcceptPartial(String remark, String param);
    }

    public interface OnRejectListener{
        void onReject(String remark, String qtyAccept, String listProd);
    }

    public interface OnSelectRejectReasonListener {
        void onSelected(String reason, int pos);
    }

    public static class NewOrderDialogModel{
        public String invoice;
        public String grandTotal;
        public String buyerName;
        public List<OrderProduct> productList;
    }

    public static void createAcceptDialog(Context context, final OnAcceptListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setMessage(context.getString(R.string.msg_confirm_order))
                .setCancelable(true)
                .setPositiveButton(context.getString(R.string.title_yes),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        listener.onAccept();
                    }
                })
                .setNegativeButton(context.getString(R.string.title_no),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public static void createPartialDialog(final Context context,final NewOrderDialogModel model, final OnPartialListener listener){
        final Dialog dialog = new Dialog(context);
        final int total = model.productList.size();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_partial_order);
        TextView Invoice = (TextView) dialog.findViewById(R.id.invoice_text);
        TextView BuyerName = (TextView) dialog.findViewById(R.id.buyer_name);
        TextView TotalInvoice = (TextView) dialog.findViewById(R.id.total_invoice);
        TextView ConfirmButton = (TextView) dialog.findViewById(R.id.confirm_button);
        final EditText Remark = (EditText) dialog.findViewById(R.id.remark);
        Invoice.setText(context.getString(R.string.title_invoice_number) + model.invoice);
        TotalInvoice.setText(context.getString(R.string.title_grand_total) + " : " + model.grandTotal);
        BuyerName.setText(model.buyerName);
        LinearLayout ProductList = (LinearLayout) dialog.findViewById(R.id.product_list);

        // TODO ---------------------------------------------------------------------------

        final EditText[] ProdQty = new EditText[total];
        final LinearLayout[] ProductView = new LinearLayout[total];
        LinearLayout.LayoutParams viewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams prodParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        prodParam.gravity = Gravity.CENTER_VERTICAL;
        viewParam.gravity = Gravity.CENTER_VERTICAL;
        TextView[] ProdNameList = new TextView[total];
        for(int i=0; i<model.productList.size(); i++){
            OrderProduct product = (model.productList.get(i));
            ProdNameList[i] = new TextView(context);
            ProductView[i] = new LinearLayout(context);
            ProductView[i].setOrientation(LinearLayout.HORIZONTAL);
            ProdNameList[i].setLayoutParams(prodParam);
            ProductView[i].setLayoutParams(viewParam);
            if(i != total - 1) {
                ProdNameList[i].setText(MethodChecker.fromHtml(product.getProductName() + " </br> "));
            } else {
                ProdNameList[i].setText(MethodChecker.fromHtml(product.getProductName()));
            }
            ProdNameList[i].setGravity(Gravity.CENTER_VERTICAL);
            ProdQty[i] = new EditText(context);
            ProdQty[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            ProdQty[i].setText(product.getOrderDeliverQuantity().toString());
            ProductView[i].addView(ProdQty[i]);
            ProductView[i].addView(ProdNameList[i]);
            ProductList.addView(ProductView[i]);
        }

        ConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Remark.setError(null);
                String data = "";
                ArrayList<partialDetails> newDialogPartialDetails = new ArrayList<partialDetails>();
                for(int i=0; i<total; i++) {
                    partialDetails partialdetails = new partialDetails();
                    partialdetails.setItemDesc(model.productList.get(i).getOrderDetailId().toString());
                    partialdetails.setItemQty(ProdQty[i].getText().toString());
                    newDialogPartialDetails.add(partialdetails);
                }
                data = partialDetailGenerator(newDialogPartialDetails);
                if(Remark.length() > 0){
                    listener.onAcceptPartial(Remark.getText().toString(), data);
                    dialog.dismiss();
                }
                else
                    Remark.setError(context.getString(R.string.error_field_required));
            }
        });
        dialog.show();
    }

    public static Dialog createRejectDialog(final Context context, final OnSelectRejectReasonListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.dialog_tolak_title));
        final String[] reason = context.getResources().getStringArray(R.array.alasan_penolakan);
        builder.setItems(reason, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onSelected(reason[which], which);
            }
        });
        Dialog dialog = builder.create();
        return dialog;
    }

    public static void createRejectDialog(final Context context,final NewOrderDialogModel model, final OnRejectListener listener){
        final Dialog dialog = new Dialog(context);
        final int total = model.productList.size();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reject_order);
        TextView Invoice = (TextView) dialog.findViewById(R.id.invoice_text);
        TextView BuyerName = (TextView) dialog.findViewById(R.id.buyer_name);
        TextView TotalInvoice = (TextView) dialog.findViewById(R.id.total_invoice);
        Invoice.setText(context.getString(R.string.title_invoice_number) + " " + model.invoice);
        TotalInvoice.setText(context.getString(R.string.title_grand_total) + " : " + model.grandTotal);
        BuyerName.setText(context.getString(R.string.title_name) + " : " + model.buyerName);
        final RadioButton OutOfStock = (RadioButton) dialog.findViewById(R.id.rb_no_stock);
        final RadioButton CannotDeliver = (RadioButton) dialog.findViewById(R.id.rb_cannot);
        final RadioButton Other = (RadioButton) dialog.findViewById(R.id.rb_other);
        final LinearLayout ProductList = (LinearLayout) dialog.findViewById(R.id.product_list);
        final EditText OtherMessage = (EditText) dialog.findViewById(R.id.other_reason);
        TextView ConfirmButton = (TextView) dialog.findViewById(R.id.confirm_button);

        // TODO -----------------------------------------------------------------
        final CheckBox[] prods = new CheckBox[total];
        try {
            for(int i=0;i<total;i++){
                OrderProduct product = model.productList.get(i);
                prods[i] = new CheckBox(context);
                prods[i].setText(MethodChecker.fromHtml(product.getProductName()));
                ProductList.addView(prods[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Other.setChecked(true);

        OutOfStock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CannotDeliver.setChecked(false);
                Other.setChecked(false);
                OutOfStock.setChecked(true);
                OtherMessage.setVisibility(View.GONE);
                ProductList.setVisibility(View.VISIBLE);
            }
        });

        CannotDeliver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                OutOfStock.setChecked(false);
                Other.setChecked(false);
                CannotDeliver.setChecked(true);
                OtherMessage.setVisibility(View.GONE);
                ProductList.setVisibility(View.GONE);
            }
        });

        Other.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                OutOfStock.setChecked(false);
                CannotDeliver.setChecked(false);
                Other.setChecked(true);
                OtherMessage.setVisibility(View.VISIBLE);
                ProductList.setVisibility(View.GONE);
            }
        });

        ConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    OtherMessage.setError(null);
                    if (OutOfStock.isChecked()) {
                        String data = "";
                        for (int i = 0; i < total; i++) {
                            OrderProduct product = model.productList.get(i);
                            data = data + product.getProductId() + "~" + product.getOrderDeliverQuantity();
                            if(i<total-1)
                                data = data + "_~_";
                        }
                        String data_prd = "";
                        for(int i=0; i<prods.length; i++){
                            OrderProduct product = model.productList.get(i);
                            if(prods[i].isChecked())
                                if(data_prd.length() == 0)
                                    data_prd = product.getProductId().toString();
                                else
                                    data_prd = "~" + product.getProductId();
                        }
                        CommonUtils.dumper("rejected goods "+data_prd);
                        listener.onReject("Stok barang habis", data, data_prd);
                        dialog.dismiss();
                    } else if (CannotDeliver.isChecked()) {
                        listener.onReject("Barang tidak dapat dikirim", "", "");
                        dialog.dismiss();
                    } else if (Other.isChecked()) {
                        if (OtherMessage.length() == 0)
                            OtherMessage.setError(context.getString(R.string.error_field_required));
                        else {
                            listener.onReject(OtherMessage.getText().toString(), "", "");
                            dialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    public static void createSpecialAcceptDialog(Context context, View view, final OnAcceptListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(context.getString(R.string.title_yes),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        listener.onAccept();
                    }
                })
                .setNegativeButton(context.getString(R.string.title_no),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private static String partialDetailGenerator(ArrayList<partialDetails> partialdetails){
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

}
