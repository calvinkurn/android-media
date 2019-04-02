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

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.util.TkpdWebView;
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
    private static final int UNHANDLED_ACTION = -1;
    private static final int ACTION_OPEN_ATTACHMENT = 1;
    private static final int ACTION_OPEN_SHOP_INFO = 2;
    private static final int ACTION_OPEN_PEOPLE_INFO = 3;
    private static final int ACTION_OPEN_INVOICE = 4;
    private static final int ACTION_CANCEL_RESOLUTION = 5;
    private static final int ACTION_REPORT_RESOLUTION = 6;
    private static final int ACTION_ACCEPT_RESOLUTION = 7;
    private static final int ACTION_FINISH_RETUR = 8;
    private static final int ACTION_ACCEPT_ADMIN = 9;
    private static final int ACTION_INPUT_SHIP_REF = 10;
    private static final int ACTION_TRACK_SHIP_REF = 11;
    private static final int ACTION_EDIT_SHIP_REF = 12;
    private static final int ACTION_APPEAL = 13;
    private static final int ACTION_EDIT_SOLUTION = 14;
    private static final int ACTION_UPSERT_RETUR_ADDRESS = 15;
    private static final int ACTION_DETAIL = 16;
    private static final int ACTION_INBOX = 17;
    private static final int ACTION_OPEN_VIDEO = 18;

    @BindView(R2.id.webview)
    TkpdWebView webView;

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

//    private String getUrl(String data) {
//        Log.d(TAG, "getUrl: " + URLGenerator.generateURLSessionLoginV4(data, getContext()));
//        return URLGenerator.generateURLSessionLoginV4(data, getContext());
//    }

