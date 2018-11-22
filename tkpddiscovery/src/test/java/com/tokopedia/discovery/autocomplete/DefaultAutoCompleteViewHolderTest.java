package com.tokopedia.discovery.autocomplete;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.search.view.adapter.SearchAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DefaultAutoCompleteViewHolderTest {

    DefaultAutoCompleteViewHolder defaultAutoCompleteViewHolder;

    @Mock
    View view;

    @Mock
    SearchAdapter searchAdapter;

    @Mock
    DefaultAutoCompleteViewModel mockedElement;

    @Before
    public void setUp(){
        defaultAutoCompleteViewHolder = new DefaultAutoCompleteViewHolder(view, searchAdapter);
    }

    @Test
    public void givenRecentSearchData_onBind_addedToAdapter(){
        List<SearchData> listWithOnlyRecentSearchData = new ArrayList<>();
        SearchData mockedSearchData = Mockito.mock(SearchData.class);
        listWithOnlyRecentSearchData.add(mockedSearchData);

        when(mockedElement.getList()).thenReturn(listWithOnlyRecentSearchData);
        when(mockedSearchData.getId()).thenReturn(SearchData.AUTOCOMPLETE_RECENT_SEARCH);

        defaultAutoCompleteViewHolder.bind(mockedElement);

        verify(searchAdapter).addAll(anyListOf(Visitable.class));
    }

    @Test
    public void givenPopularSearchData_onBind_addedToAdapter(){
        List<SearchData> listWithOnlyRecentSearchData = new ArrayList<>();
        SearchData mockedSearchData = Mockito.mock(SearchData.class);
        listWithOnlyRecentSearchData.add(mockedSearchData);

        when(mockedElement.getList()).thenReturn(listWithOnlyRecentSearchData);
        when(mockedSearchData.getId()).thenReturn(SearchData.AUTOCOMPLETE_POPULAR_SEARCH);

        defaultAutoCompleteViewHolder.bind(mockedElement);

        verify(searchAdapter).addAll(anyListOf(Visitable.class));
    }

    @Test
    public void givenRecentViewData_onBind_addedToAdapter(){
        List<SearchData> listWithOnlyRecentSearchData = new ArrayList<>();
        SearchData mockedSearchData = Mockito.mock(SearchData.class);
        listWithOnlyRecentSearchData.add(mockedSearchData);

        when(mockedElement.getList()).thenReturn(listWithOnlyRecentSearchData);
        when(mockedSearchData.getId()).thenReturn(SearchData.AUTOCOMPLETE_RECENT_VIEW);

        defaultAutoCompleteViewHolder.bind(mockedElement);

        verify(searchAdapter).addAll(anyListOf(Visitable.class));
    }
}