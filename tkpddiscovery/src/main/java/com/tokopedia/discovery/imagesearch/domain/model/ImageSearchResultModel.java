package com.tokopedia.discovery.imagesearch.domain.model;

import com.aliyuncs.imagesearch.model.v20180120.SearchItemResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class ImageSearchResultModel {

    private ArrayList auctionArrayList;

    public ImageSearchResultModel(){
        auctionArrayList = new ArrayList<>();
    }

    public ArrayList getAuctionArrayList() {
        return auctionArrayList;
    }

    public void setAuctionArrayList(List<SearchItemResponse.Auction> auctionArrayList) {
        this.auctionArrayList = (ArrayList) auctionArrayList;
    }
}
