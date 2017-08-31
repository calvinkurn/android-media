package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterDetailAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterDetailSizeAdapter;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by henrypriyono on 8/28/17.
 */

public class DynamicFilterSizeActivity extends DynamicFilterDetailGeneralActivity {
    @Override
    protected DynamicFilterDetailAdapter getAdapter() {
        return new DynamicFilterDetailSizeAdapter(this);
    }

    @Override
    public void onItemCheckedChanged(Option option, boolean isChecked) {
        super.onItemCheckedChanged(option, isChecked);
        syncOtherItemWithSameValue(option);
        getAdapter().notifyItemRangeChanged(0, getAdapter().getItemCount());
    }

    private void syncOtherItemWithSameValue(Option sourceItem) {
        for (Option otherItem : optionList) {
            if (otherItem.getValue().equals(sourceItem.getValue())) {
                otherItem.setInputState(sourceItem.getInputState());
            }
        }
    }

    public static void moveTo(AppCompatActivity activity,
                              String pageTitle,
                              List<Option> optionList,
                              boolean isSearchable,
                              String searchHint) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterSizeActivity.class);
            intent.putExtra(EXTRA_PAGE_TITLE, pageTitle);
            intent.putExtra(EXTRA_OPTION_LIST, Parcels.wrap(optionList));
            intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable);
            intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }
}
