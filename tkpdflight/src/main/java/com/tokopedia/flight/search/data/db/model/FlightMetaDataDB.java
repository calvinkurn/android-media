package com.tokopedia.flight.search.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;
import com.tokopedia.flight.search.data.cloud.model.response.Meta;

/**
 * Created by User on 11/15/2017.
 */

@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightMetaDataDB extends BaseModel implements Parcelable {
    @PrimaryKey
    @Column(name = "departure_airport")
    String departureAirport;

    @PrimaryKey
    @Column(name = "arrival_airport")
    String arrivalAirport;

    @PrimaryKey
    @Column(name = "date")
    String date;

    @Column(name = "need_refresh")
    boolean needRefresh;

    @Column(name = "refresh_time")
    int refreshTime;

    @Column(name = "max_retry")
    int max_retry;

    @Column(name = "retry_no")
    int retry_no;

    @Column(name = "last_pulled")
    long last_pulled;

    public FlightMetaDataDB(){

    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDate() {
        return date;
    }

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public int getMax_retry() {
        return max_retry;
    }

    public int getRetry_no() {
        return retry_no;
    }

    public long getLast_pulled() {
        return last_pulled;
    }

    public FlightMetaDataDB(Meta meta) {
        this.departureAirport = meta.getDepartureAirport();
        this.arrivalAirport = meta.getArrivalAirport();
        this.date = meta.getTime();
        this.needRefresh = meta.isNeedRefresh();
        this.refreshTime = meta.getRefreshTime();
        this.max_retry = meta.getMaxRetry();
        this.retry_no = 0;
        this.last_pulled = System.currentTimeMillis() / 1000L;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.departureAirport);
        dest.writeString(this.arrivalAirport);
        dest.writeString(this.date);
        dest.writeByte(this.needRefresh ? (byte) 1 : (byte) 0);
        dest.writeInt(this.refreshTime);
        dest.writeInt(this.max_retry);
        dest.writeInt(this.retry_no);
        dest.writeLong(this.last_pulled);
    }

    protected FlightMetaDataDB(Parcel in) {
        this.departureAirport = in.readString();
        this.arrivalAirport = in.readString();
        this.date = in.readString();
        this.needRefresh = in.readByte() != 0;
        this.refreshTime = in.readInt();
        this.max_retry = in.readInt();
        this.retry_no = in.readInt();
        this.last_pulled = in.readLong();
    }

    public static final Parcelable.Creator<FlightMetaDataDB> CREATOR = new Parcelable.Creator<FlightMetaDataDB>() {
        @Override
        public FlightMetaDataDB createFromParcel(Parcel source) {
            return new FlightMetaDataDB(source);
        }

        @Override
        public FlightMetaDataDB[] newArray(int size) {
            return new FlightMetaDataDB[size];
        }
    };
}
