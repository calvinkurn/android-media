package com.tokopedia.inbox.inboxchat.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.analytics.TopChatTrackingEventLabel;
import com.tokopedia.inbox.inboxchat.di.DaggerTemplateChatComponent;
import com.tokopedia.inbox.inboxchat.presenter.EditTemplateChatContract;
import com.tokopedia.inbox.inboxchat.presenter.EditTemplateChatPresenter;
import com.tokopedia.inbox.inboxchat.util.Events;
import com.tokopedia.inbox.inboxchat.viewmodel.EditTemplateViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.tokopedia.inbox.inboxchat.fragment.TemplateChatFragment.CREATE;
import static com.tokopedia.inbox.inboxchat.fragment.TemplateChatFragment.DELETE;
import static com.tokopedia.inbox.inboxchat.fragment.TemplateChatFragment.INDEX_RESULT;
import static com.tokopedia.inbox.inboxchat.fragment.TemplateChatFragment.LIST_RESULT;
import static com.tokopedia.inbox.inboxchat.fragment.TemplateChatFragment.MODE_RESULT;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_ALL;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_MESSAGE;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_MODE;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_NAV;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_POSITION;

/**
 * Created by stevenfredian on 12/22/17.
 */

public class EditTemplateChatFragment extends BaseDaggerFragment
        implements EditTemplateChatContract.View {

    private static final int MAX_CHAR = 200;
    private static final int DISABLE_DELETE = 130;
    private static final int ENABLE_DELETE = 255;

    private TextView counter;
    private TextView error;
    private TextView submit;
    private EditText editText;

    private List list;
    private String message;
    private Observable<Integer> counterObservable;
    private int allowDelete;

    public static EditTemplateChatFragment createInstance(Bundle extras) {
        EditTemplateChatFragment fragment = new EditTemplateChatFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Inject
    EditTemplateChatPresenter presenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments().getInt(PARAM_MODE) == CREATE) {
            setHasOptionsMenu(false);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.delete_template, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_organize);
        if (getArguments().getInt(PARAM_NAV) == 1) {
            allowDelete = DISABLE_DELETE;
            item.getIcon().setAlpha(DISABLE_DELETE);
        } else {
            allowDelete = ENABLE_DELETE;
            item.getIcon().setAlpha(ENABLE_DELETE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_organize) {
            if (allowDelete == ENABLE_DELETE) {
                showDialogDelete();
            } else {
                showError(getActivity().getString(R.string.minimum_template_chat_warning));
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showDialogDelete() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_chat_template)
                .setMessage(R.string.forever_deleted_template)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        presenter.deleteTemplate(getArguments().getInt(PARAM_POSITION));
                        dialog.dismiss();
                    }

                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_template_chat, container, false);

        counter = rootView.findViewById(R.id.counter);
        error = rootView.findViewById(R.id.error);
        submit = rootView.findViewById(R.id.submit);
        editText = rootView.findViewById(R.id.edittext);

        presenter.attachView(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        message = getArguments().getString(PARAM_MESSAGE);
        list = getArguments().getStringArrayList(PARAM_ALL);


        editText.setText(message);
        if (message != null) {
            editText.setSelection(message.length());
        }

        counterObservable = Events.text(editText).map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return s.length();
            }
        });

        counterObservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(final Integer integer) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorAndProceed(integer, submit);
                        counter.setText(String.format("%d/%d", integer, MAX_CHAR));
                    }
                });
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventClickTemplate(TopChatTrackingEventLabel.Category.UPDATE_TEMPLATE,
                        TopChatTrackingEventLabel.Action.UPDATE_TEMPLATE,
                        TopChatTrackingEventLabel.Name.INBOX_CHAT);
                presenter.submitText(editText.getText().toString(), message, list);
            }
        });
    }

    private void showErrorAndProceed(Integer integer, TextView proceed) {
        if (integer == 0) {
            canProceed(false, proceed);
        } else if (integer > 0 && integer < 5) {
            error.setText(getActivity().getString(R.string.minimal_char_template));
            error.setVisibility(View.VISIBLE);
            canProceed(false, proceed);
        } else if (integer > MAX_CHAR) {
            error.setText(getActivity().getString(R.string.maximal_char_template, MAX_CHAR));
            error.setVisibility(View.VISIBLE);
            canProceed(false, proceed);
        } else {
            error.setVisibility(View.GONE);
            canProceed(true, proceed);
        }
    }

    public void canProceed(boolean can, TextView proceed) {
        proceed.setEnabled(can);
        if (can) {
            proceed.getBackground().setColorFilter(MethodChecker.getColor(getActivity(), R.color.medium_green), PorterDuff.Mode.SRC_IN);
            proceed.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        } else {
            proceed.getBackground().setColorFilter(MethodChecker.getColor(getActivity(), R.color.grey_300), PorterDuff.Mode.SRC_IN);
            proceed.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerTemplateChatComponent daggerTemplateChatComponent =
                (DaggerTemplateChatComponent) DaggerTemplateChatComponent.builder()
                        .appComponent(appComponent).build();
        daggerTemplateChatComponent.inject(this);
    }

    @Override
    public void onResult(EditTemplateViewModel editTemplateViewModel, int index, String s) {
        Intent intent = getActivity().getIntent();
        intent.putExtra(INDEX_RESULT, index);
        intent.putExtra(LIST_RESULT, s);
        intent.putExtra("enabled", editTemplateViewModel.isEnabled());
        intent.putExtra(MODE_RESULT, getArguments().getInt(PARAM_MODE));
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onResult(EditTemplateViewModel editTemplateViewModel, int index) {
        Intent intent = getActivity().getIntent();
        intent.putExtra(INDEX_RESULT, index);
        intent.putExtra(MODE_RESULT, DELETE);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void showError(String error) {
        if (error.equals("")) {
            SnackbarManager.make(getActivity(), getActivity().getString(R.string.default_request_error_bad_request), Snackbar.LENGTH_LONG).show();
        } else {
            SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_LONG).show();
        }
    }

}
