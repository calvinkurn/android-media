package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.domain.SingleShipmentDataConverter;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 08/02/18.
 */

@Module
public class ConverterDataModule {

    @Provides
    SingleShipmentDataConverter provideSingleShipmentDataConverter() {
        return new SingleShipmentDataConverter();
    }
}
