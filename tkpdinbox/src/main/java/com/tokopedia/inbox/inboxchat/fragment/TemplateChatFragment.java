package com.tokopedia.inbox.inboxchat.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.activity.EditTemplateChatActivity;
import com.tokopedia.inbox.inboxchat.adapter.TemplateChatSettingAdapter;
import com.tokopedia.inbox.inboxchat.adapter.TemplateChatSettingTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.di.DaggerTemplateChatComponent;
import com.tokopedia.inbox.inboxchat.helper.SimpleItemTouchHelperCallback;
import com.tokopedia.inbox.inboxchat.presenter.TemplateChatContract;
import com.tokopedia.inbox.inboxchat.presenter.TemplateChatSettingPresenter;
import com.tokopedia.inbox.inboxchat.viewholder.ItemTemplateChatViewHolder;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_ALL;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_MESSAGE;


public class TemplateChatFragment extends BaseDaggerFragment
        implements TemplateChatContract.View{


    private SwitchCompat switchTemplate;
    private RecyclerView recyclerView;
    private View templateContainer;
    private TemplateChatSettingTypeFactoryImpl typeFactory;
    private TemplateChatSettingAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Inject
    TemplateChatSettingPresenter presenter;
    private ItemTouchHelper mItemTouchHelper;
    private Snackbar snackbarError;
    private Snackbar snackbarInfo;

    public static TemplateChatFragment createInstance(Bundle extras) {
        TemplateChatFragment fragment = new TemplateChatFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_template_chat, container, false);

        typeFactory = new TemplateChatSettingTypeFactoryImpl(this);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        switchTemplate = rootView.findViewById(R.id.switch_chat_template);
        templateContainer = rootView.findViewById(R.id.template_container);
        snackbarError = SnackbarManager.make(getActivity(), "", Snackbar.LENGTH_LONG);
        snackbarInfo = SnackbarManager.make(getActivity(), "", Snackbar.LENGTH_LONG);

        recyclerView.setHasFixedSize(true);

        presenter.attachView(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TemplateChatSettingAdapter(typeFactory, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        snackbarError.getView().setBackgroundColor(MethodChecker.getColor(getActivity(),R.color.red_template));
        TextView textView = snackbarError.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_70));

        snackbarInfo.getView().setBackgroundColor(MethodChecker.getColor(getActivity(),R.color.green_template));
        TextView textView2 = snackbarInfo.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView2.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_70));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        switchTemplate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.setArrange(b);
                if(b){
                    templateContainer.setVisibility(View.VISIBLE);
                }else {
                    templateContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
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
    public void setTemplate(List<Visitable> listTemplate) {
        adapter.setList(listTemplate);
    }

    @Override
    public void onDrag(ItemTemplateChatViewHolder ini) {
        mItemTouchHelper.startDrag(ini);
    }

    @Override
    public void onEnter(String message) {
        if(message == null && adapter.getList().size()>5){
            snackbarError.setText(getActivity().getString(R.string.limited_template_chat_warning));
            snackbarError.show();
        }else {
            Intent intent = EditTemplateChatActivity.createInstance(getActivity());
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_MESSAGE, message);
            bundle.putStringArrayList(PARAM_ALL, adapter.getListString());
            intent.putExtras(bundle);
            startActivityForResult(intent, 100);
            getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        }
    }

    @Override
    public void setChecked(boolean b) {
        switchTemplate.setChecked(b);
    }

    @Override
    public void reArrange(int from) {
        presenter.setArrange(from);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    String string =data.getStringExtra("string");
                    int index = data.getIntExtra("index",-1);
                    switchTemplate.setChecked(data.getBooleanExtra("enabled", true));
                    adapter.edit(index,string);
                    snackbarInfo.setText(data.getStringExtra("note"));
                    snackbarInfo.show();
                    break;
                }
            default:
                break;
        }
    }
}
