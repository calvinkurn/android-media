package com.tokopedia.inbox.inboxtalk.adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.talk.model.model.InboxTalk;
import com.tokopedia.core.talk.model.model.TalkUserReputation;
import com.tokopedia.core.talk.receiver.intentservice.InboxTalkIntentService;
import com.tokopedia.core.talkview.activity.TalkViewActivity;
import com.tokopedia.core.talkview.method.DeleteTalkDialog;
import com.tokopedia.core.talkview.method.FollowTalkDialog;
import com.tokopedia.core.talkview.method.ReportTalkDialog;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.TokenHandler;
import com.tokopedia.core.util.ToolTipUtils;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.inbox.inboxtalk.fragment.InboxTalkFragment;
import com.tokopedia.inbox.inboxtalk.presenter.InboxTalkPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by stevenfredian on 4/11/16.
 */
public class InboxTalkAdapter extends BaseRecyclerViewAdapter {

    public static final int MAIN_TYPE = 123456789;
    public LayoutInflater inflater;
    boolean isShop, isInbox;
    TokenHandler token;
    private TkpdProgressDialog progress;
    InboxTalkPresenter presenter;
    InboxTalkFragment fragment;
    boolean enableAction;


    public InboxTalkAdapter(Context context, InboxTalkFragment fragment, List<RecyclerViewItem> data, boolean isShop
            , boolean isInbox, InboxTalkPresenter presenter) {
        super(context, data);
        this.fragment = fragment;
        this.isShop = isShop;
        this.isInbox = isInbox;
        this.presenter = presenter;
        this.enableAction = true;
        token = new TokenHandler();
        progress = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
    }

    public static InboxTalkAdapter createAdapter(Context context, InboxTalkFragment fragment, List<RecyclerViewItem> data,
                                                 boolean isShop, boolean isInbox,
                                                 InboxTalkPresenter presenter) {
        return new InboxTalkAdapter(context, fragment, data, isShop, isInbox, presenter);
    }

    public void setEnableAction(boolean status) {
        enableAction = status;
        notifyDataSetChanged();
    }


