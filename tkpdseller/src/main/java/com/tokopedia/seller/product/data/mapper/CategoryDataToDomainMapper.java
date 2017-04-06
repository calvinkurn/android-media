package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryDataToDomainMapper implements Func1<List<CategoryDataBase>, List<CategoryDomainModel>> {
    @Override
    public List<CategoryDomainModel> call(List<CategoryDataBase> categoryDataBases) {

        return mapDomainModels(categoryDataBases);
    }

    private List<CategoryDomainModel> mapDomainModels(List<CategoryDataBase> categoryDataBases) {
        List<CategoryDomainModel> domainModels = new ArrayList<>();
        for (CategoryDataBase categoryDataBase : categoryDataBases) {
            CategoryDomainModel domainModel = new CategoryDomainModel();
            domainModel.setName(categoryDataBase.getName());
            domainModel.setId(categoryDataBase.getId());
            domainModel.setIdentifier(categoryDataBase.getIdentifier());
            domainModel.setHasChild(categoryDataBase.isHasChild());
            domainModels.add(domainModel);
        }
        return domainModels;
    }

}
