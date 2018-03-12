package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.events.data.entity.response.Form;

import java.util.List;

public class EventsDetailsViewModel implements Parcelable {


    private String title;
    private String duration;
    private String url;
    private String timeRange;
    private String genre;
    private String imageApp;
    private String thumbnailApp;
    private String tnc;
    private String offerText;
    private String longRichDesc;
    private String displayTags;
    private String promotionText;
    private String address;
    private int convenienceFee;
    private int mrp;
    private int salesPrice;
    private int seatChartTypeId;
    private int hasSeatLayout;
    private int thumbsUp;
    private int thumbsDown;
    private int rating;
    private int isFeatured;
    private int isFoodAvailable;
    private String seatMapImage;
    private Boolean dateRange;
    private String cityName;
    private List<SchedulesViewModel> schedulesViewModels;

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    private List<Form> forms;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageApp() {
        return imageApp;
    }

    public void setImageApp(String imageApp) {
        this.imageApp = imageApp;
    }

    public String getThumbnailApp() {
        return thumbnailApp;
    }

    public void setThumbnailApp(String thumbnailApp) {
        this.thumbnailApp = thumbnailApp;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }


    public String getLongRichDesc() {
        return longRichDesc;
    }

    public void setLongRichDesc(String longRichDesc) {
        this.longRichDesc = longRichDesc;
    }


    public String getPromotionText() {
        return promotionText;
    }

    public void setPromotionText(String promotionText) {
        this.promotionText = promotionText;
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(int convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getSeatChartTypeId() {
        return seatChartTypeId;
    }

    public void setSeatChartTypeId(int seatChartTypeId) {
        this.seatChartTypeId = seatChartTypeId;
    }

    public int getHasSeatLayout() {
        return hasSeatLayout;
    }

    public void setHasSeatLayout(int hasSeatLayout) {
        this.hasSeatLayout = hasSeatLayout;
    }

    public String getSeatMapImage() {
        return seatMapImage;
    }

    public void setSeatMapImage(String seatMapImage) {
        this.seatMapImage = seatMapImage;
    }


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    public int getIsFoodAvailable() {
        return isFoodAvailable;
    }

    public void setIsFoodAvailable(int isFoodAvailable) {
        this.isFoodAvailable = isFoodAvailable;
    }


    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public int getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(int thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public int getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(int isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Boolean getDateRange() {
        return dateRange;
    }

    public void setDateRange(Boolean dateRange) {
        this.dateRange = dateRange;
    }

    public List<SchedulesViewModel> getSchedulesViewModels() {
        return schedulesViewModels;
    }

    public void setSchedulesViewModels(List<SchedulesViewModel> schedulesViewModels) {
        this.schedulesViewModels = schedulesViewModels;
    }

    public String getDisplayTags() {
        return displayTags;
    }

    public void setDisplayTags(String displayTags) {
        this.displayTags = displayTags;
    }

    public EventsDetailsViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.duration);
        dest.writeString(this.url);
        dest.writeString(this.timeRange);
        dest.writeString(this.genre);
        dest.writeString(this.imageApp);
        dest.writeString(this.thumbnailApp);
        dest.writeString(this.tnc);
        dest.writeString(this.offerText);
        dest.writeString(this.longRichDesc);
        dest.writeString(this.displayTags);
        dest.writeString(this.promotionText);
        dest.writeString(this.address);
        dest.writeInt(this.convenienceFee);
        dest.writeInt(this.mrp);
        dest.writeInt(this.salesPrice);
        dest.writeInt(this.seatChartTypeId);
        dest.writeInt(this.hasSeatLayout);
        dest.writeInt(this.thumbsUp);
        dest.writeInt(this.thumbsDown);
        dest.writeInt(this.rating);
        dest.writeInt(this.isFeatured);
        dest.writeInt(this.isFoodAvailable);
        dest.writeString(this.seatMapImage);
        dest.writeValue(this.dateRange);
        dest.writeString(this.cityName);
        dest.writeTypedList(this.schedulesViewModels);
        dest.writeTypedList(this.forms);
    }

    protected EventsDetailsViewModel(Parcel in) {
        this.title = in.readString();
        this.duration = in.readString();
        this.url = in.readString();
        this.timeRange = in.readString();
        this.genre = in.readString();
        this.imageApp = in.readString();
        this.thumbnailApp = in.readString();
        this.tnc = in.readString();
        this.offerText = in.readString();
        this.longRichDesc = in.readString();
        this.displayTags = in.readString();
        this.promotionText = in.readString();
        this.address = in.readString();
        this.convenienceFee = in.readInt();
        this.mrp = in.readInt();
        this.salesPrice = in.readInt();
        this.seatChartTypeId = in.readInt();
        this.hasSeatLayout = in.readInt();
        this.thumbsUp = in.readInt();
        this.thumbsDown = in.readInt();
        this.rating = in.readInt();
        this.isFeatured = in.readInt();
        this.isFoodAvailable = in.readInt();
        this.seatMapImage = in.readString();
        this.dateRange = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.cityName = in.readString();
        this.schedulesViewModels = in.createTypedArrayList(SchedulesViewModel.CREATOR);
        this.forms = in.createTypedArrayList(Form.CREATOR);
    }

    public static final Creator<EventsDetailsViewModel> CREATOR = new Creator<EventsDetailsViewModel>() {
        @Override
        public EventsDetailsViewModel createFromParcel(Parcel source) {
            return new EventsDetailsViewModel(source);
        }

        @Override
        public EventsDetailsViewModel[] newArray(int size) {
            return new EventsDetailsViewModel[size];
        }
    };
}