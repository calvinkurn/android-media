
package com.tokopedia.inbox.inboxmessageold.model.inboxmessage;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithUser implements Parcelable {

    @SerializedName("reputation")
    @Expose
    private Reputation reputation;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("image")
    @Expose
    private String image;

    protected WithUser(Parcel in) {
        reputation = in.readParcelable(Reputation.class.getClassLoader());
        name = in.readString();
        label = in.readString();
        id = in.readInt();
        type = in.readString();
        image = in.readString();
    }

    public static final Creator<WithUser> CREATOR = new Creator<WithUser>() {
        @Override
        public WithUser createFromParcel(Parcel in) {
            return new WithUser(in);
        }

        @Override
        public WithUser[] newArray(int size) {
            return new WithUser[size];
        }
    };

    /**
     * 
     * @return
     *     The reputation
     */
    public Reputation getReputation() {
        return reputation;
    }

    /**
     * 
     * @param reputation
     *     The reputation
     */
    public void setReputation(Reputation reputation) {
        this.reputation = reputation;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The label
     */
    public String getLabel() {
        return label;
    }

    /**
     * 
     * @param label
     *     The label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * 
     * @return
     *     The id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The image
     */
    public String getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(reputation, flags);
        dest.writeString(name);
        dest.writeString(label);
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(image);
    }
}
