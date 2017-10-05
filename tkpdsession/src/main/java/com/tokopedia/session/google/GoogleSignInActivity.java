package com.tokopedia.session.google;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.tokopedia.session.R;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.appsflyer.AppsFlyerLib.LOG_TAG;

public class GoogleSignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_SIGN_IN_GOOGLE = 7777;
    public static final String KEY_GOOGLE_ACCOUNT = "GoogleSignInAccount";
    public static final String KEY_GOOGLE_ACCOUNT_TOKEN = "GoogleSignInAccAccount";
    private static final String scope = "oauth2:https://www.googleapis.com/auth/plus.login";
    private static final int REQUEST_GPLUS_AUTHORIZE = 8888;
    private static final java.lang.String AUTH_TOKEN = "authtoken";
    protected GoogleApiClient mGoogleApiClient;
    private GoogleSignInResult signInResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartlock);

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and
        // basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
//                .requestScopes(new Scope("https://www.googleapis.com/auth/user.birthday.read"))
//                .requestScopes(new Scope(Scopes.PROFILE))
//                .requestScopes(new Scope(Scopes.PLUS_ME))
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

//        signOut();
        signIn();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from

        setResult(resultCode);

        if (requestCode == RC_SIGN_IN_GOOGLE && resultCode == RESULT_OK) {
            signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            Observable<String> observable = Observable.just(true)
                    .flatMap(new Func1<Boolean, Observable<String>>() {
                        @Override
                        public Observable<String> call(Boolean aBoolean) {

                            try {
                                String accessToken = GoogleAuthUtil.getToken(getApplicationContext(), signInResult.getSignInAccount().getEmail(), scope);
                                accessToken.equals("");
                                return Observable.just(accessToken);

                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            } catch (GooglePlayServicesAvailabilityException e) {
                                Log.w(LOG_TAG, "Google Play services not available.");
                                Intent recover = e.getIntent();
                                startActivityForResult(recover, REQUEST_GPLUS_AUTHORIZE);
                            } catch (UserRecoverableAuthException e) {
                                // Recover (with e.getIntent())
                                Log.w(LOG_TAG, "User must approve " + e.toString());
                                Intent recover = e.getIntent();
                                startActivityForResult(recover, REQUEST_GPLUS_AUTHORIZE);
                            } catch (GoogleAuthException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                            return Observable.just("");
                        }
                    });

            Subscriber<String> subscriber = new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    signOut();
                    finish();
                }

                @Override
                public void onNext(String accessToken) {
                    signInWithToken(accessToken);
                }
            };

            CompositeSubscription compositeSubscription = new CompositeSubscription();
            compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber));
        } else if (requestCode == REQUEST_GPLUS_AUTHORIZE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            String oneTimeToken = extra.getString(AUTH_TOKEN);
            signInWithToken(oneTimeToken);
        } else {
            signOut();
            finish();
        }
    }

    private void signInWithToken(String accessToken) {
        if (!TextUtils.isEmpty(accessToken)) {
            handleSignInResult(accessToken, signInResult);
            signOut();
            finish();
        }
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void handleSignInResult(String accessToken, GoogleSignInResult result) {
        GoogleSignInAccount account = null;
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            account = result.getSignInAccount();
        }
        Intent intent = new Intent();
        intent.putExtra(KEY_GOOGLE_ACCOUNT, account);
        intent.putExtra(KEY_GOOGLE_ACCOUNT_TOKEN, accessToken);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.i("GMAIL", "onResult: ");
                        }
                    });
        }
    }

    public static Intent getSignInIntent(Context context) {
        return new Intent(context, GoogleSignInActivity.class);
    }
}