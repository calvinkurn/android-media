package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.FeedProductAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.TimeConverter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductCardHeaderViewModel;

/**
 * @author by nisie on 5/16/17.
 */

public class ActivityCardViewHolder extends AbstractViewHolder<ActivityCardViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_activity_card;

    View container;
    TextView title;
    View header;
    ImageView shopAvatar;
    ImageView goldMerchantBadge;
    ImageView officialStoreBadge;
    TextView time;
    View shareButton;
    View buyButton;
    RecyclerView recyclerView;

    private FeedProductAdapter adapter;
    private FeedPlus.View viewListener;

    public ActivityCardViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);

        header = itemView.findViewById(R.id.header);
        title = (TextView) itemView.findViewById(R.id.title);
        shopAvatar = (ImageView) itemView.findViewById(R.id.shop_avatar);
        goldMerchantBadge = (ImageView) itemView.findViewById(R.id.gold_merchant);
        officialStoreBadge = (ImageView) itemView.findViewById(R.id.official_store);
        time = (TextView) itemView.findViewById(R.id.time);
        shareButton = itemView.findViewById(R.id.share_button);
        buyButton = itemView.findViewById(R.id.buy_button);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.product_list);
        container = itemView.findViewById(R.id.container);

        this.viewListener = viewListener;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                6,
                LinearLayoutManager.VERTICAL,
                false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getData().getListProduct().size() == 1) {
                    return 6;
                } else if (adapter.getData().getListProduct().size() % 3 == 0
                        || adapter.getData().getListProduct().size() > 6) {
                    return 2;
                } else if (adapter.getData().getListProduct().size() % 2 == 0) {
                    return 3;
                } else if (adapter.getData().getListProduct().size() == 5) {
                    return getSpanSizeFor5Item(position);
                } else {
                    return 0;
                }
            }
        });
        adapter = new FeedProductAdapter(itemView.getContext(), viewListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private int getSpanSizeFor5Item(int position) {
        switch (position) {
            case 0:
            case 1:
                return 3;
            case 2:
            case 3:
            case 4:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public void bind(ActivityCardViewModel activityCardViewModel) {
        setHeader(activityCardViewModel);
        adapter.setData(activityCardViewModel);
        setFooter(activityCardViewModel);
    }

    public void setHeader(final ActivityCardViewModel activityCardViewModel) {

        String shopNameString = String.valueOf(MethodChecker.fromHtml(activityCardViewModel.getHeader().getShopName()));
        String actionString = String.valueOf(MethodChecker.fromHtml(activityCardViewModel.getActionText()));

        StringBuilder titleText = new StringBuilder();
        titleText.append(shopNameString)
                        .append(" ")
                        .append(actionString);
        SpannableString actionSpanString = new SpannableString(titleText);

        ClickableSpan goToFeedDetail = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToFeedDetail(activityCardViewModel.getFeedId());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(viewListener.getColor(R.color.black_70));
            }
        };

        ClickableSpan goToShopDetail = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToShopDetail(
                        activityCardViewModel.getHeader().getShopId(),
                        activityCardViewModel.getHeader().getUrl());

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.DEFAULT_BOLD);
                ds.setColor(viewListener.getColor(R.color.black));
            }
        };

        actionSpanString.setSpan(goToFeedDetail, titleText.indexOf(actionString)
                , titleText.indexOf(actionString)+actionString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionSpanString.setSpan(goToShopDetail, titleText.indexOf(shopNameString)
                , titleText.indexOf(shopNameString)+shopNameString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        title.setText(actionSpanString);
        title.setMovementMethod(LinkMovementMethod.getInstance());


        ImageHandler.LoadImage(shopAvatar, activityCardViewModel.getHeader().getShopAvatar());

        if (activityCardViewModel.getHeader().isGoldMerchant())
            goldMerchantBadge.setVisibility(View.VISIBLE);
        else
            goldMerchantBadge.setVisibility(View.GONE);

        if (activityCardViewModel.getHeader().isOfficialStore()) {
            officialStoreBadge.setVisibility(View.VISIBLE);
            goldMerchantBadge.setVisibility(View.GONE);
        }
        else
            officialStoreBadge.setVisibility(View.GONE);

        time.setText(TimeConverter.generateTime(activityCardViewModel.getHeader().getTime()));

        shopAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToShopDetail(
                        activityCardViewModel.getHeader().getShopId(),
                        activityCardViewModel.getHeader().getUrl());
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToFeedDetail(activityCardViewModel.getFeedId());
            }
        });
    }

    public void setFooter(final ActivityCardViewModel viewModel) {
        if (viewModel.getListProduct().size() > 1) {
            shareButton.setVisibility(View.VISIBLE);
            buyButton.setVisibility(View.GONE);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onShareButtonClicked(
                            viewModel.getShareUrl(),
                            viewModel.getHeader().getShopName(),
                            "",
                            viewModel.getShareLinkDescription()
                    );
                }
            });
        } else {
            shareButton.setVisibility(View.GONE);
            buyButton.setVisibility(View.VISIBLE);
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToBuyProduct(viewModel.getListProduct().get(0));
                }
            });
        }
    }
}
