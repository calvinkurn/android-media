//package com.tokopedia.discovery.newdiscovery.util;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
///**
// * Created by sachinbansal on 1/23/18.
// */
//
//public class ImageSearchProductParameter extends SearchParameter implements Parcelable {
//    private String source = "toppicks";
//    private String productIDs;
//    private int rows;
//
//    public void setSource(String source) {
//        this.source = source;
//    }
//
//    public String getSource() {
//        return source;
//    }
//
//    public void setRow(int startRow) {
//        this.rows = startRow;
//    }
//
//    public int getRow() {
//        return rows;
//    }
//
//    public String getProductIDs() {
//        return productIDs;
//    }
//
//    public void setProductIDs(String productIDs) {
//        this.productIDs = productIDs;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.source);
//        dest.writeString(this.productIDs);
//        dest.writeInt(this.rows);
//    }
//
//    public ImageSearchProductParameter() {
//    }
//
//    protected ImageSearchProductParameter(Parcel in) {
//        this.source = in.readString();
//        this.productIDs = in.readString();
//        this.rows = in.readInt();
//    }
//
//    public static final Parcelable.Creator<SearchParameter> CREATOR = new Parcelable.Creator<SearchParameter>() {
//        @Override
//        public SearchParameter createFromParcel(Parcel source) {
//            return new SearchParameter(source);
//        }
//
//        @Override
//        public SearchParameter[] newArray(int size) {
//            return new SearchParameter[size];
//        }
//    };
//}
