package com.tokopedia.seller.opportunity.data.source.local;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.CategoryList;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.OpportunityCategoryData;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.ShippingType;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.SortingType;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 3/23/17.
 */

public class LocalGetFilterOpportunitySource {

    private GlobalCacheManager globalCacheManager;


    public LocalGetFilterOpportunitySource(GlobalCacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    public Observable<OpportunityCategoryModel> getFilter() {

        return Observable.just(GetOpportunityFilterUseCase.FILTER_CACHE)
                .map(new Func1<String, OpportunityCategoryModel>() {
                    @Override
                    public OpportunityCategoryModel call(String key) {
//                        CommonUtils.dumper("NISNIS GET FILTER CACHE");
//                        if (getCache(key) != null)
//                            return CacheUtil.convertStringToModel(getCache(key),
//                                    new TypeToken<OpportunityCategoryModel>() {
//                                    }.getType());
//                        else throw new RuntimeException("NO CACHE");

                        OpportunityCategoryModel categoryModel = new OpportunityCategoryModel();
                        OpportunityCategoryData data = new OpportunityCategoryData();
                        createFakeCategoryData(data);
                        createFakeSortData(data);
                        createFakeShippingData(data);
                        categoryModel.setOpportunityCategoryData(data);
                        return categoryModel;
                    }
                });
    }

    private String getCache(String key) {
        return globalCacheManager.getValueString(key);
    }

    private void createFakeCategoryData(OpportunityCategoryData data) {
        ArrayList<CategoryList> categoryLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CategoryList item = new CategoryList();
            item.setId(String.valueOf(i));

            item.setHidden(0);
            item.setIdentifier("asdasdasd" + i);
            item.setTree(1);



            item.setName("category " + i);
            item.setParent(1);
            item.setChild(createFakeChildCategoryData(item.getName()));
            categoryLists.add(item);
        }
        data.setCategoryList(categoryLists);
    }

    private ArrayList<CategoryList> createFakeChildCategoryData(String name) {
        ArrayList<CategoryList> categoryLists = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CategoryList item = new CategoryList();
            item.setId(String.valueOf(i));
            item.setHidden(0);
            item.setIdentifier("asdasdasd" + i);
            item.setTree(1);
            item.setName("child " + i + " of " + name);
            item.setParent(1);
            item.setChild(createFakeChild2CategoryData(item.getName()));
            categoryLists.add(item);
        }
        return categoryLists;
    }

    private ArrayList<CategoryList> createFakeChild2CategoryData(String name) {
        ArrayList<CategoryList> categoryLists = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CategoryList item = new CategoryList();
            item.setId(String.valueOf(i));
            item.setHidden(0);
            item.setIdentifier("asdasdasd" + i);
            item.setTree(1);
            item.setName("child " + i + " of " + name);
            item.setParent(1);
            item.setChild(new ArrayList<CategoryList>());
            categoryLists.add(item);
        }
        return categoryLists;
    }

    private void createFakeShippingData(OpportunityCategoryData data) {
        ArrayList<ShippingType> shippingTypes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ShippingType item = new ShippingType();
            item.setShippingTypeID(i);
            item.setShippingTypeName("Shipping " + i);
            shippingTypes.add(item);
        }
        data.setShippingType(shippingTypes);
    }

    private void createFakeSortData(OpportunityCategoryData data) {
        ArrayList<SortingType> sortingTypes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SortingType item = new SortingType();
            item.setSortingTypeID(i);
            item.setSortingTypeName("Sorting " + i);
            sortingTypes.add(item);
        }
        data.setSortingType(sortingTypes);
    }

}
