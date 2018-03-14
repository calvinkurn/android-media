package com.tokopedia.transaction.checkout.view.view.addressoptions;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.usecase.GetPeopleAddressUseCase;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static java.lang.Math.min;

/**
 * @author Irfan Khoirul on 05/02/18
 *         Aghny A. Putra on 27/02/18
 */

public class CartAddressChoicePresenter extends BaseDaggerPresenter<ICartAddressChoiceView>
        implements ICartAddressChoicePresenter {

    private static final int SHORT_LIST_SIZE = 2;
    private static final int PRIME_ADDRESS = 2;

    private static final String TAG = CartAddressChoicePresenter.class.getSimpleName();

    private static final int DEFAULT_ORDER = 1;
    private static final String DEFAULT_QUERY = "";
    private static final int DEFAULT_PAGE = 1;

    private GetPeopleAddressUseCase mGetPeopleAddressUseCase;
    private RecipientAddressModel mSelectedRecipientAddress;

    @Inject
    public CartAddressChoicePresenter(GetPeopleAddressUseCase getPeopleAddressUseCase) {
        mGetPeopleAddressUseCase = getPeopleAddressUseCase;
    }

    @Override
    public void attachView(ICartAddressChoiceView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        mGetPeopleAddressUseCase.unsubscribe();
    }

    @Override
    public void getAddressShortedList(Context context) {
        getView().showLoading();
        mGetPeopleAddressUseCase.execute(mGetPeopleAddressUseCase
                        .getRequestParams(context, DEFAULT_ORDER, DEFAULT_QUERY, DEFAULT_PAGE),
                new Subscriber<List<RecipientAddressModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        if (isViewAttached()) {
                            getView().hideLoading();
                            String message;
                            if (throwable instanceof UnknownHostException ||
                                    throwable instanceof ConnectException ||
                                    throwable instanceof SocketTimeoutException) {
                                message = getView().getActivity().getResources().getString(
                                        R.string.msg_no_connection);
                            } else if (throwable instanceof UnProcessableHttpException) {
                                message = TextUtils.isEmpty(throwable.getMessage()) ?
                                        getView().getActivity().getResources().getString(
                                                R.string.msg_no_connection) :
                                        throwable.getMessage();
                            } else {
                                message = getView().getActivity().getResources().getString(
                                        R.string.default_request_error_unknown);
                            }
                            getView().showNoConnection(message);
                        }
                    }

                    @Override
                    public void onNext(List<RecipientAddressModel> shipmentAddressModels) {
                        if (!shipmentAddressModels.isEmpty()) {
                            if (isViewAttached()) {
                                getView().hideLoading();
                                getView().renderRecipientData(shortList(shipmentAddressModels));
                            }
                        }

                        Log.d(TAG, "Size: " + shipmentAddressModels.size());
                    }
                });
    }

    @Override
    public void setSelectedRecipientAddress(RecipientAddressModel recipientAddress) {
        this.mSelectedRecipientAddress = recipientAddress;
        getView().renderSaveButtonEnabled();
    }

    @Override
    public RecipientAddressModel getSelectedRecipientAddress() {
        return mSelectedRecipientAddress;
    }

    /**
     * Logic for creating short listed address
     *
     * @param addressList
     * @return
     */
    private List<RecipientAddressModel> shortList(final List<RecipientAddressModel> addressList) {
        final int shortListSize = min(addressList.size(), SHORT_LIST_SIZE);

        List<RecipientAddressModel> shortList = new ArrayList<RecipientAddressModel>() {{
            addAll(addressList.subList(0, shortListSize));
        }};

        if (mSelectedRecipientAddress != null) {
            if (mSelectedRecipientAddress.getAddressStatus() == PRIME_ADDRESS) {
                shortList.set(0, mSelectedRecipientAddress);
            } else {
                shortList.set(shortListSize - 1, mSelectedRecipientAddress);
            }
        }

        return shortList;
    }
}