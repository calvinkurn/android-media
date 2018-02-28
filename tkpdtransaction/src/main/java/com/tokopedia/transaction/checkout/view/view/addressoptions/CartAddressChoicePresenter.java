package com.tokopedia.transaction.checkout.view.view.addressoptions;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.transaction.checkout.domain.usecase.GetDefaultAddressUseCase;
import com.tokopedia.transaction.checkout.util.PeopleAddressAuthUtil;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.presenter.ICartAddressChoicePresenter;
import com.tokopedia.transaction.checkout.view.view.ICartAddressChoiceView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Irfan Khoirul on 05/02/18
 *         Aghny A. Putra on 27/02/18
 */

public class CartAddressChoicePresenter extends BaseDaggerPresenter<ICartAddressChoiceView>
        implements ICartAddressChoicePresenter {

    private static final String TAG = CartAddressChoicePresenter.class.getSimpleName();

    private static final int FIRST_ELEMENT = 0;

    private static final int DEFAULT_ORDER = 1;
    private static final String DEFAULT_QUERY = "";
    private static final int DEFAULT_PAGE = 1;

    private GetDefaultAddressUseCase mGetDefaultAddressUseCase;
    private RecipientAddressModel mSelectedRecipientAddress;

    @Inject
    public CartAddressChoicePresenter(GetDefaultAddressUseCase getDefaultAddressUseCase) {
        mGetDefaultAddressUseCase = getDefaultAddressUseCase;
    }

    @Override
    public void attachView(ICartAddressChoiceView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public void getAddressShortedList(Context context) {
        mGetDefaultAddressUseCase.execute(
                PeopleAddressAuthUtil.getRequestParams(context, DEFAULT_ORDER, DEFAULT_QUERY, DEFAULT_PAGE),
                new Subscriber<List<RecipientAddressModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.d(TAG, throwable.getMessage());
                    }

                    @Override
                    public void onNext(List<RecipientAddressModel> shipmentAddressModels) {
                        if (!shipmentAddressModels.isEmpty()) {
                            getView().renderRecipientData(shortList(shipmentAddressModels));
                        }

                        Log.d(TAG, "Size: " + shipmentAddressModels.size());
                    }
                });
    }

    @Override
    public void setSelectedRecipientAddress(RecipientAddressModel recipientAddress) {
        this.mSelectedRecipientAddress = recipientAddress;
    }

    @Override
    public RecipientAddressModel getSelectedRecipientAddress() {
        return mSelectedRecipientAddress;
    }

    /**
     * Logic for creating short listed address
     *
     * @param recipientAddressModels
     * @return
     */
    private List<RecipientAddressModel> shortList(List<RecipientAddressModel> recipientAddressModels) {
        List<RecipientAddressModel> shortList = new ArrayList<>();

        if (recipientAddressModels.remove(mSelectedRecipientAddress)) {
            shortList.add(mSelectedRecipientAddress);
            shortList.add(recipientAddressModels.get(FIRST_ELEMENT));
        } else {
            shortList.addAll(recipientAddressModels.subList(0,2));
            shortList.get(FIRST_ELEMENT).setSelected(true);
        }

        return shortList;
    }

}
