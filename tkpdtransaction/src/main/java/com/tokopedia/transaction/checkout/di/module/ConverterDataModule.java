package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.domain.SingleAddressShipmentDataConverter;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 08/02/18.
 */

@Module
public class ConverterDataModule {

    @Provides
    SingleAddressShipmentDataConverter provideSingleShipmentDataConverter() {
        return new SingleAddressShipmentDataConverter();
    }
}
