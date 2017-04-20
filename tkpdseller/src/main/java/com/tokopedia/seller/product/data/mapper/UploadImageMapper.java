package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.ResultUploadImage;
import com.tokopedia.seller.product.domain.model.ImageProcessDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class UploadImageMapper implements Func1<ResultUploadImage, ImageProcessDomainModel> {

    @Override
    public ImageProcessDomainModel call(ResultUploadImage resultUploadImage) {
        ImageProcessDomainModel domainModel = new ImageProcessDomainModel();
        domainModel.setUrl(resultUploadImage.getFilePath());
        domainModel.setPicObj(resultUploadImage.getPicObj());
        return domainModel;
    }
}
