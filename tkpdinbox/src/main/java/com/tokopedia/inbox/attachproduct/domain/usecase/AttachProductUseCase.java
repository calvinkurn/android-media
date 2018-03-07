package com.tokopedia.inbox.attachproduct.domain.usecase;

import com.tokopedia.core.shopinfo.models.productmodel.Product;
import com.tokopedia.inbox.attachproduct.data.repository.AttachProductRepository;
import com.tokopedia.inbox.attachproduct.domain.model.AttachProductDomainModel;
import com.tokopedia.inbox.attachproduct.domain.util.DomainModelToViewModelConverter;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Hendri on 14/02/18.
 */

public class AttachProductUseCase {
    private final AttachProductRepository repository;

    @Inject
    public AttachProductUseCase(AttachProductRepository repository) {
        this.repository = repository;
    }

    public Observable<List<AttachProductItemViewModel>> getProductList(String query, String shopId, int page){
        return repository.loadProductFromShop(query,shopId,page).map(new Func1<AttachProductDomainModel, List<AttachProductItemViewModel>>() {
            @Override
            public List<AttachProductItemViewModel> call(AttachProductDomainModel attachProductDomainModel) {
                ArrayList<AttachProductItemViewModel> arrayList = new ArrayList<>();
                for(Product product:attachProductDomainModel.getProducts()){
                    arrayList.add(DomainModelToViewModelConverter.convertProductDomainModel(product));
                }
                return arrayList;
            }
        });
    }
}
