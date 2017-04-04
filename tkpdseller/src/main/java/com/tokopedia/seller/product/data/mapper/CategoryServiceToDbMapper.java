package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.CategoryServiceModel;
import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase;

import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryServiceToDbMapper implements Func1<CategoryServiceModel, List<CategoryDataBase>> {
    @Override
    public List<CategoryDataBase> call(CategoryServiceModel categoryServiceModels) {
        return null;
    }
}
