package com.tokopedia.seller.product.edit.view.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.myproduct.utils.FileUtils;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hendry on 28/03/18.
 */

public class ProductAddImagePresenter extends BaseDaggerPresenter<ProductAddImageView> {

    private CompositeSubscription compositeSubscription;

    public static final int MAX_IMAGES = 5;
    public static final String CONTENT_GMAIL_LS = "content://gmail-ls/";

    public ProductAddImagePresenter(){

    }

    public void convertUrisToLocalPaths(ArrayList<Uri> imageUris){
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }

        Subscription subscription = Observable.just(imageUris).map(new Func1<ArrayList<Uri>, ArrayList<String>>() {
            @Override
            public ArrayList<String> call(ArrayList<Uri> imageUris) {
                int imagescount = (imageUris.size() > MAX_IMAGES) ? MAX_IMAGES : imageUris.size();
                ArrayList<String> imageUrls = new ArrayList<>();
                for (int i = 0; i < imagescount; i++) {
                    Uri imageUri = imageUris.get(i);
                    String uriString = imageUri.toString();
                    if (uriString.startsWith(CONTENT_GMAIL_LS)) {// get email attachment from gmail
                        imageUrls.add(FileUtils.getPathFromGmail(getView().getContext(), imageUri));
                    } else { // get extras for import from gallery
                        String url = FileUtils.getTkpdPathFromURI(getView().getContext(), imageUri);
                        if (!TextUtils.isEmpty(url)) {
                            imageUrls.add(url);
                        }
                    }
                }
                return imageUrls;
            }
        }).subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(e);
                    }

                    @Override
                    public void onNext(ArrayList<String> imageUrls) {
                        getView().onSuccessStoreImageToLocal(imageUrls);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (compositeSubscription!= null) {
            compositeSubscription.unsubscribe();
        }
    }
}
