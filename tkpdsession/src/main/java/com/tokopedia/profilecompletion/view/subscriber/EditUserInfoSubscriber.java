package com.tokopedia.profilecompletion.view.subscriber;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel;

/**
 * @author by nisie on 7/3/17.
 */

public class EditUserInfoSubscriber extends rx.Subscriber<EditUserInfoDomainModel> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        CommonUtils.dumper("NISNIS error" + e.toString());

    }

    @Override
    public void onNext(EditUserInfoDomainModel editUserInfoDomainModel) {
        CommonUtils.dumper("NISNIS sukses");

    }
}
