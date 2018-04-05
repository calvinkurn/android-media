
package com.tokopedia.discovery.imagesearch.domain.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Auctions implements Serializable {

    @SerializedName("Auction")
    @Expose
    private List<Auction> auction = null;
    private final static long serialVersionUID = 3754603510632435435L;

    protected Auctions(Parcel in) {
        in.readList(this.auction, (Auction.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public Auctions() {
    }

    /**
     * @param auction
     */
    public Auctions(List<Auction> auction) {
        super();
        this.auction = auction;
    }

    public List<Auction> getAuction() {
        return auction;
    }

    public void setAuction(List<Auction> auction) {
        this.auction = auction;
    }

    public Auctions withAuction(List<Auction> auction) {
        this.auction = auction;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(auction);
    }

    public int describeContents() {
        return 0;
    }

}
