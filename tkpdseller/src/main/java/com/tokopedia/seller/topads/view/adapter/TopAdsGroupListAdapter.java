package com.tokopedia.seller.topads.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/14/16.
 */

public class TopAdsGroupListAdapter extends TopAdsListAdapter<GroupAd> {
    public TopAdsGroupListAdapter(Context context, List<GroupAd> data) {
        super(context, data);
    }

    @Override
    public int getAdType() {
        return TopAdsListAdapter.AD_GROUP_TYPE;
    }

    @Override
    public void bindDataAds(final int position, RecyclerView.ViewHolder viewHolder) {
        GroupAd groupAd = data.get(position);
        final TopAdsViewHolder topAdsViewHolder = (TopAdsViewHolder) viewHolder;
        if (groupAd != null) {
            topAdsViewHolder.titleProduct.setText(groupAd.getName());
            topAdsViewHolder.statusActive.setText(String.valueOf(groupAd.getStatus()));
            topAdsViewHolder.promoPriceUsed.setText(groupAd.getPriceBidFmt());
            topAdsViewHolder.totalPricePromo.setText(groupAd.getPriceDailyFmt());
        }
        topAdsViewHolder.checkedPromo.setChecked(isChecked(position));
        topAdsViewHolder.checkedPromo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setChecked(position, isChecked);
                if (multiSelector.getSelectedPositions().size() == 0) {
//                    topAdsListPromoViewListener.finishActionMode();
                    multiSelector.refreshAllHolders();
                } else {
//                    topAdsListPromoViewListener.setTitleMode(multiSelector.getSelectedPositions().size() + "");
                }
            }
        });
        topAdsViewHolder.mainView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (multiSelector.getSelectedPositions().size() == 0) {
//                    topAdsListPromoViewListener.startSupportActionMode(selectionMode);
                    multiSelector.setSelected(topAdsViewHolder, true);
                } else {
                    multiSelector.tapSelection(position, topAdsViewHolder.getItemId());
//                    topAdsListPromoViewListener.setTitleMode(multiSelector.getSelectedPositions().size() + "");
                    if (multiSelector.getSelectedPositions().size() == 0) {
//                        topAdsListPromoViewListener.finishActionMode();
                        multiSelector.refreshAllHolders();
                    }
                }
                setChecked(position, multiSelector.isSelected(position, topAdsViewHolder.getItemId()));
                return true;
            }
        });
        topAdsViewHolder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!multiSelector.tapSelection(topAdsViewHolder)) {
                    /*if (isLoading()) {
                        getPaging().setPage(getPaging().getPage() - 1);
                        presenter.onFinishConnection();
                    }*/
//                    topAdsListPromoViewListener.moveToDetail(position);
                } else {
                    setChecked(position, multiSelector.isSelected(position, topAdsViewHolder.getItemId()));
                    if (multiSelector.getSelectedPositions().size() == 0) {
//                        topAdsListPromoViewListener.finishActionMode();
                        multiSelector.refreshAllHolders();
                    } else {
//                        topAdsListPromoViewListener.setTitleMode(multiSelector.getSelectedPositions().size() + "");
                    }
                }
            }
        });
    }

    @Override
    public List<GroupAd> getSelectedAds() {
        List<GroupAd> selectedAds = new ArrayList<>();
        for(int position : multiSelector.getSelectedPositions()){
            selectedAds.add(data.get(position));
        }
        return selectedAds;
    }

}
