package com.tokopedia.discovery.newdynamicfilter.helper;

import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterCategoryActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterColorActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterDetailActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterRatingActivity;

import static com.tokopedia.core.discovery.model.Filter.TEMPLATE_NAME_CATEGORY;
import static com.tokopedia.core.discovery.model.Filter.TEMPLATE_NAME_COLOR;
import static com.tokopedia.core.discovery.model.Filter.TITLE_RATING;

/**
 * Created by henrypriyono on 8/16/17.
 */

public class FilterDetailActivityRouter {

    public static void launchDetailActivity(AppCompatActivity activity, Filter filter) {
        if (TEMPLATE_NAME_COLOR.equals(filter.getTemplateName())) {
            DynamicFilterColorActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder());

        } else if (TITLE_RATING.equals(filter.getTitle())) {
            DynamicFilterRatingActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder());

        } else {
            DynamicFilterDetailActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder());
        }
    }

    public static void launchCategoryActivity(AppCompatActivity activity,
                                              Filter filter,
                                              String defaultCategoryRootId,
                                              String defaultCategoryId) {
        DynamicFilterCategoryActivity
                .moveTo(activity,
                        filter.getOptions(),
                        defaultCategoryRootId,
                        defaultCategoryId);
    }

    public static boolean isCategoryFilter(Filter filter) {
        return TEMPLATE_NAME_CATEGORY.equals(filter.getTemplateName());
    }
}
