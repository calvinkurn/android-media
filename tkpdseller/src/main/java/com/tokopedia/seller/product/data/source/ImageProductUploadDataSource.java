package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.source.cloud.GenerateHostDataSourceCloud;
import com.tokopedia.seller.product.data.source.cloud.model.GenerateHostModel;
import com.tokopedia.seller.product.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.data.source.cloud.ImageProductUploadDataSourceCloud;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ImageProductUploadDataSource {
    private ImageProductUploadDataSourceCloud imageProductUploadDataSourceCloud;
    private GenerateHostDataSourceCloud generateHostDataSourceCloud;

    @Inject
    public ImageProductUploadDataSource(ImageProductUploadDataSourceCloud imageProductUploadDataSourceCloud,
                                        GenerateHostDataSourceCloud generateHostDataSourceCloud) {
        this.imageProductUploadDataSourceCloud = imageProductUploadDataSourceCloud;
        this.generateHostDataSourceCloud = generateHostDataSourceCloud;
    }

    public Observable<UploadImageModel.Result> uploadImage(final String pathFileImage) {
        return generateHostDataSourceCloud.generateHost()
                .flatMap(new Func1<GenerateHostModel.GenerateHost, Observable<UploadImageModel.Result>>() {
                    @Override
                    public Observable<UploadImageModel.Result> call(GenerateHostModel.GenerateHost generateHost) {
                        return imageProductUploadDataSourceCloud.uploadImage(generateHost, pathFileImage);
                    }
                });
    }
}
