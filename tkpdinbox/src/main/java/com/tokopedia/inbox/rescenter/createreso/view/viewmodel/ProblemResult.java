package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ProblemResult implements Parcelable {
    public static final int TYPE_DEFAULT = 0;
    public static final int TROUBLE_DEFAULT = 0;
    private static final String REMARK_BEDA_ONGKIR = "beda ongkir";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_TROUBLE = "trouble";
    private static final String PARAM_AMOUNT = "amount";
    private static final String PARAM_QTY = "qty";
    private static final String PARAM_REMARK = "remark";

    public int id;
    public String name;
    public int type;
    public int trouble;
    public int quantity;
    public int amount;
    public String remark;
    public boolean isDelivered = false;
    public boolean canShowInfo = false;

    public ProblemResult() {
    }

    public JsonObject writeToJson() {
        JsonObject object = new JsonObject();
        try {
            if (type != TYPE_DEFAULT) {
                object.addProperty(PARAM_TYPE, type);
            }
            if (trouble != TROUBLE_DEFAULT) {
                object.addProperty(PARAM_TROUBLE, trouble);
            }
            object.addProperty(PARAM_AMOUNT, amount);
            if (type == 2) {
                if (quantity != 0) {
                    object.addProperty(PARAM_QTY, quantity);
                }
                if (remark != null && !remark.equals("")) {
                    object.addProperty(PARAM_REMARK, remark);
                }
            } else {
                object.addProperty(PARAM_REMARK, REMARK_BEDA_ONGKIR);
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
        dest.writeInt(this.amount);
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
        this.amount = in.readInt();
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
