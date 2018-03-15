package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.analytics.FeedEnhancedTracking;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.core.gcm.Constants.Applinks.SHOP;

/**
 * @author by nisie on 5/16/17.
 */

public class FeedProductAdapter extends RecyclerView.Adapter<FeedProductAdapter.ViewHolder> {

    private static final String SHOP_ID_BRACKETS = "{shop_id}";

    private static final int MAX_FEED_SIZE = 6;
    private static final int LAST_FEED_POSITION = 5;

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

        if (list.size() > MAX_FEED_SIZE && position == LAST_FEED_POSITION) {
            holder.blackScreen.setForeground(new ColorDrawable(ContextCompat.getColor(context, R.color.trans_black_40)));
            holder.extraProduct.setVisibility(View.VISIBLE);
            String extra = "+" + (activityCardViewModel.getTotalProduct() - LAST_FEED_POSITION);
            holder.extraProduct.setText(extra);
            holder.blackScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToFeedDetail(
                            activityCardViewModel.getPage(),
                            activityCardViewModel.getRowNumber(), activityCardViewModel.getFeedId());
                }
            });

        } else {
            holder.blackScreen.setForeground(null);
            holder.extraProduct.setVisibility(View.GONE);

            holder.productName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToProductDetail(list, position);
                }
            });

            holder.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToProductDetail(list, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (activityCardViewModel != null
                && activityCardViewModel.getListProduct().size() > MAX_FEED_SIZE)
            return MAX_FEED_SIZE;
        else if (activityCardViewModel != null)
            return activityCardViewModel.getListProduct().size();
        else
            return 0;
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

    private void goToProductDetail(ArrayList<ProductFeedViewModel> list, int position) {
        viewListener.onGoToProductDetailFromProductUpload(
                activityCardViewModel.getRowNumber(),
                activityCardViewModel.getPositionFeedCard(),
                list.get(position).getPage(),
                position,
                String.valueOf(list.get(position).getProductId()),
                list.get(position).getImageSourceSingle(),
                list.get(position).getName(),
                list.get(position).getPrice(),
                list.get(position).getPriceInt(),
                list.get(position).getUrl(),
                activityCardViewModel.getEventLabel()
        );

        doTrackingEnhancedEcommerce(position);
    }

    private void doTrackingEnhancedEcommerce(int position) {
        String loginIdString = SessionHandler.getLoginID(viewListener.getActivity());
        int loginIdInt = loginIdString.isEmpty() ? 0 : Integer.valueOf(loginIdString);

        String shopId = String.valueOf(activityCardViewModel.getHeader().getShopId());
        List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
        list.add(new FeedEnhancedTracking.Promotion(
                activityCardViewModel.getHeader().getShopId(),
                FeedEnhancedTracking.Promotion.createContentNameProductUpload(
                        activityCardViewModel.getTotalProduct()),
                String.valueOf(activityCardViewModel.getTotalProduct()),
                position,
                "-",
                activityCardViewModel.getHeader().getShopId(),
                SHOP.replace(SHOP_ID_BRACKETS, shopId)
        ));
        TrackingUtils.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(list, loginIdInt));
    }
}
