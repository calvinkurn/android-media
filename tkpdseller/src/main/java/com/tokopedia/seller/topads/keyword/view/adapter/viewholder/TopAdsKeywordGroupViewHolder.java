package com.tokopedia.seller.topads.keyword.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordGroupListAdapter;

/**
 * @author normansyahputa on 5/26/17.
 */

public class TopAdsKeywordGroupViewHolder extends RecyclerView.ViewHolder {
    private final LinearLayout groupNameContainer;
    private final TextView groupNameText;
    private TopAdsKeywordGroupListAdapter.Listener listener;
    private GroupAd groupAd;

    public TopAdsKeywordGroupViewHolder(View itemView) {
        super(itemView);
        groupNameContainer = (LinearLayout) itemView.findViewById(R.id.group_name_container);
        groupNameText = (TextView) itemView.findViewById(R.id.text_group_name_);

        groupNameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupAd != null) {
                    groupAd.setSelected(!groupAd.isSelected());
                    if (groupAd.isSelected()) {
                        listener.notifySelect(groupAd);
                    }
                }
            }
        });
    }

    public TopAdsKeywordGroupListAdapter.Listener getListener() {
        return listener;
    }

    public void setListener(TopAdsKeywordGroupListAdapter.Listener listener) {
        this.listener = listener;
    }

    public void bindData(final GroupAd groupAd) {
        this.groupAd = groupAd;
        groupNameText.setText(groupAd.getName());
    }
}
