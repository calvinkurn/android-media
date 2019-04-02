package com.tokopedia.discovery.newdiscovery.search.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by henrypriyono on 10/30/17.
 */

public class SuggestionModel implements Parcelable {
    private String suggestionText;
    private String suggestionCurrentKeyword;
    private String suggestedQuery;
    private String formattedResultCount;

    public String getSuggestionText() {
        return suggestionText;
    }

    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

    public String getSuggestionCurrentKeyword() {
        return suggestionCurrentKeyword;
    }

    public void setSuggestionCurrentKeyword(String suggestionCurrentKeyword) {
        this.suggestionCurrentKeyword = suggestionCurrentKeyword;
    }

    public String getSuggestedQuery() {
        return suggestedQuery;
    }

    public void setSuggestedQuery(String suggestedQuery) {
        this.suggestedQuery = suggestedQuery;
    }

    public String getFormattedResultCount() {
        return formattedResultCount;
    }

    public void setFormattedResultCount(String formattedResultCount) {
        this.formattedResultCount = formattedResultCount;
    }

    public SuggestionModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.suggestionText);
        dest.writeString(this.suggestionCurrentKeyword);
        dest.writeString(this.suggestedQuery);
        dest.writeString(this.formattedResultCount);
    }

    protected SuggestionModel(Parcel in) {
        this.suggestionText = in.readString();
        this.suggestionCurrentKeyword = in.readString();
        this.suggestedQuery = in.readString();
        this.formattedResultCount = in.readString();
    }

    public static final Creator<SuggestionModel> CREATOR = new Creator<SuggestionModel>() {
        @Override
        public SuggestionModel createFromParcel(Parcel source) {
            return new SuggestionModel(source);
        }

        @Override
        public SuggestionModel[] newArray(int size) {
            return new SuggestionModel[size];
        }
    };
}
