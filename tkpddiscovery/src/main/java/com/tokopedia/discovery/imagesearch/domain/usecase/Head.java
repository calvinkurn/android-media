
package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Head implements Serializable, Parcelable {

    @SerializedName("DocsReturn")
    @Expose
    private int docsReturn;
    @SerializedName("DocsFound")
    @Expose
    private int docsFound;
    @SerializedName("SearchTime")
    @Expose
    private int searchTime;
    public final static Creator<Head> CREATOR = new Creator<Head>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Head createFromParcel(Parcel in) {
            return new Head(in);
        }

        public Head[] newArray(int size) {
            return (new Head[size]);
        }

    };
    private final static long serialVersionUID = -2772717694563315620L;

    protected Head(Parcel in) {
        this.docsReturn = ((int) in.readValue((int.class.getClassLoader())));
        this.docsFound = ((int) in.readValue((int.class.getClassLoader())));
        this.searchTime = ((int) in.readValue((int.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Head() {
    }

    /**
     * @param docsFound
     * @param docsReturn
     * @param searchTime
     */
    public Head(int docsReturn, int docsFound, int searchTime) {
        super();
        this.docsReturn = docsReturn;
        this.docsFound = docsFound;
        this.searchTime = searchTime;
    }

    public int getDocsReturn() {
        return docsReturn;
    }

    public void setDocsReturn(int docsReturn) {
        this.docsReturn = docsReturn;
    }

    public Head withDocsReturn(int docsReturn) {
        this.docsReturn = docsReturn;
        return this;
    }

    public int getDocsFound() {
        return docsFound;
    }

    public void setDocsFound(int docsFound) {
        this.docsFound = docsFound;
    }

    public Head withDocsFound(int docsFound) {
        this.docsFound = docsFound;
        return this;
    }

    public int getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(int searchTime) {
        this.searchTime = searchTime;
    }

    public Head withSearchTime(int searchTime) {
        this.searchTime = searchTime;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(docsReturn);
        dest.writeValue(docsFound);
        dest.writeValue(searchTime);
    }

    public int describeContents() {
        return 0;
    }

}
