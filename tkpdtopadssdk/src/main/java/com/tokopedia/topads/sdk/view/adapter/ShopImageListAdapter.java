package com.tokopedia.topads.sdk.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.ImageProduct;
import com.tokopedia.topads.sdk.utils.ImageLoader;

import java.util.List;

/**
 * @author by errysuprayogi on 3/30/17.
 */

public class ShopImageListAdapter extends RecyclerView.Adapter<ShopImageListAdapter.ViewHolder> {

    private List<ImageProduct> imageProducts;
    private LayoutInflater inflater;
    private Context context;
    private View.OnClickListener onClickListener;
    private ImageLoader imageLoader;
    private int layoutViewHolder;

    public ShopImageListAdapter(Context context, ImageLoader imageLoader, List<ImageProduct> imageProducts, View.OnClickListener onClickListener, int layoutViewHolder) {
        this.imageProducts = imageProducts;
        this.context = context;
        this.onClickListener = onClickListener;
        this.imageLoader = imageLoader;
        this.layoutViewHolder = layoutViewHolder;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(layoutViewHolder, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        imageLoader.loadImage(imageProducts.get(position).getImageUrl(), holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageProducts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            cardView = (CardView) itemView.findViewById(R.id.image_container);
            if (layoutViewHolder == R.layout.layout_shop_product_image_big) {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int dpWidth = (int) ((displayMetrics.widthPixels / displayMetrics.density) / 3) -
                        ((int) (5 / displayMetrics.density) * 2);
                dpWidth = toPixels(dpWidth, displayMetrics);
                cardView.setLayoutParams(new CardView.LayoutParams(dpWidth, dpWidth));
            }
        }

        private int toPixels(int dp, DisplayMetrics metrics) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        }
    }
}
