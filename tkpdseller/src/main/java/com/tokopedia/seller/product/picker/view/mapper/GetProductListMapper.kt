package com.tokopedia.seller.product.picker.view.mapper

import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel
import com.tokopedia.seller.product.picker.view.model.ProductListSellerModelView
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Data
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductListResponse
import javax.inject.Inject

class GetProductListMapper @Inject constructor() {

    fun mapIntoViewModel(productList: ProductListResponse): ProductListSellerModelView {
        val productListManageModelView = ProductListSellerModelView()

        productListManageModelView.isHasNextPage = productList.getProductList.links.next != ""

        if (productList.getProductList.data.isNotEmpty()) {
            val products = mapToListProducts(productList.getProductList.data)
            productListManageModelView.productListPickerViewModels = products
        }

        return productListManageModelView
    }


    private fun mapToListProducts(productList: List<Data>): MutableList<ProductListPickerViewModel> {
        val productManageListViewModel = mutableListOf<ProductListPickerViewModel>()

        productManageListViewModel.addAll(
                productList.map {
                    convertData(it)
                }
        )
        return productManageListViewModel
    }


    private fun convertData(data: Data): ProductListPickerViewModel {
        val productListPickerViewModel = ProductListPickerViewModel()

        productListPickerViewModel.run {
            id = data.productId
            setImageUrl(data.primaryImage.thumbnail)
            title = data.name
            productPrice = data.price.text
            productStatus = data.status.toString()

        }

        return productListPickerViewModel
    }
}