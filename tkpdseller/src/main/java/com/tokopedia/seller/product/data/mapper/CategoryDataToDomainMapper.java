package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase;
import com.tokopedia.seller.product.domain.model.CategoryGroupDomainModel;

import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryDataToDomainMapper implements Func1<List<CategoryDataBase>, CategoryGroupDomainModel> {
    @Override
    public CategoryGroupDomainModel call(List<CategoryDataBase> categoryDataBases) {
        return null;
    }
}
