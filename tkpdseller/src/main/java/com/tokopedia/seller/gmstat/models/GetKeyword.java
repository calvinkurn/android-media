package com.tokopedia.seller.gmstat.models;

/**
 * Created by normansyahputa on 11/14/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetKeyword {

    @SerializedName("SearchKeyword")
    @Expose
    private List<SearchKeyword> searchKeyword = new ArrayList<>();

    /**
     * @return The searchKeyword
     */
    public List<SearchKeyword> getSearchKeyword() {
        return searchKeyword;
    }

    /**
     * @param searchKeyword The SearchKeyword
     */
    public void setSearchKeyword(List<SearchKeyword> searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public static class SearchKeyword {

        @SerializedName("Keyword")
        @Expose
        private String keyword;
        @SerializedName("Frequency")
        @Expose
        private int frequency;

        /**
         * @return The keyword
         */
        public String getKeyword() {
            return keyword;
        }

        /**
         * @param keyword The Keyword
         */
        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        /**
         * @return The frequency
         */
        public int getFrequency() {
            return frequency;
        }

        /**
         * @param frequency The Frequency
         */
        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

    }
}


