package com.tokopedia.seller.product.edit.data.repository;

import com.tokopedia.core.product.model.goldmerchant.Video;
import com.tokopedia.seller.product.edit.data.source.FetchVideoEditProductDataSource;
import com.tokopedia.seller.product.edit.data.source.ProductDataSource;
import com.tokopedia.seller.product.edit.domain.ProductRepository;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class ProductRepositoryImpl implements ProductRepository {
    private final ProductDataSource productDataSource;
    private final FetchVideoEditProductDataSource fetchVideoEditProductDataSource;

    public ProductRepositoryImpl(ProductDataSource productDataSource,
                                 FetchVideoEditProductDataSource fetchVideoEditProductDataSource) {
        this.productDataSource = productDataSource;
        this.fetchVideoEditProductDataSource = fetchVideoEditProductDataSource;
    }

    @Override
    public Observable<Boolean> addProductSubmit(ProductViewModel productViewModel) {
        return productDataSource.addProductSubmit(productViewModel);
    }

    @Override
    public Observable<Boolean> editProductSubmit(ProductViewModel productViewModel) {
        return productDataSource.editProduct(productViewModel);
    }

    @Override
    public Observable<ProductViewModel> getProductDetail(String productId) {
        return Observable.zip(productDataSource.getProductDetail(productId),
                fetchVideoEditProductDataSource.fetchVideos(productId),
                new Func2<ProductViewModel, List<Video>, ProductViewModel>() {
                    @Override
                    public ProductViewModel call(ProductViewModel productViewModel, List<Video> videos) {
                        List<ProductVideoViewModel> productVideoViewModelList = new ArrayList<>();
                        for (Video video : videos) {
                            productVideoViewModelList.add(new ProductVideoViewModel(video.getUrl(), video.getType()));
                        }
                        productViewModel.setProductVideo(productVideoViewModelList);
                        return productViewModel;
                    }
                });
    }

}
