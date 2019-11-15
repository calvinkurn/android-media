package com.tokopedia.discovery.newdiscovery.hotlistRevamp.viewmodel

import androidx.lifecycle.*
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductListResponse
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.domain.usecase.CategoryProductUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.DynamicFilterUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.GetProductListUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.QuickFilterUseCase
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.cpmAds.CpmItem
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.data.hotListDetail.HotListDetailResponse
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.domain.usecases.CpmAdsUseCase
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.domain.usecases.HotlistDetailUseCase
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class HotlistNavViewModel @Inject constructor(var hotlistDetailUseCase: HotlistDetailUseCase,
                                              var getProductListUseCase: GetProductListUseCase,
                                              var categoryProductUseCase: CategoryProductUseCase,
                                              var quickFilterUseCase: QuickFilterUseCase,
                                              var dynamicFilterUseCase: DynamicFilterUseCase,
                                              var cpmAdsUseCase: CpmAdsUseCase) : ViewModel(), LifecycleObserver {

    val mHotListDetailResponse = MutableLiveData<Result<HotListDetailResponse>>()
    val mProductList = MutableLiveData<Result<List<ProductsItem>>>()
    val mProductCount = MutableLiveData<String>()
    var mDynamicFilterModel = MutableLiveData<Result<DynamicFilterModel>>()
    var mQuickFilterModel = MutableLiveData<Result<List<Filter>>>()
    var mCpmTopAdsData = MutableLiveData<Result<List<CpmItem>>>()


    fun getHotlistDetail(params: RequestParams) {
        hotlistDetailUseCase.execute(params, object : Subscriber<HotListDetailResponse>() {
            override fun onNext(hotlistRes: HotListDetailResponse?) {
                hotlistRes?.let {
                    mHotListDetailResponse.value = Success(it)
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mHotListDetailResponse.value = Fail(e ?: Throwable("Error Thrown"))
            }
        })
    }


    fun fetchProductListingWithTopAds(params: RequestParams) {
        getProductListUseCase.execute(params, object : Subscriber<ProductListResponse>() {
            override fun onNext(productListResponse: ProductListResponse?) {
                productListResponse?.let { productResponse ->
                    (productResponse.searchProduct)?.let { searchProduct ->
                        searchProduct.products?.let { productList ->
                            mProductList.value = Success((productList) as List<ProductsItem>)
                        }

                        mProductCount.value = searchProduct.countText
                    }
                }

            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mProductList.value = Fail(e ?: Throwable("Throw Error"))
            }
        })
    }

    fun getProductListWithoutTopAds(params: RequestParams) {
        categoryProductUseCase.execute(params, object : Subscriber<ProductListResponse>() {
            override fun onNext(productListResponse: ProductListResponse?) {
                productListResponse?.let { productResponse ->
                    (productResponse.searchProduct)?.let { searchProduct ->
                        searchProduct.products?.let { productList ->
                            mProductList.value = Success((productList) as List<ProductsItem>)
                        }

                        mProductCount.value = searchProduct.countText
                    }
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mProductList.value = Fail(e ?: Throwable("Throw Error"))
            }
        })
    }


    fun fetchDynamicAttribute(params: RequestParams) {
        dynamicFilterUseCase.execute(params, object : Subscriber<DynamicFilterModel>() {
            override fun onNext(t: DynamicFilterModel?) {
                mDynamicFilterModel.value = Success(t as DynamicFilterModel)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                mDynamicFilterModel.value = (Fail(e))
            }
        })
    }

    fun fetchQuickFilters(params: RequestParams) {

        quickFilterUseCase.execute(params, object : Subscriber<List<Filter>>() {
            override fun onNext(t: List<Filter>?) {
                mQuickFilterModel.value = Success(t as List<Filter>)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                mQuickFilterModel.value = Fail(e)
            }
        })
    }

    fun fetchCpmData(params: RequestParams) {
        cpmAdsUseCase.execute(params, object : Subscriber<List<CpmItem>>() {
            override fun onNext(t: List<CpmItem>?) {
                mCpmTopAdsData.value = Success(t as List<CpmItem>)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                mCpmTopAdsData.value = Fail(e)
            }
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy() {
        hotlistDetailUseCase.unsubscribe()
        getProductListUseCase.unsubscribe()
        categoryProductUseCase.unsubscribe()
        quickFilterUseCase.unsubscribe()
        dynamicFilterUseCase.unsubscribe()
        cpmAdsUseCase.unsubscribe()
    }
}