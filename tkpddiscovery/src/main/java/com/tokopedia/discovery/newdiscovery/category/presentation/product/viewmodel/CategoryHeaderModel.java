package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.core.network.entity.intermediary.Child;
import com.tokopedia.core.network.entity.intermediary.Image;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.discovery.categorynav.domain.model.Category;
import com.tokopedia.discovery.intermediary.domain.model.IntermediaryCategoryDomainModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.typefactory.CategoryProductListTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 3/24/17.
 */

public class CategoryHeaderModel implements Parcelable , Visitable<CategoryProductListTypeFactory> {

    public static final String LIFESTYLE_TEMPLATE = "LIFESTYLE";
    public static final String DEFAULT_TEMPLATE = "DEFAULT";
    public static final String TECHNOLOGY_TEMPLATE = "TECHNOLOGY";

    private boolean isRevamp = false;
    private String departementId = "0";
    private int isAdult;
    private String template = DEFAULT_TEMPLATE;
    private HeaderModel headerModel;
    private List<ChildCategoryModel> childCategoryModelList = new ArrayList<>();
    private List<BannerModel> bannerModelList = new ArrayList<>();
    private int totalData = 0;
    private String rootCategoryId;
    private String headerImage;
    private String headerImageHexColor;
    private boolean doneTrackImpression = false;
    private List<QuickFilterItem> optionList = new ArrayList<>();

    public CategoryHeaderModel(){

    }

    public HeaderModel getHeaderModel() {
        return headerModel;
    }

    public void setHeaderModel(HeaderModel headerModel) {
        this.headerModel = headerModel;
    }

    public List<ChildCategoryModel> getChildCategoryModelList() {
        return childCategoryModelList;
    }

    public void setChildCategoryModelList(List<ChildCategoryModel> childCategoryModelList) {
        this.childCategoryModelList = childCategoryModelList;
    }

    public boolean isRevamp() {
        return isRevamp;
    }

    public void setRevamp(boolean revamp) {
        isRevamp = revamp;
    }

    public String getDepartementId() {
        return departementId;
    }

    public void setDepartementId(String departementId) {
        this.departementId = departementId;
    }

    public List<BannerModel> getBannerModelList() {
        return bannerModelList;
    }

    public void setBannerModelList(List<BannerModel> bannerModelList) {
        this.bannerModelList = bannerModelList;
    }

    public int getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(int isAdult) {
        this.isAdult = isAdult;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    @Override
    public int type(CategoryProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public static CategoryHeaderModel convertIntermediaryToCategoryHeader
            (CategoryHadesModel categoryHadesModel) {
        CategoryHeaderModel categoryHeaderModel = new CategoryHeaderModel();
        categoryHeaderModel.setRevamp(categoryHadesModel.getData().getIsRevamp());
        categoryHeaderModel.setDepartementId(categoryHadesModel.getData().getId());
        categoryHeaderModel.setTemplate(categoryHadesModel.getData().getTemplate());
        categoryHeaderModel.setRootCategoryId(String.valueOf(categoryHadesModel.getData().getRootCategoryId()));
        categoryHeaderModel.setIsAdult(categoryHadesModel.getData().getIsAdult());
        categoryHeaderModel.setHeaderImage(categoryHadesModel.getData().getHeaderImage());
        categoryHeaderModel.setHeaderImageHexColor(categoryHadesModel.getData().getHeaderHexColor());
        HeaderModel headerModel = new HeaderModel();
        headerModel.setCategoryName(categoryHadesModel.getData().getName());
        headerModel.setHeaderImageUrl(categoryHadesModel.getData().getHeaderImage());
        categoryHeaderModel.setHeaderModel(headerModel);
        List<BannerModel> bannerModelList = new ArrayList<>();
        if (categoryHadesModel.getData().getBanner()!=null && categoryHadesModel.getData().getBanner().getImages()!=null) {
            for (Image image: categoryHadesModel.getData().getBanner().getImages()) {
                BannerModel bannerModel = new BannerModel();
                bannerModel.setUrl(image.getUrl());
                bannerModel.setImageUrl(image.getImageUrl());
                bannerModel.setPosition(image.getPosition());
                bannerModelList.add(bannerModel);
            }
        }
        categoryHeaderModel.setBannerModelList(bannerModelList);
        List<ChildCategoryModel> categoryModelList = new ArrayList<>();
        if (categoryHadesModel.getData()!=null && categoryHadesModel.getData().getChild()!=null) {
            for (Child child: categoryHadesModel.getData().getChild()) {
                ChildCategoryModel childCategoryModel = new ChildCategoryModel();
                childCategoryModel.setCategoryId(child.getId());
                childCategoryModel.setIsAdult(child.getIsAdult());
                childCategoryModel.setCategoryImageUrl(child.getThumbnailImage());
                childCategoryModel.setCategoryName(child.getName());
                childCategoryModel.setCategoryUrl(child.getUrl());
                categoryModelList.add(childCategoryModel);
            }
        }
        categoryHeaderModel.setChildCategoryModelList(categoryModelList);
        return  categoryHeaderModel;
    }

    public void setRootCategoryId(String rootCategoryId) {
        this.rootCategoryId = rootCategoryId;
    }

    public String getRootCategoryId() {
        return rootCategoryId;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImageHexColor(String headerImageHexColor) {
        this.headerImageHexColor = headerImageHexColor;
    }

    public String getHeaderImageHexColor() {
        return headerImageHexColor;
    }

    public boolean isDoneTrackImpression() {
        return doneTrackImpression;
    }

    public void setDoneTrackImpression(boolean doneTrackImpression) {
        this.doneTrackImpression = doneTrackImpression;
    }

    public void setQuickFilterList(List<QuickFilterItem> optionList) {
        this.optionList=optionList;
    }

    public List<QuickFilterItem> getOptionList() {
        return optionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isRevamp ? (byte) 1 : (byte) 0);
        dest.writeString(this.departementId);
        dest.writeInt(this.isAdult);
        dest.writeString(this.template);
        dest.writeParcelable(this.headerModel, flags);
        dest.writeTypedList(this.childCategoryModelList);
        dest.writeTypedList(this.bannerModelList);
        dest.writeInt(this.totalData);
        dest.writeString(this.rootCategoryId);
        dest.writeString(this.headerImage);
        dest.writeString(this.headerImageHexColor);
        dest.writeByte(this.doneTrackImpression ? (byte) 1 : (byte) 0);
    }

    protected CategoryHeaderModel(Parcel in) {
        this.isRevamp = in.readByte() != 0;
        this.departementId = in.readString();
        this.isAdult = in.readInt();
        this.template = in.readString();
        this.headerModel = in.readParcelable(HeaderModel.class.getClassLoader());
        this.childCategoryModelList = in.createTypedArrayList(ChildCategoryModel.CREATOR);
        this.bannerModelList = in.createTypedArrayList(BannerModel.CREATOR);
        this.totalData = in.readInt();
        this.rootCategoryId = in.readString();
        this.headerImage = in.readString();
        this.headerImageHexColor = in.readString();
        this.doneTrackImpression = in.readByte() != 0;
    }

    public static final Creator<CategoryHeaderModel> CREATOR = new Creator<CategoryHeaderModel>() {
        @Override
        public CategoryHeaderModel createFromParcel(Parcel source) {
            return new CategoryHeaderModel(source);
        }

        @Override
        public CategoryHeaderModel[] newArray(int size) {
            return new CategoryHeaderModel[size];
        }
    };
}
