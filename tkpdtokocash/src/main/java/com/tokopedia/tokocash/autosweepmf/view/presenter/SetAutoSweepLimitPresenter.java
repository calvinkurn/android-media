package com.tokopedia.tokocash.autosweepmf.view.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.tokocash.autosweepmf.domain.interactor.PostAutoSweepLimitUseCase;
import com.tokopedia.tokocash.autosweepmf.view.contract.SetAutoSweepLimitContract;
import com.tokopedia.tokocash.autosweepmf.view.mapper.AutoSweepLimitMapper;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.AUTO_SWEEP_MF_MAX_LIMIT;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.AUTO_SWEEP_MF_MIN_LIMIT;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.EXTRA_AUTO_SWEEP_LIMIT;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.EXTRA_AVAILABLE_TOKOCASH;

public class SetAutoSweepLimitPresenter extends BaseDaggerPresenter<SetAutoSweepLimitContract.View>
        implements SetAutoSweepLimitContract.Presenter {
    private PostAutoSweepLimitUseCase mAutoSweepLimitUseCase;
    private AutoSweepLimitMapper mMapper;

    @Inject
    public SetAutoSweepLimitPresenter(@NonNull PostAutoSweepLimitUseCase autoSweepLimitUseCase,
                                      @NonNull AutoSweepLimitMapper mapper) {
        this.mAutoSweepLimitUseCase = autoSweepLimitUseCase;
        this.mMapper = mapper;
    }

    @Override
    public void destroyView() {
    }

    @Override
    public void postAutoSweepLimit(boolean isEnable, int amount) {
        getView().showLoading();
        mAutoSweepLimitUseCase.setBody(getPayload(isEnable, amount));
        mAutoSweepLimitUseCase.getExecuteObservable(RequestParams.EMPTY).map(
                new Func1<com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit, AutoSweepLimit>() {
                    @Override
                    public AutoSweepLimit call(com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimit data) {
                        return mMapper.transform(data);

                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AutoSweepLimit>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideLoading();
                        getView().onErrorAccountStatus(ErrorHandler.getErrorMessage(getView()
                                .getActivityContext(), e));
                    }

                    @Override
                    public void onNext(AutoSweepLimit data) {
                        getView().hideLoading();
                        getView().onSuccessAccountStatus(data);
                    }
                });
    }

    public String getTokocashBalance(@NonNull Bundle bundle) {
        return bundle.getString(EXTRA_AVAILABLE_TOKOCASH);
    }

    public int getAutoSweepMaxLimit() {
        return AUTO_SWEEP_MF_MAX_LIMIT;
    }

    public int getAutoSweepMinLimit() {
        return AUTO_SWEEP_MF_MIN_LIMIT;
    }

    /**
     * Payload creator utility method for auto sweep detail api
     *
     * @param isEnable - Auto sweep status
     * @param amount   - Auto sweep limit (Min CommonConstant.AUTO_SWEEP_MF_MIN_LIMIT)
     * @return Payload JSON object
     */
    private JsonObject getPayload(boolean isEnable, int amount) {
        JsonObject outerNode = new JsonObject();

        if (isEnable) {
            outerNode.addProperty(CommonConstant.ApiKeys.KEY_AUTO_SWEEP, 1);
            outerNode.addProperty(CommonConstant.ApiKeys.KEY_AMOUNT_LIMIT, amount);
        } else {
            outerNode.addProperty(CommonConstant.ApiKeys.KEY_AUTO_SWEEP, 0);
        }

        return outerNode;
    }

    /**
     * Invoke broadcast message regarding mutual fund and auto sweep status changed event
     */
    public void sendMessage() {
        Intent intent = new Intent(CommonConstant.EVENT_AUTOSWEEPMF_STATUS_CHANGED);
        intent.putExtra(CommonConstant.EVENT_KEY_NEEDED_RELOADING, true);
        LocalBroadcastManager.getInstance(getView().getAppContext()).sendBroadcast(intent);
    }

    public long getAutoSweepLimit(@NonNull Bundle bundle) {
        if (bundle.getLong(EXTRA_AUTO_SWEEP_LIMIT) <= 0) {
            return getAutoSweepMinLimit();
        }

        return bundle.getLong(EXTRA_AUTO_SWEEP_LIMIT);
    }

}
