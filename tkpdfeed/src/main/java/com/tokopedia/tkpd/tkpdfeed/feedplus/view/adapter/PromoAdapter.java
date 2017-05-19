package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/17/17.
 */

public class PromoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_MORE = 234;
    private static final int VIEW_LAYOUT = 344;
    private ArrayList<ProductFeedViewModel> list;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case VIEW_MORE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.promo_more_layout, parent, false);
                return new ViewMoreViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.promo_image_layout, parent, false);
                return new LayoutViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_MORE) {

        }else {
            LayoutViewHolder temp = (LayoutViewHolder) holder;
            ImageHandler.LoadImage(temp.imageView, list.get(position).getImageSource());
        }
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

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()-1){
            return VIEW_MORE;
        }else{
            return VIEW_LAYOUT;
        }
    }

    public class LayoutViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public LayoutViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.product_image);
        }
    }

    public class ViewMoreViewHolder extends RecyclerView.ViewHolder{

        public ViewMoreViewHolder(View itemView) {
            super(itemView);
        }
    }


}
