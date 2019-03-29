package com.tokopedia.seller.reputation.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.base.list.seller.common.util.ItemType;

/**
 * @author normansyahputa on 3/16/17.
 */

public class ReputationReviewModel implements Parcelable, ItemType {
    public static final int VIEW_DEPOSIT = 1912123;
    public static final Creator<ReputationReviewModel> CREATOR = new Creator<ReputationReviewModel>() {
        @Override
        public ReputationReviewModel createFromParcel(Parcel source) {
            return new ReputationReviewModel(source);
        }

        @Override
        public ReputationReviewModel[] newArray(int size) {
            return new ReputationReviewModel[size];
        }
    };
    Data data;

    public ReputationReviewModel() {
    }

    protected ReputationReviewModel(Parcel in) {
        this.data = in.readParcelable(Data.class.getClassLoader());
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
    }

    @Override
    public int getType() {
        return VIEW_DEPOSIT;
    }

    public static class Data implements Parcelable {
        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
        private String date;
        private String information;
        private float penaltyScore;

        public Data() {
        }

        protected Data(Parcel in) {
            this.date = in.readString();
            this.information = in.readString();
            this.penaltyScore = in.readFloat();
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }

        public float getPenaltyScore() {
            return penaltyScore;
        }

        public void setPenaltyScore(float penaltyScore) {
            this.penaltyScore = penaltyScore;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.date);
            dest.writeString(this.information);
            dest.writeFloat(this.penaltyScore);
        }

        @Override
        public String toString() {
            return "Data{" +
                    "date='" + date + '\'' +
                    ", information='" + information + '\'' +
                    ", penaltyScore=" + penaltyScore +
                    '}';
        }
    }
}
