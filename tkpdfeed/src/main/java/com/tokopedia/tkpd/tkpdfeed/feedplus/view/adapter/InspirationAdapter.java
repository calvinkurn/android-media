package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.content.Context;
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
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class InspirationAdapter extends RecyclerView.Adapter<InspirationAdapter.ViewHolder>{

    protected ArrayList<ProductFeedViewModel> list;
    private final Context context;
    private final FeedPlus.View viewListener;

    public InspirationAdapter(Context context, FeedPlus.View viewListener) {
        this.context = context;
        this.viewListener = viewListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView productPrice;
        public ImageView productImage;
        public FloatingActionButton favoriteButton;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            productName = (TextView) itemLayoutView.findViewById(R.id.title);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.price);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            favoriteButton = (FloatingActionButton) itemLayoutView.findViewById(R.id.fab);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inspiration_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);

        holder.favoriteButton.setBackgroundResource(list.get(position).isFavorited() ? R.drawable.ic_faved : R.drawable.ic_fav);

        holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.productPrice.setText(list.get(position).getPrice());
        ImageHandler.LoadImage(holder.productImage, list.get(position).getImageSource());

        holder.productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail(list.get(position).getUrl());
            }
        });

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail(list.get(position).getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list.size() > 6)
            return 6;
        else
            return list.size();
    }

    public void setList(ArrayList<ProductFeedViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<ProductFeedViewModel> getList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}
