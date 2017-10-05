package com.tokopedia.seller.opportunity.data.source.local;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.FilterData;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.OpportunityCategoryData;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.OptionItem;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.SearchData;
import com.tokopedia.core.network.entity.replacement.opportunitycategorydata.SortData;
import com.tokopedia.seller.opportunity.data.OpportunityFilterModel;
import com.tokopedia.seller.opportunity.domain.interactor.GetOpportunityFilterUseCase;

import java.util.ArrayList;

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

    public Observable<OpportunityFilterModel> getFilter() {

        return Observable.just(GetOpportunityFilterUseCase.FILTER_CACHE)
                .map(new Func1<String, OpportunityFilterModel>() {
                    @Override
                    public OpportunityFilterModel call(String key) {
                        CommonUtils.dumper("NISNIS GET FILTER CACHE");
                        if (getCache(key) != null)
                            return CacheUtil.convertStringToModel(getCache(key),
                                    new TypeToken<OpportunityFilterModel>() {
                                    }.getType());
                        else throw new RuntimeException("NO CACHE");

//                        OpportunityCategoryModel categoryModel = new OpportunityCategoryModel();
//                        OpportunityCategoryData data = new OpportunityCategoryData();
//                        createFakeCategoryData(data);
//                        createFakeSortData(data);
//                        categoryModel.setOpportunityCategoryData(data);
//                        return categoryModel;
                    }
                });
    }

    private String getCache(String key) {
        return globalCacheManager.getValueString(key);
    }

    private void createFakeCategoryData(OpportunityCategoryData data) {
        ArrayList<FilterData> filterDatas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            FilterData item = new FilterData();
            item.setTitle("Filter Title " + i);

            SearchData searchData = new SearchData();
            searchData.setPlaceholder("");
            searchData.setSearchable(i % 2 == 0 ? 1 : 0);

            item.setSearchData(searchData);

            item.setOptionItemList(createFakeChildCategoryData(item.getTitle()));
            filterDatas.add(item);
        }
        data.setFilter(filterDatas);
    }

    private ArrayList<OptionItem> createFakeChildCategoryData(String name) {
        ArrayList<OptionItem> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OptionItem item = new OptionItem();
            item.setValue("value" + i);
            item.setHidden(0);
            item.setIdentifier("asdasdasd" + i);
            item.setTree(1);
            item.setName("child " + i + " of " + name);
            item.setParent(1);
            item.setKey("key" + i);
            item.setListChild(createFakeChild2CategoryData(item.getName()));
            list.add(item);
        }
        return list;
    }

    private ArrayList<OptionItem> createFakeChild2CategoryData(String name) {
        ArrayList<OptionItem> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OptionItem item = new OptionItem();
            item.setValue("childvalue" + i);
            item.setHidden(0);
            item.setIdentifier("asdasdasd" + i);
            item.setTree(1);
            item.setName("grandchild " + i + " of " + name);
            item.setParent(1);
            item.setKey("childkey" + i);
            item.setListChild(new ArrayList<OptionItem>());
            list.add(item);
        }
        return list;
    }

    private void createFakeSortData(OpportunityCategoryData data) {
        ArrayList<SortData> sortDatas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SortData item = new SortData();
            item.setValue("sort" + i);
            item.setName("Sorting " + i);
            sortDatas.add(item);
        }
        data.setSort(sortDatas);
    }

}
