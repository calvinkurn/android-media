package com.tokopedia.shop.product.di.module;

import com.tokopedia.shop.note.data.repository.ShopNoteRepositoryImpl;
import com.tokopedia.shop.note.data.source.ShopNoteDataSource;
import com.tokopedia.shop.note.domain.repository.ShopNoteRepository;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;
import com.tokopedia.shop.product.di.scope.ShopProductScope;

import dagger.Module;
import dagger.Provides;

@ShopProductScope
@Module
public class ShopProductModule {

    @ShopProductScope
    @Provides
    public ShopNoteRepository provideShopNoteRepository(ShopNoteDataSource shopNoteDataSource){
        return new ShopNoteRepositoryImpl(shopNoteDataSource);
    }

    @ShopProductScope
    @Provides
    public ShopNoteViewModel provideShopNoteViewModel(){
        return new ShopNoteViewModel();
    }
}

