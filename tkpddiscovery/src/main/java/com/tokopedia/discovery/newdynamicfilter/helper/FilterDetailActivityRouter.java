package com.tokopedia.discovery.newdynamicfilter.helper;

import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterCategoryActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterColorActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterDetailActivity;

import static com.tokopedia.core.discovery.model.Filter.TEMPLATE_NAME_CATEGORY;
import static com.tokopedia.core.discovery.model.Filter.TEMPLATE_NAME_COLOR;

/**
 * Created by henrypriyono on 8/16/17.
 */

public class FilterDetailActivityRouter {

    public static void launchDetailActivity(AppCompatActivity activity, Filter filter) {
        switch (filter.getTemplateName()) {
            case TEMPLATE_NAME_COLOR:
                DynamicFilterColorActivity
                        .moveTo(activity,
                                filter.getOptions(),
                                filter.getSearch().getSearchable() == 1,
                                filter.getSearch().getPlaceholder());
                break;
            case TEMPLATE_NAME_CATEGORY:
                DynamicFilterCategoryActivity
                        .moveTo(activity,
                                filter.getOptions());
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
