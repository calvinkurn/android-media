package com.tokopedia.flight.airport.data.source.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Collate;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.airport.view.adapter.FlightAirportAdapterTypeFactory;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightAirportDB extends BaseModel implements Parcelable, Visitable<FlightAirportAdapterTypeFactory> {

    public static final Creator<FlightAirportDB> CREATOR = new Creator<FlightAirportDB>() {
        @Override
        public FlightAirportDB createFromParcel(Parcel source) {
            return new FlightAirportDB(source);
        }

        @Override
        public FlightAirportDB[] newArray(int size) {
            return new FlightAirportDB[size];
        }
    };
    @PrimaryKey
    @Column(name = "country_id")
    String countryId;
    @PrimaryKey
    @Column(name = "city_id")
    String cityId;
    @PrimaryKey
    @Column(name = "airport_id")
    String airportId;
    @Column(name = "country_name", collate = Collate.NOCASE)
    String countryName;
    @Column(name = "phone_code")
    long phoneCode;
    @Column(name = "city_name")
    String cityName;
    @Column(name = "city_code")
    String cityCode;
    @Column(name = "airport_name")
    String airportName;
    @Column(name = "aliases")
    String aliases;
    @Column(name = "airport_ids")
    String airportIds;

    public FlightAirportDB() {
    }

    protected FlightAirportDB(Parcel in) {
        this.countryId = in.readString();
        this.cityId = in.readString();
        this.airportId = in.readString();
        this.countryName = in.readString();
        this.phoneCode = in.readLong();
        this.cityName = in.readString();
        this.cityCode = in.readString();
        this.airportName = in.readString();
        this.aliases = in.readString();
        this.airportIds = in.readString();
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAirportId() {
        return airportId;
    }

    public void setAirportId(String airportId) {
        this.airportId = airportId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public long getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(long phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public String getAirportIds() {
        return airportIds;
    }

    public void setAirportIds(String airportIds) {
        this.airportIds = airportIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryId);
        dest.writeString(this.cityId);
        dest.writeString(this.airportId);
        dest.writeString(this.countryName);
        dest.writeLong(this.phoneCode);
        dest.writeString(this.cityName);
        dest.writeString(this.cityCode);
        dest.writeString(this.airportName);
        dest.writeString(this.aliases);
        dest.writeString(this.airportIds);
    }

    @Override
    public int type(FlightAirportAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
