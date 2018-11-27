package com.tokopedia.discovery.search.subscriber;

import com.tokopedia.discovery.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.domain.model.SearchItem;
import com.tokopedia.discovery.search.view.SearchContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchSubscriberTest {
    @Mock
    DefaultAutoCompleteViewModel defaultAutoCompleteViewModel;

    @Mock
    TabAutoCompleteViewModel tabAutoCompleteViewModel;

    @Mock
    SearchContract.View view;

    @Mock
    SearchData searchData;

    @Mock
    List<SearchItem> searchItems;

    public static final String testSearchQuery = "testSearchQuery";

    private SearchSubscriber searchSubscriber;
    private List<SearchData> searchDatas;

    @Before
    public void setUp(){
        this.searchSubscriber = new SearchSubscriber(
                testSearchQuery,
                defaultAutoCompleteViewModel,
                tabAutoCompleteViewModel,
                view
        );

        searchDatas = new ArrayList<>();
        searchDatas.add(searchData);
    }

    @Test
    public void onSubscriberNext_recentViewIdDetected_addedToDefaultAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.RECENT_VIEW);

        searchSubscriber.onNext(searchDatas);

        verify(defaultAutoCompleteViewModel).addList(searchData);
    }

    @Test
    public void onSubscriberNext_recentSearchIdDetected_addedToDefaultAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.RECENT_SEARCH);

        searchSubscriber.onNext(searchDatas);

        verify(defaultAutoCompleteViewModel).addList(searchData);
    }

    @Test
    public void onSubscriberNext_popularSearchIdDetected_addedToDefaultAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.POPULAR_SEARCH);

        searchSubscriber.onNext(searchDatas);

        verify(defaultAutoCompleteViewModel).addList(searchData);
    }

    @Test
    public void onSubscriberNext_recentViewIdDetected_notAddedToTabAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.RECENT_VIEW);

        searchSubscriber.onNext(searchDatas);

        verify(tabAutoCompleteViewModel, never()).addList(searchData);
    }

    @Test
    public void onSubscriberNext_recentSearchIdDetected_notAddedToTabAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.RECENT_SEARCH);

        searchSubscriber.onNext(searchDatas);

        verify(tabAutoCompleteViewModel, never()).addList(searchData);
    }

    @Test
    public void onSubscriberNext_popularSearchIdDetected_notAddedToTabAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.POPULAR_SEARCH);

        searchSubscriber.onNext(searchDatas);

        verify(tabAutoCompleteViewModel, never()).addList(searchData);
    }

    @Test
    public void onSubscriberNext_digitalIdDetected_addedToTabAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.DIGITAL);

        searchSubscriber.onNext(searchDatas);

        verify(tabAutoCompleteViewModel).addList(searchData);
    }

    @Test
    public void onSubscriberNext_categoryIdDetected_addedToTabAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.CATEGORY);

        searchSubscriber.onNext(searchDatas);

        verify(tabAutoCompleteViewModel).addList(searchData);
    }

    @Test
    public void onSubscriberNext_autocompleteIdDetected_addedToTabAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.AUTOCOMPLETE);

        searchSubscriber.onNext(searchDatas);

        verify(tabAutoCompleteViewModel).addList(searchData);
    }

    @Test
    public void onSubscriberNext_hotlistIdDetected_addedToTabAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.HOTLIST);

        searchSubscriber.onNext(searchDatas);

        verify(tabAutoCompleteViewModel).addList(searchData);
    }

    @Test
    public void onSubscriberNext_inCategoryIdDetected_addedToTabAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.IN_CATEGORY);

        searchSubscriber.onNext(searchDatas);

        verify(tabAutoCompleteViewModel).addList(searchData);
    }

    @Test
    public void onSubscriberNext_shopIdDetected_addedToTabAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.SHOP);

        searchSubscriber.onNext(searchDatas);

        verify(tabAutoCompleteViewModel).addList(searchData);
    }

    @Test
    public void onSubscriberNext_digitalIdDetected_notAddedToDefaultAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.DIGITAL);

        searchSubscriber.onNext(searchDatas);

        verify(defaultAutoCompleteViewModel, never()).addList(searchData);
    }

    @Test
    public void onSubscriberNext_categoryIdDetected_notAddedToDefaultAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.CATEGORY);

        searchSubscriber.onNext(searchDatas);

        verify(defaultAutoCompleteViewModel, never()).addList(searchData);
    }

    @Test
    public void onSubscriberNext_autocompleteIdDetected_notAddedToDefaultAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.AUTOCOMPLETE);

        searchSubscriber.onNext(searchDatas);

        verify(defaultAutoCompleteViewModel, never()).addList(searchData);
    }

    @Test
    public void onSubscriberNext_hotlistIdDetected_notAddedToDefaultAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.HOTLIST);

        searchSubscriber.onNext(searchDatas);

        verify(defaultAutoCompleteViewModel, never()).addList(searchData);
    }

    @Test
    public void onSubscriberNext_inCategoryIdDetected_notAddedToDefaultAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.IN_CATEGORY);

        searchSubscriber.onNext(searchDatas);

        verify(defaultAutoCompleteViewModel, never()).addList(searchData);
    }

    @Test
    public void onSubscriberNext_shopIdDetected_notAddedToDefaultAutoCompleteViewModel() {
        when(searchData.getItems()).thenReturn(searchItems);
        when(searchItems.size()).thenReturn(1);
        when(searchData.getId()).thenReturn(SearchSubscriber.SHOP);

        searchSubscriber.onNext(searchDatas);

        verify(defaultAutoCompleteViewModel, never()).addList(searchData);
    }
}