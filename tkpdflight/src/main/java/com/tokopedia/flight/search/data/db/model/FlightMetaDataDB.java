package com.tokopedia.flight.search.data.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;
import com.tokopedia.flight.search.data.cloud.model.response.Meta;

import java.util.List;

/**
 * Created by User on 11/15/2017.
 */

@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightMetaDataDB extends BaseModel {
    @PrimaryKey
    @Column(name = "departure_airport")
    String departureAirport;

    @PrimaryKey
    @Column(name = "arrival_airport")
    String arrivalAirport;

    @PrimaryKey
    @Column(name = "time")
    String time;

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

    public FlightMetaDataDB(Meta meta) {
        this.departureAirport = meta.getDepartureAirport();
        this.arrivalAirport = meta.getArrivalAirport();
        this.time = meta.getTime();
        this.needRefresh = meta.isNeedRefresh();
        this.refreshTime = meta.getRefreshTime();
        this.max_retry = meta.getMaxRetry();
        this.retry_no = 0;
        this.last_pulled = System.currentTimeMillis() / 1000L;
    }
}
