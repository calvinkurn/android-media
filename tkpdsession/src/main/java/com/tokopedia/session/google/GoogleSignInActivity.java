package com.tokopedia.session.google;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

public class GoogleSignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_SIGN_IN_GOOGLE = 7777;
    public static final String KEY_GOOGLE_ACCOUNT = "GoogleSignInAccount";
    protected GoogleApiClient mGoogleApiClient;

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

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from

        setResult(resultCode);

        if (requestCode == RC_SIGN_IN_GOOGLE && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        signOut();
        finish();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        GoogleSignInAccount account = null;
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            account = result.getSignInAccount();
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent.putExtra(KEY_GOOGLE_ACCOUNT, account));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.i("GMAIL", "onResult: ");
                    }
                });
    }

}