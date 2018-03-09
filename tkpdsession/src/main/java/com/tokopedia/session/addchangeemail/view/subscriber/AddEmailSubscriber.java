package com.tokopedia.session.addchangeemail.view.subscriber;

import android.content.Context;

import com.tokopedia.session.addchangeemail.view.listener.AddEmailListener;
import com.tokopedia.session.addchangeemail.view.viewmodel.AddEmailViewModel;

import rx.Subscriber;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailSubscriber  extends Subscriber<AddEmailViewModel> {

    private AddEmailListener.View mainView;
    private Context context;

    public AddEmailSubscriber(Context context, AddEmailListener.View mainView) {
        this.mainView = mainView;
        this.context = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onNext(AddEmailViewModel addEmailViewModel) {
    }
}
