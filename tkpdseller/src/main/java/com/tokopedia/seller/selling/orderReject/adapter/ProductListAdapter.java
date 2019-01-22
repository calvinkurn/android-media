package com.tokopedia.seller.selling.orderReject.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.selling.model.orderShipping.OrderProduct;
import com.tokopedia.seller.selling.orderReject.fragment.EditPriceDialog;
import com.tokopedia.seller.selling.orderReject.fragment.EditVarianDialog;
import com.tokopedia.seller.selling.orderReject.model.ModelEditDescription;
import com.tokopedia.seller.selling.orderReject.model.ModelEditPrice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by Erry on 6/3/2016.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    public enum Type {stock, varian, price, courrier, buyer}

    public static final String ORDER_PRODUCT = "order_product";
    public static final String POSITION = "position";
    public static final String STOCK_CHANGE_CONDITION = "stock_change_condition";

    private Type type;
    private Activity activity;
    private OnItemClickListener itemClickListener;
    private List<OrderProduct> orderProducts = new ArrayList<>();
    private HashMap<Integer, ModelEditPrice> mapEditPrices = new HashMap<>();
    private HashMap<Integer, ModelEditDescription> mapEditDescription = new HashMap<>();
    private HashMap<Integer, Boolean> mapProductIdEmpty = new HashMap<>();

    public ProductListAdapter(Activity activity, Type type, List<OrderProduct> orderProducts) {
        this.activity = activity;
        this.type = type;
        this.orderProducts = orderProducts;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public List<ModelEditDescription> getModelEditDescriptions(){
        List<ModelEditDescription> modelEditDescriptions = new ArrayList<>();
        Set set = mapEditDescription.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            modelEditDescriptions.add((ModelEditDescription) me.getValue());
        }
        return modelEditDescriptions;
    }

    public List<ModelEditPrice> getModelEditPrice(){
        List<ModelEditPrice> modelEditPrices = new ArrayList<>();
        Set set = mapEditPrices.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            modelEditPrices.add((ModelEditPrice) me.getValue());
        }
        return modelEditPrices;
    }

    public String getStockEmptyList(){
        List<Integer> stockEmptys= new ArrayList<>();
        String productListEmpty = "";
        Set set = mapProductIdEmpty.entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            Boolean isStockEmpty = (Boolean) me.getValue();
            if(isStockEmpty){
                stockEmptys.add((Integer) me.getKey());
            }
        }

        for(int pos = 0; pos<stockEmptys.size(); pos++){
            productListEmpty = productListEmpty + orderProducts.get(stockEmptys.get(pos)).getProductId();
            if(pos != stockEmptys.size()-1){
                productListEmpty = productListEmpty + "~";
            }
        }
        return productListEmpty;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reject_order_list_item, parent, false);
        return new ViewHolder(activity, view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderProduct orderProduct = orderProducts.get(position);
        switch (type) {
            case price:
                holder.enableEditPrice();
                break;
            case varian:
                holder.enableEditDecription();
                break;
            case stock:
                holder.enableEditStock();
                break;
        }
        holder.titleTxt.setText(MethodChecker.fromHtml(orderProduct.getProductName()));
        holder.priceTxt.setText(orderProduct.getProductPrice());
        String weight = "Kilogram (kg)";
        if(orderProduct.getProductWeightUnit()!= null && !orderProduct.getProductWeightUnit().equals("")){
            String[] arrayWeight = activity.getResources().getStringArray(R.array.weight);
            if(orderProduct.getProductWeightUnit().equals("1")){
                weight = arrayWeight[0];
            }else{
                weight = arrayWeight[1];
            }
        }
        holder.weightTxt.setText(orderProduct.getProductWeight() + " " + weight);
        if(CommonUtils.checkNullForZeroJson(orderProduct.getProductDescription())) {
            holder.descTxt.setText(MethodChecker.fromHtml(orderProduct.getProductDescription()));
        }else{
            holder.descTxt.setText(activity.getString(R.string.message_no_desc));
        }
        ImageHandler.LoadImage(holder.imageView, orderProduct.getProductPicture());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mapProductIdEmpty.put(position, isChecked);
                if(isChecked){
                    holder.labelEmptyStock.setVisibility(View.VISIBLE);
                }else{
                    holder.labelEmptyStock.setVisibility(View.GONE);
                }
            }
        });

        holder.containerPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type!=Type.stock && type != Type.varian) {
                    editPriceWeight(position, orderProduct, holder);
                }
            }
        });

        holder.editPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type!=Type.stock && type != Type.varian) {
                    editPriceWeight(position, orderProduct, holder);
                }
            }
        });

        holder.editDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDescription(position, orderProduct, holder);
            }
        });

        holder.containerDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDescription(position, orderProduct, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
        TextView titleTxt;
        TextView priceTxt;
        TextView weightTxt;
        TextView labelEmptyStock;
        ImageButton editDesc;
        ImageButton editPrice;
        TextView descTxt;
        LinearLayout containerDesc;
        LinearLayout containerPrice;
        Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            imageView = (ImageView) itemView.findViewById(R.id.image);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            titleTxt = (TextView)  itemView.findViewById(R.id.title);
            priceTxt = (TextView) itemView.findViewById(R.id.price);
            weightTxt = (TextView) itemView.findViewById(R.id.weight);
            labelEmptyStock = (TextView) itemView.findViewById(R.id.label_stock_empty);
            editDesc = (ImageButton) itemView.findViewById(R.id.edit_description);
            editPrice = (ImageButton) itemView.findViewById(R.id.edit_price);
            descTxt = (TextView) itemView.findViewById(R.id.description);
            containerDesc = (LinearLayout) itemView.findViewById(R.id.container_description);
            containerPrice = (LinearLayout) itemView.findViewById(R.id.container_price);
        }

        public void enableEditDecription() {
            containerDesc.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.GONE);
            containerPrice.setPadding(0, 0, 0, 0);
            containerPrice.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            editPrice.setVisibility(View.GONE);
        }

        public void enableEditStock() {
            containerPrice.setPadding(0, 0, 0, 0);
            containerPrice.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            editPrice.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);
            containerDesc.setVisibility(View.GONE);
        }

        public void enableEditPrice() {
            containerDesc.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            containerPrice.setPadding(10, 0, 0, 0);
            containerPrice.setBackgroundResource(R.drawable.border_button_rounded);
        }
    }

    public interface OnItemClickListener {
        void onEditPriceClicked();
        void onEditDescClicked();
    }



    private void editDescription(int position, OrderProduct orderProduct, final ViewHolder holder) {
        boolean isStockEmpty = false;
        if(mapEditDescription.get(position) != null){
            ModelEditDescription modelEditDescription = mapEditDescription.get(position);
            orderProduct.setProductDescription(modelEditDescription.getProduct_description());
        }
        if(mapProductIdEmpty.get(position) != null){
            isStockEmpty = mapProductIdEmpty.get(position);
        }
        EditVarianDialog varianDialog = EditVarianDialog.newInstance(orderProduct, position, isStockEmpty);
        varianDialog.setListenerOnChangeDesc(new EditVarianDialog.ListenerOnChangeDesc() {
            @Override
            public void onChangeDesc(ModelEditDescription modelEditDescription, int position, boolean isStockChange) {
                mapEditDescription.put(position, modelEditDescription);
                holder.descTxt.setText(modelEditDescription.getProduct_description());
                mapProductIdEmpty.put(position, isStockChange);
                if(isStockChange){
                    holder.labelEmptyStock.setVisibility(View.VISIBLE);
                }else{
                    holder.labelEmptyStock.setVisibility(View.GONE);
                }
            }
        });
        varianDialog.show(activity.getFragmentManager(), "varianDialog");
    }

    private void editPriceWeight(int position, OrderProduct orderProduct, final ViewHolder holder) {
        boolean isStockEmpty = false;
        if(mapEditPrices.get(position) != null){
            ModelEditPrice modelEditPrice = mapEditPrices.get(position);
            orderProduct.setProductNormalPrice(modelEditPrice.getProduct_price());
            orderProduct.setProductPriceCurrency(modelEditPrice.getProduct_price_currency());
            orderProduct.setProductWeight(modelEditPrice.getProduct_weight_value());
            orderProduct.setProductWeightUnit(modelEditPrice.getProduct_weight());
        }
        if(mapProductIdEmpty.get(position) != null){
            isStockEmpty = mapProductIdEmpty.get(position);
        }
        EditPriceDialog priceDialog = EditPriceDialog.newInstance(orderProduct, position, isStockEmpty);
        priceDialog.setListenerChangePrice(new EditPriceDialog.OnChangePrice() {
            @Override
            public void OnChangePrice(ModelEditPrice modelEditPrice, int position, boolean isStockChange) {
                mapEditPrices.put(position, modelEditPrice);
                String price = "";
                String[] arrayprice = activity.getResources().getStringArray(R.array.priceList);
                switch (modelEditPrice.getProduct_price_currency()){
                    case "1":
                        price = arrayprice[0];
                        break;
                    case "2":
                        price = arrayprice[1];
                        break;
                }
                String weight = "";
                String[] arrayweight = activity.getResources().getStringArray(R.array.weight);
                switch (modelEditPrice.getProduct_weight()){
                    case "1":
                        weight = arrayweight[0];
                        break;
                    case "2":
                        weight = arrayweight[1];
                        break;
                }
                holder.priceTxt.setText(price + " " + modelEditPrice.getProduct_price());
                holder.weightTxt.setText(modelEditPrice.getProduct_weight_value() + " " + weight);
                mapProductIdEmpty.put(position, isStockChange);
                if(isStockChange){
                    holder.labelEmptyStock.setVisibility(View.VISIBLE);
                }else{
                    holder.labelEmptyStock.setVisibility(View.GONE);
                }
            }
        });
        priceDialog.show(activity.getFragmentManager(), "priceDialog");
    }
}
