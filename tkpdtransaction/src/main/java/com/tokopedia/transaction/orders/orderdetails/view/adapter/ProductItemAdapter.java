package com.tokopedia.transaction.orders.orderdetails.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;

import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Items> itemsList;
    private Context context;
    OrderListDetailPresenter presenter;

    public ProductItemAdapter(Context context, List<Items> itemsList, OrderListDetailPresenter presenter) {
        this.context = context;
        this.itemsList = itemsList;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;

        v = inflater.inflate(R.layout.marketplace_product_detail_item, parent, false);
        holder = new ItemViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ProductItemAdapter.ItemViewHolder) holder).setIndex(position);
        ((ProductItemAdapter.ItemViewHolder) holder).bindData(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int index;
        private ImageView productImage;
        private TextView productName;
        private TextView quantity;
        private TextView productPrice;
        private TextView productDescription, totalPrice, buyBtn;


        public ItemViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.img_product);
            productName = itemView.findViewById(R.id.txt_product_name);
            quantity = itemView.findViewById(R.id.txt_quantity);
            productPrice = itemView.findViewById(R.id.txt_item_price);
            productDescription = itemView.findViewById(R.id.txt_description);
            totalPrice = itemView.findViewById(R.id.txt_total_price);
            buyBtn = itemView.findViewById(R.id.btn_buy);
        }

        void bindData(final Items items) {
            if (items != null) {
                if (!TextUtils.isEmpty(items.getImageUrl())) {
                    ImageHandler.loadImage(context, productImage, items.getImageUrl(), R.color.grey_1100, R.color.grey_1100);
                }
                if (!TextUtils.isEmpty(items.getTitle())) {
                    productName.setText(items.getTitle());
                }
                quantity.setText(items.getQuantity() + " " + "Barang" + " (" + items.getWeight() + ")");
                if (!TextUtils.isEmpty(items.getPrice()))
                    productPrice.setText(items.getPrice());
            }

            if (!TextUtils.isEmpty(items.getDescription())) {
                productDescription.setText(items.getDescription());
            }
            if (!TextUtils.isEmpty(items.getTotalPrice())) {
                totalPrice.setText(items.getTotalPrice());
            }
            if (items.getActionButtons().size() > 0) {
                buyBtn.setVisibility(View.VISIBLE);
            }

        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onClick(View view) {

        }
    }
}