//    public String getPath(String resolutionLinkEncode) {
//        Log.d(TAG, "getPath: " + Uri.parse(getUrl(resolutionLinkEncode)).getPath());
//        return Uri.parse(getUrl(resolutionLinkEncode)).getPath();
//    }
//
//    public String getQuery(String resolutionLinkEncode) {
//        Log.d(TAG, "getQuery: " + Uri.parse(getUrl(resolutionLinkEncode)).getQuery());
//        return Uri.parse(getUrl(resolutionLinkEncode)).getQuery();
//    }

    @Override
    public void renderData(@NonNull DetailResCenterData.Detail data) {
        this.resolutionDetailModel = data;
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(false);
        webView.setWebViewClient(new MyWebClient());
        webView.loadAuthUrlWithFlags(getUrl(data.getResolutionLinkEncode()));
    }

    private String getUrl(String data) {
        Log.d(TAG, "getImageUrl: " + URLGenerator.generateURLSessionLogin(data, getContext()));
        return URLGenerator.generateURLSessionLogin(data, getContext());
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

            final String parserUrl = url;
            switch (getUrlToHandle(parserUrl)) {
                case ACTION_OPEN_ATTACHMENT:
                    listener.openAttachment(parserUrl);
                    return true;
                case ACTION_OPEN_SHOP_INFO:
                    listener.openShop();
                    return true;
                case ACTION_OPEN_PEOPLE_INFO:
                    listener.openPeople(parserUrl);
                    return true;
                case ACTION_OPEN_INVOICE:
                    listener.openInvoice();
                    return true;
                case ACTION_CANCEL_RESOLUTION:
                    listener.showConfirmationDialog(R.string.msg_rescen_cancel, new ConfirmationDialog.Listener() {
                        @Override
                        public void onSubmitButtonClick() {
                            listener.actionCancelResolution(getResolutionIDFromQueryParameter(parserUrl));
                        }
                    });
                    return true;
                case ACTION_REPORT_RESOLUTION:
                    listener.showConfirmationDialog(R.string.msg_rescen_help, new ConfirmationDialog.Listener() {
                        @Override
                        public void onSubmitButtonClick() {
                            listener.actionReportResolution(getResolutionIDFromQueryParameter(parserUrl));
                        }
                    });
                    return true;
                case ACTION_ACCEPT_RESOLUTION:
                    listener.showConfirmationDialog(R.string.msg_accept_sol, new ConfirmationDialog.Listener() {
                        @Override
                        public void onSubmitButtonClick() {
                            if (resolutionDetailModel.getResolutionLast().getLastShowAcceptReturButton() == 1) {
                                listener.openInputAddress();
                            } else {
                                listener.actionAcceptResolution(getResolutionIDFromQueryParameter(parserUrl));
                            }
                        }
                    });
                    return true;
                case ACTION_FINISH_RETUR:
                    listener.showConfirmationDialog(R.string.msg_rescen_finish, new ConfirmationDialog.Listener() {
                        @Override
                        public void onSubmitButtonClick() {
                            listener.actionFinishRetur(getResolutionIDFromQueryParameter(parserUrl));
                        }
                    });
                    return true;
                case ACTION_ACCEPT_ADMIN:
                    listener.showConfirmationDialog(R.string.msg_accept_admin, new ConfirmationDialog.Listener() {
                        @Override
                        public void onSubmitButtonClick() {
                            if (resolutionDetailModel.getResolutionLast().getLastShowAcceptAdminReturButton() == 1) {
                                listener.openInputAddressForAcceptAdmin();
                            } else {
                                listener.actionAcceptAdmin(getResolutionIDFromQueryParameter(parserUrl));
                            }
                        }
                    });
                    return true;
                case ACTION_INPUT_SHIP_REF:
                    listener.openInputShippingRef();
                    return true;
                case ACTION_TRACK_SHIP_REF:
                    listener.openTrackShippingRef(parserUrl);
                    return true;
                case ACTION_EDIT_SHIP_REF:
                    listener.openEditShippingRef(parserUrl);
                    return true;
                case ACTION_APPEAL:
                    listener.openAppealSolution(getResolutionIDFromQueryParameter(parserUrl));
                    return true;
                case ACTION_EDIT_SOLUTION:
                    listener.openEditSolution(getResolutionIDFromQueryParameter(parserUrl));
                    return true;
                case ACTION_UPSERT_RETUR_ADDRESS:
                    if (Uri.parse(parserUrl).getQueryParameter("act").equals(String.valueOf(2))) {
                        listener.openEditAddress(parserUrl);
                    } else {
                        listener.openInputAddressMigrateVersion();
                    }
                    return true;
                case ACTION_DETAIL:
                    return false;
                case ACTION_INBOX:
                    listener.setFailSaveRespond();
                    listener.setErrorWvLogin();
                    return true;
                case UNHANDLED_ACTION:
                    listener.setFailSaveRespond();
                    listener.setErrorWvLogin();
                    return true;
                case ACTION_OPEN_VIDEO:
                    listener.openVideoPlayer(parserUrl);
                    return true;
                default:
                    return false;
            }
        }
    }

    private int getUrlToHandle(String url) {
        try {
            if (Uri.parse(url).getLastPathSegment().contains("attachment.pl")) {
                return ACTION_OPEN_ATTACHMENT;
            } else if (Uri.parse(url).getLastPathSegment().contains("shop.pl")) {
                return ACTION_OPEN_SHOP_INFO;
            } else if (Uri.parse(url).getLastPathSegment().contains("people.pl")) {
                return ACTION_OPEN_PEOPLE_INFO;
            } else if (Uri.parse(url).getLastPathSegment().contains("invoice.pl")) {
                return ACTION_OPEN_INVOICE;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("cancel_resolution")) {
                return ACTION_CANCEL_RESOLUTION;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("report_resolution")) {
                return ACTION_REPORT_RESOLUTION;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("accept_resolution")) {
                return ACTION_ACCEPT_RESOLUTION;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("finish_retur")) {
                return ACTION_FINISH_RETUR;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("accept_admin")) {
                return ACTION_ACCEPT_ADMIN;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("input_ship_ref")) {
                return ACTION_INPUT_SHIP_REF;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("track_ship_ref")) {
                return ACTION_TRACK_SHIP_REF;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("edit_ship_ref")) {
                return ACTION_EDIT_SHIP_REF;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("appeal")) {
                return ACTION_APPEAL;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("edit_solution")) {
                return ACTION_EDIT_SOLUTION;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("upsert_retur_address")) {
                return ACTION_UPSERT_RETUR_ADDRESS;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("detail")) {
                return ACTION_DETAIL;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("inbox")) {
                return ACTION_INBOX;
            } else if (isResolutionCenter(Uri.parse(url))
                    && Uri.parse(url).getQueryParameter("action").contains("popup_video")) {
                return ACTION_OPEN_VIDEO;
            } else {
                return 0;
            }
        } catch (NullPointerException e) {
            return UNHANDLED_ACTION;
        }
    }

    private boolean isResolutionCenter(Uri uri) {
        return uri.getLastPathSegment().contains("resolution-center.pl");
    }

    private String getResolutionIDFromQueryParameter(String parserUrl) {
        return Uri.parse(parserUrl).getQueryParameter("id");
    }
}
