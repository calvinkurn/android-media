package com.tokopedia.tkpd.tkpdfeed.feedplus.view.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.widgets.ShareItem;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.ShareFeedAdapter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.analytics.FeedTrackingEventLabel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.FeedPlusDetailFragment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.FeedPlusFragment;

import java.util.ArrayList;

/**
 * @author by nisie on 5/18/17.
 */

public class ShareBottomDialog {

    private static final int SNACKBAR_DURATION = 3500;
    private static final int SHARE_GOOGLE_REQUEST_CODE = 123;
    private static final String CHECK_NOW = " cek sekarang di :\n";
    private final Activity activity;
    private final BottomSheetDialog dialog;
    private final RecyclerView appGrid;
    private final CallbackManager callbackManager;
    private final Fragment fragment;
    private final android.support.v4.app.Fragment fragmentV4;

    private ShareData shareModel;
    private ShareFeedAdapter adapter;
    private ArrayList<ShareItem> list;

    public ShareBottomDialog(Activity activity,
                             CallbackManager callbackManager) {
        this.activity = activity;
        this.fragment = null;
        this.fragmentV4 = null;
        this.dialog = new BottomSheetDialog(activity);
        this.callbackManager = callbackManager;
        this.dialog.setContentView(R.layout.share_feed_dialog);
        appGrid = (RecyclerView) this.dialog.findViewById(R.id.grid);
        initVar(activity);
        initAdapter();
        setListener();
        setShareList();
    }

    public ShareBottomDialog(Fragment fragment, CallbackManager callbackManager) {
        this.fragment = fragment;
        this.fragmentV4 = null;
        this.activity = fragment.getActivity();
        this.dialog = new BottomSheetDialog(activity);
        this.callbackManager = callbackManager;
        this.dialog.setContentView(R.layout.share_feed_dialog);
        appGrid = (RecyclerView) this.dialog.findViewById(R.id.grid);
        initVar(fragment.getActivity());
        initAdapter();
        setListener();
        setShareList();
    }

    public ShareBottomDialog(android.support.v4.app.Fragment fragment, CallbackManager callbackManager) {
        this.fragment = null;
        this.fragmentV4 = fragment;
        this.activity = fragment.getActivity();
        this.dialog = new BottomSheetDialog(activity);
        this.callbackManager = callbackManager;
        this.dialog.setContentView(R.layout.share_feed_dialog);
        appGrid = (RecyclerView) this.dialog.findViewById(R.id.grid);
        initVar(fragment.getActivity());
        initAdapter();
        setListener();
        setShareList();
    }

