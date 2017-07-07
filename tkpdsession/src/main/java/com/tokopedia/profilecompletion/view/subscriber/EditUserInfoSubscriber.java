package com.tokopedia.profilecompletion.view.subscriber;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.profilecompletion.domain.model.EditUserInfoDomainModel;
import com.tokopedia.profilecompletion.view.listener.EditProfileListener;

/**
 * @author by nisie on 7/3/17.
 */

public class EditUserInfoSubscriber extends rx.Subscriber<EditUserInfoDomainModel> {

    private int edit;
    private EditProfileListener listener;

    public EditUserInfoSubscriber(EditProfileListener listener, int edit) {
        this.listener = listener;
        this.edit = edit;
    }

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
        if(editUserInfoDomainModel.isSuccess()) {
            listener.onSuccessEditProfile(edit);
        }else {
            listener.onFailedEditProfile(editUserInfoDomainModel.getErrorMessage());
        }
    }
}
