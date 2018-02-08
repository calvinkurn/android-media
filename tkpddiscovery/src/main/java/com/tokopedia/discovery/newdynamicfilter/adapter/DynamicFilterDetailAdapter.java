package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdynamicfilter.adapter.viewholder.DynamicFilterDetailViewHolder;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterDetailAdapter extends RecyclerView.Adapter<DynamicFilterDetailViewHolder>{

    protected DynamicFilterDetailView filterDetailView;

    public DynamicFilterDetailAdapter(DynamicFilterDetailView filterDetailView) {
        this.filterDetailView = filterDetailView;
    }

    private List<Option> list = new ArrayList<>();

    @Override
    public DynamicFilterDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(getLayout(), parent, false);
        return getViewHolder(view);
    }

    protected int getLayout() {
        return R.layout.filter_detail_item;
    }

    protected DynamicFilterDetailViewHolder getViewHolder(View view) {
        return new ViewHolder(view, filterDetailView);
    }

    @Override
    public void onBindViewHolder(DynamicFilterDetailViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOptionList(List<Option> optionList) {
        list = optionList;
        notifyDataSetChanged();
    }

    public void resetAllOptionsInputState() {
        for (Option option : list) {
            option.setInputState("");
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder extends DynamicFilterDetailViewHolder {

        public ViewHolder(View itemView, DynamicFilterDetailView filterDetailView) {
            super(itemView, filterDetailView);
        }

        @Override
        public void bind(final Option option) {
            super.bind(option);
            checkBox.setText(option.getName());
        }
    }
}
