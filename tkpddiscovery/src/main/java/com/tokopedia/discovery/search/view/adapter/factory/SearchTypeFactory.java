package com.tokopedia.discovery.search.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.autocomplete.viewmodel.AutoCompleteSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.CategorySearch;
import com.tokopedia.discovery.autocomplete.viewmodel.DigitalSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.InCategorySearch;
import com.tokopedia.discovery.autocomplete.viewmodel.PopularSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.RecentSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.ShopSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.TitleSearch;

/**
 * @author erry on 14/02/17.
 */

public interface SearchTypeFactory {

    int type(TitleSearch viewModel);

    int type(DigitalSearch viewModel);

    int type(CategorySearch viewModel);

    int type(InCategorySearch viewModel);

    int type(PopularSearch viewModel);

    int type(RecentSearch viewModel);

    int type(ShopSearch viewModel);

    int type(AutoCompleteSearch viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}