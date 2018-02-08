package com.tokopedia.shop.note.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.shop.note.view.adapter.ShopNoteTypeFactory;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopNoteViewModel implements Visitable<ShopNoteTypeFactory> {

    private long shopNoteId;
    private String title;
    private long position;
    private String url;
    private String lastUpdate;

    public long getShopNoteId() {
        return shopNoteId;
    }

    public void setShopNoteId(long shopNoteId) {
        this.shopNoteId = shopNoteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public int type(ShopNoteTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
