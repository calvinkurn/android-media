package com.tokopedia.flight.airline.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightAirlineDB extends BaseModel implements Parcelable {

    public static final Creator<FlightAirlineDB> CREATOR = new Creator<FlightAirlineDB>() {
        @Override
        public FlightAirlineDB createFromParcel(Parcel in) {
            return new FlightAirlineDB(in);
        }

        @Override
        public FlightAirlineDB[] newArray(int size) {
            return new FlightAirlineDB[size];
        }
    };
    @PrimaryKey
    @Column(name = "id")
    String id;
    @Column(name = "name")
    String name;
    @Column(name = "short_name")
    String shortName;
    @Column(name = "logo")
    String logo;
    @Column(name = "mandatory_dob")
    boolean mandatoryDob;
    public FlightAirlineDB(){

    }

    public FlightAirlineDB(AirlineData airlineData) {
        this.id = airlineData.getId();
        this.name = airlineData.getAttributes().getFullName();
        this.shortName = airlineData.getAttributes().getShortName();
        this.logo = airlineData.getAttributes().getLogo();
        this.mandatoryDob = airlineData.getAttributes().isMandatoryDob();
    }

    public FlightAirlineDB(String id, String name, String shortName, String logo, boolean mandatoryDob) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.logo = logo;
        this.mandatoryDob = mandatoryDob;
    }

    protected FlightAirlineDB(Parcel in) {
        id = in.readString();
        name = in.readString();
        shortName = in.readString();
        logo = in.readString();
        mandatoryDob = in.readByte() != 0;
    }

    public String getId() {
        return id;
    }

    public String getName(){
        if (TextUtils.isEmpty(shortName)) {
            return name;
        }
        return shortName;
    }

    public String getFullName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLogo() {
        return logo;
    }

    public boolean isMandatoryDob() {
        return mandatoryDob;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(shortName);
        parcel.writeString(logo);
        parcel.writeByte((byte) (mandatoryDob ? 1 : 0));
    }
}
