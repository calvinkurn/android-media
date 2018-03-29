package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard;

import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.TimeConverter;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ActivityCardViewModel;

/**
 * @author by nisie on 5/16/17.
 */

public class ActivityCardViewHolder extends AbstractViewHolder<ActivityCardViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_activity_card;

    private TextView title;
    private View header;
    private ImageView shopAvatar;
    private ImageView gmBadge;
    private ImageView osBadge;
    private TextView time;
    private View shareButton;
    private View buyButton;

    private FeedProductAdapter adapter;
    private FeedPlus.View viewListener;

    public ActivityCardViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);

        header = itemView.findViewById(R.id.header);
        title = itemView.findViewById(R.id.title);
        shopAvatar = itemView.findViewById(R.id.shop_avatar);
        time = itemView.findViewById(R.id.time);
        shareButton = itemView.findViewById(R.id.share_button);
        buyButton = itemView.findViewById(R.id.buy_button);
        gmBadge = itemView.findViewById(R.id.gold_merchant);
        osBadge = itemView.findViewById(R.id.official_store);
        RecyclerView recyclerView = itemView.findViewById(R.id.product_list);

        this.viewListener = viewListener;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                itemView.getContext(),
                6,
                LinearLayoutManager.VERTICAL,
                false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getData().getListProduct().size()) {
                    case 1:
                        return 6;
                    case 2:
                        return 3;
                    default:
                        return 2;
                }
            }
        });
        adapter = new FeedProductAdapter(itemView.getContext(), viewListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void bind(ActivityCardViewModel activityCardViewModel) {
        if (activityCardViewModel != null) {
            setHeader(activityCardViewModel);
            activityCardViewModel.setRowNumber(getAdapterPosition());
            adapter.setData(activityCardViewModel, getAdapterPosition() + 1);
            setFooter(activityCardViewModel);
        }
    }

    public void setHeader(final ActivityCardViewModel activityCardViewModel) {

        String shopNameString = String.valueOf(MethodChecker.fromHtml(activityCardViewModel.getHeader().getShopName()));
        String actionString = String.valueOf(MethodChecker.fromHtml(activityCardViewModel.getActionText()));

        StringBuilder titleText = new StringBuilder();

        if (activityCardViewModel.getHeader().isGoldMerchant() || activityCardViewModel.getHeader().isOfficialStore())
            titleText.append("     ");

        titleText
                .append(shopNameString)
                .append(" ")
                .append(actionString);
        SpannableString actionSpanString = new SpannableString(titleText);

        ClickableSpan goToFeedDetail = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToFeedDetail(
                        activityCardViewModel.getPage(),
                        activityCardViewModel.getRowNumber(),
                        activityCardViewModel.getFeedId());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(viewListener.getColor(R.color.black_38));
                ds.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            }
        };

        ClickableSpan goToShopDetail = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToShopDetail(
                        activityCardViewModel.getPage(),
                        activityCardViewModel.getRowNumber(),
                        activityCardViewModel.getHeader().getShopId(),
                        activityCardViewModel.getHeader().getUrl());

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(viewListener.getColor(R.color.black_70));
                ds.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            }
        };

        setSpan(actionSpanString, goToFeedDetail, titleText, actionString);
        setSpan(actionSpanString, goToShopDetail, titleText, shopNameString);

        if (activityCardViewModel.getHeader().isOfficialStore()) {
            gmBadge.setVisibility(View.GONE);
            osBadge.setVisibility(View.VISIBLE);
        } else if (activityCardViewModel.getHeader().isGoldMerchant()) {
            gmBadge.setVisibility(View.VISIBLE);
            osBadge.setVisibility(View.GONE);
        } else {
            gmBadge.setVisibility(View.GONE);
            osBadge.setVisibility(View.GONE);
        }

        title.setText(actionSpanString);
        title.setMovementMethod(LinkMovementMethod.getInstance());

        ImageHandler.LoadImage(shopAvatar, activityCardViewModel.getHeader().getShopAvatar());

        time.setText(TimeConverter.generateTime(activityCardViewModel.getHeader().getTime()));

        shopAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToShopDetail(
                        activityCardViewModel.getPage(), activityCardViewModel.getRowNumber(), activityCardViewModel.getHeader().getShopId(),
                        activityCardViewModel.getHeader().getUrl());
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToFeedDetail(
                        activityCardViewModel.getPage(),
                        activityCardViewModel.getRowNumber(), activityCardViewModel.getFeedId());
            }
        });
    }

    private void setSpan(SpannableString actionSpanString, Object object, StringBuilder titleText, String stringEdited) {
        if (object instanceof ImageSpan) {
            actionSpanString.setSpan(object, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (titleText.toString().contains(stringEdited)) {
            actionSpanString.setSpan(object, titleText.indexOf(stringEdited)
                    , titleText.indexOf(stringEdited) + stringEdited.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void setFooter(final ActivityCardViewModel viewModel) {
        if (viewModel.getListProduct().size() > 1) {
            shareButton.setVisibility(View.GONE);
            buyButton.setVisibility(View.GONE);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onShareButtonClicked(
                            viewModel.getShareUrl(),
                            viewModel.getHeader().getShopName(),
                            viewModel.getHeader().getShopAvatar(),
                            viewModel.getShareLinkDescription(),
                            viewModel.getPage() + "." + viewModel.getRowNumber()
                    );
                }
            });
        } else {
            shareButton.setVisibility(View.GONE);
            buyButton.setVisibility(View.GONE);
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onGoToBuyProduct(viewModel.getListProduct().get(0));
                }
            });
        }
    }
}
