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
import com.tokopedia.events.R;
import com.tokopedia.events.view.presenter.EventHomePresenter;
import com.tokopedia.events.view.utils.CardAdapter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CategoryItemsViewModel> mData;
    private float mBaseElevation;
    private CardOnClickListener clickListener;
    private EventHomePresenter mPresenter;
    private int currentDataIndex;

    public CardPagerAdapter(EventHomePresenter presenter) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        clickListener = new CardOnClickListener();
        mPresenter = presenter;
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
        Log.d("CardPagerAdapter", "View Created");
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.event_category_item_revamp, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = view.findViewById(R.id.event_category_cardview);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
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
    }

    private void bind(CategoryItemsViewModel item, View view) {
        TextView tv1Price = view.findViewById(R.id.tv1_price);
        tv1Price.setText("Rp" + " " + CurrencyUtil.convertToCurrencyString(item.getSalesPrice()));
        TextView tvAddToWishlist = view.findViewById(R.id.tv_add_to_wishlist);
        tvAddToWishlist.setText(String.valueOf(item.getLikes()));
        tvAddToWishlist.setOnClickListener(clickListener);
        TextView tvEventShare = view.findViewById(R.id.tv_event_share);
        tvEventShare.setText(String.valueOf(item.getLikes()));
        tvEventShare.setOnClickListener(clickListener);
        ImageView ivEventThumb = view.findViewById(R.id.iv_event_thumb);
        ivEventThumb.setOnClickListener(clickListener);
        TextView tv3SoldCnt = view.findViewById(R.id.tv3_sold_cnt);
        tv3SoldCnt.setVisibility(View.GONE);
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

    private class CardOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_add_to_wishlist) {
                mPresenter.setEventLike(mData.get(currentDataIndex));
            } else if (id == R.id.tv_event_share) {

            } else if (id == R.id.iv_event_thumb ||
                    id == R.id.tv4_event_title || id == R.id.tv4_location ||
                    id == R.id.tv4_date_time) {
                mPresenter.showEventDetails(mData.get(currentDataIndex));
            }
        }
    }

}