package com.tokopedia.discovery.model;

/**
 * Created by noiz354 on 3/23/16.
 */
public final class ErrorContainer implements ObjContainer<Throwable> {
    Throwable e;

    public ErrorContainer(Throwable e){
        this.e = e;
    }
    @Override
    public Throwable body() {
        return e;
    }
}
