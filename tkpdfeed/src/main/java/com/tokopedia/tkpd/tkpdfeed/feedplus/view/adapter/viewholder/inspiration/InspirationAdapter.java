package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.inspiration;

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
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration.InspirationViewModel;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 5/18/17.
 */
public class InspirationAdapter extends RecyclerView.Adapter<InspirationAdapter.ViewHolder> {

    private final FeedPlus.View viewListener;
    private InspirationViewModel inspirationViewModel;

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
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getPage(),
                            inspirationViewModel.getRowNumber(),
                            String.valueOf(inspirationViewModel.getListProduct().get(getAdapterPosition())
                                    .getProductId()),
                            inspirationViewModel.getListProduct().get(getAdapterPosition())
                                    .getImageSource(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition())
                                    .getName(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getPrice(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getPriceInt(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getUrl(),
                            inspirationViewModel.getSource(),
                            inspirationViewModel.getPositionFeedCard(),
                            getAdapterPosition(),
                            inspirationViewModel.getEventLabel());
                }
            });
            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetailFromInspiration(
                            inspirationViewModel.getListProduct().get(getAdapterPosition())
                                    .getPage(),
                            inspirationViewModel.getRowNumber(),
                            String.valueOf(inspirationViewModel.getListProduct().get(getAdapterPosition()).getProductId()),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getImageSource(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getName(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getPrice(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getPriceInt(),
                            inspirationViewModel.getListProduct().get(getAdapterPosition()).getUrl(),
                            inspirationViewModel.getSource(),
                            inspirationViewModel.getPositionFeedCard(),
                            getAdapterPosition(),
                            inspirationViewModel.getEventLabel());
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
        holder.productName.setText(MethodChecker.fromHtml(inspirationViewModel.getListProduct().get(position).getName()));
        holder.productPrice.setText(inspirationViewModel.getListProduct().get(position).getPrice());
        ImageHandler.LoadImage(holder.productImage, inspirationViewModel.getListProduct().get(position).getImageSource());
    }

    @Override
    public int getItemCount() {
        if (inspirationViewModel != null
                && inspirationViewModel.getListProduct() != null
                && !inspirationViewModel.getListProduct().isEmpty()) {
            if (inspirationViewModel.getListProduct().size() > 6)
                return 6;
            else
                return inspirationViewModel.getListProduct().size();
        } else {
            return 0;
        }
    }

    public void setData(InspirationViewModel inspirationViewModel) {
        this.inspirationViewModel = inspirationViewModel;
        notifyDataSetChanged();
    }

    public ArrayList<InspirationProductViewModel> getList() {
        return inspirationViewModel.getListProduct();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

}
