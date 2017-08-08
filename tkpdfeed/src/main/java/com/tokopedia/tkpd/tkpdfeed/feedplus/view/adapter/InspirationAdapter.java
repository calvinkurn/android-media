package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.content.Context;
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
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration.InspirationProductViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class InspirationAdapter extends RecyclerView.Adapter<InspirationAdapter.ViewHolder> {

    protected ArrayList<InspirationProductViewModel> list;
    private final FeedPlus.View viewListener;

    public InspirationAdapter(Context context, FeedPlus.View viewListener) {
        this.viewListener = viewListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView productPrice;
        public ImageView productImage;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            productName = (TextView) itemLayoutView.findViewById(R.id.title);
            productPrice = (TextView) itemLayoutView.findViewById(R.id.price);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.product_image);
            productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetailFromInspiration(
                            String.valueOf(list.get(getAdapterPosition()).getProductId()));
                }
            });
            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetailFromInspiration(
                            String.valueOf(list.get(getAdapterPosition()).getProductId()));
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inspiration_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.productPrice.setText(list.get(position).getPrice());
        ImageHandler.LoadImage(holder.productImage, list.get(position).getImageSource());
    }

    @Override
    public int getItemCount() {
        if (list != null && !list.isEmpty()) {
            if (list.size() > 6)
                return 6;
            else
                return list.size();
        } else {
            return 0;
        }
    }

    public void setList(ArrayList<InspirationProductViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<InspirationProductViewModel> getList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}
