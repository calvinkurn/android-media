package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterDetailAdapter;
import com.tokopedia.discovery.newdynamicfilter.adapter.DynamicFilterDetailColorAdapter;
import com.tokopedia.discovery.newdynamicfilter.view.DynamicFilterDetailView;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by henrypriyono on 8/16/17.
 */

public class DynamicFilterColorActivity extends DynamicFilterDetailActivity {

    @Override
    protected DynamicFilterDetailAdapter getAdapter() {
        return new DynamicFilterDetailColorAdapter(this);
    }

    public static void moveTo(AppCompatActivity activity,
                              String pageTitle,
                              List<Option> optionList,
                              boolean isSearchable,
                              String searchHint) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterColorActivity.class);
            intent.putExtra(EXTRA_PAGE_TITLE, pageTitle);
            intent.putExtra(EXTRA_OPTION_LIST, Parcels.wrap(optionList));
            intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable);
            intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }
}
