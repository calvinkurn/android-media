package com.tokopedia.seller.product.picker.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.seller.product.picker.view.listener.ProductListPickerSearchView
import com.tokopedia.seller.product.picker.view.mapper.GetProductListMapper
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductListResponse
import com.tokopedia.shop.common.domain.interactor.GetProductListUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class ProductListPickerSearchPresenterImpl @Inject constructor(
        private val getProductListUseCase: GetProductListUseCase,
        val getProductListMapper: GetProductListMapper,
        private val userSessionInterface: UserSessionInterface
        ) : BaseDaggerPresenter<ProductListPickerSearchView>(), ProductListPickerSearchPresenter {


    override fun getProductList(page: Int, keywordFilter: String?) {
        getProductListUseCase.execute(GetProductListUseCase.createRequestParams(userSessionInterface.shopId, page, keywordFilter, null, null, null, null, null, null),
                object : Subscriber<ProductListResponse>() {
                    override fun onNext(data: ProductListResponse) {
                        val productListSellerModelView = getProductListMapper.mapIntoViewModel(data)
                        view.onSearchLoaded(productListSellerModelView.productListPickerViewModels,
                                productListSellerModelView.productListPickerViewModels.size,
                                productListSellerModelView.isHasNextPage)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (isViewAttached) {
                            view.onLoadSearchError(e)
                        }
                    }

                })
    }

    override fun detachView() {
        super.detachView()
        getProductListUseCase.unsubscribe()
    }
}