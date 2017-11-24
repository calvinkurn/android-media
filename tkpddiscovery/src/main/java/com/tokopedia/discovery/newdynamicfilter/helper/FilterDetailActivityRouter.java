package com.tokopedia.discovery.newdynamicfilter.helper;

import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterCategoryActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterColorActivity;
import com.tokopedia.discovery.newdynamicfilter.AbstractDynamicFilterDetailActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterDetailBrandActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterDetailGeneralActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterLocationActivity;
import com.tokopedia.discovery.newdynamicfilter.DynamicFilterRatingActivity;

/**
 * Created by henrypriyono on 8/16/17.
 */

public class FilterDetailActivityRouter {

    public static void launchDetailActivity(AppCompatActivity activity, Filter filter) {
        if (filter.isColorFilter()) {
            DynamicFilterColorActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder());

        } else if (filter.isRatingFilter()) {
            DynamicFilterRatingActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder());

        } else if (filter.isBrandFilter()) {
            DynamicFilterDetailBrandActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder());

        } else if (filter.isLocationFilter()) {
            DynamicFilterLocationActivity
                    .moveTo(activity,
                            filter.getTitle(),
                            filter.getOptions(),
                            filter.getSearch().getSearchable() == 1,
                            filter.getSearch().getPlaceholder());

        } else {
            DynamicFilterDetailGeneralActivity
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
}
