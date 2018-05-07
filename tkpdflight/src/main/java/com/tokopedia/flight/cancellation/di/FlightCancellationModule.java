package com.tokopedia.flight.cancellation.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.airline.domain.FlightAirlineUseCase;
import com.tokopedia.flight.cancellation.domain.FlightCancellationEstimateRefundUseCase;
import com.tokopedia.flight.cancellation.domain.FlightCancellationUploadImageUseCase;
import com.tokopedia.flight.cancellation.domain.model.AttachmentImageModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier;
import com.tokopedia.imageuploader.domain.GenerateHostRepository;
import com.tokopedia.imageuploader.domain.UploadImageRepository;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.utils.ImageUploaderUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author by furqan on 21/03/18.
 */

@Module(includes = ImageUploaderModule.class)
public class FlightCancellationModule {

    @Provides
    FlightCancellationEstimateRefundUseCase provideFlightCancellationEstimateRefundUseCase(FlightRepository flightRepository){
        return new FlightCancellationEstimateRefundUseCase(flightRepository);
    }

    @Provides
    FlightAirlineUseCase provideFlightAirlineUseCase(FlightRepository flightRepository){
        return new FlightAirlineUseCase(flightRepository);
    }

    @Provides
    public UploadImageUseCase<AttachmentImageModel> provideAttachmentImageModelUploadImageUseCase(@ImageUploaderQualifier UploadImageRepository uploadImageRepository,
                                                                                                  @ImageUploaderQualifier GenerateHostRepository generateHostRepository,
                                                                                                  @ImageUploaderQualifier Gson gson,
                                                                                                  @ImageUploaderQualifier UserSession userSession,
                                                                                                  @ImageUploaderQualifier ImageUploaderUtils imageUploaderUtils) {
        return new UploadImageUseCase<>(uploadImageRepository, generateHostRepository, gson, userSession, AttachmentImageModel.class, imageUploaderUtils);
    }
}
