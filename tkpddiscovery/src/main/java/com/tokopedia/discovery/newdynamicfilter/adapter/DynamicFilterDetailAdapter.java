package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/11/17.
 */

public class DynamicFilterDetailAdapter extends RecyclerView.Adapter<AbstractViewHolder<Option>>{

    List<Option> list;

    public DynamicFilterDetailAdapter() {
        this.list = new ArrayList<>();
    }

    @Override
    public AbstractViewHolder<Option> onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(getLayout(), parent, false);
        return getViewHolder(view);
    }

    protected int getLayout() {
        return R.layout.filter_detail_item;
    }

    protected AbstractViewHolder<Option> getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder<Option> holder, int position) {
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

    private static class ViewHolder extends AbstractViewHolder<Option> {

        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.filter_detail_item_checkbox);
        }

        public void bind(final Option option) {
            checkBox.setText(option.getName());
            checkBox.setOnCheckedChangeListener(null);
            if (!TextUtils.isEmpty(option.getInputState())) {
                checkBox.setChecked(Boolean.parseBoolean(option.getInputState()));
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    option.setInputState(Boolean.toString(isChecked));
                }
            });
        }
    }
}
