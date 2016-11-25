package com.tokopedia.discovery.model.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.discovery.model.history.searchSuggestion.SearchDataModel;
import com.tokopedia.discovery.presenter.history.SearchHistoryImpl;
import com.tokopedia.discovery.view.history.SearchHistoryViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toped18 on 6/30/2016.
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SearchHistoryImpl.OnItemClickListener listener;
    private View.OnClickListener clearHistoryListener;
    private SearchHistoryModel searchHistoryModel;
    private List<String> searchHistoryList;
    public static final int SUGGESTION = 0;
    public static final int HOTLIST = 1;
    public static final int HISTORY = 2;
    public static final int POPULAR = 3;
    public static final int DELETE_ITEM = 10;
    private static final String TAG = SearchHistoryAdapter.class.getSimpleName();


    public SearchHistoryAdapter(SearchHistoryImpl.OnItemClickListener listener, View.OnClickListener clearHistoryListener) {
        searchHistoryModel = new SearchHistoryModel();
        this.listener = listener;
        this.clearHistoryListener = clearHistoryListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(SearchHistoryViewHolder.getItemViewLayout(), parent, false);
        return new SearchHistoryViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SearchHistoryViewHolder) holder).setSearchTerm(searchHistoryModel.getSearchTerm());
        switch (getItemViewType(position)) {
            case SUGGESTION:
                ((SearchHistoryViewHolder) holder).setSuggestion(searchHistoryModel.getSuggestion());
                break;
            case HOTLIST:
                ((SearchHistoryViewHolder) holder).setHotlist(searchHistoryModel.getHotlist());
                break;
            case HISTORY:
                ((SearchHistoryViewHolder) holder).setHistory(searchHistoryModel.getHistory(), clearHistoryListener);
                break;
            case POPULAR:
                ((SearchHistoryViewHolder) holder).setPopular(searchHistoryModel.getPopular());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        List<Integer> index = new ArrayList<>();
        if (searchHistoryModel.isSuggestionExist()) {
            index.add(SUGGESTION);
        }
        if (searchHistoryModel.isHotlistExist()) {
            index.add(HOTLIST);
        }
        if (searchHistoryModel.isHistoryExist()) {
            index.add(HISTORY);
        }

        if (searchHistoryModel.isPopularExist()) {
            index.add(POPULAR);
        }


        return index.get(position);

    }

    @Override
    public int getItemCount() {
        return searchHistoryModel.getCount();
    }

    public void setNewSearchSuggestion(String query, SearchDataModel dataModel) {
        for (SearchDataModel.Data data : dataModel.getData()) {
            if(data.getId().equals("autocomplete")){
                ArrayList<SearchHistoryModel.Data> autocomplete = new ArrayList<>();
                for (int i = 0; i < data.getItems().size(); i++) {
                    autocomplete.add(new SearchHistoryModel.Data(data.getItems().get(i).getKeyword().toLowerCase(), SUGGESTION, data.getItems().get(i).getUrl(), i));
                }
                searchHistoryModel.setSuggestion(autocomplete);
            }
            if(data.getId().equals("hotlist")){
                ArrayList<SearchHistoryModel.Data> hotlists = new ArrayList<>();
                for (int i = 0; i < data.getItems().size(); i++) {
                    hotlists.add(new SearchHistoryModel.Data(data.getItems().get(i).getKeyword().toLowerCase(), HOTLIST, data.getItems().get(i).getUrl(), i));
                }
                searchHistoryModel.setHotlist(hotlists);
            }
            if(data.getId().equals("recent_search")){
                ArrayList<SearchHistoryModel.Data> recent_search = new ArrayList<>();
                for (int i = 0; i < data.getItems().size(); i++) {
                    recent_search.add(new SearchHistoryModel.Data(data.getItems().get(i).getKeyword().toLowerCase(), HISTORY, data.getItems().get(i).getUrl(), i));
                }
                searchHistoryModel.setHistory(recent_search);
            }
            if(data.getId().equals("popular_search")){
                ArrayList<SearchHistoryModel.Data> popular_search = new ArrayList<>();
                for (int i = 0; i < data.getItems().size(); i++) {
                    popular_search.add(new SearchHistoryModel.Data(data.getItems().get(i).getKeyword().toLowerCase(), POPULAR, data.getItems().get(i).getUrl(), i));
                }
                searchHistoryModel.setPopular(popular_search);
            }
        }
        notifyDataSetChanged();
    }

    public SearchHistoryModel getSearchHistoryModel() {
        return searchHistoryModel;
    }

    public void storeSearchHistoryCache(List<String> searchHistoryList) {
        this.searchHistoryList = searchHistoryList;
    }

    public void reset(){
        searchHistoryModel.clear();
        notifyDataSetChanged();
    }

    public void clearHistorySearch() {
        if (searchHistoryModel.isHistoryExist()) {
            searchHistoryModel.clearHistory();
        }
    }

}
