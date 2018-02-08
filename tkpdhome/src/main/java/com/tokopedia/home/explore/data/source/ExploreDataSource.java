package com.tokopedia.home.explore.data.source;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.home.R;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.home.explore.domain.model.DataResponseModel;
import com.tokopedia.home.explore.domain.model.DynamicHomeIcon;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.domain.model.LayoutSections;
import com.tokopedia.home.explore.domain.model.ShopData;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryFavoriteViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.MyShopViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.SellViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreDataSource {
    private Context context;
    private HomeDataApi homeDataApi;
    private GlobalCacheManager cacheManager;
    private Gson gson;

    public ExploreDataSource(Context context, HomeDataApi homeDataApi,
                             GlobalCacheManager cacheManager,
                             Gson gson) {
        this.context = context;
        this.homeDataApi = homeDataApi;
        this.cacheManager = cacheManager;
        this.gson = gson;
    }

    public Observable<List<ExploreSectionViewModel>> getExploreData(Context context) {
        return homeDataApi.getExploreData(String.format(getRequestPayload(), SessionHandler.getShopDomain(context)))
                .doOnNext(saveToCache())
                .map(getMapper());
    }

    private Action1<Response<GraphqlResponse<DataResponseModel>>> saveToCache() {
        return new Action1<Response<GraphqlResponse<DataResponseModel>>>() {
            @Override
            public void call(Response<GraphqlResponse<DataResponseModel>> response) {
                if (response.isSuccessful()) {
                    DataResponseModel data = response.body().getData();
                    cacheManager.setKey(TkpdCache.Key.EXPLORE_DATA_CACHE);
                    cacheManager.setValue(gson.toJson(data));
                    cacheManager.store();
                }
            }
        };
    }

    @NonNull
    private Func1<Response<GraphqlResponse<DataResponseModel>>, List<ExploreSectionViewModel>> getMapper() {
        return new Func1<Response<GraphqlResponse<DataResponseModel>>, List<ExploreSectionViewModel>>() {
            @Override
            public List<ExploreSectionViewModel> call(Response<GraphqlResponse<DataResponseModel>> response) {
                if (response.isSuccessful()) {
                    List<ExploreSectionViewModel> models = new ArrayList<>();

                    DynamicHomeIcon model = response.body().getData().getDynamicHomeIcon();
                    for (int i = 0; i < model.getLayoutSections().size(); i++) {
                        LayoutSections s = model.getLayoutSections().get(i);
                        ExploreSectionViewModel sectionViewModel = new ExploreSectionViewModel();
                        sectionViewModel.setTitle(s.getTitle());
                        if (i == 0 && model.getFavCategory() != null) {
                            sectionViewModel.addVisitable(mappingFavoriteCategory(model.getFavCategory()));
                        }
                        if (i == 4) {
                            if (SessionHandler.isUserHasShop(context)) {
                                sectionViewModel.addVisitable(mappingManageShop(response.body().getData()
                                        .getShopInfo().getData()));
                            } else {
                                sectionViewModel.addVisitable(mappingOpenShop());
                            }
                        }
                        sectionViewModel.addVisitable(mappingCategory(s));
                        models.add(sectionViewModel);
                    }
                    return models;
                } else {
                    String messageError = ErrorHandler.getErrorMessage(response);
                    if (!TextUtils.isEmpty(messageError)) {
                        throw new ErrorMessageException(messageError);
                    } else {
                        throw new RuntimeException(String.valueOf(response.code()));
                    }
                }
            }

            private Visitable mappingOpenShop() {
                SellViewModel model = new SellViewModel();
                model.setTitle(context.getString(R.string.empty_shop_wording_title));
                model.setSubtitle(context.getString(R.string.empty_shop_wording_subtitle));
                model.setBtn_title(context.getString(R.string.buka_toko));
                return model;
            }

            private Visitable mappingManageShop(ShopData data) {
                MyShopViewModel model = new MyShopViewModel(data);
                return model;
            }

            private Visitable mappingFavoriteCategory(List<LayoutRows> favCategory) {
                CategoryFavoriteViewModel viewModel = new CategoryFavoriteViewModel();
                viewModel.setTitle(context.getString(R.string.kategori_favorit));
                viewModel.setItemList(favCategory);
                return viewModel;
            }

            private Visitable mappingCategory(LayoutSections s) {
                CategoryGridListViewModel viewModel = new CategoryGridListViewModel();
                viewModel.setSectionId(s.getId());
                viewModel.setTitle(s.getTitle());
                viewModel.setItemList(s.getLayoutRows());
                return viewModel;
            }
        };
    }

    private String getRequestPayload() {
        return loadRawString(context.getResources(), R.raw.explore_data_query);
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }

    public Observable<List<ExploreSectionViewModel>> getDataCache() {
        return Observable.just(true).map(new Func1<Boolean, Response<GraphqlResponse<DataResponseModel>>>() {
            @Override
            public Response<GraphqlResponse<DataResponseModel>> call(Boolean aBoolean) {
                String cache = cacheManager.getValueString(TkpdCache.Key.EXPLORE_DATA_CACHE);
                if (cache != null) {
                    DataResponseModel data = gson.fromJson(cache, DataResponseModel.class);
                    GraphqlResponse<DataResponseModel> graphqlResponse = new GraphqlResponse<>();
                    graphqlResponse.setData(data);
                    return Response.success(graphqlResponse);
                }
                throw new RuntimeException("Cache is empty!!");
            }
        }).map(getMapper()).onErrorResumeNext(getExploreData(context));
    }
}
