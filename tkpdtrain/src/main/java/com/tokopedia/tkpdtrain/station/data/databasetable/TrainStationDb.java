package com.tokopedia.tkpdtrain.station.data.databasetable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.tkpdtrain.common.database.TkpdTrainDatabase;

/**
 * @author by alvarisi on 3/7/18.
 */
@Table(database = TkpdTrainDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class TrainStationDb extends BaseModel {
    @PrimaryKey
    @Column(name = "station_id")
    private
    String stationId;
    @PrimaryKey
    @Column(name = "station_name")
    private
    String stationName;
    @Column(name = "station_display_name")
    private
    String stationDisplayName;
    @PrimaryKey
    @Column(name = "station_code")
    private
    String stationCode;
    @Column(name = "popularity_order")
    private
    int popularityOrder;
    @Column(name = "station_status")
    private
    String stationStatus;
    @Column(name = "city_id")
    private
    int cityId;
    @Column(name = "city_name")
    private
    String cityName;
    @Column(name = "island_id")
    private
    int islandId;
    @Column(name = "island_name")
    private
    String islandName;

    public TrainStationDb() {
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public int getPopularityOrder() {
        return popularityOrder;
    }

    public void setPopularityOrder(int popularityOrder) {
        this.popularityOrder = popularityOrder;
    }

    public String getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(String stationStatus) {
        this.stationStatus = stationStatus;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getIslandId() {
        return islandId;
    }

    public void setIslandId(int islandId) {
        this.islandId = islandId;
    }

    public String getIslandName() {
        return islandName;
    }

    public void setIslandName(String islandName) {
        this.islandName = islandName;
    }

    public String getStationDisplayName() {
        return stationDisplayName;
    }

    public void setStationDisplayName(String stationDisplayName) {
        this.stationDisplayName = stationDisplayName;
    }
}
