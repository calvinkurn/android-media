package com.tokopedia.seller.opportunity.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.domain.ReplacementRepository;
import com.tokopedia.seller.opportunity.viewmodel.CategoryViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityItemViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityListPageViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityViewModel;
import com.tokopedia.seller.opportunity.viewmodel.ShippingTypeViewModel;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunityListUseCase extends UseCase<OpportunityListPageViewModel> {

    public static final String USER_ID = "user_id";
    public static final String PER_PAGE = "per_page";
    public static final String PAGE = "page";
    public static final String CAT_1 = "cat_1";
    public static final String CAT_2 = "cat_2";
    public static final String CAT_3 = "cat_3";
    public static final String SHIP_TYPE = "ship_type";
    public static final String ORDER_BY = "order_by";
    public static final String SHOP_ID = "shop_id";
    public static final String DEVICE_ID = "device_id";
    public static final String OS_TYPE = "os_type";


    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private final GetOpportunityUseCase getOpportunityUseCase;
    private final GetOpportunityFilterUseCase getOpportunityFilterUseCase;

    public OpportunityListUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  ReplacementRepository repository,
                                  GetOpportunityUseCase getOpportunityUseCase,
                                  GetOpportunityFilterUseCase getOpportunityFilterUseCase
    ) {
        super(threadExecutor, postExecutionThread);
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.getOpportunityUseCase = getOpportunityUseCase;
        this.getOpportunityFilterUseCase = getOpportunityFilterUseCase;
    }

    @Override
    public Observable<OpportunityListPageViewModel> createObservable(RequestParams params) {
//        return Observable.zip(
//                getOpportunityListObservable(params),
//                getOpportunityFilterObservable(params),
//                new Func2<OpportunityModel,
//                        OpportunityCategoryModel,
//                        OpportunityListPageViewModel>() {
//                    @Override
//                    public OpportunityListPageViewModel call(OpportunityModel opportunityModel,
//                                                             OpportunityCategoryModel opportunityCategoryModel) {
//                        return getCombinedPageData(opportunityModel, opportunityCategoryModel);
//                    }
//                }).onErrorReturn(new Func1<Throwable, OpportunityListPageViewModel>() {
//            @Override
//            public OpportunityListPageViewModel call(Throwable throwable) {
//                return null;
//            }
//        });
        return Observable.just(getCombinedPageData(new OpportunityModel(), new OpportunityCategoryModel()));

    }

    private OpportunityListPageViewModel getCombinedPageData(OpportunityModel opportunityModel,
                                                             OpportunityCategoryModel opportunityCategoryModel) {

        OpportunityListPageViewModel viewModel = new OpportunityListPageViewModel();
        viewModel.setOpportunityViewModel(getOpportunityViewModel(opportunityModel));
        viewModel.setListCategory(getListCategory(opportunityCategoryModel));
        viewModel.setListShippingType(getListShippingType(opportunityCategoryModel));
        viewModel.setListSortingType(getListSortingType(opportunityCategoryModel));
        return viewModel;
    }

    private ArrayList<SortingTypeViewModel> getListSortingType(OpportunityCategoryModel opportunityCategoryModel) {
        ArrayList<SortingTypeViewModel> list = new ArrayList<>();
        list.add(new SortingTypeViewModel("Jatuh Tempo", 1));
        list.add(new SortingTypeViewModel("Nilai Transaksi Tertinggi", 2));
        list.add(new SortingTypeViewModel("Nilai Transaksi Terendah", 3));

        return list;
    }

    private ArrayList<ShippingTypeViewModel> getListShippingType(OpportunityCategoryModel opportunityCategoryModel) {
        ArrayList<ShippingTypeViewModel> list = new ArrayList<>();
        list.add(new ShippingTypeViewModel("Same Day", 1));
        list.add(new ShippingTypeViewModel("Next Day", 2));
        list.add(new ShippingTypeViewModel("Reguler Day", 3));

        return list;
    }

    private ArrayList<CategoryViewModel> getListCategory(OpportunityCategoryModel opportunityCategoryModel) {
        ArrayList<CategoryViewModel> list = new ArrayList<>();
        CategoryViewModel cat1 = new CategoryViewModel();
        cat1.setCategoryId(1);
        cat1.setCategoryName("Category 1");
        cat1.setTreeLevel(1);

        CategoryViewModel cat2 = new CategoryViewModel();
        cat2.setCategoryId(2);
        cat2.setCategoryName("Category 2");
        cat2.setTreeLevel(2);

        ArrayList<CategoryViewModel> list2 = new ArrayList<>();
        CategoryViewModel cat11 = new CategoryViewModel();
        cat11.setCategoryId(11);
        cat11.setCategoryName("Category 1-1");
        cat11.setTreeLevel(2);
        CategoryViewModel cat12 = new CategoryViewModel();
        cat12.setCategoryId(12);
        cat12.setCategoryName("Category 1-2");
        cat12.setTreeLevel(2);

        list2.add(cat11);
        list2.add(cat12);

        cat1.setListChild(list2);


        list.add(cat1);
        list.add(cat2);

        return list;
    }

    private OpportunityViewModel getOpportunityViewModel(OpportunityModel opportunityModel) {
        ArrayList<OpportunityItemViewModel> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(new OpportunityItemViewModel("Produk " + i));
        }

        OpportunityViewModel viewModel = new OpportunityViewModel();
        viewModel.setListOpportunity(list);

        return viewModel;
    }

    private Observable<OpportunityCategoryModel> getOpportunityFilterObservable(RequestParams requestParams) {
        return getOpportunityFilterUseCase.createObservable(getOpportunityFilterParam(requestParams))
                .onErrorReturn(new Func1<Throwable, OpportunityCategoryModel>() {
            @Override
            public OpportunityCategoryModel call(Throwable throwable) {
                throwable.printStackTrace();
                return OpportunityCategoryModel.createEmptyModel();
            }
        });

    }

    private RequestParams getOpportunityFilterParam(RequestParams requestParams) {
        RequestParams filterParam = RequestParams.create();
        filterParam.putString(GetOpportunityFilterUseCase.USER_ID,
                requestParams.getString(OpportunityListUseCase.USER_ID,
                        SessionHandler.getLoginID(MainApplication.getAppContext())));
        filterParam.putString(GetOpportunityFilterUseCase.SHOP_ID,
                requestParams.getString(OpportunityListUseCase.SHOP_ID,
                        SessionHandler.getShopID(MainApplication.getAppContext())));
        filterParam.putString(GetOpportunityFilterUseCase.DEVICE_ID,
                requestParams.getString(OpportunityListUseCase.DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())));
        filterParam.putString(GetOpportunityFilterUseCase.OS_TYPE,
                requestParams.getString(OpportunityListUseCase.OS_TYPE, "1"));
        return filterParam;
    }

    private Observable<OpportunityModel> getOpportunityListObservable(RequestParams requestParams) {

        return getOpportunityUseCase
                .createObservable(getOpportunityListParam(requestParams))
                .onErrorReturn(new Func1<Throwable, OpportunityModel>() {
                    @Override
                    public OpportunityModel call(Throwable throwable) {
                        throwable.printStackTrace();
                        return OpportunityModel.createEmptyModel();
                    }
                });
    }

    private RequestParams getOpportunityListParam(RequestParams requestParams) {
        RequestParams getListParam = RequestParams.create();

        getListParam.putString(GetOpportunityUseCase.USER_ID,
                requestParams.getString(OpportunityListUseCase.USER_ID,
                        SessionHandler.getLoginID(MainApplication.getAppContext())));

        getListParam.putString(GetOpportunityUseCase.DEVICE_ID,
                requestParams.getString(OpportunityListUseCase.DEVICE_ID,
                        GCMHandler.getRegistrationId(MainApplication.getAppContext())));

        getListParam.putString(GetOpportunityUseCase.OS_TYPE,
                requestParams.getString(OpportunityListUseCase.OS_TYPE, "1"));

        getListParam.putString(GetOpportunityUseCase.PER_PAGE,
                requestParams.getString(OpportunityListUseCase.PER_PAGE, "10"));

        getListParam.putString(GetOpportunityUseCase.PAGE,
                requestParams.getString(OpportunityListUseCase.PAGE, "1"));

        if (!requestParams.getString(GetOpportunityUseCase.CAT_1, "").equals(""))
            getListParam.putString(GetOpportunityUseCase.CAT_1,
                    requestParams.getString(OpportunityListUseCase.CAT_1, ""));

        if (!requestParams.getString(GetOpportunityUseCase.CAT_2, "").equals(""))
            getListParam.putString(GetOpportunityUseCase.CAT_2,
                    requestParams.getString(OpportunityListUseCase.CAT_2, ""));

        if (!requestParams.getString(GetOpportunityUseCase.CAT_3, "").equals(""))
            getListParam.putString(GetOpportunityUseCase.CAT_3,
                    requestParams.getString(OpportunityListUseCase.CAT_3, ""));

        if (!requestParams.getString(GetOpportunityUseCase.SHIP_TYPE, "").equals(""))
            getListParam.putString(GetOpportunityUseCase.SHIP_TYPE,
                    requestParams.getString(OpportunityListUseCase.SHIP_TYPE, ""));

        if (!requestParams.getString(GetOpportunityUseCase.ORDER_BY, "").equals(""))
            getListParam.putString(GetOpportunityUseCase.ORDER_BY,
                    requestParams.getString(OpportunityListUseCase.ORDER_BY, ""));

        return getListParam;

    }

}
