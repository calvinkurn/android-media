package com.tokopedia.topads.sdk.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.ImageProduct;
import com.tokopedia.topads.sdk.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 29/03/18.
 */

public class FeedNewShopAdapter extends RecyclerView.Adapter<FeedNewShopAdapter.ViewHolder> {

    private static final int MAX_SIZE = 3;

    private List<ImageProduct> list;
    private ImageLoader imageLoader;
    private View.OnClickListener itemClickListener;

    public FeedNewShopAdapter(View.OnClickListener itemClickListener) {
        this.list = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public FeedNewShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_new_shop_product_image, parent, false);
        return new FeedNewShopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeedNewShopAdapter.ViewHolder holder, int position) {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(holder.imageView.getContext());
        }
        imageLoader.loadImage(list.get(position).getImageUrl(), holder.imageView);
        holder.imageView.setOnClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        if (list.size() > MAX_SIZE) {
            return MAX_SIZE;
        }

        return list.size();
    }

    public List<ImageProduct> getList() {
        return list;
    }

    public void setList(List<ImageProduct> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
        }
    }
}
