package com.tokopedia.tkpdstream.chatroom.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
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
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.StreamModuleRouter;
import com.tokopedia.tkpdstream.common.analytics.StreamAnalytics;

import java.util.ArrayList;

/**
 * @author by StevenFredian on 2/26/18.
 */

public class ShareLayout {

    private static final int SNACKBAR_DURATION = 3500;
    private static final int SHARE_GOOGLE_REQUEST_CODE = 123;
    private static final String CHECK_NOW = " cek sekarang di :\n";
    private final Activity activity;
    private final BottomSheetDialog dialog;
    private final RecyclerView appGrid;
    private final CallbackManager callbackManager;
    private final Fragment fragment;
    private final android.support.v4.app.Fragment fragmentV4;
    private final StreamAnalytics analytics;

    private ShareData shareModel;
    private ShareFeedAdapter adapter;
    private ArrayList<ShareItem> list;

    private String urlLink, channelUrl, channelName;

    public ShareLayout(Activity activity, CallbackManager callbackManager, String channelUrl, String
            channelName, StreamAnalytics analytics) {
        this.fragment = null;
        this.fragmentV4 = null;
        this.activity = activity;
        this.dialog = new BottomSheetDialog(activity);
        this.callbackManager = callbackManager;
        this.dialog.setContentView(R.layout.share_groupchat_dialog);
        appGrid = this.dialog.findViewById(R.id.grid);
        this.channelUrl = channelUrl;
        this.channelName = channelName;
        this.analytics = analytics;
        initVar(activity);
        initAdapter();
        setShareList();
    }

