package com.tokopedia.flight.airport.data.source.db.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.common.database.TkpdFightDatabase;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdFightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightAirportDB extends BaseModel implements ItemType {

    public static final int TYPE = 12345;

    @PrimaryKey()
    @Column(name = "country_id")
    String countryId;

    @PrimaryKey()
    @Column(name = "city_id")
    String cityId;

    @PrimaryKey()
    @Column(name = "airport_id")
    String airportId;

    @Column(name = "country_name")
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

    @Override
    public int getType() {
        return TYPE;
    }
}
