package com.tokopedia.session.changephonenumber.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.session.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by milhamj on 19/12/17.
 */

public class WarningListAdapter extends RecyclerView.Adapter<WarningListAdapter
        .WarningListViewHolder> {
    private List<String> warningList;

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
        String warning = warningList.get(i);
        if (!isNullOrEmpty(warning)) {
            CharSequence warningHtml = MethodChecker.fromHtml(warning);
            warningHtml = trimTrailingWhitespace(warningHtml);
            warningListViewHolder.warning.setText(warningHtml);
            warningListViewHolder.warning.setVisibility(View.VISIBLE);
        } else {
            warningListViewHolder.warning.setVisibility(View.GONE);
        }
        warningListViewHolder.separator.setVisibility(isLastItem(i) ? View.GONE : View.VISIBLE);
    }

    private boolean isNullOrEmpty(String string) {
        return (string == null || string.equalsIgnoreCase("null") || string.isEmpty());
    }

    private boolean isLastItem(int position) {
        return (position == getItemCount() - 1);
    }

    public void addData(List<String> warningList) {
        this.warningList.addAll(warningList);
        notifyItemRangeInserted(0, warningList.size());
    }

    private CharSequence trimTrailingWhitespace(CharSequence source) {
        if (source == null)
            return "";

        int i = source.length();

        // loop back to the first non-whitespace character
        while (--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        return source.subSequence(0, i + 1);
    }

    @Override
    public int getItemCount() {
        return warningList.size();
    }

    public class WarningListViewHolder extends RecyclerView.ViewHolder {
        TextView warning;
        View separator;

        public WarningListViewHolder(View itemView) {
            super(itemView);
            warning = itemView.findViewById(R.id.warning);
            separator = itemView.findViewById(R.id.separator);
        }
    }
}
