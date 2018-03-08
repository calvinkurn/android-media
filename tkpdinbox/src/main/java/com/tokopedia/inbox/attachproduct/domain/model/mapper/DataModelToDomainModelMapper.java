package com.tokopedia.inbox.attachproduct.domain.model.mapper;

import com.tokopedia.inbox.attachproduct.data.model.DataProductResponse;
import com.tokopedia.inbox.attachproduct.domain.model.AttachProductDomainModel;
import com.tokopedia.inbox.attachproduct.domain.util.DomainModelToViewModelConverter;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by Hendri on 08/03/18.
 */

public class DataModelToDomainModelMapper implements Func1<AttachProductDomainModel, List<AttachProductItemViewModel>> {
    @Override
    public List<AttachProductItemViewModel> call(AttachProductDomainModel attachProductDomainModel) {
        ArrayList<AttachProductItemViewModel> arrayList = new ArrayList<>();
        for(DataProductResponse product:attachProductDomainModel.getProducts()){
            arrayList.add(DomainModelToViewModelConverter.convertProductDomainModel(product));
        }
        return arrayList;
    }
}
