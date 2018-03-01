package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.transaction.checkout.domain.mapper.IMapperUtil;
import com.tokopedia.transaction.checkout.domain.mapper.MapperUtil;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 23/02/18.
 */
@Module
public class UtilModule {

    @Provides
    IMapperUtil provideIMapperUtil(){
        return new MapperUtil();
    }
}
