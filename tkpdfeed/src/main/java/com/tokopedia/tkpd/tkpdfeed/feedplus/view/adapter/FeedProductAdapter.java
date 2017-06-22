package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 5/16/17.
 */

public class FeedProductAdapter extends RecyclerView.Adapter<FeedProductAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;

        public TextView productPrice;

        public ImageView productImage;

        public FrameLayout blackScreen;

        public TextView extraProduct;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            productName = (TextView) itemView.findViewById(R.id.title);
            productPrice = (TextView) itemView.findViewById(R.id.price);
            extraProduct = (TextView) itemView.findViewById(R.id.extra_product);
            blackScreen = (FrameLayout) itemView.findViewById(R.id.black_screen);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
        }
    }

    protected ArrayList<ProductFeedViewModel> list;
    private ActivityCardViewModel activityCardViewModel;
    private final FeedPlus.View viewListener;
    protected final Context context;

    public FeedProductAdapter(Context context, FeedPlus.View viewListener) {
        this.context = context;
        this.viewListener = viewListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_feed_product_item_blurred, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ArrayList<ProductFeedViewModel> list = activityCardViewModel.getListProduct();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (getItemCount() == 1) {
            holder.productName.setEllipsize(TextUtils.TruncateAt.END);
            holder.productName.setMaxLines(1);
            lp.setMargins(0, 5, 0, 0);

        } else {
            holder.productName.setEllipsize(TextUtils.TruncateAt.END);
            holder.productName.setMaxLines(2);
            lp.setMargins(18, 18, 18, 0);
        }

        holder.productImage.setLayoutParams(lp);

        holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.productPrice.setText(list.get(position).getPrice());
        ImageHandler.loadImageFit2(holder.productImage.getContext(),
                holder.productImage,
                getItemCount() > 1 ? list.get(position).getImageSource() : list.get(position).getImageSourceSingle());

        if (list.size() > 6 && position == 5) {
            holder.blackScreen.setForeground(new ColorDrawable(ContextCompat.getColor(context, R.color.trans_black_40)));
            holder.extraProduct.setVisibility(View.VISIBLE);
            String extra = "+" + (activityCardViewModel.getTotalProduct() - 5);
            holder.extraProduct.setText(extra);
            holder.blackScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToFeedDetail(activityCardViewModel.getFeedId());
                }
            });

        } else {
            holder.blackScreen.setForeground(null);
            holder.extraProduct.setVisibility(View.GONE);

            holder.productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetail(String.valueOf(list.get(position).getProductId()));
                }
            });

            holder.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToProductDetail(String.valueOf(list.get(position).getProductId()));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (activityCardViewModel.getListProduct().size() > 6)
            return 6;
        else
            return activityCardViewModel.getListProduct().size();
    }

    public void setData(ActivityCardViewModel activityCardViewModel) {
        this.activityCardViewModel = activityCardViewModel;
        notifyDataSetChanged();
    }

    public ActivityCardViewModel getData() {
        return activityCardViewModel;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }
}
