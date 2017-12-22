package com.tokopedia.session.changephonenumber.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningItemViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by milhamj on 19/12/17.
 */

public class WarningListAdapter extends RecyclerView.Adapter<WarningListAdapter.WarningListViewHolder> {
    private List<WarningItemViewModel> warningList;

    @Inject
    public WarningListAdapter() {
        warningList = new ArrayList<>();
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
        if (!isNullOrEmpty(warningItemViewModel.getWarning())) {
            warningListViewHolder.warning.setText(warningItemViewModel.getWarning());
            warningListViewHolder.warning.setVisibility(View.VISIBLE);
        } else {
            warningListViewHolder.warning.setVisibility(View.GONE);
        }

        if (!isNullOrEmpty(warningItemViewModel.getSuggestion())) {
            warningListViewHolder.suggestion.setText(warningItemViewModel.getSuggestion());
            warningListViewHolder.suggestion.setVisibility(View.VISIBLE);
        } else {
            warningListViewHolder.suggestion.setVisibility(View.GONE);
        }

        if (!isNullOrEmpty(warningItemViewModel.getNote())) {
            warningListViewHolder.note.setText(warningItemViewModel.getNote());
            warningListViewHolder.note.setVisibility(View.VISIBLE);
        } else {
            warningListViewHolder.note.setVisibility(View.GONE);
        }

        warningListViewHolder.separator.setVisibility((i == getItemCount() - 1) ? View.GONE : View.VISIBLE);
    }

    private boolean isNullOrEmpty(String string) {
        return (string == null || string.equalsIgnoreCase("null") || string.isEmpty());
    }

    public void addData(List<WarningItemViewModel> warningList) {
        this.warningList.addAll(warningList);
        notifyItemRangeInserted(0, warningList.size());
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