    private void initVar(Context context) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        appGrid.setLayoutManager(layoutManager);
        list = new ArrayList<>();
    }

    public void setShareModel(ShareData shareModel) {
        this.shareModel = shareModel;
    }

    private void initAdapter() {
        adapter = new ShareFeedAdapter();
        appGrid.setAdapter(adapter);
    }

    protected void setShareList() {

        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_g), activity.getString(R.string.share_gplus), shareGoogle()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_fb), activity.getString(R.string.share_fb), shareFb()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_twitter), activity.getString(R.string.share_twitter), shareTwitter()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_line), activity.getString(R.string.share_line), shareLine()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_wa), activity.getString(R.string.share_wa), shareWhatsapp()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_sms), activity.getString(R.string.share_sms), shareSMS()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_copy), activity.getString(R.string.share_copy), shareCopyLink()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_other), activity.getString(R.string.share_others),
                shareOthers()));
        adapter.setList(list);
    }

    private String getTrackingLabel(String method) {
        if (fragmentV4 != null && fragmentV4 instanceof FeedPlusFragment) {
            //shareModel.getId() has the exacly same value with shareModel.getPageRowNumber()
            //It is not the id it is PageRowNumber
            return shareModel.getId()
                    + " "
                    + FeedTrackingEventLabel.Click.SHARE
                    + " "
                    + FeedTrackingEventLabel.PAGE_FEED
                    + " / "
                    + method;
        } else if (fragmentV4 != null && fragmentV4 instanceof FeedPlusDetailFragment) {
            return shareModel.getId()
                    + " "
                    + FeedTrackingEventLabel.Click.SHARE
                    + " "
                    + FeedTrackingEventLabel.PAGE_PRODUCT_LIST
                    + " / "
                    + method;
        } else return "";
    }

    private View.OnClickListener shareOthers() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String shareText = shareModel.getContentMessage() + CHECK_NOW + shareModel.getUrl();
                BranchSdkUtils.generateBranchLink(shareModel, activity, new BranchSdkUtils.GenerateShareContents() {
                    @Override
                    public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, shareContents);
                        sendIntent.setType("text/plain");
                        activity.startActivity(sendIntent);

                        UnifyTracking.eventFeedClick(getTrackingLabel(FeedTrackingEventLabel.Share.OTHERS));

                    }
                });

            }
        };
    }

    private View.OnClickListener shareSMS() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BranchSdkUtils.generateBranchLink(shareModel, activity, new BranchSdkUtils.GenerateShareContents() {
                    @Override
                    public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                        Intent smsIntent = MethodChecker.getSmsIntent(activity, shareUri);
                        activity.startActivity(smsIntent);

                        UnifyTracking.eventFeedClick(getTrackingLabel(FeedTrackingEventLabel.Share.SMS));
                    }
                });
                // String shareText = shareModel.getContentMessage() + " cek sekarang di :\n" + shareModel.getUrl();


            }
        };
    }


    private View.OnClickListener shareTwitter() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("com.twitter.android");
                UnifyTracking.eventFeedClick(getTrackingLabel(FeedTrackingEventLabel.Share
                        .TWITTER));

            }
        };
    }

    private View.OnClickListener shareWhatsapp() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("com.whatsapp");
                UnifyTracking.eventFeedClick(getTrackingLabel(FeedTrackingEventLabel.Share
                        .WHATSAPP));
            }
        };
    }

    private void shareToApp(final String appName) {
        BranchSdkUtils.generateBranchLink(shareModel, activity, new BranchSdkUtils.GenerateShareContents() {
            @Override
            public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage(appName);

                //  String shareText = shareModel.getContentMessage() + CHECK_NOW + shareModel.getUrl();

                if (shareUri != null
                        && !shareUri.equals(""))
                    intent.putExtra(Intent.EXTRA_TEXT, shareUri);

                try {
                    activity.startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(activity,
                            activity.getString(R.string.error_apps_not_installed),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private View.OnClickListener shareLine() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("jp.naver.line.android");
                UnifyTracking.eventFeedClick(getTrackingLabel(FeedTrackingEventLabel.Share
                        .LINE));
            }
        };
    }

    private View.OnClickListener shareGoogle() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BranchSdkUtils.generateBranchLink(shareModel, activity, new BranchSdkUtils.GenerateShareContents() {
                    @Override
                    public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                        PlusShare.Builder builder = new PlusShare.Builder(activity);

                        builder.setType("text/plain");

                        // String shareText = shareModel.getContentMessage() + CHECK_NOW + shareModel.getUrl();

                        if (shareContents != null
                                && shareContents.equals(""))
                            builder.setText(shareContents);

                        if (shareUri != null && !shareUri.equals(""))
                            builder.setContentUrl(Uri.parse(shareUri));

                        if (fragment != null)
                            fragment.startActivityForResult(builder.getIntent(), SHARE_GOOGLE_REQUEST_CODE);
                        else if (fragmentV4 != null)
                            fragmentV4.startActivityForResult(builder.getIntent(), SHARE_GOOGLE_REQUEST_CODE);
                        else
                            activity.startActivityForResult(builder.getIntent(), SHARE_GOOGLE_REQUEST_CODE);

                        UnifyTracking.eventFeedClick(getTrackingLabel(FeedTrackingEventLabel.Share
                                .GOOGLEPLUS));
                    }
                });

            }
        };
    }

    private void setListener() {
    }

    protected View.OnClickListener shareCopyLink() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                BranchSdkUtils.generateBranchLink(shareModel, activity, new BranchSdkUtils.GenerateShareContents() {
                    @Override
                    public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                        ClipboardHandler.CopyToClipboard((Activity) activity, shareUri);
                        Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                        UnifyTracking.eventFeedClick(getTrackingLabel(FeedTrackingEventLabel.Share
                                .COPY));
                    }
                });

            }
        };
    }

    protected View.OnClickListener shareFb() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();

                BranchSdkUtils.generateBranchLink(shareModel, activity, new BranchSdkUtils.GenerateShareContents() {
                    @Override
                    public void onCreateShareContents(String shareContents, final String shareUri, String branchUrl) {
                        final ShareDialog shareDialog;

                        if (fragment != null)
                            shareDialog = new ShareDialog(fragment);
                        else if (fragmentV4 != null)
                            shareDialog = new ShareDialog(fragmentV4);
                        else
                            shareDialog = new ShareDialog(activity);

                        shareDialog.registerCallback(callbackManager, new
                                FacebookCallback<Sharer.Result>() {
                                    @Override
                                    public void onSuccess(Sharer.Result result) {
                                        dismissDialog();
                                    }

                                    @Override
                                    public void onCancel() {
                                        Log.i("facebook", "onCancel");
                                    }

                                    @Override
                                    public void onError(FacebookException error) {
                                        Log.i("facebook", "onError: " + error);
                                        SnackbarManager.make(activity, error.toString(), SNACKBAR_DURATION).show();
                                        dismissDialog();
                                    }
                                });

                        if (ShareDialog.canShow(ShareLinkContent.class)) {

                            if (shareModel != null && !TextUtils.isEmpty(branchUrl)) {
                                ShareLinkContent.Builder linkBuilder = new ShareLinkContent.Builder()
                                        .setContentUrl(Uri.parse(branchUrl));

                                if (!TextUtils.isEmpty(shareModel.getName())) {
                                    linkBuilder.setContentTitle(shareModel.getName());
                                }
                                if (!TextUtils.isEmpty(shareModel.getTextContent(activity))) {
                                    linkBuilder.setContentDescription(shareModel.getTextContent(activity));
                                }
                                if (!TextUtils.isEmpty(shareModel.getDescription())) {
                                    linkBuilder.setQuote(shareModel.getDescription());
                                }
                                if (!TextUtils.isEmpty(shareModel.getImgUri())) {
                                    linkBuilder.setImageUrl(Uri.parse(shareModel.getImgUri()));
                                }
                                ShareLinkContent linkContent = linkBuilder.build();
                                shareDialog.show(linkContent);
                            }
                        }

                        UnifyTracking.eventFeedClick(getTrackingLabel(FeedTrackingEventLabel.Share
                                .FACEBOOK));
                    }
                });

            }
        };

    }

    public void show() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
