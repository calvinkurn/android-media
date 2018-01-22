package com.tokopedia.discovery.imagesearch.domain.model;

import com.tokopedia.core.network.entity.discovery.ImageSearchResponse;

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

    public void setAuctionArrayList(List<ImageSearchResponse.Auction> auctionArrayList) {
        this.auctionArrayList = (ArrayList) auctionArrayList;
    }
}
