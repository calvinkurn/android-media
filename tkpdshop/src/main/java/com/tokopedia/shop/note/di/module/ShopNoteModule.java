package com.tokopedia.shop.note.di.module;

import com.tokopedia.shop.note.data.repository.ShopNoteRepositoryImpl;
import com.tokopedia.shop.note.data.source.ShopNoteDataSource;
import com.tokopedia.shop.note.di.scope.ShopNoteScope;
import com.tokopedia.shop.note.domain.repository.ShopNoteRepository;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

import dagger.Module;
import dagger.Provides;

@ShopNoteScope
@Module
public class ShopNoteModule {

    @ShopNoteScope
    @Provides
    public ShopNoteRepository provideShopNoteRepository(ShopNoteDataSource shopNoteDataSource){
        return new ShopNoteRepositoryImpl(shopNoteDataSource);
    }

    @ShopNoteScope
    @Provides
    public ShopNoteViewModel provideShopNoteViewModel(){
        return new ShopNoteViewModel();
    }
}

