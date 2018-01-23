//package com.tokopedia.discovery.newdiscovery.base;
//
//import rx.Subscriber;
//
///**
// * Created by sachinbansal on 1/23/18.
// */
//
//public class ImageSearchProductSubscriber<D2 extends BaseDiscoveryContract.View>
//        extends Subscriber<> {
//
//
//    private D2 discoveryView;
//
//    public ImageSearchProductSubscriber(D2 discoveryView) {
//        this.discoveryView = discoveryView;
//    }
//
//    @Override
//    public void onCompleted() {
//
//    }
//
//    @Override
//    public void onError(Throwable throwable) {
//        discoveryView.onHandleResponseError();
//        throwable.printStackTrace();
//    }
//
//    @Override
//    public void onNext(Object o) {
//        discoveryView.onHandleImageSearchResponse();
//    }
//}
