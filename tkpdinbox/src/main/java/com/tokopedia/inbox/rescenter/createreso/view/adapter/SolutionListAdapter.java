package com.tokopedia.inbox.rescenter.createreso.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListAdapterListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import java.util.ArrayList;
import java.util.List;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * Created by yoasfs on 25/08/17.
 */

public class SolutionListAdapter extends RecyclerView.Adapter<SolutionListAdapter.ItemHolder> {

    private Context context;
    private List<SolutionViewModel> solutionViewModelList = new ArrayList<>();
    private SolutionListAdapterListener listener;

    public SolutionListAdapter(Context context, List<SolutionViewModel> solutionViewModelList, SolutionListAdapterListener listener) {
        this.context = context;
        this.solutionViewModelList = solutionViewModelList;
        this.listener = listener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_solution_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        final SolutionViewModel solutionViewModel = solutionViewModelList.get(position);
        holder.tvSolution.setText(solutionViewModel.getName());
        holder.cardView.setOnClickListener(view -> listener.onItemClicked(solutionViewModel));
        holder.ivCheck.setVisibility(listener.showCheckOnItem(solutionViewModel.getId()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return solutionViewModelList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvSolution;
        ImageView ivCheck;

        public ItemHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card);
            tvSolution = (TextView) itemView.findViewById(R.id.tv_solution);
            ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
            ivCheck.setBackground(MethodChecker.getDrawable(ivCheck.getContext(), R.drawable.ic_check));
        }
    }
}
