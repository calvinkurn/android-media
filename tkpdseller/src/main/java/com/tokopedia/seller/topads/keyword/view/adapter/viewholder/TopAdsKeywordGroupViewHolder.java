package com.tokopedia.seller.topads.keyword.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.data.model.data.GroupAd;

/**
 * Created by normansyahputa on 5/26/17.
 */

public class TopAdsKeywordGroupViewHolder extends RecyclerView.ViewHolder {
    private final TextView groupName;

    public TopAdsKeywordGroupViewHolder(View itemView) {
        super(itemView);
        groupName = (TextView) itemView.findViewById(R.id.group_name_);
    }

    public void bindData(final GroupAd groupAd) {
        groupName.setText(groupAd.getName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupAd != null) {
                    // do something in here
                }
            }
        });
    }
}
