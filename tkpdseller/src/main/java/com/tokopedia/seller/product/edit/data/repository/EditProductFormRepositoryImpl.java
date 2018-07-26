package com.tokopedia.seller.product.edit.data.repository;

import com.tokopedia.core.product.model.goldmerchant.Video;
import com.tokopedia.seller.product.edit.data.mapper.EditProductFormMapper;
import com.tokopedia.seller.product.edit.data.source.EditProductFormDataSource;
import com.tokopedia.seller.product.edit.data.source.FetchVideoEditProductDataSource;
import com.tokopedia.seller.product.edit.domain.EditProductFormRepository;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * @author sebastianuskh on 4/21/17.
 */

@Deprecated
public class EditProductFormRepositoryImpl implements EditProductFormRepository {
    private final EditProductFormDataSource editProductFormDataSource;
    private final EditProductFormMapper editProductFormMapper;
    private final FetchVideoEditProductDataSource fetchVideoEditProductDataSource;

    public EditProductFormRepositoryImpl(EditProductFormDataSource editProductFormDataSource, EditProductFormMapper editProductFormMapper, FetchVideoEditProductDataSource fetchVideoEditProductDataSource) {
        this.editProductFormDataSource = editProductFormDataSource;
        this.editProductFormMapper = editProductFormMapper;
        this.fetchVideoEditProductDataSource = fetchVideoEditProductDataSource;
    }

    @Override
    public Observable<UploadProductInputDomainModel> fetchEditProduct(final String productId) {
        return Observable.zip(
                editProductFormDataSource.fetchEditProductForm(productId).map(editProductFormMapper),
                fetchVideoEditProductDataSource.fetchVideos(productId),
                new Func2<UploadProductInputDomainModel, List<Video>, UploadProductInputDomainModel>() {
                    @Override
                    public UploadProductInputDomainModel call(UploadProductInputDomainModel uploadProductInputDomainModel,
                                                              List<Video> videoList) {
                        List<String> videoIds = new ArrayList<>();
                        for (Video video : videoList) {
                            videoIds.add(video.getUrl());
                        }
                        uploadProductInputDomainModel.setProductVideos(videoIds);
                        return uploadProductInputDomainModel;
                    }
                }
        );
    }
}
