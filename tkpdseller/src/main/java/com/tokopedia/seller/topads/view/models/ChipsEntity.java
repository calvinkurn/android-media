package com.tokopedia.seller.topads.view.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokopedia.seller.topads.domain.model.GenericClass;

public class ChipsEntity<E extends GenericClass> implements Parcelable {

    @DrawableRes
    private Integer drawableResId;
    @Nullable
    private String description;
    @NonNull
    private String name;
    @Nullable
    private E rawData;

    private String className;

    private ChipsEntity(Builder<E> builder) {
        drawableResId = builder.drawableResId;
        description = builder.description;
        name = builder.name;
        rawData = builder.rawData;
        this.className = builder.className;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Integer getDrawableResId() {
        return drawableResId;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Nullable
    public E getRawData() {
        return rawData;
    }

    public void setRawData(@Nullable E rawData) {
        this.rawData = rawData;
    }

    public static final class Builder<T extends GenericClass> {
        private int drawableResId;
        private String description;
        private String name;
        private T rawData;
        private String className;

        private Builder() {
        }

        @NonNull
        public Builder drawableResId(int drawableResId) {
            this.drawableResId = drawableResId;
            return this;
        }

        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        @NonNull
        public Builder description(@Nullable String description) {
            this.description = description;
            return this;
        }

        public Builder rawData(@Nullable T rawData){
            this.rawData = rawData;
            this.className = rawData.getClassName();
            return this;
        }

        @NonNull
        public ChipsEntity build() {
            return new ChipsEntity(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.drawableResId);
        dest.writeString(this.description);
        dest.writeString(this.name);
        dest.writeString(className);
        dest.writeParcelable(this.rawData, flags);
    }

    protected ChipsEntity(Parcel in) {
        this.drawableResId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
        this.name = in.readString();
        this.className = in.readString();
        ClassLoader classLoader = null;
        try{
            classLoader = Class.forName(this.className).getClassLoader();
        }catch(ClassNotFoundException cfe){
            cfe.printStackTrace();
        }
        this.rawData = in.readParcelable(classLoader);
    }

    public static final Parcelable.Creator<ChipsEntity> CREATOR = new Parcelable.Creator<ChipsEntity>() {
        @Override
        public ChipsEntity createFromParcel(Parcel source) {
            return new ChipsEntity(source);
        }

        @Override
        public ChipsEntity[] newArray(int size) {
            return new ChipsEntity[size];
        }
    };
}
