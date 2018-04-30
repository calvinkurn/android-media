package com.tokopedia.events.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.events.R;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.presenter.EventHomePresenter;
import com.tokopedia.events.view.utils.CardAdapter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter, EventsContract.AdapterCallbacks {

    private List<CardView> mViews;
    private List<CategoryItemsViewModel> mData;
    private float mBaseElevation;
    private CardOnClickListener clickListener;
    private EventHomePresenter mPresenter;
    private int currentDataIndex;
    private ViewGroup parent;

    public CardPagerAdapter(EventHomePresenter presenter) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        clickListener = new CardOnClickListener();
        mPresenter = presenter;
        mPresenter.setupCallback(this);
    }

    public void addData(List<CategoryItemsViewModel> items) {
        mData.addAll(items);
        for (int i = 0; i < mData.size(); i++)
            mViews.add(null);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        parent = container;
        Log.d("CardPagerAdapter", "View Created");
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.event_category_item_revamp, container, false);
        container.addView(view);
        view.setTag(position);
        bind(mData.get(position), view);
        CardView cardView = view.findViewById(R.id.event_category_cardview);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        cardView.setScaleY((float) (0.9));
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("CardPagerAdapter", "View Destroyed");
        container.removeView((View) object);
        mViews.set(position, null);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentDataIndex = position;
        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_PRODUCT_IMPRESSION, mData.get(currentDataIndex).getTitle()
                + " - " + currentDataIndex);
    }

    private void bind(CategoryItemsViewModel item, View view) {
        TextView tv1Price = view.findViewById(R.id.tv1_price);
        tv1Price.setText("Rp" + " " + CurrencyUtil.convertToCurrencyString(item.getSalesPrice()));
        TextView tvAddToWishlist = view.findViewById(R.id.tv_add_to_wishlist);
        tvAddToWishlist.setText(String.valueOf(item.getLikes()));
        if (item.isLiked()) {
            tvAddToWishlist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_wishlist_red,
                    0, 0, 0);
            tvAddToWishlist.setTextColor(view.getResources().getColor(R.color.red_1));
        }

        tvAddToWishlist.setOnClickListener(clickListener);
        TextView tvEventShare = view.findViewById(R.id.tv_event_share);
        tvEventShare.setOnClickListener(clickListener);
        ImageView ivEventThumb = view.findViewById(R.id.iv_event_thumb);
        ivEventThumb.setOnClickListener(clickListener);
        TextView tv3SoldCnt = view.findViewById(R.id.tv3_sold_cnt);
        if (item.getOfferText() != null && item.getOfferText().length() > 2) {
            tv3SoldCnt.setText(item.getOfferText());
            tv3SoldCnt.setVisibility(View.VISIBLE);
        } else {
            tv3SoldCnt.setVisibility(View.GONE);
        }
        TextView tv3Tag = view.findViewById(R.id.tv3_tag);
        if (item.getDisplayTags() != null && item.getDisplayTags().length() > 2)
            tv3Tag.setText(item.getDisplayTags());
        else
            tv3Tag.setVisibility(View.GONE);
        TextView tv4DateTime = view.findViewById(R.id.tv4_date_time);
        if (item.getMinStartDate() == 0) {
            tv4DateTime.setVisibility(View.GONE);
        } else {
            if (item.getMinStartDate() == item.getMaxEndDate())
                tv4DateTime.setText(Utils.convertEpochToString(item.getMinStartDate()));
            else
                tv4DateTime.setText(Utils.convertEpochToString(item.getMinStartDate())
                        + " - " + Utils.convertEpochToString(item.getMaxEndDate()));
        }
        tv4DateTime.setOnClickListener(clickListener);
        TextView tv4Location = view.findViewById(R.id.tv4_location);
        tv4Location.setText(item.getCityName());
        tv4Location.setOnClickListener(clickListener);
        TextView tv4EventTitle = view.findViewById(R.id.tv4_event_title);
        tv4EventTitle.setText(item.getDisplayName());
        tv4EventTitle.setOnClickListener(clickListener);
        ImageHandler.loadImageCover2(ivEventThumb, item.getThumbnailApp());
    }

    @Override
    public void notifyDatasetChanged(int position) {
        View view = parent.findViewWithTag(position);
        if (view != null) {
            TextView textView = view.findViewById(R.id.tv_add_to_wishlist);
            textView.setText(String.valueOf(mData.get(position).getLikes()));
            if (mData.get(position).isLiked()) {
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_wishlist_red,
                        0, 0, 0);
                textView.setTextColor(view.getResources().getColor(R.color.red_1));
            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_event_wishlist,
                        0, 0, 0);
                textView.setTextColor(view.getResources().getColor(R.color.black_54));
            }
        }
    }

    private class CardOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_add_to_wishlist) {
                mPresenter.setEventLike(mData.get(currentDataIndex), currentDataIndex);
            } else if (id == R.id.tv_event_share) {
                mPresenter.shareEvent(mData.get(currentDataIndex));
            } else if (id == R.id.iv_event_thumb ||
                    id == R.id.tv4_event_title || id == R.id.tv4_location ||
                    id == R.id.tv4_date_time) {
                mPresenter.showEventDetails(mData.get(currentDataIndex));
            }
        }
    }

}