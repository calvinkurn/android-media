package com.tokopedia.seller.shop.setting.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.setting.constant.ShopSettingConstant;
import com.tokopedia.seller.shop.setting.constant.ShopCloseAction;
import com.tokopedia.seller.shop.setting.domain.UpdateShopScheduleRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/15/17.
 */

public class UpdateShopScheduleUseCase extends UseCase<Boolean> {

    private final UpdateShopScheduleRepository updateShopScheduleRepository;

    @Inject
    public UpdateShopScheduleUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                     UpdateShopScheduleRepository updateShopScheduleRepository) {
        super(threadExecutor, postExecutionThread);
        this.updateShopScheduleRepository = updateShopScheduleRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return updateShopScheduleRepository.updateShopSchedule(requestParams.getParamsAllValueInString());
    }

    /**
     * update shop schedule
     * @param closedNote shop note why it's closed
     * @param closeStart (string with DD/MM/YYYY format)
     * @param closeEnd (string with DD/MM/YYYY format)
     * @param closeAction ⇒ 1 ⇒ Open Shop (close_start, close_end, closed_note will be ignored)
                            2 ⇒ Close Shop (close_start will be ignored)
                            3 ⇒ Set Close Schedule
                            4 ⇒ Abort Close Schedule (close_start, close_end, closed_note will be ignored)
                            5 ⇒ Extend Close Shop (close_start will be ignored)*/
    public static RequestParams cerateRequestParams(String closedNote, String closeStart,
                                          String closeEnd, @ShopCloseAction int closeAction){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ShopSettingConstant.CLOSED_NOTE, closedNote);
        requestParams.putString(ShopSettingConstant.CLOSE_START, closeStart);
        requestParams.putString(ShopSettingConstant.CLOSE_END, closeEnd);
        requestParams.putString(ShopSettingConstant.CLOSE_ACTION, String.valueOf(closeAction));
        return requestParams;
    }
}
