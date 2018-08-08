package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;

import java.util.List;

/**
 * Created by henrypriyono on 14/02/18.
 */

public class GuidedSearchViewModel implements Visitable<ProductListTypeFactory> {
    private List<Item> itemList;

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public static class Item {
        private String keyword;
        private String url;
        private String currentPage;
        private String previousKey;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public void setPreviousKey(String previousKey) {
            this.previousKey = previousKey;
        }

        public String getPreviousKey() {
            return previousKey;
        }
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
