package com.tokopedia.inbox.rescenter.shipping.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.graphql.data.model.GraphqlError;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.common.data.pojo.GenerateHostDataResponse;
import com.tokopedia.inbox.common.data.pojo.GenerateHostResponse;
import com.tokopedia.inbox.rescenter.shipping.interactor.NetworkParam;
import com.tokopedia.inbox.rescenter.shipping.interactor.RetrofitInteractor;
import com.tokopedia.inbox.rescenter.shipping.interactor.RetrofitInteractorImpl;
import com.tokopedia.inbox.rescenter.shipping.model.EditAWBRequest;
import com.tokopedia.inbox.rescenter.shipping.model.EditAWBResponse;
import com.tokopedia.inbox.rescenter.shipping.model.InputAWBRequest;
import com.tokopedia.inbox.rescenter.shipping.model.InputAWBResponse;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsGetModel;
import com.tokopedia.inbox.rescenter.shipping.model.NewUploadResCenterImageData;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;
import com.tokopedia.inbox.rescenter.shipping.model.ResKurirListResponse;
import com.tokopedia.inbox.rescenter.shipping.model.ShippingParamsPostModel;
import com.tokopedia.inbox.rescenter.shipping.view.InputShippingFragmentView;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;
import com.tokopedia.inbox.rescenter.utils.UploadImageResCenter;
import com.tokopedia.usecase.RequestParams;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.tokopedia.inbox.rescenter.detailv2.domain.interactor.UploadImageV2UseCase.PARAM_FILE_TO_UPLOAD;
import static com.tokopedia.inbox.rescenter.shipping.fragment.InputShippingFragment.EXTRA_PARAM_ATTACHMENT;
import static com.tokopedia.inbox.rescenter.shipping.fragment.InputShippingFragment.EXTRA_PARAM_MODEL;

/**
 * Created by hangnadi on 12/13/16.
 */
public class InputShippingFragmentImpl implements InputShippingFragmentPresenter, GraphQLResponseSubscriber.ResponseCallbacks {

    private static final String TAG = InputShippingFragmentPresenter.class.getSimpleName();
    private static final int REQUEST_CODE_SCAN_BARCODE = 19;

    private final GlobalCacheManager cacheManager;
    private final RetrofitInteractor retrofit;
    private final InputShippingFragmentView viewListener;
    private InputAWBRequest AWBRequest;
    private ShippingParamsPostModel shippingParams;

    private boolean isShippingRefValid = false;
    private boolean isShippingSpinnerValid = false;
    private boolean isListAttachmentValid = false;
    private GraphqlUseCase graphqlUseCase;


    public InputShippingFragmentImpl(InputShippingFragmentView viewListener) {
        graphqlUseCase = new GraphqlUseCase();
        this.viewListener = viewListener;
        this.cacheManager = new GlobalCacheManager();
        this.retrofit = new RetrofitInteractorImpl();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelable(EXTRA_PARAM_MODEL, viewListener.getParamsModel());
        state.putParcelableArrayList(EXTRA_PARAM_ATTACHMENT, viewListener.getAttachmentData());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        viewListener.setParamsModel((InputShippingParamsGetModel) savedState.getParcelable(EXTRA_PARAM_MODEL));
        viewListener.setAttachmentData(savedState.<AttachmentResCenterVersion2DB>getParcelableArrayList(EXTRA_PARAM_ATTACHMENT));
    }

