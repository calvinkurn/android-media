package com.tokopedia.seller.topads.keyword.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.view.adapter.TopAdsKeywordGroupListAdapter;

/**
 * Created by normansyahputa on 5/26/17.
 */

public class TopAdsKeywordGroupViewHolder extends RecyclerView.ViewHolder {
    private final CheckBox groupName;
    private TopAdsKeywordGroupListAdapter.Listener listener;

    public TopAdsKeywordGroupViewHolder(View itemView) {
        super(itemView);
        groupName = (CheckBox) itemView.findViewById(R.id.group_name_);
    }

    public TopAdsKeywordGroupListAdapter.Listener getListener() {
        return listener;
    }

    public void setListener(TopAdsKeywordGroupListAdapter.Listener listener) {
        this.listener = listener;
    }

    public void bindData(final GroupAd groupAd) {
        groupName.setText(groupAd.getName());

        if (listener.isSelection(groupAd)) {
            groupName.setChecked(true);
        }

        groupName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.notifySelect(groupAd);
            }
        });
    }
}
