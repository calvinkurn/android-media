package com.tokopedia.seller.shopsettings.notes.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopsettings.notes.activity.ManageShopNotesActivity;
import com.tokopedia.seller.shopsettings.notes.listener.ManageShopNoteFormView;
import com.tokopedia.core.manage.shop.notes.model.ShopNote;
import com.tokopedia.core.manage.shop.notes.model.ShopNoteDetail;
import com.tokopedia.seller.shopsettings.notes.presenter.ManageShopNotesFormPresenter;
import com.tokopedia.seller.shopsettings.notes.presenter.ManageShopNotesFormPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;

/**
 * Created by nisie on 10/26/16.
 */
public class ManageShopNotesFormFragment extends BasePresenterFragment<ManageShopNotesFormPresenter>
        implements ManageShopNoteFormView {

    TextInputLayout layoutNoteName;
    TextInputLayout layoutNoteContent;
    EditText noteName;
    EditText noteContent;
    Button saveButton;

    TkpdProgressDialog progressDialog;

    public interface FinishActionListener {
        void onFinishAction();
    }

    public static ManageShopNotesFormFragment createInstance(Bundle bundle) {
        ManageShopNotesFormFragment fragment = new ManageShopNotesFormFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        layoutNoteName = (TextInputLayout) view.findViewById(R.id.layout_note_name);
        layoutNoteContent = (TextInputLayout) view.findViewById(R.id.layout_note_content);
        noteName = (EditText) view.findViewById(R.id.note_name);
        noteContent = (EditText) view.findViewById(R.id.note_content);
        saveButton = (Button) view.findViewById(R.id.save_button);
        return view;
    }

    FinishActionListener listener;

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    private void setNoteName() {
        if (getArguments().getBoolean(ManageShopNotesActivity.PARAM_IS_RETURNABLE_POLICY)) {
            noteName.setEnabled(false);
            noteName.setText(getString(R.string.title_returnable_policy));
        } else
            noteName.setEnabled(true);
    }

    @Override
    public void setNoteFromBundle(ShopNote shopNote) {
        if (shopNote != null) {
            noteName.setText(shopNote.getNoteTitle());
        }
    }

    @Override
    public void showLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void finishLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void setResult(ShopNoteDetail detail) {
        noteName.setText(detail.getNotes_title());
        noteContent.setText(detail.getNotes_content());
    }

    @Override
    public void showError(String message) {
        if (message.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public EditText getNoteContent() {
        return noteContent;
    }

    @Override
    public EditText getNoteName() {
        return noteName;
    }

    @Override
    public void onSuccessEdit() {
        listener.onFinishAction();
    }

    @Override
    public TextInputLayout getNoteContentLayout() {
        return layoutNoteContent;
    }

    @Override
    public TextInputLayout getNoteNameLayout() {
        return layoutNoteName;
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ManageShopNotesFormPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shop_note_form;
    }

    @Override
    protected void initView(View view) {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        setNoteName();
        if (getArguments().getBoolean(ManageShopNotesActivity.PARAM_IS_EDIT)) {
            setNoteFromBundle((ShopNote) getArguments().getParcelable(ManageShopNotesActivity.PARAM_SHOP_NOTE));
            presenter.getShopNoteDetail();
        }
    }

    @Override
    protected void setViewListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                if (getArguments().getBoolean(ManageShopNotesActivity.PARAM_IS_EDIT))
                    presenter.onEditNote();
                else
                    presenter.onAddNote();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void setOnFinishActionListener(FinishActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        presenter.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextWatcher();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setTextWatcher() {
        noteContent.addTextChangedListener(watcher(layoutNoteContent));
        noteName.addTextChangedListener(watcher(layoutNoteName));
    }

    private TextWatcher watcher(final TextInputLayout wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    notifyError(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public void notifyError(TextInputLayout wrapper, String errorMessage) {
        wrapper.setError(errorMessage);
        if (errorMessage == null) wrapper.setErrorEnabled(false);
        wrapper.requestFocus();
    }


}