    @Override
    public void onFirstTimeLaunched() {
        try {
            String json = cacheManager.getValueString(viewListener.getParamsModel().getResolutionID());
            if (json != null) {
                ResCenterKurir shippingModel = convertCacheToModel(json);
                renderInputShippingForm(shippingModel);
                renderPreviousShipping(shippingModel);
                showLoading(false);
                showMainPage(true);
            } else {
                requestShippingList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestShippingList();
        }
    }

    private void showMainPage(boolean isVisible) {
        viewListener.getMainView().setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void showLoading(boolean isVisible) {
        viewListener.getLoadingView().setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private ResCenterKurir convertCacheToModel(String json) {
        return CacheUtil.convertStringToModel(json, new TypeToken<ResCenterKurir>() {
        }.getType());
    }

    private void requestShippingList() {
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(viewListener.getActivity().getResources(),
                R.raw.get_kurir_list), ResKurirListResponse.class, false);
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);

        showLoading(true);
        showMainPage(false);

        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                viewListener.showErrorMessage(ErrorHandler.getErrorMessage(viewListener.getActivity(), e));
                showLoading(false);
                showMainPage(false);
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {

                if (graphqlResponse != null) {
                    ResKurirListResponse resKurirListResponse = graphqlResponse.getData(ResKurirListResponse.class);
                    JsonObject shippingList = resKurirListResponse.getGetShippingListRespose().getAsJsonObject("data");
                    Gson gson = new Gson();
                    ResCenterKurir resCenterKurir = gson.fromJson(shippingList, ResCenterKurir.class);

                    storeCacheKurirList(resCenterKurir);
                    renderInputShippingForm(resCenterKurir);
                    renderPreviousShipping(resCenterKurir);
                    showLoading(false);
                    showMainPage(true);
                }

            }
        });
    }

    private Observable<ShippingParamsPostModel> getObservableGeneratedHost(ShippingParamsPostModel passData) {
        GraphqlRequest getUploadhostRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(viewListener.getActivity().getResources(),
                R.raw.get_upload_host), JsonObject.class, false);
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(getUploadhostRequest);
        return graphqlUseCase.getExecuteObservable(RequestParams.EMPTY)
                .concatMap((Func1<GraphqlResponse, Observable<ShippingParamsPostModel>>) graphqlResponse -> {
                    if (graphqlResponse != null) {
                        List<GraphqlError> errorList = graphqlResponse.getError(JsonObject.class);
                        if (errorList == null || graphqlResponse.getError(JsonObject.class).isEmpty()) {
                            JsonObject jsonData = graphqlResponse.getData(JsonObject.class);
                            GenerateHostDataResponse hostDataResponse = new Gson().fromJson(jsonData.getAsJsonObject("get_resolution_upload_host").getAsJsonObject("data"), GenerateHostDataResponse.class);
                            GenerateHostResponse uploadHostData = hostDataResponse.getGenerateHostResponse();
                            passData.setServerID(uploadHostData.getServerId());
                            passData.setUploadHost(uploadHostData.getUploadHost());
                            passData.setUserId(uploadHostData.getUserId());
                            passData.setToken(uploadHostData.getToken());
                        } else {
                            throw new RuntimeException(graphqlResponse.getError(JsonObject.class).toString());
                        }
                    }
                    return Observable.just(passData);

                });

    }

    private void renderPreviousShipping(ResCenterKurir shippingModel) {
        if (isInstanceForEdit()) {
            viewListener.getShippingRefNum().setText(viewListener.getParamsModel().getShippingRefNum());

            for (ResCenterKurir.Kurir kurir : shippingModel.getList()) {
                if (kurir.getShipmentId().equals(viewListener.getParamsModel().getShippingID())) {
                    viewListener.getShippingSpinner().setSelection(shippingModel.getList().indexOf(kurir) + 1);
                }
            }
        }
    }

    private TKPDMapParam generateGetShippingListParams() {
        return AuthUtil.generateParamsNetwork(viewListener.getActivity(), NetworkParam.getShippingListParams());
    }

    private void renderInputShippingForm(ResCenterKurir shippingListModel) {
        viewListener.renderSpinner(shippingListModel.getList());
    }

    private void storeCacheKurirList(ResCenterKurir kurirList) {
        cacheManager.setKey(viewListener.getParamsModel().getResolutionID());
        cacheManager.setValue(
                CacheUtil.convertModelToString(kurirList, new TypeToken<ResCenterKurir>() {
                }.getType())
        );
        cacheManager.setCacheDuration(1800000); // expired in 30minutes
        cacheManager.store();
    }

    @Override
    public void onScanBarcodeClick(Context context) {
        Intent intent = new Intent(context, CaptureActivity.class);
        viewListener.startActivityForResult(intent, REQUEST_CODE_SCAN_BARCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_BARCODE:
                    viewListener.renderInputShippingRefNum(data != null ? data.getStringExtra("SCAN_RESULT") : "");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        removeAttachment();
        retrofit.unSubscribe();
    }

    @Override
    public void removeAttachment() {
        LocalCacheManager.AttachmentShippingResCenter.Builder(viewListener.getParamsModel().getResolutionID()).clearAll();
    }

    @Override
    public void onConfirrmButtonClick() {
        shippingParams = generatePostParams();
        if (isInstanceForEdit()) {
            doEditShippingService(shippingParams);
        } else {
            doStoreShippingService(shippingParams);
        }
    }

    private ShippingParamsPostModel generatePostParams() {
        return new ShippingParamsPostModel.Builder()
                .setResolutionID(viewListener.getParamsModel().getResolutionID())
                .setConversationID(viewListener.getParamsModel().getConversationID())
                .setShippingNumber(viewListener.getShippingRefNum().getText().toString())
                .setAttachmentList(viewListener.getAttachmentData())
                .setShippingID(generateShippingID())
                .build();
    }

    private String generateShippingID() {
        try {
            return getSelectedKurir().getShipmentId();
        } catch (Exception e) {
            return "";
        }
    }

    private void doStoreShippingService(ShippingParamsPostModel params) {

        if (!isValidToSubmit(params)) {
            return;
        }

        InputAWBRequest inputAWBRequest = new InputAWBRequest();
        inputAWBRequest.setResId(Integer.parseInt(params.getResolutionID()));
        inputAWBRequest.setAwbNum(params.getShippingNumber());
        inputAWBRequest.setCourierId(Integer.parseInt(params.getShippingID()));

        if (params.getAttachmentList() != null && params.getAttachmentList().size() > 0)
            inputAWBRequest.setAttachmentCount(params.getAttachmentList().size());

        AWBRequest = inputAWBRequest;

        Map<String, Object> variables = new HashMap<>();
        variables.put("input", AWBRequest);

        GraphqlRequest graphqlRequest = getGraphQLRequest(R.raw.input_awb_request, InputAWBResponse.class, variables);
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);

        showLoadingState();

        GraphQLResponseSubscriber responseSubscriber = getResponseSubscriber(true);

        graphqlUseCase.execute(responseSubscriber);
    }

    private void doEditShippingService(ShippingParamsPostModel params) {
        if (!isValidToSubmit(params)) {
            return;
        }

        EditAWBRequest editAWBRequest = new EditAWBRequest();
        editAWBRequest.setResId(Integer.parseInt(params.getResolutionID()));
        editAWBRequest.setAwbNum(params.getShippingNumber());
        editAWBRequest.setConversationId(Integer.parseInt(params.getConversationID()));
        editAWBRequest.setCourierId(Integer.parseInt(params.getShippingID()));

        if (params.getAttachmentList() != null && params.getAttachmentList().size() > 0)
            editAWBRequest.setAttachmentCount(params.getAttachmentList().size());

        AWBRequest = editAWBRequest;

        Map<String, Object> variables = new HashMap<>();
        variables.put("input", AWBRequest);

        GraphqlRequest graphqlRequest = getGraphQLRequest(R.raw.edit_awb_request, EditAWBResponse.class, variables);
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);

        showLoadingState();

        GraphQLResponseSubscriber responseSubscriber = getResponseSubscriber(true);

        graphqlUseCase.execute(responseSubscriber);
    }

    private void resAwbSubmit(InputAWBRequest awbRequest, ShippingParamsPostModel params) {
        List<String> imageList = new ArrayList<>();
        Map<String, Object> variables = new HashMap<>();

        GraphqlRequest graphqlRequest;

        if (isInstanceForEdit()) {
            graphqlRequest = getGraphQLRequest(R.raw.edit_awb_request, EditAWBResponse.class, variables);
        } else {
            graphqlRequest = getGraphQLRequest(R.raw.input_awb_request, InputAWBResponse.class, variables);
        }

        GraphQLResponseSubscriber responseSubscriber = getResponseSubscriber(false);

        if (params.getAttachmentList() != null && params.getAttachmentList().size() > 0) {
            awbRequest.setAttachmentCount(params.getAttachmentList().size());
            showLoadingState();

            getObservableGeneratedHost(params)
                    .flatMap((Func1<ShippingParamsPostModel, Observable<ShippingParamsPostModel>>)
                            shippingParamsPostModel -> getObservableUploadingFile(viewListener.getActivity(), params))
                    .flatMap(new Func1<ShippingParamsPostModel, Observable<InputAWBRequest>>() {
                        @Override
                        public Observable<InputAWBRequest> call(ShippingParamsPostModel shippingParamsPostModel) {
                            for (AttachmentResCenterVersion2DB attachment : params.getAttachmentList())
                                imageList.add(attachment.picObj);
                            awbRequest.setPictures(imageList);
                            return Observable.just(awbRequest);
                        }
                    }).flatMap(new Func1<InputAWBRequest, Observable<GraphqlResponse>>() {
                @Override
                public Observable<GraphqlResponse> call(InputAWBRequest awbRequest) {
                    variables.put("input", awbRequest);
                    graphqlUseCase.clearRequest();
                    graphqlUseCase.addRequest(graphqlRequest);
                    return graphqlUseCase.getExecuteObservable(RequestParams.EMPTY);
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseSubscriber);
        } else {
            showLoadingState();
            variables.put("input", awbRequest);
            graphqlUseCase.clearRequest();
            graphqlUseCase.addRequest(graphqlRequest);
            graphqlUseCase.execute(responseSubscriber);
        }
    }


    private Observable<ShippingParamsPostModel> getObservableUploadingFile(Context
                                                                                   context, ShippingParamsPostModel passData) {
        return Observable.zip(Observable.just(passData), doUploadFile(context, passData), new Func2<ShippingParamsPostModel, List<AttachmentResCenterVersion2DB>, ShippingParamsPostModel>() {
            @Override
            public ShippingParamsPostModel call(ShippingParamsPostModel inputModel, List<AttachmentResCenterVersion2DB> attachmentResCenterDBs) {
                inputModel.setAttachmentList(attachmentResCenterDBs);
                return inputModel;
            }
        });
    }

    private Observable<List<AttachmentResCenterVersion2DB>> doUploadFile(
            final Context context, final ShippingParamsPostModel inputModel) {
        return Observable
                .from(inputModel.getAttachmentList())
                .flatMap(new Func1<AttachmentResCenterVersion2DB, Observable<AttachmentResCenterVersion2DB>>() {
                    @Override
                    public Observable<AttachmentResCenterVersion2DB> call(AttachmentResCenterVersion2DB attachmentResCenterDB) {
                        String uploadUrl = "https://" + inputModel.getUploadHost();
                        NetworkCalculator networkCalculator = new NetworkCalculator(NetworkConfig.POST, context, uploadUrl)
                                .setIdentity()
                                .addParam("id", attachmentResCenterDB.imageUUID)
                                .addParam("token", inputModel.getToken())
                                .addParam("web_service", "1")
                                .compileAllParam()
                                .finish();

                        File file;
                        try {
                            file = ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(attachmentResCenterDB.imagePath));
                        } catch (IOException e) {
                            throw new RuntimeException(context.getString(com.tokopedia.core2.R.string.error_upload_image));
                        }
                        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.USER_ID));
                        RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
                        RequestBody hash = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.HASH));
                        RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
                        RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"),
                                file);
                        RequestBody imageId = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get("id"));
                        RequestBody token = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get("token"));
                        RequestBody web_service = RequestBody.create(MediaType.parse("text/plain"),
                                networkCalculator.getContent().get("web_service"));
                        RequestBody osType = RequestBody.create(MediaType.parse("text/plain"),
                                "1");

                        Map<String, RequestBody> requestBodyMap = new HashMap<>();
                        requestBodyMap.put(NetworkCalculator.USER_ID, userId);
                        requestBodyMap.put(NetworkCalculator.DEVICE_ID, deviceId);
                        requestBodyMap.put("os_type", osType);
                        requestBodyMap.put(NetworkCalculator.HASH, hash);
                        requestBodyMap.put(NetworkCalculator.DEVICE_TIME, deviceTime);
                        requestBodyMap.put("id", imageId);
                        requestBodyMap.put("token", token);
                        requestBodyMap.put(PARAM_FILE_TO_UPLOAD, imageId);
                        requestBodyMap.put("web_service", web_service);

                        Log.d(TAG + "(step 2):host", inputModel.getUploadHost());
                        final Observable<NewUploadResCenterImageData> upload = getRetrofit()
                                .create(UploadImageResCenter.class)
                                .uploadImageNew(
                                        networkCalculator.getUrl() + "/upload/attachment",
                                        requestBodyMap,
                                        fileToUpload
                                );

                        return Observable.zip(Observable.just(attachmentResCenterDB), upload, new Func2<AttachmentResCenterVersion2DB, NewUploadResCenterImageData, AttachmentResCenterVersion2DB>() {
                            @Override
                            public AttachmentResCenterVersion2DB call(AttachmentResCenterVersion2DB attachmentResCenterDB, NewUploadResCenterImageData responseData) {
                                if (responseData != null) {
                                    if (responseData.getData() != null) {
                                        attachmentResCenterDB.picSrc = responseData.getData().getPicSrc();
                                        attachmentResCenterDB.picObj = responseData.getData().getPicObj();
                                        attachmentResCenterDB.save();
                                        return attachmentResCenterDB;
                                    } else {
                                        throw new RuntimeException(responseData.getMessageError());
                                    }
                                } else {
                                    throw new RuntimeException("upload error");
                                }
                            }

                        });
                    }
                })
                .toList();
    }

    private Retrofit getRetrofit() {
        return viewListener.getRetrofit();
    }

    private void clearAttachment() {
        LocalCacheManager.AttachmentShippingResCenter.Builder(viewListener.getParamsModel().getResolutionID())
                .clearAll();
    }

    @Override
    public void onShippingRefChanged(Editable editable) {
        String shippingRef = editable.toString().replaceAll("\\s+", "");
        isShippingRefValid = (shippingRef.length() >= 8 && shippingRef.length() <= 17);

        checkFormValidity();
    }

    @Override
    public void onShippingSpinnerChanged(int position) {
        String shippingId = generateShippingID();
        isShippingSpinnerValid = !shippingId.isEmpty();

        checkFormValidity();
    }

    @Override
    public void onListAttachmentChanged(int itemCount) {
        isListAttachmentValid = (itemCount > 1);

        checkFormValidity();
    }

    private void checkFormValidity() {
        if (isShippingRefValid && isShippingSpinnerValid && isListAttachmentValid) {
            viewListener.setConfirmButtonEnabled();
        } else {
            viewListener.setConfirmButtonDisabled();
        }
    }

    private void showLoadingState() {
        viewListener.dropKeyBoard();
        showLoading(true);
        showMainPage(false);
    }

    private boolean isValidToSubmit(ShippingParamsPostModel params) {
        viewListener.getErrorSpinner().setVisibility(View.GONE);
        viewListener.getShippingRefNum().setError(null);

        if (params.getShippingNumber().replaceAll("\\s+", "").length() == 0) {
            viewListener.getShippingRefNum().setError(viewListener.getActivity().getString(R.string.error_field_required));
            return false;
        }

        if (params.getShippingNumber().length() < 8 || params.getShippingNumber().length() > 17) {
            viewListener.getShippingRefNum().setError(viewListener.getActivity().getString(R.string.error_receipt_number));
            return false;
        }

        if (params.getShippingID().isEmpty()) {
            viewListener.getErrorSpinner().setVisibility(View.VISIBLE);
            return false;
        }

        if (isInstanceForEdit()
                && isShippingRefNumSame(params.getShippingNumber())
                && isShippingsSame(params.getShippingID())) {
            viewListener.getShippingRefNum().setError(viewListener.getActivity().getString(R.string.error_update_receipt_number));
            return false;
        }

        return true;
    }

    private boolean isInstanceForEdit() {
        return viewListener.getParamsModel().getConversationID() != null
                && !viewListener.getParamsModel().getConversationID().isEmpty();
    }

    private boolean isShippingRefNumSame(String shippingRefNum) {
        return shippingRefNum.equals(viewListener.getParamsModel().getShippingRefNum());
    }

    private boolean isShippingsSame(String shippingID) {
        return shippingID.equals(viewListener.getParamsModel().getShippingID());
    }

    private ResCenterKurir.Kurir getSelectedKurir() throws Exception {
        return (ResCenterKurir.Kurir) viewListener.getShippingSpinner().getItemAtPosition(
                viewListener.getShippingSpinner().getSelectedItemPosition() - 1
        );
    }

    private GraphqlRequest getGraphQLRequest(int queryId, Type
            typeOfT, Map<String, Object> variables) {
        return new GraphqlRequest(GraphqlHelper.loadRawString(viewListener.getActivity().getResources(),
                queryId), typeOfT, variables, false);
    }

    private GraphQLResponseSubscriber getResponseSubscriber(boolean isVerify) {
        GraphQLResponseSubscriber responseSubscriber = new GraphQLResponseSubscriber();
        responseSubscriber.setResponseCallbacks(this);
        responseSubscriber.setVerifyCall(isVerify);
        return responseSubscriber;
    }

    @Override
    public void onError(Throwable e) {
        viewListener.toastErrorMessage(e.getLocalizedMessage());
        showLoading(false);
        showMainPage(true);
    }

    @Override
    public void onRequestVerified(GraphqlResponse graphqlResponse) {
        if (graphqlResponse != null) {
            String cacheKey = "";
            if (isInstanceForEdit()) {
                EditAWBResponse editAWBResponse = graphqlResponse.getData(EditAWBResponse.class);
                if (editAWBResponse != null) {
                    List<String> messageError = editAWBResponse.getResInputValidationResponse().getMessageError();
                    if (!messageError.isEmpty())
                        throw new RuntimeException(messageError.get(0));
                    else
                        cacheKey = editAWBResponse.getResInputValidationResponse().getResInputResponseData().getCacheKey();
                }
            } else {
                InputAWBResponse inputAWBResponse = graphqlResponse.getData(InputAWBResponse.class);
                if (inputAWBResponse != null) {
                    List<String> messageError = inputAWBResponse.getResInputValidationResponse().getMessageError();
                    if (!messageError.isEmpty())
                        throw new RuntimeException(messageError.get(0));
                    else
                        cacheKey = inputAWBResponse.getResInputValidationResponse().getResInputResponseData().getCacheKey();
                }
            }
            AWBRequest.setCacheKey(cacheKey);
            resAwbSubmit(AWBRequest, shippingParams);
        }
    }

    @Override
    public void onCreateEditTicket(GraphqlResponse graphqlResponse) {
        if (graphqlResponse != null) {
            if (isInstanceForEdit()) {
                EditAWBResponse editAWBResponse = graphqlResponse.getData(EditAWBResponse.class);
                List<String> messageError = editAWBResponse.getResInputValidationResponse().getMessageError();
                if (!messageError.isEmpty())
                    throw new RuntimeException(messageError.get(0));
            } else {
                InputAWBResponse inputAWBResponse = graphqlResponse.getData(InputAWBResponse.class);
                if (inputAWBResponse != null) {
                    List<String> messageError = inputAWBResponse.getResInputValidationResponse().getMessageError();
                    if (!messageError.isEmpty())
                        throw new RuntimeException(messageError.get(0));
                }
            }
            clearAttachment();
            viewListener.finishAsSuccessResult();
            showLoading(false);
            showMainPage(true);
        }
    }
}
