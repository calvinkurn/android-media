package com.tokopedia.inbox.rescenter.inboxv2.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.ResoInboxSortModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.SortModel;

/**
 * Created by yfsx on 26/01/18.
 */

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.Holder> {
    private Context context;
    private ResoInboxFragmentListener.View mainView;
    private ResoInboxSortModel inboxSortModel;

    public SortAdapter(ResoInboxFragmentListener.View mainView, ResoInboxSortModel inboxSortModel) {
        this.mainView = mainView;
        this.inboxSortModel = inboxSortModel;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_sort, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        SortModel sortModel = inboxSortModel.getSortModelList().get(pos);
        bindView(holder, sortModel);
        bindViewListener(holder, sortModel);
    }

    private void bindView(Holder holder, SortModel sortModel) {
        holder.tvSortName.setText(sortModel.getSortName());
        holder.tvSortName.setTextColor(
                context.getResources().getColor(
                        sortModel.getSortId() == inboxSortModel.getSelectedSortId() ?
                                R.color.tkpd_main_green :
                                R.color.black_70));
        holder.ivCheck.setVisibility(sortModel.getSortId() == inboxSortModel.getSelectedSortId() ?
                View.VISIBLE :
                View.GONE);
    }

    private void bindViewListener(Holder holder, final SortModel sortModel) {
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.onSortItemClicked(sortModel);
            }
        });
    }


    @Override
    public int getItemCount() {
        return inboxSortModel.getSortModelList().size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tvSortName;
        ImageView ivCheck;
        View mainView;

        public Holder(View itemView) {
            super(itemView);
            mainView = itemView.findViewById(R.id.mainView);
            tvSortName = (TextView) itemView.findViewById(R.id.tv_sort);
            ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
        }
    }
}
