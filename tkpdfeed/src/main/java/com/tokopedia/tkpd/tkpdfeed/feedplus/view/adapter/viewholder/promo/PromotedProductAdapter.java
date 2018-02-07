package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo;

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
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromotedProductViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class PromotedProductAdapter extends RecyclerView.Adapter<PromotedProductAdapter.ViewHolder> {

    private final FeedPlus.View viewListener;
    private PromotedProductViewModel promotedProductViewModel;

    public PromotedProductAdapter(Context context, FeedPlus.View viewListener) {
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
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promoted_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ArrayList<ProductFeedViewModel> list = promotedProductViewModel.getListProduct();
        holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.productPrice.setText(list.get(position).getPrice());
        ImageHandler.LoadImage(holder.productImage, list.get(position).getImageSource());

        holder.productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail(promotedProductViewModel.getRowNumber(),
                        list.get(position).getPage(),
                        String.valueOf(list.get(position).getProductId()), list.get(position).getImageSourceSingle(), list.get(position).getName(), String.valueOf(list.get(position).getProductId()));

            }
        });

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToProductDetail(promotedProductViewModel.getRowNumber(),
                        list.get(position).getPage(),
                        String.valueOf(list.get(position).getProductId()), list.get(position).getImageSourceSingle(), list.get(position).getName(), String.valueOf(list.get(position).getProductId()));

            }
        });
    }

    @Override
    public int getItemCount() {
        if (promotedProductViewModel.getListProduct().size() > 6)
            return 6;
        else
            return promotedProductViewModel.getListProduct().size();
    }

    public void setData(PromotedProductViewModel promotedProductViewModel) {
        this.promotedProductViewModel = promotedProductViewModel;
        notifyDataSetChanged();
    }

    public ArrayList<ProductFeedViewModel> getList() {
        return promotedProductViewModel.getListProduct();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}
