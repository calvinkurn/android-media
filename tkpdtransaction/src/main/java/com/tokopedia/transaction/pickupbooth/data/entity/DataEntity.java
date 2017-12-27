package com.tokopedia.transaction.pickupbooth.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class DataEntity {
    @SerializedName("list")
    @Expose
    private ArrayList<StoreEntity> storeEntities;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("prev_page")
    @Expose
    private boolean prevPage;
    @SerializedName("next_page")
    @Expose
    private boolean nextPage;

    public ArrayList<StoreEntity> getStoreEntities() {
        return storeEntities;
    }

    public void setStoreEntities(ArrayList<StoreEntity> storeEntities) {
        this.storeEntities = storeEntities;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isPrevPage() {
        return prevPage;
    }

    public void setPrevPage(boolean prevPage) {
        this.prevPage = prevPage;
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }
}
