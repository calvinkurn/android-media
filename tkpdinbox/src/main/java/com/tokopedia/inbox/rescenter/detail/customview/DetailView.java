package com.tokopedia.inbox.rescenter.detail.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.inbox.rescenter.create.customview.BaseView;
import com.tokopedia.inbox.rescenter.detail.dialog.ConfirmationDialog;
import com.tokopedia.inbox.rescenter.detail.listener.DetailResCenterView;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData.Detail;

import butterknife.BindView;

/**
 * Created on 8/16/16.
 */
public class DetailView extends BaseView<Detail, DetailResCenterView> {

    public static final String TAG = DetailView.class.getSimpleName();

    @BindView(R2.id.webview)
    WebView webView;

    private Detail resolutionDetailModel;

    public DetailView(Context context) {
        super(context);
    }

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterView detailResCenterView) {
        this.listener = detailResCenterView;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_webview;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

//    @Override
//    public void renderData(@NonNull DetailResCenterData.Detail data) {
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setBuiltInZoomControls(false);
//        webView.setWebViewClient(new MyWebClient());
//        webView.loadUrl(getUrl(data.getResolutionLinkEncode()),
//                AuthUtil.generateHeaders(
//                        getPath(data.getResolutionLinkEncode()),
//                        getQuery(data.getResolutionLinkEncode()),
//                        "GET",
//                        "web_service_v4")
//        );
//    }

    @Override
    public void renderData(@NonNull DetailResCenterData.Detail data) {
        this.resolutionDetailModel = data;
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(false);
        webView.setWebViewClient(new MyWebClient());
        webView.loadUrl(getUrl(data.getResolutionLinkEncode()));
    }

    private String getUrl(String data) {
        Log.d(TAG, "getUrl: " + URLGenerator.generateURLSessionLoginV4(data, getContext()));
        return URLGenerator.generateURLSessionLoginV4(data, getContext());
    }

    public String getPath(String resolutionLinkEncode) {
        Log.d(TAG, "getPath: " + Uri.parse(getUrl(resolutionLinkEncode)).getPath());
        return Uri.parse(getUrl(resolutionLinkEncode)).getPath();
    }

    public String getQuery(String resolutionLinkEncode) {
        Log.d(TAG, "getQuery: " + Uri.parse(getUrl(resolutionLinkEncode)).getQuery());
        return Uri.parse(getUrl(resolutionLinkEncode)).getQuery();
    }

    private class MyWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            listener.setProgressLoading(true);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Log.d(TAG, "onReceivedSslError url:" + error.getUrl());
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.d(TAG, "onReceivedError url:" + view.getUrl());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d(TAG, "onReceivedError errorCode: " + error.getErrorCode());
                Log.d(TAG, "onReceivedError description: " + error.getDescription());
            }
            super.onReceivedError(view, request, error);
            listener.setFailSaveRespond();
            listener.setErrorWvLogin();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished: " + url);
            super.onPageFinished(view, url);
            String lastPathSegment = Uri.parse(url).getLastPathSegment();
            if (lastPathSegment == null || !lastPathSegment.contains("resolution-center.pl")) {
                listener.setFailSaveRespond();
                listener.setErrorWvLogin();
            } else {
                listener.setProgressLoading(false);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading: " + url);

            if (Uri.parse(url).getLastPathSegment().contains("attachment.pl")) {
                listener.openAttachment(url);
                return true;
            } else if (Uri.parse(url).getLastPathSegment().contains("shop.pl")) {
                listener.openShop();
                return true;
            } else if (Uri.parse(url).getLastPathSegment().contains("people.pl")) {
                listener.openPeople(url);
                return true;
            } else if (Uri.parse(url).getLastPathSegment().contains("invoice.pl")) {
                listener.openInvoice();
                return true;
            } else {
                return Uri.parse(url).getLastPathSegment().contains("resolution-center.pl") && generateActionFromUrl(url);
            }
        }
    }

    private boolean generateActionFromUrl(String url) {
        Log.d(TAG, "generateActionFromUrl: " + url);
        String paramAction = Uri.parse(url).getQueryParameter("action");
        final String paramID = Uri.parse(url).getQueryParameter("id");
        switch (paramAction) {
            case "cancel_resolution":
                listener.showConfirmationDialog(R.string.msg_rescen_cancel, new ConfirmationDialog.Listener() {
                    @Override
                    public void onSubmitButtonClick() {
                        listener.actionCancelResolution(paramID);
                    }
                });
                return true;
            case "report_resolution":
                listener.showConfirmationDialog(R.string.msg_rescen_help, new ConfirmationDialog.Listener() {
                    @Override
                    public void onSubmitButtonClick() {
                        listener.actionReportResolution(paramID);
                    }
                });
                return true;
            case "accept_resolution":
                listener.showConfirmationDialog(R.string.msg_accept_sol, new ConfirmationDialog.Listener() {
                    @Override
                    public void onSubmitButtonClick() {
                        if (resolutionDetailModel.getResolutionLast().getLastShowAcceptReturButton() == 1) {
                            listener.openInputAddress();
                        } else {
                            listener.actionAcceptResolution(paramID);
                        }
                    }
                });
                return true;
            case "finish_retur":
                listener.showConfirmationDialog(R.string.msg_rescen_finish, new ConfirmationDialog.Listener() {
                    @Override
                    public void onSubmitButtonClick() {
                        listener.actionFinishRetur(paramID);
                    }
                });
                return true;
            case "accept_admin":
                listener.showConfirmationDialog(R.string.msg_accept_admin, new ConfirmationDialog.Listener() {
                    @Override
                    public void onSubmitButtonClick() {
                        if (resolutionDetailModel.getResolutionLast().getLastShowAcceptAdminReturButton() == 1) {
                            listener.openInputAddressForAcceptAdmin();
                        } else {
                            listener.actionAcceptAdmin(paramID);
                        }
                    }
                });
                return true;
            case "input_ship_ref":
                listener.openInputShippingRef();
                return true;
            case "track_ship_ref":
                listener.openTrackShippingRef(url);
                return true;
            case "edit_ship_ref":
                listener.openEditShippingRef(url);
                return true;
            case "appeal":
                listener.openAppealSolution(paramID);
                return true;
            case "edit_solution":
                listener.openEditSolution(paramID);
                return true;
            case "upsert_retur_address":
                if (Uri.parse(url).getQueryParameter("act").equals(String.valueOf(2))) {
                    listener.openEditAddress(url);
                } else {
                    listener.openInputAddressMigrateVersion();
                }
                return true;
            case "detail":
                return false;
            case "inbox":
                listener.setFailSaveRespond();
                listener.setErrorWvLogin();
                return true;
            default:
                return true;
        }
    }

}
