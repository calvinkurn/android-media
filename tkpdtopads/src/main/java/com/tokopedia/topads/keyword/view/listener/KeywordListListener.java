package com.tokopedia.topads.keyword.view.listener;

import android.support.design.widget.TabLayout;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;

/**
 * Created by normansyahputa on 5/23/17.
 */

public class KeywordListListener extends TabLayout.TabLayoutOnPageChangeListener {
    private static final String TAG = "KeywordListListener";
    private SearchView searchView;
    private Listener listener;
    private SparseArrayCompat<String> prevQuery = new SparseArrayCompat<>();
    private SparseArrayCompat<Boolean> prevVisible = new SparseArrayCompat<>();
    private boolean isProcessing = false;

    public KeywordListListener(TabLayout tabLayout, Listener listener) {
        super(tabLayout);
        this.listener = listener;
    }

    public void attachSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public void removeSearchView() {
        this.searchView = null;
    }

    public boolean isSearchViewAttached() {
        return searchView != null;
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);

        if (searchView == null)
            return;

        if (listener != null) {
            listener.validateMenuItem();
        }

        isProcessing = true;

        // get previous position
        int prevPosition = -1;
        if (position - 1 < 0) {// positive keyword
            prevPosition = 1;
        } else if (position - 1 == 0) {// negative keyword
            prevPosition = 0;
        }

        prevQuery.put(prevPosition, searchView.getQuery().toString());

        // reset to begin.
        resetSearch();

        // restore current position data
        String s = prevQuery.get(position);

        boolean isVisible = false;
        if (prevVisible.get(position) != null) {
            isVisible = prevVisible.get(position);
        }

        if (s != null && isVisible) {
            expand();
            searchView.setQuery(s, true);
        }

        // restore listener
        addListener();

        isProcessing = false;
    }

    public void add(int position) {
        prevVisible.put(position, true);
    }

    public void remove(int position) {
        if (isProcessing)
            return;
        prevVisible.put(position, false);
    }

    private void expand() {
        if (listener != null) {
            listener.expand();
        }
    }

    private void addListener() {
        if (listener != null) {
            listener.addListener();
        }
    }

    private void resetSearch() {
        if (listener != null) {
            listener.removeListener();
        }
    }

    public interface Listener {
        void removeListener();

        void addListener();

        void expand();

        void validateMenuItem();
    }
}
