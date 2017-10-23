package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ProblemResult implements Parcelable {
    public static final int TYPE_DEFAULT = 0;
    public static final int TROUBLE_DEFAULT = 0;

    public int id;
    public String name;
    public int type;
    public int trouble;
    public int quantity;
    public ProblemOrderResult order;
    public String remark;
    public boolean isDelivered = false;
    public boolean canShowInfo = false;

    public ProblemResult() {
        order = new ProblemOrderResult();
    }

    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            if (type != TYPE_DEFAULT) {
                object.put("type", type);
            }
            if (trouble != TROUBLE_DEFAULT) {
                object.put("trouble", trouble);
            }
            if (type == 2) {
                if (quantity != 0) {
                    object.put("quantity", quantity);
                }
                if (order != null) {
                    object.put("order", order.writeToJson());
                }
                if (!remark.equals("")) {
                    object.put("remark", remark);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeInt(this.trouble);
        dest.writeInt(this.quantity);
        dest.writeParcelable(this.order, flags);
        dest.writeString(this.remark);
        dest.writeByte(this.isDelivered ? (byte) 1 : (byte) 0);
        dest.writeByte(this.canShowInfo ? (byte) 1 : (byte) 0);
    }

    protected ProblemResult(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.type = in.readInt();
        this.trouble = in.readInt();
        this.quantity = in.readInt();
        this.order = in.readParcelable(ProblemOrderResult.class.getClassLoader());
        this.remark = in.readString();
        this.isDelivered = in.readByte() != 0;
        this.canShowInfo = in.readByte() != 0;
    }

    public static final Creator<ProblemResult> CREATOR = new Creator<ProblemResult>() {
        @Override
        public ProblemResult createFromParcel(Parcel source) {
            return new ProblemResult(source);
        }

        @Override
        public ProblemResult[] newArray(int size) {
            return new ProblemResult[size];
        }
    };
}
