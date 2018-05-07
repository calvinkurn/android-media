package com.tokopedia.tokocash.historytokocash.presentation.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.model.HelpHistoryTokoCash;

import java.util.List;

/**
 * Created by nabillasabbaha on 10/17/17.
 */

public class HelpHistoryAdapter extends ArrayAdapter<HelpHistoryTokoCash> {

    private static final String HINT_HELP_CATEGORY = "Pilih Salah Satu";

    private List<HelpHistoryTokoCash> helpHistoryTokoCashList;
    private LayoutInflater inflater;

    public HelpHistoryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<HelpHistoryTokoCash> objects) {
        super(context, resource, objects);
        this.helpHistoryTokoCashList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.helpHistoryTokoCashList.add(new HelpHistoryTokoCash(HINT_HELP_CATEGORY));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HelpHistoryTokoCash helpHistoryTokoCash = helpHistoryTokoCashList.get(position);
        HelpCategoryViewHolder helpCategoryViewHolder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_help_history, parent, false);
            helpCategoryViewHolder = new HelpCategoryViewHolder(view);
            view.setTag(helpCategoryViewHolder);
        } else {
            helpCategoryViewHolder = (HelpCategoryViewHolder) view.getTag();
        }

        helpCategoryViewHolder.helpCategory.setText(helpHistoryTokoCash.getTranslation());
        return view;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }

    static class HelpCategoryViewHolder {

        private TextView helpCategory;

        public HelpCategoryViewHolder(View view) {
            helpCategory = view.findViewById(R.id.help_category);
        }
    }
}