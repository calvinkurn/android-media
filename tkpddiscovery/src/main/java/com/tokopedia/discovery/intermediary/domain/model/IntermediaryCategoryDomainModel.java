package com.tokopedia.discovery.intermediary.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 3/24/17.
 */

public class IntermediaryCategoryDomainModel {

    public static final String LIFESTYLE_TEMPLATE = "LIFESTYLE";
    public static final String DEFAULT_TEMPLATE = "DEFAULT";
    public static final String TECHNOLOGY_TEMPLATE = "TECHNOLOGY";

    boolean isIntermediary = false;
    boolean isRevamp = false;
    String departementId = "0";
    String template = "";
    HeaderModel headerModel;
    List<ChildCategoryModel> childCategoryModelList = new ArrayList<>();
    List<CuratedSectionModel> curatedSectionModelList = new ArrayList<>();
    List<HotListModel> hotListModelList = new ArrayList<>();
    List<BannerModel> bannerModelList = new ArrayList<>();
    List<BrandModel> brandModelList = new ArrayList<>();
    VideoModel videoModel;

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

    public List<CuratedSectionModel> getCuratedSectionModelList() {
        return curatedSectionModelList;
    }

    public void setCuratedSectionModelList(List<CuratedSectionModel> curatedSectionModelList) {
        this.curatedSectionModelList = curatedSectionModelList;
    }

    public List<HotListModel> getHotListModelList() {
        return hotListModelList;
    }

    public void setHotListModelList(List<HotListModel> hotListModelList) {
        this.hotListModelList = hotListModelList;
    }

    public boolean isIntermediary() {
        return isIntermediary;
    }

    public void setIntermediary(boolean intermediary) {
        isIntermediary = intermediary;
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

    public VideoModel getVideoModel() {
        return videoModel;
    }

    public void setVideoModel(VideoModel videoModel) {
        this.videoModel = videoModel;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<BrandModel> getBrandModelList() {
        return brandModelList;
    }

    public void setBrandModelList(List<BrandModel> brandModelList) {
        this.brandModelList = brandModelList;
    }

    public boolean isRevamp() {
        return isRevamp;
    }

    public void setRevamp(boolean revamp) {
        isRevamp = revamp;
    }
}
