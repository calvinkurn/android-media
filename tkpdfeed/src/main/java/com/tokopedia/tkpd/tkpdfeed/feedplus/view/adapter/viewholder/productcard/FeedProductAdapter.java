package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private static final int MAX_FEED_SIZE_SMALL = 3;
    private static final int LAST_FEED_POSITION = 5;
    private static final int LAST_FEED_POSITION_SMALL = 2;
    protected final Context context;
    private final FeedPlus.View viewListener;
    protected ArrayList<ProductFeedViewModel> list;
    private ActivityCardViewModel activityCardViewModel;
    private int positionInFeed;
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
        ImageHandler.loadImage2(
                holder.productImage,
                getItemCount() > 1 ? list.get(position).getImageSource() : list.get(position)
                        .getImageSourceSingle(),
                R.drawable.ic_loading_image);

        if (list.size() > MAX_FEED_SIZE && position == LAST_FEED_POSITION) {
            int extraProduct = (activityCardViewModel.getTotalProduct() - LAST_FEED_POSITION);
            showBlackScreen(holder, extraProduct);

        } else if (list.size() < MAX_FEED_SIZE
                && list.size() > MAX_FEED_SIZE_SMALL
                && position == LAST_FEED_POSITION_SMALL) {
            int extraProduct = (activityCardViewModel.getTotalProduct() - LAST_FEED_POSITION_SMALL);
            showBlackScreen(holder, extraProduct);

        } else {
            holder.extraProduct.setBackground(null);
            holder.extraProduct.setVisibility(View.GONE);

            holder.productName.setText(MethodChecker.fromHtml(list.get(position).getName()));
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
        if (activityCardViewModel != null) {
            if (activityCardViewModel.getListProduct().size() >= MAX_FEED_SIZE)
                return MAX_FEED_SIZE;
            else if (activityCardViewModel.getListProduct().size() >= MAX_FEED_SIZE_SMALL)
                return MAX_FEED_SIZE_SMALL;
            else
                return activityCardViewModel.getListProduct().size();
        }

        return 0;
    }

    public void setData(ActivityCardViewModel activityCardViewModel, int positionInFeed) {
        this.activityCardViewModel = activityCardViewModel;
        this.positionInFeed = positionInFeed;
        notifyDataSetChanged();
    }

    public ActivityCardViewModel getData() {
        return activityCardViewModel;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    private void showBlackScreen(ViewHolder holder, int extraProduct) {
        String extra = String.format("+%s", String.valueOf(extraProduct));
        holder.extraProduct.setBackground(new ColorDrawable(
                MethodChecker.getColor(
                        holder.extraProduct.getContext(),
                        R.color.black_screen_overlay))
        );
        holder.extraProduct.setVisibility(View.VISIBLE);
        holder.extraProduct.setText(extra);
        holder.extraProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToFeedDetail(
                        activityCardViewModel.getPage(),
                        activityCardViewModel.getRowNumber(), activityCardViewModel.getFeedId());
            }
        });

        String seeOtherProduct = String.format(
                holder.productName.getContext().getString(R.string.see_other_product),
                String.valueOf(extraProduct));
        holder.productName.setText(seeOtherProduct);
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

        doTrackingEnhancedEcommerce();
    }

    private void doTrackingEnhancedEcommerce() {
        String loginIdString = SessionHandler.getLoginID(viewListener.getActivity());
        int loginIdInt = loginIdString.isEmpty() ? 0 : Integer.valueOf(loginIdString);

        String shopId = String.valueOf(activityCardViewModel.getHeader().getShopId());
        List<FeedEnhancedTracking.Promotion> list = new ArrayList<>();
        list.add(new FeedEnhancedTracking.Promotion(
                activityCardViewModel.getHeader().getShopId(),
                FeedEnhancedTracking.Promotion.createContentNameProductUpload(
                        activityCardViewModel.getTotalProduct()),
                String.valueOf(activityCardViewModel.getTotalProduct()),
                this.positionInFeed,
                "-",
                activityCardViewModel.getHeader().getShopId(),
                SHOP.replace(SHOP_ID_BRACKETS, shopId)
        ));
        TrackingUtils.eventTrackingEnhancedEcommerce(
                FeedEnhancedTracking.getClickTracking(list, loginIdInt));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;

        public ImageView productImage;

        public TextView extraProduct;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            productName = (TextView) itemView.findViewById(R.id.title);
            extraProduct = (TextView) itemView.findViewById(R.id.extra_product);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
        }
    }
}
