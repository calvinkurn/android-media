package com.tokopedia.flight.passenger.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.flight.passenger.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;
import com.tokopedia.flight.common.util.FlightDateUtil;

/**
 * @author by furqan on 28/02/18.
 */

@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightPassengerDb extends BaseModel implements Parcelable {
    @PrimaryKey
    @Column(name = "id")
    String passengerId;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(name = "birthdate")
    String birthdate;
    @Column(name = "title_id")
    int titleId;
    @Column(name = "is_selected")
    int isSelected;

    public FlightPassengerDb() {
    }

    public FlightPassengerDb(SavedPassengerEntity savedPassengerEntity) {
        this.passengerId = savedPassengerEntity.getId();
        this.firstName = savedPassengerEntity.getPassengerAttribute().getFirstName();
        this.lastName = savedPassengerEntity.getPassengerAttribute().getLastName();

        if (savedPassengerEntity.getPassengerAttribute().getDob() != null) {
            this.birthdate = FlightDateUtil.formatDate(
                    FlightDateUtil.FORMAT_DATE_API,
                    FlightDateUtil.DEFAULT_FORMAT,
                    savedPassengerEntity.getPassengerAttribute().getDob()
            );
        }

        this.titleId = savedPassengerEntity.getPassengerAttribute().getTitle();
        this.isSelected = 0;
    }


    protected FlightPassengerDb(Parcel in) {
        passengerId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        birthdate = in.readString();
        titleId = in.readInt();
        isSelected = in.readInt();
    }

    public static final Creator<FlightPassengerDb> CREATOR = new Creator<FlightPassengerDb>() {
        @Override
        public FlightPassengerDb createFromParcel(Parcel in) {
            return new FlightPassengerDb(in);
        }

        @Override
        public FlightPassengerDb[] newArray(int size) {
            return new FlightPassengerDb[size];
        }
    };

    public String getPassengerId() {
        return passengerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected == 1;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(passengerId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(birthdate);
        dest.writeInt(titleId);
        dest.writeInt(isSelected);
    }
}