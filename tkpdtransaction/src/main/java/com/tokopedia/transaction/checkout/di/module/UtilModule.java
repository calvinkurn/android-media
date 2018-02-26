package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.domain.IMapperUtil;
import com.tokopedia.transaction.checkout.domain.MapperUtil;

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
