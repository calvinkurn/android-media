package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.mapper.CategoryDataToDomainMapper;
import com.tokopedia.seller.product.data.source.db.CategoryDataManager;
import com.tokopedia.seller.product.data.source.db.model.CategoryDataBase;
import com.tokopedia.seller.product.domain.model.CategoryDomainModel;
import com.tokopedia.seller.product.domain.model.CategoryLevelDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/7/17.
 */

public class FetchCategoryDataSource {


    private final CategoryDataManager categoryDataManager;

    @Inject
    public FetchCategoryDataSource(CategoryDataManager categoryDataManager) {
        this.categoryDataManager = categoryDataManager;
    }

    public Observable<List<CategoryDomainModel>> fetchCategoryLevelOne(int parent) {
        return Observable.just(parent)
                .map(new FetchFromParent())
                .map(new CategoryDataToDomainMapper());
    }

    public Observable<List<CategoryLevelDomainModel>> fetchCategoryFromSelected(int initSelected) {
        return Observable
                .just(initSelected)
                .map(new FetchCategoryFromSelected());
    }

    private class FetchCategoryFromSelected implements Func1<Integer, List<CategoryLevelDomainModel>> {
        @Override
        public List<CategoryLevelDomainModel> call(Integer selectedId) {
            List<CategoryLevelDomainModel> categoryLevelDomainModels = new ArrayList<>();
            int currentLevelSelected = selectedId;
            do {
                CategoryLevelDomainModel categoryLevelDomain = new CategoryLevelDomainModel();
                categoryLevelDomain.setSelected(currentLevelSelected);

                int parentId = getParentId(currentLevelSelected);
                categoryLevelDomain.setParent(parentId);
                List<CategoryDataBase> categoryFromParent = getCategoryFromParent(parentId);
                categoryLevelDomain.setCategoryModels(
                        CategoryDataToDomainMapper.mapDomainModels(categoryFromParent)
                );

                categoryLevelDomainModels.add(0, categoryLevelDomain);

                currentLevelSelected = parentId;
            } while (
                    categoryLevelDomainModels.get(0).getParent()
                            != CategoryDataBase.LEVEL_ONE_PARENT
                    );

            return categoryLevelDomainModels;
        }


    }

    private class FetchFromParent implements Func1<Integer, List<CategoryDataBase>> {

        @Override
        public List<CategoryDataBase> call(Integer parent) {
            return getCategoryFromParent(parent);
        }
    }

    private List<CategoryDataBase> getCategoryFromParent(int parent) {
        return categoryDataManager
                .fetchCategoryFromParent(parent);
    }

    private int getParentId(Integer selectedId) {
        CategoryDataBase selectedCategory = categoryDataManager.fetchCategoryWithId(selectedId);
        return selectedCategory.getParentId();
    }
}
