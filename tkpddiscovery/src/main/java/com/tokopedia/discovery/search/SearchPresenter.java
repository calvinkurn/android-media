package com.tokopedia.discovery.search;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.usecase.AutoCompleteUseCase;
import com.tokopedia.discovery.autocomplete.usecase.DeleteRecentSearchUseCase;
import com.tokopedia.discovery.search.subscriber.SearchSubscriber;
import com.tokopedia.discovery.search.view.SearchContract;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * @author erry on 23/02/17.
 */

public class SearchPresenter extends BaseDaggerPresenter<SearchContract.View>
        implements SearchContract.Presenter {

    private static final String TAG = SearchPresenter.class.getSimpleName();
    private final Context context;
    private String querySearch = "";

    @Inject
    AutoCompleteUseCase autoCompleteUseCase;

    @Inject
    DeleteRecentSearchUseCase deleteRecentSearchUseCase;

    public SearchPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void search(String query) {
        this.querySearch = query;
        autoCompleteUseCase.execute(
                AutoCompleteUseCase.getParams(
                        this.querySearch,
                        GCMHandler.getRegistrationId(context),
                        SessionHandler.getLoginID(context)
                ),
                new SearchSubscriber(querySearch,
                        new DefaultAutoCompleteViewModel(),
                        new TabAutoCompleteViewModel(),
                        getView())
        );
    }

    @Override
    public void deleteRecentSearchItem(String keyword) {
        RequestParams params = DeleteRecentSearchUseCase.getParams(
                keyword,
                GCMHandler.getRegistrationId(context),
                SessionHandler.getLoginID(context)
        );
        deleteRecentSearchUseCase.execute(
                params,
                new SearchSubscriber(querySearch,
                        new DefaultAutoCompleteViewModel(),
                        new TabAutoCompleteViewModel(),
                        getView())
        );
    }

    @Override
    public void deleteAllRecentSearch() {
        RequestParams params = DeleteRecentSearchUseCase.getParams(
                GCMHandler.getRegistrationId(context),
                SessionHandler.getLoginID(context)
        );
        deleteRecentSearchUseCase.execute(
                params,
                new SearchSubscriber("",
                        new DefaultAutoCompleteViewModel(),
                        new TabAutoCompleteViewModel(),
                        getView())
        );
    }

    @Override
    public void initializeDataSearch() {
        checkViewAttached();
    }

    @Override
    public void detachView() {
        super.detachView();
        autoCompleteUseCase.unsubscribe();
    }
}
