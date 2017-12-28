package com.tokopedia.inbox.inboxchat.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
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

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_ALL;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_MESSAGE;

/**
 * Created by stevenfredian on 12/22/17.
 */

public class EditTemplateChatFragment extends BaseDaggerFragment
                    implements EditTemplateChatContract.View {
    private TextView counter;
    private TextView error;
    private TextView submit;
    private EditText editText;

    private List list;
    private String message;
    private Observable<Integer> counterObservable;

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
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.delete_template, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_organize) {
            showDialogDelete();
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
                        presenter.deleteTemplate();
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
        editText.setSelection(message.length());

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
                        canProceed(integer!=0, submit);
                        showError(integer);
                        counter.setText(integer + "/500");
                    }
                });
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.submitText(editText.getText().toString(), message, list);
            }
        });
    }

    private void showError(Integer integer) {
        if(integer>0 && integer<5){
            error.setVisibility(View.VISIBLE);
        }else {
            error.setVisibility(View.GONE);
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
    public void onResult(EditTemplateViewModel editTemplateViewModel, int index, String s, boolean isSuccess) {
        Intent intent = getActivity().getIntent();
        intent.putExtra("index", index);
        intent.putExtra("string", s);
        intent.putExtra("enabled", editTemplateViewModel.isEnabled());
        if(isSuccess){
            if(isAdd()){
                intent.putExtra("note", getActivity().getString(R.string.success_add_template_chat));
            }else {
                intent.putExtra("note", getActivity().getString(R.string.success_edit_template_chat));
            }
        }else {

        }
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

    private boolean isAdd(){
        return message == null;
    }
}
