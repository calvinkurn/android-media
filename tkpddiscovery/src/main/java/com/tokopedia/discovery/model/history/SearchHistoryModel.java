package com.tokopedia.discovery.model.history;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toped18 on 7/4/2016.
 */
public class SearchHistoryModel {

    private List<DetailSearchHistoryModel> searchHistoryModel;
    private String searchTerm;

    public SearchHistoryModel(){
        searchHistoryModel = new ArrayList<DetailSearchHistoryModel>(){{
            add(new DetailSearchHistoryModel(new ArrayList<Data>()));
            add(new DetailSearchHistoryModel(new ArrayList<Data>()));
            add(new DetailSearchHistoryModel(new ArrayList<Data>()));
            add(new DetailSearchHistoryModel(new ArrayList<Data>()));
        }};
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSuggestion(List<Data> suggestion){
        searchHistoryModel.get(SearchHistoryAdapter.SUGGESTION).setDatas(suggestion);
    }

    public void setHotlist(List<Data> hotlist){
        searchHistoryModel.get(SearchHistoryAdapter.HOTLIST).setDatas(hotlist);
    }

    public void setHistory(List<Data> history){
        searchHistoryModel.get(SearchHistoryAdapter.HISTORY).setDatas(history);
    }

    public void setPopular(List<Data> history){
        searchHistoryModel.get(SearchHistoryAdapter.POPULAR).setDatas(history);
    }

    public boolean isSuggestionExist(){
        return !searchHistoryModel.get(SearchHistoryAdapter.SUGGESTION).isEmpty();

    }

    public boolean isHotlistExist(){
        return !searchHistoryModel.get(SearchHistoryAdapter.HOTLIST).isEmpty();
    }

    public boolean isHistoryExist(){
        return !searchHistoryModel.get(SearchHistoryAdapter.HISTORY).isEmpty();
    }

    public boolean isPopularExist(){
        return !searchHistoryModel.get(SearchHistoryAdapter.POPULAR).isEmpty();
    }

    public List<Data> getSuggestion(){
        return searchHistoryModel.get(SearchHistoryAdapter.SUGGESTION).getDatas();
    }

    public List<Data> getHotlist(){
        return searchHistoryModel.get(SearchHistoryAdapter.HOTLIST).getDatas();
    }

    public List<Data> getHistory(){
        return searchHistoryModel.get(SearchHistoryAdapter.HISTORY).getDatas();
    }

    public List<Data> getPopular(){
        return searchHistoryModel.get(SearchHistoryAdapter.POPULAR).getDatas();
    }

    public void clearHistory(){
        searchHistoryModel.get(SearchHistoryAdapter.HISTORY).clear();
    }

    public int getCount() {
        int count = 0;
        if (isHistoryExist()) count++;
        if (isHotlistExist()) count++;
        if (isSuggestionExist()) count++;
        if (isPopularExist()) count++;
        return count;
    }

    public void clear() {
        searchHistoryModel.get(SearchHistoryAdapter.HISTORY).clear();
        searchHistoryModel.get(SearchHistoryAdapter.HOTLIST).clear();
        searchHistoryModel.get(SearchHistoryAdapter.SUGGESTION).clear();
        searchHistoryModel.get(SearchHistoryAdapter.POPULAR).clear();
    }


    private class DetailSearchHistoryModel{
        private List<Data> datas;

        public DetailSearchHistoryModel(List<Data> datas){
            this.datas = datas;
        }

        public List<Data> getDatas() {
            return datas;
        }

        public void setDatas(List<Data> datas) {
            this.datas = datas;
        }

        public boolean isEmpty(){
            return datas.isEmpty();
        }

        public void clear(){
            datas.clear();
        }
    }

    public static class Data {
        String title;
        int type;
        String keyword;
        String url;
        int pos;


        public Data(String title, int type, int pos) {
            this.title = title;
            this.type = type;
            this.pos = pos;
        }

        public Data(String title, String keyword, int type, String url, int pos) {
            this.title = title;
            this.keyword = keyword;
            this.type = type;
            this.url = url;
            this.pos = pos;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
