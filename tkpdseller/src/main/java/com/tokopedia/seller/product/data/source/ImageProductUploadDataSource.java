package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.source.cloud.GenerateHostDataSourceCloud;
import com.tokopedia.seller.product.data.source.cloud.model.GenerateHost;
import com.tokopedia.seller.product.data.source.cloud.model.ResultUploadImage;
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

    public Observable<ResultUploadImage> uploadImage(final String pathFileImage) {
        return generateHostDataSourceCloud.generateHost()
                .flatMap(new Func1<GenerateHost, Observable<ResultUploadImage>>() {
                    @Override
                    public Observable<ResultUploadImage> call(GenerateHost generateHost) {
                        return imageProductUploadDataSourceCloud.uploadImage(generateHost, pathFileImage);
                    }
                });
    }
}
