package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by normansyahputa on 5/26/17.
 * refer to old {@link com.tokopedia.core.app.BasePresenterFragment}
 */

public class BasePresenterFragment<P> extends BaseDaggerFragment {
    private static final String TAG = com.tokopedia.core.app.BasePresenterFragment.class.getSimpleName();

    protected P presenter;

    protected Bundle savedState;
    protected Unbinder unbinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(getOptionsMenuEnable());
        initialListener(activity);
    }

    @Override
    protected String getScreenName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(isRetainInstance());
        Log.d(TAG, "ON CREATE");
        if (getArguments() != null) {
            setupArguments(getArguments());
        }
        initialPresenter();
    }

    @Override
    protected void initInjector() {

    }

    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "ON CREATE VIEW");
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectView(view);
        initView(view);
        initialVar();
        setViewListener();
        setActionVar();
    }

    protected void onFirstTimeLaunched() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "ON SAVE INSTANCE STATE");
        saveStateToArguments();
    }

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b == null) b = new Bundle();
            b.putBundle("internalSavedViewState8954201239547", savedState);
        }
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    public void onSaveState(Bundle state) {
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b == null) b = new Bundle();
        savedState = b.getBundle("internalSavedViewState8954201239547");
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

    private void restoreState() {
        if (savedState != null) {
            // For Example
            //tv1.setText(savedState.getString("text"));
            onRestoreState(savedState);
        }
    }

    public void onRestoreState(Bundle savedState) {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();
        Log.d(TAG, "ON DESTROY VIEW");
        unbinder.unbind();
    }

    private void injectView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!restoreStateFromArguments()) {
            onFirstTimeLaunched();
            Log.d(TAG, "ON ACTIVITY CREATE FIRST");
        } else {
            Log.d(TAG, "ON ACTIVITY CREATE");
        }
    }

    /**
     * apakah fragment ini support options menu?
     *
     * @return iya atau tidak
     */
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    /**
     * instantiate presenter disini. sesuai dengan Type param di class
     */
    protected void initialPresenter() {
    }

    /**
     * Cast si activity ke listener atau bisa juga ini untuk context activity
     *
     * @param activity si activity yang punya fragment
     */
    protected void initialListener(Activity activity) {
    }

    /**
     * kalau memang argument tidak kosong. ini data argumentnya
     *
     * @param arguments argument nya
     */
    protected void setupArguments(Bundle arguments) {
    }

    /**
     * Layout xml untuk si fragment
     *
     * @return layout id
     */
    protected int getFragmentLayout() {
        return -1;
    }

    /**
     * initial view atau widget.. misalkan textView = (TextView) findById...
     *
     * @param view root view si fragment
     */
    protected void initView(View view) {
    }

    /**
     * set listener atau attribute si view. misalkan texView.setText("blablalba");
     */
    protected void setViewListener() {
    }

    /**
     * initial Variabel di fragment, selain yg sifatnya widget. Misal: variable state, handler dll
     */
    protected void initialVar() {
    }

    /**
     * setup aksi, attr, atau listener untuk si variable. misal. appHandler.startAction(){}
     */
    protected void setActionVar() {
    }


}
