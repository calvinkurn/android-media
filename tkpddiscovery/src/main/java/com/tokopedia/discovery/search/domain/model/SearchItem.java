package com.tokopedia.discovery.search.domain.model;

/**
 * @author erry on 23/02/17.
 */

public class SearchItem {
    private String keyword;
    private String url;
    private String recom;
    private int sc;
    private String imageURI;
    private boolean isOfficial;

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

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public String getRecom() {
        return recom;
    }

    public void setRecom(String recom) {
        this.recom = recom;
    }

    public int getSc() {
        return sc;
    }

    public void setSc(int sc) {
        this.sc = sc;
    }
}

