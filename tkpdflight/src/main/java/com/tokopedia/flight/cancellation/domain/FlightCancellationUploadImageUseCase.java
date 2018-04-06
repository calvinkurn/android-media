package com.tokopedia.flight.cancellation.domain;

import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by alvarisi on 3/29/18.
 */

public class FlightCancellationUploadImageUseCase extends UseCase<String> {
    private static final String PATH_FILE = "PATH_FILE";
    private static final String PATH_UPLOAD = "PATH_UPLOAD";
    private static final String DEFAULT_FOLDER_UPLOAD = "/web-service/travel/flight/cancellation";
    private FlightModuleRouter flightModuleRouter;

    public FlightCancellationUploadImageUseCase(FlightModuleRouter flightModuleRouter) {
        this.flightModuleRouter = flightModuleRouter;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return flightModuleRouter.uploadImage(DEFAULT_FOLDER_UPLOAD, requestParams.getString(PATH_FILE, ""));
    }

    public RequestParams createParam(String pathFile) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PATH_FILE, pathFile);
        return requestParams;
    }
}
