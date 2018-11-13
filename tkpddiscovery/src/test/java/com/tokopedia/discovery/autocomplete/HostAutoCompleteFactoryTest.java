package com.tokopedia.discovery.autocomplete;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.autocomplete.adapter.AutoCompleteViewHolder;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class HostAutoCompleteFactoryTest {

    @Mock
    ItemClickListener clickListener;

    @Mock
    FragmentManager fragmentManager;

    @Mock
    View view;

    private HostAutoCompleteFactory hostAutoCompleteFactory;

    @Before
    public void setUp(){
        this.hostAutoCompleteFactory = new HostAutoCompleteFactory(clickListener, fragmentManager);
    }

    @Test
    public void onCreateViewHolder_defaultAutoCompleteType_instantiateDefaultAutoCompleteViewHolder() {
        AbstractViewHolder viewHolder = hostAutoCompleteFactory.createViewHolder(view, AutoCompleteViewHolder.LAYOUT);
        assertThat(viewHolder, instanceOf(DefaultAutoCompleteViewHolder.class));
    }

    @Test
    public void onCreateViewHolder_tabAutoCompleteType_instantiateTabAutoCompleteViewHolder() {
        AbstractViewHolder viewHolder = hostAutoCompleteFactory.createViewHolder(view, TabAutoCompleteViewHolder.LAYOUT);
        assertThat(viewHolder, instanceOf(TabAutoCompleteViewHolder.class));
    }
}