    private void initVar(Context context) {
        String link = "https://tokopedia.com/groupchat/{channel_url}";
        urlLink = link.replace("{channel_url}", channelUrl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        appGrid.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        this.dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
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
                R.drawable.ic_btn_g), activity.getString(R.string.share_gplus), shareGoogle(activity.getString(R.string.share_gplus))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_fb), activity.getString(R.string.share_fb), shareFb(activity.getString(R.string.share_fb))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_twitter), activity.getString(R.string.share_twitter), shareTwitter(activity.getString(R.string.share_twitter))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_line), activity.getString(R.string.share_line), shareLine(activity.getString(R.string.share_line))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_wa), activity.getString(R.string.share_wa), shareWhatsapp(activity.getString(R.string.share_wa))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_sms), activity.getString(R.string.share_sms), shareSMS(activity.getString(R.string.share_sms))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_copy), activity.getString(R.string.share_copy), shareCopyLink(activity.getString(R.string.share_copy))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_other), activity.getString(R.string.share_others), shareOthers(activity.getString(R.string.share_others))));
        adapter.setList(list);
    }

    private View.OnClickListener shareOthers(final String channelType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = generateShareText(urlLink);
                if (activity.getApplication() instanceof StreamModuleRouter) {
                    ((StreamModuleRouter) activity.getApplication()).generateBranchLink
                            (shareModel.getId(), shareModel.getName
                                    (), description, shareModel.getImgUri(), urlLink, activity, new
                                    StreamModuleRouter.ShareListener() {
                                        @Override
                                        public void onGenerateLink(String shareContents, String shareUri) {

                                            Intent sendIntent = new Intent();
                                            sendIntent.setAction(Intent.ACTION_SEND);
                                            sendIntent.putExtra(Intent.EXTRA_TEXT, generateShareText(shareUri));
                                            sendIntent.setType("text/plain");
                                            activity.startActivity(sendIntent);
                                            analytics.eventClickShareChannel(channelType, channelName);

                                        }
                                    });
                }


            }
        };
    }

    private View.OnClickListener shareSMS(final String channelType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = generateShareText(urlLink);
                if (activity.getApplication() instanceof StreamModuleRouter) {
                    ((StreamModuleRouter) activity.getApplication()).generateBranchLink(shareModel.getId(), shareModel.getName
                            (), description, shareModel.getImgUri(), urlLink, activity, new StreamModuleRouter.ShareListener() {
                        @Override
                        public void onGenerateLink(String shareContents, String shareUri) {

                            Intent smsIntent = MethodChecker.getSmsIntent(activity,
                                    generateShareText(shareUri));
                            activity.startActivity(smsIntent);
                            analytics.eventClickShareChannel(channelType, channelName);

                        }
                    });
                }


            }
        };
    }

    private View.OnClickListener shareTwitter(final String channelType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("com.twitter.android", channelType);
            }
        };
    }

    private View.OnClickListener shareWhatsapp(final String channelType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("com.whatsapp", channelType);
            }
        };
    }

    private void shareToApp(final String appName, final String channelType) {

        String description = generateShareText(urlLink);
        if (activity.getApplication() instanceof StreamModuleRouter) {
            ((StreamModuleRouter) activity.getApplication()).generateBranchLink(shareModel.getId(), shareModel.getName
                    (), description, shareModel.getImgUri(), urlLink, activity, new StreamModuleRouter.ShareListener() {
                @Override
                public void onGenerateLink(String shareContents, String shareUri) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setPackage(appName);

                    if (!TextUtils.isEmpty(shareUri))
                        intent.putExtra(Intent.EXTRA_TEXT, generateShareText(shareUri));

                    try {
                        activity.startActivity(intent);
                        analytics.eventClickShareChannel(channelType, channelName);
                    } catch (android.content.ActivityNotFoundException ex) {

                        Toast.makeText(activity,
                                activity.getString(R.string.error_apps_not_installed),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    private String generateShareText(String link) {
        return String.format("%s %s", shareModel.getDescription(), link);

    }

    private View.OnClickListener shareLine(final String channelType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("jp.naver.line.android", channelType);
            }
        };
    }

    private View.OnClickListener shareGoogle(final String channelType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = generateShareText(urlLink);
                if (activity.getApplication() instanceof StreamModuleRouter) {
                    ((StreamModuleRouter) activity.getApplication()).generateBranchLink(shareModel.getId(), shareModel.getName
                            (), description, shareModel.getImgUri(), urlLink, activity, new StreamModuleRouter.ShareListener() {
                        @Override
                        public void onGenerateLink(String shareContents, String shareUri) {

                            analytics.eventClickShareChannel(channelType, channelName);
                            PlusShare.Builder builder = new PlusShare.Builder(activity);

                            builder.setType("text/plain");

                            if (!TextUtils.isEmpty(shareUri))
                                builder.setText(generateShareText(shareUri));

                            if (!TextUtils.isEmpty(shareUri))
                                builder.setContentUrl(Uri.parse(shareUri));

                            if (fragment != null)
                                fragment.startActivityForResult(builder.getIntent(), SHARE_GOOGLE_REQUEST_CODE);
                            else if (fragmentV4 != null)
                                fragmentV4.startActivityForResult(builder.getIntent(), SHARE_GOOGLE_REQUEST_CODE);
                            else
                                activity.startActivityForResult(builder.getIntent(), SHARE_GOOGLE_REQUEST_CODE);

                        }
                    });
                }
            }
        };
    }

    protected View.OnClickListener shareCopyLink(final String channelType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = generateShareText(urlLink);
                if (activity.getApplication() instanceof StreamModuleRouter) {
                    ((StreamModuleRouter) activity.getApplication()).generateBranchLink(shareModel.getId(), shareModel.getName
                            (), description, shareModel.getImgUri(), urlLink, activity, new StreamModuleRouter.ShareListener() {
                        @Override
                        public void onGenerateLink(String shareContents, String shareUri) {

                            dialog.dismiss();
                            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Activity.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Tokopedia", generateShareText
                                    (shareUri));
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                            analytics.eventClickShareChannel(channelType, channelName);

                        }
                    });
                }
            }
        };
    }

    protected View.OnClickListener shareFb(final String channelType) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = generateShareText(urlLink);
                if (activity.getApplication() instanceof StreamModuleRouter) {
                    ((StreamModuleRouter) activity.getApplication()).generateBranchLink(shareModel.getId(), shareModel.getName
                            (), description, shareModel.getImgUri(), urlLink, activity, new StreamModuleRouter.ShareListener() {
                        @Override
                        public void onGenerateLink(String shareContents, String shareUri) {
                            processShareFb(channelType, shareContents, shareUri);
                        }
                    });
                }
            }

        };

    }

    private void processShareFb(String channelType, String shareContents, String shareUri) {

        dismissDialog();

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

            if (shareModel != null && !TextUtils.isEmpty(shareUri)) {
                ShareLinkContent.Builder linkBuilder = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(shareUri));

                linkBuilder.setQuote(generateShareText(shareUri));

                ShareLinkContent linkContent = linkBuilder.build();
                shareDialog.show(linkContent);
                analytics.eventClickShareChannel(channelType, channelName);
            }
        }
    }

    public void show() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
