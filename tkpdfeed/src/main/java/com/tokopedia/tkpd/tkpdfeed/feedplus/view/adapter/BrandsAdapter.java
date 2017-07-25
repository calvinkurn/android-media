package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.brands.BrandsFeedViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.ViewHolder> {

    protected ArrayList<BrandsFeedViewModel> list;
    private final FeedPlus.View viewListener;

    public BrandsAdapter(FeedPlus.View viewListener) {
        this.viewListener = viewListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView productPrice;
        public ImageView productImage;
        public ImageView shopAva;
        public TextView shopName;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            productName = (TextView) itemLayoutView.findViewById(R.id.title);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.price);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            shopAva = (ImageView) itemLayoutView.findViewById(R.id.shop_ava);
            shopName = (TextView) itemLayoutView.findViewById(R.id.shop_name);
            productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetail(String.valueOf(list.get(getAdapterPosition())
                            .getProductId()));
                }
            });

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetail(String.valueOf(list.get(getAdapterPosition())
                            .getProductId()));
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_store_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageHandler.LoadImage(holder.shopAva, list.get(holder.getAdapterPosition()).getShopAva());
        holder.shopName.setText(list.get(position).getShopName());

        holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.productPrice.setText(list.get(position).getPrice());
        ImageHandler.LoadImage(holder.productImage, list.get(position).getImageSource());

    }

    @Override
    public int getItemCount() {
        if (list.size() > 6)
            return 6;
        else
            return list.size();
    }

    public void setList(ArrayList<BrandsFeedViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<BrandsFeedViewModel> getList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}
