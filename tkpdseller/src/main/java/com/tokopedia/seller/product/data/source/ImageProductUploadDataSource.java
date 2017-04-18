package com.tokopedia.seller.product.data.source;

import com.tokopedia.seller.product.data.source.cloud.GenerateHostDataSourceCloud;
import com.tokopedia.seller.product.data.source.cloud.model.GenerateHost;
import com.tokopedia.seller.product.data.source.cloud.model.ResultUploadImage;
import com.tokopedia.seller.product.data.source.cloud.ImageProductUploadDataSourceCloud;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

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

    public Observable<ResultUploadImage> uploadImage(final String pathFileImage, final String productId) {
        return generateHostDataSourceCloud.generateHost()
                .flatMap(new Func1<GenerateHost, Observable<ResultUploadImage>>() {
                    @Override
                    public Observable<ResultUploadImage> call(GenerateHost generateHost) {
                        Observable<ResultUploadImage> imageUpload
                                = imageProductUploadDataSourceCloud
                                .uploadImage(generateHost, pathFileImage, productId);
                        return Observable.zip(imageUpload, Observable.just(generateHost), new InsertGenerateHostResult());
                    }
                });
    }

    private class InsertGenerateHostResult implements Func2<ResultUploadImage, GenerateHost, ResultUploadImage> {

        @Override
        public ResultUploadImage call(ResultUploadImage resultUploadImage, GenerateHost generateHost) {
            resultUploadImage.setServerId(Integer.parseInt(generateHost.getServerId()));
            return resultUploadImage;
        }
    }
}