    public static class TalkProductViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R2.id.user_ava) ImageView pImageView;

        @BindView(R2.id.user_ava)
        ImageView UserImageView;
        @BindView(R2.id.rep_icon)
        ImageView RepIcon;
        @BindView(R2.id.but_overflow)
        View ButtonOverflow;
        @BindView(R2.id.product_name)
        TextView pProdName;
        @BindView(R2.id.user_name)
        TextView UserView;
        @BindView(R2.id.create_time)
        TextView TimeView;
        @BindView(R2.id.message)
        TextView MessageView;
        @BindView(R2.id.total_comment)
        TextView TotalCommentView;
        @BindView(R2.id.rep_rating)
        TextView RepRate;
        @BindView(R2.id.main_view)
        View CommentBut;
        //        @BindView(R2.id.)View ProdView;
        @BindView(R2.id.reputation_view)
        View viewReputation;
        @BindViews({R2.id.user_name, R2.id.product_name})
        List<TextView> textViews;

        TalkProductViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.size() == 0) {
            return super.getItemViewType(position);
        } else if (isLastItemPosition(position)) {
            return super.getItemViewType(position);
        } else {
            return MAIN_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case MAIN_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_talk, parent, false);
                return new TalkProductViewHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case MAIN_TYPE:
                bindTalkProductView(viewHolder, position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

//    @BindView({R.id.user_name, R.id.product_name})
//    List<TextView> textViews;
//    ButterKnife.Action<TextView> CHANGE_COLOR = new ButterKnife.Action<TextView>() {
//        @Override
//        public void apply(TextView view, int index) {
//            for (TextView tv : textViews) {
//                tv.setTypeface(Typeface.DEFAULT_BOLD);
//            }
//        }
//    };

    ButterKnife.Setter<View, Boolean> CHANGE_COLOR = new ButterKnife.Setter<View, Boolean>() {
        @Override
        public void set(View view, Boolean value, int index) {
            TextView tv = (TextView) view;
            if (value) {
//                tv.setTypeface(Typeface.DEFAULT_BOLD);
                tv.setTextColor(Color.BLACK);
//                tv.setBackgroundResource(R.drawable.inbox_unread_message);
            } else {
//                tv.setTypeface(Typeface.DEFAULT);
                tv.setTextColor(Color.GRAY);
//                tv.setBackgroundResource(R.drawable.inbox_read_message);
            }
        }
    };


    private RecyclerView.ViewHolder bindTalkProductView(final RecyclerView.ViewHolder viewHolder, final int position) {
        final Context context = viewHolder.itemView.getContext();
        final TalkProductViewHolder holder = (TalkProductViewHolder) viewHolder;
        final InboxTalk talk = (InboxTalk) data.get(position);
        LabelUtils label = LabelUtils.getInstance(context, holder.UserView);

        if (isShop) {
            ImageHandler.loadImageCircle2(context, holder.UserImageView, String.valueOf(talk.getTalkProductImage()));
            holder.UserImageView.setOnClickListener(goToProduct(talk.getTalkProductId()));
        } else {
            ImageHandler.loadImageCircle2(context, holder.UserImageView, talk.getTalkUserImage());
//			ImageHandler.LoadImageCircle(holder.UserImageView, UserImgUri.get(position));
            holder.UserImageView.setOnClickListener(goToPeople(talk.getTalkUserId()));
        }
        holder.UserView.setText(MethodChecker.fromHtml(talk.getTalkUserName()));
        holder.pProdName.setText(MethodChecker.fromHtml(String.valueOf(talk.getTalkProductName())));

        holder.RepRate.setText(String.format("%s%%", talk.getTalkUserReputation().getPositivePercentage()));

        if (talk.getTalkUserReputation().getNoReputation() == 0) {
            holder.RepIcon.setImageResource(R.drawable.ic_icon_repsis_smile_active);
            holder.RepRate.setVisibility(View.VISIBLE);
        } else {
            holder.RepIcon.setImageResource(R.drawable.ic_icon_repsis_smile);
            holder.RepRate.setVisibility(View.GONE);
        }

        holder.viewReputation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(setViewToolTip(talk.getTalkUserReputation()), v);
            }
        });
        holder.TimeView.setText(talk.getTalkCreateTime());
        if (isInbox) {
            boolean status;
            if (talk.getTalkReadStatus() == 1) {
                status = true;
//                holder.UserView.setTypeface(Typeface.DEFAULT_BOLD);
//                holder.CommentBut.setBackgroundResource(R.drawable.selectable_unread_background);
                holder.CommentBut.setBackgroundResource(R.drawable.inbox_unread_message);
            } else {
                status = false;
//                holder.UserView.setTypeface(Typeface.DEFAULT);
//                holder.CommentBut.setBackgroundResource(R.drawable.selectable_white_background);
                holder.CommentBut.setBackgroundResource(R.drawable.inbox_read_message);
            }
            ButterKnife.apply(holder.textViews, CHANGE_COLOR, status);
        }
        label.giveSquareLabel(talk.getTalkUserLabel());
        holder.MessageView.setText(MethodChecker.fromHtml(talk.getTalkMessage()));


        holder.TotalCommentView.setText(talk.getTalkTotalComment());

        if (SessionHandler.isV4Login(context)) {

            holder.ButtonOverflow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    showPopup(v, talk, position);
                }

            });
        } else {
            holder.ButtonOverflow.setVisibility(View.INVISIBLE);
        }

        if (enableAction)
            holder.ButtonOverflow.setVisibility(View.VISIBLE);
        else
            holder.ButtonOverflow.setVisibility(View.GONE);


        holder.UserView.setOnClickListener(goToPeople(talk.getTalkUserId()));

        holder.pProdName.setOnClickListener(goToProduct(talk.getTalkProductId()));

        holder.CommentBut.setOnClickListener(goToDetail(talk, position));


        return holder;
    }

    private View.OnClickListener goToDetail(final InboxTalk talk, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventDiscussionDetail();
                Bundle bundle = new Bundle();
                Intent intent = new Intent(context, TalkViewActivity.class);
                bundle.putString("from", TalkViewActivity.INBOX_TALK);
                bundle.putParcelable("talk", talk);
                bundle.putInt("position", position);
                intent.putExtras(bundle);

                fragment.startActivityForResult(intent, InboxTalkFragment.GO_TO_DETAIL);
            }
        };
    }

    private View.OnClickListener goToPeople(final String talkUserId) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                context.startActivity(
                        ((TkpdCoreRouter) context.getApplicationContext())
                                .getTopProfileIntent(context, talkUserId));
            }
        };
    }

    private View.OnClickListener goToProduct(final String productID) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductDetailRouter
                        .createInstanceProductDetailInfoActivity(context, productID);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };
    }

    private View setViewToolTip(final TalkUserReputation reputation) {
        return ToolTipUtils.setToolTip(context, R.layout.view_tooltip_user, new ToolTipUtils.ToolTipListener() {
            @Override
            public void setView(View view) {
                TextView smile = (TextView) view.findViewById(R.id.text_smile);
                TextView netral = (TextView) view.findViewById(R.id.text_netral);
                TextView bad = (TextView) view.findViewById(R.id.text_bad);
                smile.setText(String.valueOf(reputation.getPositive()));
                netral.setText(String.valueOf(reputation.getNeutral()));
                bad.setText(String.valueOf(reputation.getNegative()));
            }

            @Override
            public void setListener() {

            }
        });
    }

    public void showPopup(View v, final InboxTalk talk, final int position) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        if (getMenuID(talk) != 0) {
            inflater.inflate(getMenuID(talk), popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    DialogFragment dialog;
                    if (item.getItemId() == R.id.action_follow || item.getItemId() == R.id.action_unfollow) {
                        dialog = FollowTalkDialog.createInstance(followListener(talk, position)
                                , talk.getTalkFollowStatus());
                        dialog.show(fragment.getFragmentManager(), FollowTalkDialog.FRAGMENT_TAG);
                        return true;
                    } else if (item.getItemId() == R.id.action_delete_talk || item.getItemId() == R.id.action_delete) {
                        dialog = DeleteTalkDialog.createInstance(deleteListener(talk, position));
                        dialog.show(fragment.getFragmentManager(), DeleteTalkDialog.FRAGMENT_TAG);
                        return true;
                    } else if (item.getItemId() == R.id.action_report) {
                        dialog = ReportTalkDialog.createInstance(reportListener(talk, position));
                        dialog.show(fragment.getFragmentManager(), ReportTalkDialog.FRAGMENT_TAG);
                        return true;
                    } else
                        return false;
                }
            });
        }
        popup.show();
    }


    private ReportTalkDialog.ReportTalkListener reportListener(final InboxTalk talk, final int position) {
        return new ReportTalkDialog.ReportTalkListener() {
            @Override
            public void reportTalk(String s) {
                progress.showDialog();
                presenter.reportTalk(talk, position);
            }
        };
    }

    private DeleteTalkDialog.DeleteTalkListener deleteListener(final InboxTalk talk, final int position) {
        return new DeleteTalkDialog.DeleteTalkListener() {
            @Override
            public void deleteTalk() {
                progress.showDialog();
                presenter.deleteTalk(talk, position);
            }
        };
    }

    private FollowTalkDialog.FollowTalkListener followListener(final InboxTalk talk, final int position) {
        return new FollowTalkDialog.FollowTalkListener() {
            @Override
            public void followTalk() {
                progress.showDialog();
                presenter.followTalk(talk, position);
            }
        };
    }

    private int getMenuID(InboxTalk talk) {
        int menuID;
        if (talk.getTalkShopId().equals(SessionHandler.getShopID(context))) {
            menuID = R.menu.report_menu;
        } else {
            if (token.getLoginID(context).equals(talk.getTalkUserId())) {
                if (talk.getTalkFollowStatus() == 1) {
                    menuID = R.menu.unfollow_delete_menu;
                } else {
                    menuID = R.menu.follow_delete_menu;
                }
            } else {
                if (talk.getTalkFollowStatus() == 1) {
                    menuID = R.menu.unfollow_report_menu;
                } else {
                    menuID = R.menu.follow_report_menu;
                }
            }
        }
        return menuID;
    }


    public void onSuccessAction(Bundle resultData, int resultCode, int position) {
        progress.dismiss();
        if (resultData.getString(InboxTalkIntentService.EXTRA_RESULT).equals("1")) {
            InboxTalk temp = (InboxTalk) data.get(position);
            switch (resultCode) {
                case InboxTalkIntentService.STATUS_SUCCESS_FOLLOW:
                    if (temp.getTalkFollowStatus() == 1) {
                        temp.setTalkFollowStatus(0);
                        SnackbarManager.make((Activity) context,
                                context.getApplicationContext().getString(R.string.message_success_unfollow),
                                Snackbar.LENGTH_LONG).show();
                    } else {
                        temp.setTalkFollowStatus(1);
                        SnackbarManager.make((Activity) context,
                                context.getApplicationContext().getString(R.string.message_success_follow),
                                Snackbar.LENGTH_LONG).show();
                    }
                    notifyDataSetChanged();
                    break;
                case InboxTalkIntentService.STATUS_SUCCESS_DELETE:
                    data.remove(position);
                    SnackbarManager.make((Activity) context,
                            context.getApplicationContext().getString(R.string.message_success_delete_talk),
                            Snackbar.LENGTH_LONG).show();
                    notifyDataSetChanged();
                    break;
                case InboxTalkIntentService.STATUS_SUCCESS_REPORT:
                    SnackbarManager.make((Activity) context,
                            context.getString(R.string.toast_success_report),
                            Snackbar.LENGTH_LONG).show();
                    break;
                default:
                    resultData.putString(InboxTalkIntentService.EXTRA_RESULT,
                            context.getResources().getString(R.string.title_retry));
                    onErrorAction(resultData, resultCode + 10);
                    break;
            }
        } else {
            resultData.putString(InboxTalkIntentService.EXTRA_RESULT,
                    context.getResources().getString(R.string.title_retry));
            onErrorAction(resultData, resultCode + 10);
        }
    }

    public void onErrorAction(Bundle resultData, int resultCode) {
        progress.dismiss();
        switch (resultCode) {
            case InboxTalkIntentService.STATUS_ERROR_FOLLOW:
            case InboxTalkIntentService.STATUS_ERROR_REPORT:
            case InboxTalkIntentService.STATUS_ERROR_DELETE:
                SnackbarManager.make((Activity) context,
                        resultData.getString(InboxTalkIntentService.EXTRA_RESULT),
                        Snackbar.LENGTH_LONG).show();
                break;
            default:
                SnackbarManager.make((Activity) context,
                        context.getResources().getString(R.string.title_try_again),
                        Snackbar.LENGTH_LONG).show();
                break;
        }
    }

}
