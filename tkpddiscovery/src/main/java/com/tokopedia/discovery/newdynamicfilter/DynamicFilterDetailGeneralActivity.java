package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterDetailAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/31/17.
 */

public class DynamicFilterDetailGeneralActivity
        extends AbstractDynamicFilterDetailActivity<DynamicFilterDetailAdapter> {

    public static void moveTo(AppCompatActivity activity,
                              String pageTitle,
                              List<Option> optionList,
                              boolean isSearchable,
                              String searchHint) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterDetailGeneralActivity.class);
            intent.putExtra(EXTRA_PAGE_TITLE, pageTitle);
            intent.putParcelableArrayListExtra(EXTRA_OPTION_LIST, new ArrayList<>(optionList));
            intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable);
            intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected DynamicFilterDetailAdapter getAdapter() {
        return new DynamicFilterDetailAdapter(this);
    }

    @Override
    protected void loadFilterItems(List<Option> options) {
        adapter.setOptionList(options);
    }

    @Override
    protected void resetFilter() {
        super.resetFilter();
        adapter.resetAllOptionsInputState();
    }
}
