package com.tokopedia.session.changephonenumber.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningItemViewModel;

import java.util.List;

/**
 * Created by milhamj on 19/12/17.
 */

public class WarningListAdapter extends RecyclerView.Adapter<WarningListAdapter.WarningListViewHolder> {
    private List<WarningItemViewModel> warningList;

    public WarningListAdapter(List<WarningItemViewModel> warningList) {
        this.warningList = warningList;
    }

    @Override
    public WarningListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_change_phone_number_warning, viewGroup, false);
        return new WarningListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WarningListViewHolder warningListViewHolder, int i) {
        WarningItemViewModel warningItemViewModel = warningList.get(i);
        warningListViewHolder.warning.setText(warningItemViewModel.getWarning());
        warningListViewHolder.suggestion.setText(warningItemViewModel.getSuggestion());
        warningListViewHolder.note.setText(warningItemViewModel.getNote());
        if (i == getItemCount() - 1) {
            warningListViewHolder.separator.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return warningList.size();
    }

    public class WarningListViewHolder extends RecyclerView.ViewHolder {
        TextView warning;
        TextView suggestion;
        TextView note;
        View separator;

        public WarningListViewHolder(View itemView) {
            super(itemView);
            warning = itemView.findViewById(R.id.warning);
            suggestion = itemView.findViewById(R.id.suggestion);
            note = itemView.findViewById(R.id.note);
            separator = itemView.findViewById(R.id.separator);
        }
    }
}
