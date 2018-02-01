package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolFollowingList;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingAdapter extends RecyclerView.Adapter<KolFollowingAdapter.Holder> {

    private Context context;
    private List<KolFollowingViewModel> itemList = new ArrayList<>();
    private KolFollowingList.View mainView;

    public KolFollowingAdapter(Context context, KolFollowingList.View view) {
        this.context = context;
        this.mainView = view;
    }

    public List<KolFollowingViewModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<KolFollowingViewModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_kol_following, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final KolFollowingViewModel viewModel = itemList.get(position);
        initView(holder, viewModel);
        initData(holder, viewModel);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.onListItemClicked(viewModel);
            }
        });
    }

    private void initView(Holder holder, KolFollowingViewModel viewModel) {
        holder.layout.setVisibility(viewModel.isLoadingItem() ? View.GONE : View.VISIBLE);
        holder.progressBar.setVisibility(viewModel.isLoadingItem()? View.VISIBLE : View.GONE);
        holder.ivVerified.setVisibility(viewModel.isInfluencer() ? View.VISIBLE : View.GONE);
    }

    private void initData(Holder holder, KolFollowingViewModel viewModel) {
        if (!viewModel.isLoadingItem()) {
            ImageHandler.loadImageCircle2(context, holder.ivAvatar, viewModel.getAvatarUrl());
            holder.tvName.setText(MethodChecker.fromHtml(viewModel.getName()));
        }
    }

    public void addBottomLoading() {
        getItemList().add(new KolFollowingViewModel(true));
        notifyDataSetChanged();
    }

    public void removeBottomLoading() {
        getItemList().remove(getItemCount() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivVerified;
        View layout;
        TextView tvName;
        ProgressBar progressBar;
        public Holder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            ivVerified = (ImageView) itemView.findViewById(R.id.iv_verified);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            layout = itemView.findViewById(R.id.layout);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
