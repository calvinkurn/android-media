package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.KolRecommendationItemDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.analytics.KolTracking;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolRecommendItemViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolRecommendationViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 10/30/17.
 */

public class KolRecommendationAdapter extends RecyclerView.Adapter<KolRecommendationAdapter
        .ViewHolder> {

    private final FeedPlus.View.Kol kolViewListener;
    private KolRecommendationViewModel data;

    public KolRecommendationAdapter(FeedPlus.View.Kol kolViewListener) {
        this.kolViewListener = kolViewListener;
        ArrayList<KolRecommendItemViewModel> list = new ArrayList<>();
        this.data = new KolRecommendationViewModel("", "", "", list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView name;
        private TextView label;
        private TextView followButton;
        private View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            name = (TextView) itemView.findViewById(R.id.name);
            label = (TextView) itemView.findViewById(R.id.label);
            followButton = (TextView) itemView.findViewById(R.id.follow_button);
            mainView = itemView.findViewById(R.id.main_view);

            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToProfilePage(getAdapterPosition());
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToProfilePage(getAdapterPosition());
                }
            });

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToProfilePage(getAdapterPosition());
                }
            });

            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KolRecommendItemViewModel kolItem = data.getListRecommend().get(getAdapterPosition());
                    if (kolItem.isFollowed()) {
                        UnifyTracking.eventKolRecommendationUnfollowClick(kolItem.getLabel(), kolItem.getName());
                        kolViewListener.onUnfollowKolFromRecommendationClicked(data.getPage(),
                                data.getRowNumber(),
                                kolItem.getId(),
                                getAdapterPosition());
                        kolItem.setFollowed(false);
                        notifyItemChanged(getAdapterPosition());
                    } else {
                        UnifyTracking.eventKolRecommendationFollowClick(kolItem.getLabel(), kolItem.getName());

                        List<KolTracking.Promotion> list = new ArrayList<>();
                        KolRecommendItemViewModel recItem = data.getListRecommend().get(getAdapterPosition());
                        list.add(new KolTracking.Promotion(
                                recItem.getId(),
                                KolTracking.Promotion.createContentNameRecommendation(),
                                recItem.getName().equals("") ? "-" : recItem.getName(),
                                data.getRowNumber(),
                                recItem.getLabel().equals("") ? "-" : recItem.getLabel(),
                                recItem.getId(),
                                recItem.getUrl().equals("") ? "-" : recItem.getUrl()));
                        TrackingUtils.eventTrackingEnhancedEcommerce(KolTracking
                                .getKolClickTracking(list,
                                        Integer.parseInt(SessionHandler.getLoginID(MainApplication.getAppContext()))
                                ));

                        kolViewListener.onFollowKolFromRecommendationClicked(data.getPage(),
                                data.getRowNumber(),
                                kolItem.getId(),
                                getAdapterPosition());
                        kolItem.setFollowed(true);
                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });

        }
    }

    private void navigateToProfilePage(int adapterPosition) {
        KolRecommendItemViewModel kolItem = data.getListRecommend().get(adapterPosition);
        UnifyTracking.eventKolRecommendationGoToProfileClick(kolItem.getLabel(), kolItem.getName());
        kolViewListener.onGoToKolProfile(data.getPage(),
                data.getRowNumber(),
                kolItem.getUrl());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kol_recommendation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageHandler.LoadImage(holder.avatar, data.getListRecommend().get(position).getImageUrl());
        holder.name.setText(MethodChecker.fromHtml(data.getListRecommend().get(position).getName()));
        holder.label.setText(data.getListRecommend().get(position).getLabel());

        setFollowing(holder, position);

    }

    private void setFollowing(ViewHolder holder, int position) {
        if (data.getListRecommend().get(position).isFollowed()) {
            holder.followButton.setText(MainApplication.getAppContext().getString(R.string.following));
            MethodChecker.setBackground(holder.followButton, MethodChecker.getDrawable(MainApplication
                    .getAppContext(), R.drawable.btn_share_transaparent));
            holder.followButton.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R
                    .color.tkpd_main_green));
        } else {
            holder.followButton.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R
                    .color.white));
            holder.followButton.setText(MainApplication.getAppContext().getString(R.string.action_follow_english));
            MethodChecker.setBackground(holder.followButton, MethodChecker.getDrawable(MainApplication
                    .getAppContext(), R.drawable.green_button_rounded_unify));
        }
    }

    @Override
    public int getItemCount() {
        return data.getListRecommend().size();
    }

    public void setData(KolRecommendationViewModel data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public KolRecommendationViewModel getData() {
        return data;
    }


}
