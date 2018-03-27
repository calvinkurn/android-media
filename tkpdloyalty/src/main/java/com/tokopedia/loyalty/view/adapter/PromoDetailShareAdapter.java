package com.tokopedia.loyalty.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.data.ShareSocialViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 27/03/18
 */

public class PromoDetailShareAdapter extends RecyclerView.Adapter<PromoDetailShareAdapter.ShareSocialViewHolder> {

    private List<ShareSocialViewModel> shareSocialList;

    public void setShareSocialList(List<ShareSocialViewModel> shareSocialList) {
        this.shareSocialList = new ArrayList<>(shareSocialList);
    }

    @Override
    public ShareSocialViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_view_share_social, viewGroup, false);
        return new ShareSocialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShareSocialViewHolder viewHolder, int position) {
        ShareSocialViewModel holderData = shareSocialList.get(position);

        viewHolder.ibShareSocialIcon.setImageResource(holderData.getResource());
        viewHolder.tvShareSocialName.setText(holderData.getName());
    }

    @Override
    public int getItemCount() {
        return shareSocialList.size();
    }

    static class ShareSocialViewHolder extends RecyclerView.ViewHolder {

        private ImageButton ibShareSocialIcon;
        private TextView tvShareSocialName;

        ShareSocialViewHolder(View itemView) {
            super(itemView);

            ibShareSocialIcon = itemView.findViewById(R.id.ib_share_social_icon);
            tvShareSocialName = itemView.findViewById(R.id.tv_share_social_name);
        }
    }
}
