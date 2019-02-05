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

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.customwidget.FlowLayout;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.BadgeModel;
import com.tokopedia.discovery.intermediary.domain.model.LabelModel;
import com.tokopedia.discovery.intermediary.domain.model.ProductModel;

import java.util.List;

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
                onItemClickListener.onItemClicked(productModel,curatedName);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }


    public static class ViewHolderProductitem extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView title;
        private TextView price;
        private FlowLayout labelContainer;
        private TextView shopName;
        private TextView location;
        private LinearLayout badgesContainer;
        private RelativeLayout container;

        private Context context;
        private ProductModel data;


        public ViewHolderProductitem(Context context, View itemView) {
            super(itemView);
            initView(itemView);
            this.context = context;
        }

        private void initView(View view) {
            productImage = view.findViewById(R.id.product_image);
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            labelContainer = view.findViewById(R.id.label_container);
            shopName = view.findViewById(R.id.shop_name);
            location = view.findViewById(R.id.location);
            badgesContainer = view.findViewById(R.id.badges_container);
            container = view.findViewById(R.id.container);
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
                    View view = LayoutInflater.from(context).inflate(com.tokopedia.core2.R.layout.label_layout, null);
                    TextView labelText = (TextView) view.findViewById(com.tokopedia.core2.R.id.label);
                    labelText.setText(label.getTitle());
                    if (!label.getColor().toLowerCase().equals("#ffffff")) {
                        labelText.setBackgroundResource(com.tokopedia.core2.R.drawable.bg_label);
                        labelText.setTextColor(ContextCompat.getColor(context, com.tokopedia.core2.R.color.white));
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
