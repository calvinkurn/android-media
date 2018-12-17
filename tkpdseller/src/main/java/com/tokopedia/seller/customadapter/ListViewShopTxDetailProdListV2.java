package com.tokopedia.seller.customadapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.selling.model.orderShipping.OrderProduct;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 2/13/2015.
 */
public class ListViewShopTxDetailProdListV2 extends BaseAdapter{


    public static ListViewShopTxDetailProdListV2 createInstance(Context context, List<OrderProduct> productList){
        ListViewShopTxDetailProdListV2 adapter = new ListViewShopTxDetailProdListV2();
        adapter.modelList = new ArrayList<>();
        adapter.modelList.addAll(getModelList(productList));
        adapter.context = context;
        return adapter;
    }

    @Deprecated
    public static ListViewShopTxDetailProdListV2 createInstance(Context context, JSONArray productList){
        ListViewShopTxDetailProdListV2 adapter = new ListViewShopTxDetailProdListV2();
        adapter.modelList = new ArrayList<>();
        adapter.modelList.addAll(getModelList(productList));
        adapter.context = context;
        return adapter;
    }

    public static ListViewShopTxDetailProdListV2 createInstanceV4(Context context, List<OrderProduct> productList){
        ListViewShopTxDetailProdListV2 adapter = new ListViewShopTxDetailProdListV2();
        adapter.modelList = new ArrayList<>();
        adapter.modelList.addAll(getModelListV4(productList));
        adapter.context = context;
        return adapter;
    }

    private static List<TxProdModel> getModelList(List<OrderProduct> productList){
        List<TxProdModel> list = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            OrderProduct orderProduct = productList.get(i);
            TxProdModel model = new TxProdModel();
            model.ProductId = orderProduct.getOrderDetailId()+"";
            model.ProductUrl = orderProduct.getProductUrl();
            model.ProductIdList = orderProduct.getProductId()+"";
            model.ImageUrl = orderProduct.getProductPicture();
            model.Name = MethodChecker.fromHtml(orderProduct.getProductName()).toString();
            model.Price = orderProduct.getProductPrice();
            model.TotalOrder = orderProduct.getProductQuantity()+"";
            model.TotalPrice = orderProduct.getOrderSubtotalPriceIdr();
            model.Message = MethodChecker.fromHtml(orderProduct.getProductNotes()).toString();
            list.add(model);
        }
        return list;
    }

    @Deprecated
    private static List<TxProdModel> getModelList(JSONArray productList){
        List<TxProdModel> list = new ArrayList<>();
        try {
            for (int i = 0; i < productList.length(); i++) {
                JSONObject product = new JSONObject(productList.getString(i));
                TxProdModel model = new TxProdModel();
                model.ProductId = product.getString("order_dtl_id");
                model.ProductUrl = product.getString("product_url");
                model.ProductIdList = product.getString("product_id");
                model.ImageUrl = product.getString("product_pic");
                model.Name = MethodChecker.fromHtml(product.getString("product_name")).toString();
                model.Price = product.getString("product_price");
                model.TotalOrder = product.getString("deliver_qty");
                model.TotalPrice = product.getString("subtotal_price_idr");
                model.Message = MethodChecker.fromHtml(product.getString("notes")).toString();
                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<TxProdModel> getModelListV4(List<OrderProduct> productList){
        List<TxProdModel> list = new ArrayList<>();
        try {
            for (int i = 0; i < productList.size(); i++) {
                OrderProduct product = productList.get(i);
                TxProdModel model = new TxProdModel();
                model.ProductId = product.getOrderDetailId().toString();
                model.ProductUrl = product.getProductUrl();
                model.ProductIdList = product.getProductId().toString();
                model.ImageUrl = product.getProductPicture();
                model.Name = MethodChecker.fromHtml(product.getProductName()).toString();
                model.Price = product.getProductPrice();
                model.TotalOrder = product.getProductQuantity().toString();
                model.TotalPrice = product.getOrderSubtotalPriceIdr();
                model.Message = CommonUtils.checkNullForZeroJson(product.getProductNotes()) ? MethodChecker.fromHtml(product.getProductNotes()).toString() : "";
                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<TxProdModel> modelList;
    private ViewHolder holder;
    private Context context;

    public static class TxProdModel{
        public String ImageUrl;
        public String Name;
        public String Price;
        public String TotalOrder;
        public String TotalPrice;
        public String Message;
        public String ProductId;
        public String ProductUrl;
        public String ProductIdList;
    }

    private class ViewHolder{
        ImageView ProductImage;
        TextView ProductName;
        TextView ProductPrice;
        TextView TotalOrder;
        TextView TotalPrice;
        TextView Message;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.listview_shop_order_detail_product, null);
            initCreateView(convertView);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        setViewData(position);
//        setListener(position, convertView);
        return convertView;
    }

    private void initCreateView(View convertView){
        holder = new ViewHolder();
        initView(convertView);
        convertView.setTag(holder);
    }

//    private void setListener(int pos, View convertView){
////        holder.ProductName.setOnClickListener(onOpenProduct(pos));
////        holder.ProductImage.setOnClickListener(onOpenProduct(pos));
//        convertView.setOnClickListener(onOpenProduct(pos));
//    }

    /*private View.OnClickListener onOpenProduct(final int pos){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(ProductInfoActivity.createInstance(context, modelList.get(pos).ProductIdList));
            }
        };
    }
*/
    private void setViewData(int pos){
        TxProdModel model = modelList.get(pos);
        holder.ProductName.setText(model.Name);
        holder.ProductPrice.setText(model.Price);
        holder.TotalOrder.setText(" x " + model.TotalOrder + " Barang");
        holder.TotalPrice.setText(model.TotalPrice);
        ImageHandler.loadImageRounded2(holder.TotalPrice.getContext(), holder.ProductImage, model.ImageUrl);
//        ImageHandler.LoadImageRounded(holder.ProductImage, model.ImageUrl);
        //holder.Message.setMovementMethod(new ScrollingMovementMethod());
        if(CommonUtils.checkNullForZeroJson(model.Message)){
            holder.Message.setText(model.Message);
        }else {
            holder.Message.setText("-");
        }
    }

    private void initView(View convertView){
        holder.ProductImage = (ImageView) convertView.findViewById(R.id.product_image);
        holder.ProductName = (TextView) convertView.findViewById(R.id.product_name);
        holder.ProductPrice = (TextView) convertView.findViewById(R.id.product_price);
        holder.TotalOrder = (TextView) convertView.findViewById(R.id.total_order);
        holder.TotalPrice = (TextView) convertView.findViewById(R.id.total_price);
        holder.Message = (TextView) convertView.findViewById(R.id.message);
    }
}
