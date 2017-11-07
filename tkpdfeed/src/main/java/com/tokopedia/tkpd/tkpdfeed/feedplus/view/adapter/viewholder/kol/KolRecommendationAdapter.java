package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolRecommendItemViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolRecommendationViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 10/30/17.
 */

public class KolRecommendationAdapter extends RecyclerView.Adapter<KolRecommendationAdapter
        .ViewHolder> {

    private final FeedPlus.View.Kol kolViewListener;
    private KolRecommendationViewModel data;

    private ImageView avatar;
    private TextView name;
    private TextView label;
    private TextView followButton;
    private View mainView;

    public KolRecommendationAdapter(FeedPlus.View.Kol kolViewListener) {
        this.kolViewListener = kolViewListener;
        ArrayList<KolRecommendItemViewModel> list = new ArrayList<>();
        this.data = new KolRecommendationViewModel("", "", list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
                    kolViewListener.onGoToKolProfile(data.getPage(),
                            data.getRowNumber(),
                            data.getListRecommend().get(getAdapterPosition()).getUrl());
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kolViewListener.onGoToKolProfile(data.getPage(),
                            data.getRowNumber(),
                            data.getListRecommend().get(getAdapterPosition()).getUrl());
                }
            });

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kolViewListener.onGoToKolProfile(data.getPage(),
                            data.getRowNumber(),
                            data.getListRecommend().get(getAdapterPosition()).getUrl());
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kol_recommendation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageHandler.LoadImage(avatar, data.getListRecommend().get(position).getImageUrl());
        name.setText(MethodChecker.fromHtml(data.getListRecommend().get(position).getName()));
        label.setText(data.getListRecommend().get(position).getLabel());

        if (data.getListRecommend().get(position).isFollowed()) {
            followButton.setText(MainApplication.getAppContext().getString(R.string.following));
            MethodChecker.setBackground(followButton, MethodChecker.getDrawable(MainApplication
                    .getAppContext(), R.drawable.btn_share_transaparent));
            followButton.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R
                    .color.tkpd_main_green));
        } else {
            followButton.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(), R
                    .color.white));
            followButton.setText(MainApplication.getAppContext().getString(R.string.action_follow_english));
            MethodChecker.setBackground(followButton, MethodChecker.getDrawable(MainApplication
                    .getAppContext(), R.drawable.green_button_rounded_unify));
        }

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followButton.getText().equals(MainApplication.getAppContext().getString(R.string
                        .action_follow_english))) {
                    kolViewListener.onFollowKolFromRecommendationClicked(data.getPage(),
                            data.getRowNumber(),
                            data.getListRecommend().get(position).getId(),
                            position);
                } else {
                    kolViewListener.onUnfollowKolFromRecommendationClicked(data.getPage(),
                            data.getRowNumber(),
                            data.getListRecommend().get(position).getId(),
                            position);
                }
            }
        });
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
