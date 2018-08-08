package com.tokopedia.discovery.newdynamicfilter.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.network.apiservices.product.apis.DynamicFilter;
import com.tokopedia.design.list.adapter.SectionDividedItemAdapter;
import com.tokopedia.design.list.adapter.SectionTitleDictionary;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.model.OptionWrapper;
import com.tokopedia.discovery.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/31/17.
 */

public class DynamicFilterBrandAdapter extends SectionDividedItemAdapter<OptionWrapper> {

    private SectionTitleDictionary sectionTitleDictionary = new SectionTitleDictionary();
    private DynamicFilterDetailView filterDetailView;

    public DynamicFilterBrandAdapter(DynamicFilterDetailView filterDetailView) {
        this.filterDetailView = filterDetailView;
    }

    public void setOptionList(List<Option> optionList) {
        setItemList(wrapOptionList(optionList));
    }

    private List<OptionWrapper> wrapOptionList(List<Option> optionList) {
        List<OptionWrapper> optionWrapperList = new ArrayList<>();
        for (Option option : optionList) {
            optionWrapperList.add(new OptionWrapper(option));
        }
        return optionWrapperList;
    }

    public void resetAllOptionsInputState() {
        for (OptionWrapper item : itemList) {
            item.getOption().setInputState("");
        }
        notifyDataSetChanged();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.filter_detail_item;
    }

    @Override
    protected SectionDividedItemAdapter.ViewHolder<OptionWrapper> getViewHolder(View itemView) {
        return new BrandViewHolder(itemView, filterDetailView, sectionTitleDictionary);
    }

    private static class BrandViewHolder extends SectionDividedItemAdapter.ViewHolder<OptionWrapper> {

        private final DynamicFilterDetailView filterDetailView;
        CheckBox checkBox;

        BrandViewHolder(View itemView, DynamicFilterDetailView filterDetailView,
                        SectionTitleDictionary sectionTitleDictionary) {

            super(itemView, sectionTitleDictionary);
            this.filterDetailView = filterDetailView;
        }

        @Override
        protected void initItem(View itemView) {
            checkBox = (CheckBox) itemView.findViewById(R.id.filter_detail_item_checkbox);
        }

        @Override
        protected void bindItem(OptionWrapper item) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                }
            });
            OptionHelper.bindOptionWithCheckbox(item.getOption(), checkBox, filterDetailView);
            checkBox.setText(item.getOption().getName());
        }
    }
}
