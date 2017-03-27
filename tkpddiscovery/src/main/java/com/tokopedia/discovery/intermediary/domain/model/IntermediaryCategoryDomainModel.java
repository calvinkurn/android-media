package com.tokopedia.discovery.intermediary.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 3/24/17.
 */

public class IntermediaryCategoryDomainModel {

    HeaderModel headerModel;
    List<ChildCategoryModel> childCategoryModelList = new ArrayList<>();
    List<CuratedSectionModel> curatedSectionModelList = new ArrayList<>();

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
}
