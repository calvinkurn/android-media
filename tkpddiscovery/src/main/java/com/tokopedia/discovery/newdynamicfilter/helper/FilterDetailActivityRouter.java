package com.tokopedia.discovery.newdynamicfilter.helper;

import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterColorActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterDetailActivity;

/**
 * Created by henrypriyono on 8/16/17.
 */

public class FilterDetailActivityRouter {
    public static final String COLOR_TEMPLATE_NAME = "template_color";

    public static void launchDetailActivity(AppCompatActivity activity, Filter filter) {
        switch (filter.getTemplateName()) {
            case COLOR_TEMPLATE_NAME:
                DynamicFilterColorActivity
                        .moveTo(activity,
                                filter.getOptions(),
                                filter.getSearch().getSearchable() == 1,
                                filter.getSearch().getPlaceholder());
                break;
            default:
                DynamicFilterDetailActivity
                        .moveTo(activity,
                                filter.getOptions(),
                                filter.getSearch().getSearchable() == 1,
                                filter.getSearch().getPlaceholder());
        }
    }
}
