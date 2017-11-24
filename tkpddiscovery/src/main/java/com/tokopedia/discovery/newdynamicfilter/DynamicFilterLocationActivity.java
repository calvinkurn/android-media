package com.tokopedia.discovery.newdynamicfilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.newdynamicfilter.helper.DynamicFilterDbManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 11/24/17.
 */

public class DynamicFilterLocationActivity extends DynamicFilterDetailGeneralActivity {
    @Override
    protected void retrieveOptionListData() {
        String data = new DynamicFilterDbManager()
                .getValueString(getIntent().getStringExtra(EXTRA_OPTION_LIST));
        Type listType = new TypeToken<List<Option>>() {}.getType();
        optionList = new Gson().fromJson(data, listType);
    }

    public static void moveTo(AppCompatActivity activity,
                              String pageTitle,
                              String templateName,
                              boolean isSearchable,
                              String searchHint) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterLocationActivity.class);
            intent.putExtra(EXTRA_PAGE_TITLE, pageTitle);
            intent.putExtra(EXTRA_OPTION_LIST, templateName);
            intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable);
            intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }
}
