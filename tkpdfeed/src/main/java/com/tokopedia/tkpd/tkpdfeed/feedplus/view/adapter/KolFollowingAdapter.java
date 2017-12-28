package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingAdapter extends RecyclerView.Adapter<KolFollowingAdapter.Holder> {

    private Context context;
    private List<KolFollowingViewModel> itemList = new ArrayList<>();

    public KolFollowingAdapter(Context context) {
        this.context = context;
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
        KolFollowingViewModel viewModel = itemList.get(position);
        initView(holder, viewModel);
        initData(holder, viewModel);
    }

    private void initView(Holder holder, KolFollowingViewModel viewModel) {
        holder.ivVerified.setVisibility(viewModel.isVerified() ? View.VISIBLE : View.GONE);
    }

    private void initData(Holder holder, KolFollowingViewModel viewModel) {
        ImageHandler.loadImageCircle2(context, holder.ivAvatar, viewModel.getAvatarUrl());
        holder.tvName.setText(MethodChecker.fromHtml(viewModel.getName()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivVerified;
        TextView tvName;
        public Holder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            ivVerified = (ImageView) itemView.findViewById(R.id.iv_verified);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
