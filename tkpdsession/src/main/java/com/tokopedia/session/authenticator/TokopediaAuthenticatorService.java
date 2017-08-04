package com.tokopedia.session.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TokopediaAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        TokopediaAuthenticator authenticator = new TokopediaAuthenticator(this);
        return authenticator.getIBinder();
    }
}
