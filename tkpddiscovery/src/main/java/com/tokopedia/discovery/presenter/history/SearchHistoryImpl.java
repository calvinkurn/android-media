package com.tokopedia.discovery.presenter.history;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.myproduct.presenter.ImageGalleryImpl.Pair;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.fragment.history.SearchHistoryFragment;
import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;
import com.tokopedia.discovery.interactor.SearchInteractor;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.ObjContainer;
import com.tokopedia.discovery.model.history.SearchHistoryAdapter;
import com.tokopedia.discovery.model.history.SearchHistoryModel;
import com.tokopedia.discovery.model.history.searchSuggestion.SearchDataModel;
import com.tokopedia.discovery.view.history.SearchHistoryView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Toped18 on 6/30/2016.
 */
public class SearchHistoryImpl extends SearchHistory {

    /* Parameter */
    private DiscoveryInteractor discoveryInteractor;
    private SearchHistoryAdapter searchHistoryAdapter;  // the model data is inside this adapter
    private BroadcastReceiver searchReceiver;
    private DiscoveryListener discoveryListener;
    /* cache search */
//    private SearchInteractor searchCacheManager;
    private SearchInteractor.GetSearchCacheListener listener;
    private List<String> searchHistoryCache;
    private static final String TAG = SearchHistoryImpl.class.getSimpleName();

    private String query = "";

    public interface OnItemClickListener {
        void onItemClick(SearchHistoryModel.Data selected, String urlHotlist, int type);
    }

    /**
     * Constructor
     *
     * @param view
     */
    public SearchHistoryImpl(SearchHistoryView view) {
        super(view);
    }

    @NonNull
    private DiscoveryListener getDiscoveryListener(final Context context) {
        return new DiscoveryListener() {
            @Override
            public void onComplete(int type, Pair<String, ? extends ObjContainer> data) {

            }

            @Override
            public void onFailed(int type, Pair<String, ? extends ObjContainer> data) {
                Log.e(TAG, "onFailed data " + data.toString());
            }

            @Override
            public void onSuccess(int type, Pair<String, ? extends ObjContainer> data) {
                switch (type) {
                    case DiscoveryListener.SEARCH_SUGGESTION:
                        SearchDataModel dataModel = ((SearchDataModel.SearchSuggestionContainer) data.getModel2()).body();
                        searchHistoryAdapter.setNewSearchSuggestion(query, dataModel);
                        break;
                    case DELETE_SUGGESTION:
                        HashMap<String, String> hashMap = (HashMap<String, String>) data.getModel2().body();
                        Log.d(TAG, "Success DELETE_SUGGESTION hashMap " + hashMap.toString());
                        getSearchData(context);
                        break;
                }
            }
        };
    }

    @NonNull
    private BroadcastReceiver getBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                query = intent.getStringExtra(BrowseProductActivity.EXTRAS_SEARCH_TERM);
                getSearchData(context);
                Log.d(TAG, "Search get data form cache");
            }
        };
    }

    private void getSearchData(Context context) {
        String unique_id = AuthUtil.md5(GCMHandler.getRegistrationId(context));
        if (SessionHandler.isV4Login(context)) {
            unique_id = AuthUtil.md5(SessionHandler.getLoginID(context));
        }
        discoveryInteractor.loadSearchSuggestion(query, unique_id, 5);
    }

    private OnItemClickListener getItemListener(final Context context) {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(SearchHistoryModel.Data selected, String urlHotlist, int type) {
                switch (type) {
                    case SearchHistoryAdapter.HISTORY:
                    case SearchHistoryAdapter.POPULAR:
                    case SearchHistoryAdapter.SUGGESTION:
                        view.sendSearchResult(selected.getTitle().toLowerCase());
                        break;
                    case SearchHistoryAdapter.HOTLIST:
                        view.sendHotlistResult(new URLParser(selected.getUrl()).getHotAlias());
                        break;
                    case SearchHistoryAdapter.DELETE_ITEM:
                        String unique_id = AuthUtil.md5(GCMHandler.getRegistrationId(context));
                        if (SessionHandler.isV4Login(context)) {
                            unique_id = AuthUtil.md5(SessionHandler.getLoginID(context));
                        }
                        discoveryInteractor.deleteSearchHistory(unique_id, selected.getTitle(), false);
                        break;
                }
            }
        };
    }

    private View.OnClickListener getClearHistoryListener(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unique_id = AuthUtil.md5(GCMHandler.getRegistrationId(context));
                if (SessionHandler.isV4Login(context)) {
                    unique_id = AuthUtil.md5(SessionHandler.getLoginID(context));
                }
                discoveryInteractor.deleteSearchHistory(unique_id, "", true);
            }
        };
    }


    @Override
    public RecyclerView.Adapter getAdapter() {
        return searchHistoryAdapter;
    }

    @Override
    public String getMessageTAG() {
        return null;
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return null;
    }

    @Override
    public void initData(@NonNull Context context) {
        // initialize broadcast listener
        ((DiscoveryInteractorImpl) discoveryInteractor).setCompositeSubscription(compositeSubscription);
        context.registerReceiver(searchReceiver, new IntentFilter(BrowseProductActivity.SEARCH_ACTION_INTENT));
    }

    @Override
    public void unregisterBroadcast(Context context) {
        context.unregisterReceiver(searchReceiver);
    }


    @Override
    public void fetchArguments(Bundle argument) {
        // init search
        if (argument.getString(SearchHistoryFragment.INIT_QUERY) != null) {
            query = argument.getString(SearchHistoryFragment.INIT_QUERY);
        }
    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {
        // init history adapter
        searchHistoryAdapter = new SearchHistoryAdapter(getItemListener(context), getClearHistoryListener(context));
        // initialize network impl
        discoveryListener = getDiscoveryListener(context);
        discoveryInteractor = new DiscoveryInteractorImpl();
        discoveryInteractor.setDiscoveryListener(discoveryListener);

        // init broadcast
        searchReceiver = getBroadcastReceiver();

    }
}
