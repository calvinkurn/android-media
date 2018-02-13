package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.customview.RoundedCornerImageView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * @author by milhamj on 12/02/18.
 */

public class PromotedShopAdapter extends RecyclerView.Adapter<PromotedShopAdapter.ViewHolder>{

    ArrayList<ProductFeedViewModel> list;

    public PromotedShopAdapter() {
    }

    @Override
    public PromotedShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_topads_shop_product_image, parent, false);
        return new PromotedShopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PromotedShopAdapter.ViewHolder holder, int position) {
        ImageHandler.LoadImage(holder.imageView, list.get(position).getImageSource());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<ProductFeedViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<ProductFeedViewModel> getList() {
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RoundedCornerImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image_rounded);
        }
    }
}
