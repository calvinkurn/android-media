package com.tokopedia.discovery.intermediary.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.CategoryPageTracking;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.BadgeModel;
import com.tokopedia.discovery.intermediary.domain.model.LabelModel;
import com.tokopedia.discovery.intermediary.domain.model.ProductModel;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alifa on 3/30/17.
 */

public class CuratedProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TOPPICKS_TITLE = 0;

    private List<ProductModel> productModelList;
    private final int homeMenuWidth;
    private final CuratedProductAdapter.OnItemClickListener onItemClickListener;
    private final String curatedName;
    Context context;

    CuratedProductAdapter(Context context, List<ProductModel> productModelList, int homeMenuWidth,
                          CuratedProductAdapter.OnItemClickListener onItemClickListener,String curatedName) {
        this.productModelList = productModelList;
        this.homeMenuWidth = homeMenuWidth;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.curatedName = curatedName;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.listview_product_item_grid, null
        );
        v.setMinimumWidth(homeMenuWidth);
        return new ViewHolderProductitem(context,v);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {
        ViewHolderProductitem itemViewHolder = (ViewHolderProductitem) holder;
        final ProductModel productModel = productModelList.get(i);
        itemViewHolder.bindData(productModel,itemViewHolder);
        itemViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackingClickEnhance(productModel);
                onItemClickListener.onItemClicked(productModel,curatedName);
            }
        });


    }

    private void trackingClickEnhance(ProductModel product) {
        Map<String, Object> map = DataLayer.mapOf("event", "productClick",
                "eventCategory", "intermediary page",
                "eventAction", "click product curation",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", product.getTrackerListName()),
                                "products", DataLayer.listOf(
                                        product.generateClickDataLayer()
                                )
                        )
                )
        );
        CategoryPageTracking.eventEnhance(map);
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }


    public static class ViewHolderProductitem extends RecyclerView.ViewHolder {

        @BindView(R2.id.product_image)
        ImageView productImage;
        @BindView(R2.id.title)
        TextView title;
        @BindView(R2.id.price)
        TextView price;
        @BindView(R2.id.label_container)
        FlowLayout labelContainer;
        @BindView(R2.id.shop_name)
        TextView shopName;
        @BindView(R2.id.location)
        TextView location;
        @BindView(R2.id.badges_container)
        LinearLayout badgesContainer;
        @BindView(R2.id.container)
        RelativeLayout container;

        private Context context;
        private ProductModel data;


        public ViewHolderProductitem(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        public void bindData(ProductModel data, ViewHolderProductitem viewHolder) {
            this.data = data;
            title.setText(data.getName());
            price.setText(data.getPrice());
            if (data.getShopLocation() != null)
                location.setText(MethodChecker.fromHtml(data.getShopLocation()));
            else
                location.setVisibility(View.INVISIBLE);

            shopName.setText(data.getShopName());
            ImageHandler.loadImageThumbs(context, productImage, data.getImageUrl());
            viewHolder.badgesContainer.removeAllViews();
            if (data.getBadges() != null) {
                for (BadgeModel badges : data.getBadges()) {
                    LuckyShopImage.loadImage(context, badges.getImageUrl(), badgesContainer);
                }
            }
            viewHolder.labelContainer.removeAllViews();
            if (data.getLabels() != null) {
                for (LabelModel label : data.getLabels()) {
                    View view = LayoutInflater.from(context).inflate(com.tokopedia.core.R.layout.label_layout, null);
                    TextView labelText = (TextView) view.findViewById(com.tokopedia.core.R.id.label);
                    labelText.setText(label.getTitle());
                    if (!label.getColor().toLowerCase().equals("#ffffff")) {
                        labelText.setBackgroundResource(com.tokopedia.core.R.drawable.bg_label);
                        labelText.setTextColor(ContextCompat.getColor(context, com.tokopedia.core.R.color.white));
                        ColorStateList tint = ColorStateList.valueOf(Color.parseColor(label.getColor()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            labelText.setBackgroundTintList(tint);
                        } else {
                            ViewCompat.setBackgroundTintList(labelText, tint);
                        }
                    }
                    labelContainer.addView(view);
                }
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClicked(ProductModel productModel, String curatedName);
    }


}
