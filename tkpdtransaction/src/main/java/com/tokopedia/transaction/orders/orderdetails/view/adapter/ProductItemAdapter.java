package com.tokopedia.transaction.orders.orderdetails.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.data.Items;
import com.tokopedia.transaction.orders.orderdetails.view.presenter.OrderListDetailPresenter;

import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Items> itemsList;
    private Context context;
    OrderListDetailPresenter presenter;
    public static final String ORDER_LIST_URL_ENCODING = "UTF-8";

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
        View v = inflater.inflate(R.layout.marketplace_product_detail_item, parent, false);
        return new ItemViewHolder(v);
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
                    productName.setText(Html.fromHtml(items.getTitle()));
                }
                quantity.setText(String.format(context.getResources().getString(R.string.quantity), items.getQuantity(), items.getWeight()));
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
                buyBtn.setText(items.getActionButtons().get(0).getLabel());
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.dp_4));
                if (!items.getActionButtons().get(0).getActionColor().getBackground().equals("")) {
                    shape.setColor((Color.parseColor(items.getActionButtons().get(0).getActionColor().getBackground())));
                }
                if (!items.getActionButtons().get(0).getActionColor().getBorder().equals("")) {
                    shape.setStroke(context.getResources().getDimensionPixelSize(R.dimen.dp_2), Color.parseColor(items.getActionButtons().get(0).getActionColor().getBorder()));
                }
                buyBtn.setBackground(shape);
                if (!items.getActionButtons().get(0).getActionColor().getTextColor().equals("")) {
                    buyBtn.setTextColor(Color.parseColor(items.getActionButtons().get(0).getActionColor().getTextColor()));
                }
                buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.onBuyAgain(context.getResources());
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, String.valueOf(items.getId()));
                }
            });

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
