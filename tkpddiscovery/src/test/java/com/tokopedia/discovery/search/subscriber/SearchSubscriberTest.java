package com.tokopedia.discovery.search.subscriber;

import com.google.gson.Gson;
import com.tokopedia.discovery.SearchDataAssetJson;
import com.tokopedia.discovery.UnitTestFileUtils;
import com.tokopedia.discovery.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.domain.model.SearchResponse;
import com.tokopedia.discovery.search.view.SearchContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchSubscriberTest {
    DefaultAutoCompleteViewModel defaultAutoCompleteViewModel;

    @Mock
    TabAutoCompleteViewModel tabAutoCompleteViewModel;

    @Mock
    SearchContract.View view;

    public static final String testSearchQuery = "testSearchQuery";

    private SearchSubscriber searchSubscriber;
    private UnitTestFileUtils unitTestFileUtils;

    @Before
    public void setUp(){
        defaultAutoCompleteViewModel = new DefaultAutoCompleteViewModel();

        this.searchSubscriber = new SearchSubscriber(
                testSearchQuery,
                defaultAutoCompleteViewModel,
                tabAutoCompleteViewModel,
                view
        );

        unitTestFileUtils = new UnitTestFileUtils();
    }

    @Test
    public void onSubscriberNext_successResultData_allSearchDataAddedToList() {
        Gson gson = new Gson();
        SearchResponse dataResponse =
                gson.fromJson(unitTestFileUtils.getJsonFromAsset(SearchDataAssetJson.SEARCH_DATA_SUCCESS),
                        SearchResponse.class);
        List<SearchData> searchDatas = dataResponse.getData();
        searchSubscriber.onNext(searchDatas);
        assertEquals(defaultAutoCompleteViewModel.getList().size(), 3);
    }

    @Test
    public void onSubscriberNext_successResultData_viewShowAutoCompleteWithGivenViewModel() {
        Gson gson = new Gson();
        SearchResponse dataResponse =
                gson.fromJson(unitTestFileUtils.getJsonFromAsset(SearchDataAssetJson.SEARCH_DATA_SUCCESS),
                        SearchResponse.class);
        List<SearchData> searchDatas = dataResponse.getData();
        searchSubscriber.onNext(searchDatas);
        Mockito.verify(view).showAutoCompleteResult(defaultAutoCompleteViewModel, tabAutoCompleteViewModel);
    }
}