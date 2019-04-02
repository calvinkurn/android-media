package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.core.network.entity.intermediary.Child;
import com.tokopedia.core.network.entity.intermediary.Image;
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
    private String template = DEFAULT_TEMPLATE;
    private HeaderModel headerModel;
    private List<ChildCategoryModel> childCategoryModelList = new ArrayList<>();
    private List<BannerModel> bannerModelList = new ArrayList<>();
    private int totalData = 0;
    private String rootCategoryId;
    private String headerImage;
    private String headerImageHexColor;
    private boolean doneTrackImpression = false;

    public CategoryHeaderModel(){

    }

    protected CategoryHeaderModel(Parcel in) {
        isRevamp = in.readByte() != 0x00;
        departementId = in.readString();
        template = in.readString();
        headerModel = (HeaderModel) in.readValue(HeaderModel.class.getClassLoader());
        if (in.readByte() == 0x01) {
            childCategoryModelList = new ArrayList<ChildCategoryModel>();
            in.readList(childCategoryModelList, ChildCategoryModel.class.getClassLoader());
        } else {
            childCategoryModelList = null;
        }
        if (in.readByte() == 0x01) {
            bannerModelList = new ArrayList<BannerModel>();
            in.readList(bannerModelList, BannerModel.class.getClassLoader());
        } else {
            bannerModelList = null;
        }
        totalData = in.readInt();
        rootCategoryId = in.readString();
        headerImage = in.readString();
        headerImageHexColor = in.readString();
        doneTrackImpression = in.readInt() == 1 ? true : false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isRevamp ? 0x01 : 0x00));
        dest.writeString(departementId);
        dest.writeString(template);
        dest.writeValue(headerModel);
        if (childCategoryModelList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(childCategoryModelList);
        }
        if (bannerModelList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(bannerModelList);
        }
        dest.writeInt(totalData);
        dest.writeString(rootCategoryId);
        dest.writeString(headerImage);
        dest.writeString(headerImageHexColor);
        dest.writeInt(doneTrackImpression ? 1 : 0);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CategoryHeaderModel> CREATOR = new Parcelable.Creator<CategoryHeaderModel>() {
        @Override
        public CategoryHeaderModel createFromParcel(Parcel in) {
            return new CategoryHeaderModel(in);
        }

        @Override
        public CategoryHeaderModel[] newArray(int size) {
            return new CategoryHeaderModel[size];
        }
    };

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

}
