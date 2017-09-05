package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateResoStep2ViewModel implements Parcelable {

    public String cacheKey;
    public List<String> pictures;
    public List<String> videos;

    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            if (!cacheKey.equals("")) {
                object.put("cacheKey", cacheKey);
            }
            if (pictures.size() != 0) {
                JSONArray array = new JSONArray();
                for (String string : pictures) {
                    array.put(string);
                }
                object.put("pictures", array);
            }
            if (videos.size() != 0) {
                JSONArray array = new JSONArray();
                for (String string : videos) {
                    array.put(string);
                }
                object.put("videos", array);
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
        dest.writeString(this.cacheKey);
        dest.writeStringList(this.pictures);
        dest.writeStringList(this.videos);
    }

    public CreateResoStep2ViewModel() {
    }

    protected CreateResoStep2ViewModel(Parcel in) {
        this.cacheKey = in.readString();
        this.pictures = in.createStringArrayList();
        this.videos = in.createStringArrayList();
    }

    public static final Creator<CreateResoStep2ViewModel> CREATOR = new Creator<CreateResoStep2ViewModel>() {
        @Override
        public CreateResoStep2ViewModel createFromParcel(Parcel source) {
            return new CreateResoStep2ViewModel(source);
        }

        @Override
        public CreateResoStep2ViewModel[] newArray(int size) {
            return new CreateResoStep2ViewModel[size];
        }
    };
}
