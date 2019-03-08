package com.tokopedia.discovery.search.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.design.component.EditTextCompat;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameterOwnerListener;
import com.tokopedia.discovery.search.view.fragment.SearchMainFragment;
import com.tokopedia.discovery.util.AnimationUtil;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Erry Suprayogi
 */
@SuppressWarnings("all")
public class DiscoverySearchView extends FrameLayout implements Filter.FilterListener, SearchParameterOwnerListener {
    public static final int REQUEST_VOICE = 9999;
    private static final String TAG = DiscoverySearchView.class.getSimpleName();
    private static final String LOCALE_INDONESIA = "in_ID";
    public static final int TAB_SHOP_SUGGESTION = 1;
    public static final int TAB_PRODUCT_SUGGESTION = 0;
    public static final int TAB_DEFAULT_SUGGESTION = TAB_PRODUCT_SUGGESTION;
    private static final long IMAGE_SEARCH_SHOW_CASE_DIALOG_DELAY = 600;
    private MenuItem mMenuItem;
    private boolean mIsSearchOpen = false;
    private int mAnimationDuration;
    private boolean mClearingFocus;

    //Views
    private View mSearchLayout;
    private View mTintView;
    private SearchMainFragment mSuggestionFragment;
    private RelativeLayout mSuggestionView;
    private EditTextCompat mSearchSrcTextView;
    private ImageButton mBackBtn;
    private ImageButton mVoiceBtn;
    private ImageButton mImageSearchButton;
    private ImageButton mEmptyBtn;
    private LinearLayout mSearchTopBar;
    private LinearLayout mSearchContainer;
    private CharSequence mOldQueryText;
    private CharSequence mUserQuery;

    private OnQueryTextListener mOnQueryChangeListener;
    private ImageSearchClickListener mImageSearchClickListener;
    private SearchViewListener mSearchViewListener;
    private AppCompatActivity activity;
    private boolean finishOnClose = false;
    private SavedState mSavedState;
    private boolean submit = false;
    private SearchParameter searchParameter = new SearchParameter();

    private boolean ellipsize = false;

    private boolean allowVoiceSearch;
    private boolean allowImageSearch;
    private Drawable suggestionIcon;
    private boolean copyText = false;
    private Context mContext;
    private CompositeSubscription compositeSubscription;
    private Subscription querySubscription;
    private QueryListener queryListener;
    private ShowCaseDialog showCaseDialog;
    private RemoteConfig remoteConfig;
    private boolean showShowCase = false;

    private interface QueryListener {
        void onQueryChanged(String query);
    }

    private String lastQuery;

    public DiscoverySearchView(Context context) {
        this(context, null);
    }

    public DiscoverySearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscoverySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);

        mContext = context;

        initiateView();

        initStyle(attrs, defStyleAttr);

        initCompositeSubscriber();
    }

    private void initCompositeSubscriber() {
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        compositeSubscription.add(querySubscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                queryListener = new QueryListener() {
                    @Override
                    public void onQueryChanged(String query) {
                        subscriber.onNext(query);
                    }
                };
            }
        })
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        if (s != null) {
                            Log.d(TAG, "Sending the text " + s);
                            DiscoverySearchView.this.onTextChanged(s);
                        }
                    }
                }));
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
        this.mSuggestionFragment = (SearchMainFragment) activity.getSupportFragmentManager().findFragmentById(R.id.search_suggestion);
    }

    public void setCopyText(boolean copyText) {
        this.copyText = copyText;
    }

    private void initStyle(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.DiscoverySearchView, defStyleAttr, 0);

        if (a != null) {
            if (a.hasValue(R.styleable.DiscoverySearchView_searchBackground)) {
                setBackground(a.getDrawable(R.styleable.DiscoverySearchView_searchBackground));
            }

            if (a.hasValue(R.styleable.DiscoverySearchView_android_textColor)) {
                setTextColor(a.getColor(R.styleable.DiscoverySearchView_android_textColor, 0));
            }

            if (a.hasValue(R.styleable.DiscoverySearchView_android_textColorHint)) {
                setHintTextColor(a.getColor(R.styleable.DiscoverySearchView_android_textColorHint, 0));
            }

            if (a.hasValue(R.styleable.DiscoverySearchView_android_hint)) {
                setHint(a.getString(R.styleable.DiscoverySearchView_android_hint));
            }

            if (a.hasValue(R.styleable.DiscoverySearchView_searchVoiceIcon)) {
                setVoiceIcon(a.getDrawable(R.styleable.DiscoverySearchView_searchVoiceIcon));
            }

            if (a.hasValue(R.styleable.DiscoverySearchView_searchImageIcon)) {
                setImageIcon(a.getDrawable(R.styleable.DiscoverySearchView_searchImageIcon));
            }

            if (a.hasValue(R.styleable.DiscoverySearchView_searchCloseIcon)) {
                setCloseIcon(a.getDrawable(R.styleable.DiscoverySearchView_searchCloseIcon));
            }

            if (a.hasValue(R.styleable.DiscoverySearchView_searchBackIcon)) {
                setBackIcon(a.getDrawable(R.styleable.DiscoverySearchView_searchBackIcon));
            }

            if (a.hasValue(R.styleable.DiscoverySearchView_searchSuggestionBackground)) {
                setSuggestionBackground(a.getDrawable(R.styleable.DiscoverySearchView_searchSuggestionBackground));
            }

            if (a.hasValue(R.styleable.DiscoverySearchView_searchSuggestionIcon)) {
                setSuggestionIcon(a.getDrawable(R.styleable.DiscoverySearchView_searchSuggestionIcon));
            }

            if (a.hasValue(R.styleable.DiscoverySearchView_android_inputType)) {
                setInputType(a.getInt(R.styleable.DiscoverySearchView_android_inputType, EditorInfo.TYPE_NULL));
            }

            a.recycle();
        }
    }

    public void setSuggestionBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSuggestionView.setBackground(background);
        } else {
            mSuggestionView.setBackgroundDrawable(background);
        }
    }

    private void initiateView() {
        LayoutInflater.from(mContext).inflate(R.layout.search_view, this, true);
        mSearchLayout = findViewById(R.id.search_layout);
        mSearchContainer = (LinearLayout) mSearchLayout.findViewById(R.id.search_container);
        mSearchTopBar = (LinearLayout) mSearchLayout.findViewById(R.id.search_top_bar);
        mSearchSrcTextView = (EditTextCompat) mSearchLayout.findViewById(R.id.searchTextView);
        mBackBtn = (ImageButton) mSearchLayout.findViewById(R.id.action_up_btn);
        mVoiceBtn = (ImageButton) mSearchLayout.findViewById(R.id.action_voice_btn);
        mImageSearchButton = (ImageButton) mSearchLayout.findViewById(R.id.action_image_search_btn);
        mEmptyBtn = (ImageButton) mSearchLayout.findViewById(R.id.action_empty_btn);
        mTintView = mSearchLayout.findViewById(R.id.transparent_view);
        mSuggestionView = (RelativeLayout) mSearchLayout.findViewById(R.id.search_suggestion_container);
        mSearchSrcTextView.setOnClickListener(mOnClickListener);
        mBackBtn.setOnClickListener(mOnClickListener);
        mVoiceBtn.setOnClickListener(mOnClickListener);
        mEmptyBtn.setOnClickListener(mOnClickListener);
        mTintView.setOnClickListener(mOnClickListener);
        mImageSearchButton.setOnClickListener(mOnClickListener);
        allowVoiceSearch = true;

        remoteConfig = new FirebaseRemoteConfigImpl(getContext());
        setImageSearch(remoteConfig.getBoolean(RemoteConfigKey.SHOW_IMAGE_SEARCH,
                false));

        showVoice(true);
        showImageSearch(true);

        initSearchView();
        mSuggestionView.setVisibility(GONE);
        setAnimationDuration(AnimationUtil.ANIMATION_DURATION_MEDIUM);
    }

    public void hideShowCaseDialog(boolean b) {
        showShowCase = true;
    }

    public boolean isShowShowCase() {
        return showShowCase;
    }

    private void initSearchView() {
        mSearchSrcTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });

        mSearchSrcTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    String keyword = s.toString();

                    if (copyText) {
                        keyword = keyword.trim();
                        copyText = false;
                    }
                    mUserQuery = keyword;
                    if (queryListener != null) {
                        queryListener.onQueryChanged(keyword);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchSrcTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(mSearchSrcTextView);
                    showSuggestions();
                }
            }
        });
    }

    private void startShowCase() {

        if (isAllowImageSearch() &&
                !isShowShowCase()) {


            final String showCaseTag = "Image Search ShowCase";
            if (ShowCasePreference.hasShown(mContext, showCaseTag)) {
                return;
            }
            if (showCaseDialog != null) {
                return;
            }
            showCaseDialog = createShowCase();
            showCaseDialog.setShowCaseStepListener(new ShowCaseDialog.OnShowCaseStepListener() {
                @Override
                public boolean onShowCaseGoTo(int previousStep, int nextStep, ShowCaseObject showCaseObject) {
                    return false;
                }
            });

            if (remoteConfig == null) {
                remoteConfig = new FirebaseRemoteConfigImpl(getContext());
            }

            mImageSearchButton.setWillNotCacheDrawing(false);
            ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();
            showCaseObjectList.add(new ShowCaseObject(
                    mImageSearchButton,
                    mContext.getResources().getString(R.string.on_board_title),
                    remoteConfig.getString(RemoteConfigKey.IMAGE_SEARCH_ONBOARD_DESC,
                            mContext.getResources().getString(R.string.on_board_desc)),
                    ShowCaseContentPosition.UNDEFINED,
                    R.color.tkpd_main_green));
            if(activity != null) {
                showCaseDialog.show(activity, showCaseTag, showCaseObjectList);
            }

        }
    }

    private ShowCaseDialog createShowCase() {
        return new ShowCaseBuilder()
                .customView(R.layout.view_showcase)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .textSizeRes(R.dimen.fontvs)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .prevStringRes(R.string.navigate_back)
                .nextStringRes(R.string.next)
                .finishStringRes(R.string.title_done)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .useSkipWord(false)
                .build();
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (v == mBackBtn) {
                if (finishOnClose && activity != null) {
                    KeyboardHandler.DropKeyboard(activity, mSearchSrcTextView);
                    activity.finish();
                } else {
                    closeSearch();
                }
            } else if (v == mVoiceBtn) {
                onVoiceClicked();
            } else if (v == mImageSearchButton) {
                onImageSearchClicked();
            } else if (v == mEmptyBtn) {
                mSearchSrcTextView.setText(null);
            } else if (v == mSearchSrcTextView) {
                showSuggestions();
            } else if (v == mTintView) {
                closeSearch();
            }
        }
    };

    private void onImageSearchClicked() {
        clearFocus();
        mImageSearchClickListener.onImageSearchClicked();
    }

    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak an item name or number");    // user hint
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);    // setting recognition model, optimized for short phrases – search queries
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LOCALE_INDONESIA);  //This is priority for Indonesian language
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);    // quantity of results we want to receive
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent, REQUEST_VOICE);
        }
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mSearchSrcTextView.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            mEmptyBtn.setVisibility(VISIBLE);
            showVoice(false);
            showImageSearch(false);

            searchParameter.setSearchQuery(newText.toString()); // TODO:: Fix this to always set the value regardless of newText
        } else {
            mEmptyBtn.setVisibility(GONE);
            showVoice(true);
            showImageSearch(true);
        }

        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }

        mOldQueryText = newText.toString();

        if (mSuggestionFragment != null) {
            mSuggestionFragment.search(searchParameter);
        }
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchSrcTextView.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(searchParameter)) {
                closeSearch();
                mSearchSrcTextView.setText(null);
            }
        }
    }

    private boolean isVoiceAvailable() {
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    //Public Attributes

    @Override
    public void setBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSearchTopBar.setBackground(background);
        } else {
            mSearchTopBar.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        mSearchTopBar.setBackgroundColor(color);
    }

    public void setTextColor(int color) {
        mSearchSrcTextView.setTextColor(color);
    }

    public void setHintTextColor(int color) {
        mSearchSrcTextView.setHintTextColor(color);
    }

    public void setHint(CharSequence hint) {
        mSearchSrcTextView.setHint(hint);
    }

    public void setVoiceIcon(Drawable drawable) {
        mVoiceBtn.setImageDrawable(drawable);
    }

    public void setImageIcon(Drawable drawable) {
        mImageSearchButton.setImageDrawable(drawable);
    }

    public void setCloseIcon(Drawable drawable) {
        mEmptyBtn.setImageDrawable(drawable);
    }

    public void setBackIcon(Drawable drawable) {
        mBackBtn.setImageDrawable(drawable);
    }

    public void setSuggestionIcon(Drawable drawable) {
        suggestionIcon = drawable;
    }

    public void setInputType(int inputType) {
        mSearchSrcTextView.setInputType(inputType);
    }

    public void setCursorDrawable(int drawable) {
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mSearchSrcTextView, drawable);
        } catch (Exception ignored) {
            Log.e("DiscoverySearchView", ignored.toString());
        }
    }

    public void setVoiceSearch(boolean voiceSearch) {
        allowVoiceSearch = voiceSearch;
    }

    public void setImageSearch(boolean imageSearch) {
        allowImageSearch = imageSearch;
    }

    public boolean isAllowImageSearch() {
        return allowImageSearch;
    }

    //Public Methods

    /**
     * Call this method to show suggestions list. This shows up when adapter is set.
     */
    public void showSuggestions() {
        if (mSuggestionFragment != null && mSuggestionView.getVisibility() == GONE) {
            mSuggestionView.setVisibility(VISIBLE);
        }
    }

    /**
     * Submit the query as soon as the user clicks the item.
     *
     * @param submit submit state
     */
    public void setSubmitOnClick(boolean submit) {
        this.submit = submit;
    }

    /**
     * Dismiss the suggestions list.
     */
    public void dismissSuggestions() {
        if (mSuggestionView.getVisibility() == VISIBLE) {
            mSuggestionView.setVisibility(GONE);
        }
    }


    /**
     * Calling this will set the query to search text box. if submit is true, it'll submit the query.
     *
     * @param query
     * @param submit
     */

    public void setQuery(CharSequence query, boolean submit, boolean copyText) {
        this.copyText = copyText;
        setQuery(query, submit);
    }

    public void setQuery(CharSequence query, boolean submit) {
        mSearchSrcTextView.setText(query);
        if (query != null) {
            mSearchSrcTextView.setSelection(mSearchSrcTextView.length());
            mUserQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    /**
     * if show is true, this will enable voice search. If voice is not available on the device, this method call has not effect.
     *
     * @param show
     */
    public void showVoice(boolean show) {
        if (show && isVoiceAvailable() && allowVoiceSearch) {
            mVoiceBtn.setVisibility(VISIBLE);
        } else {
            mVoiceBtn.setVisibility(GONE);
        }
    }

    public void showImageSearch(boolean show) {
        if (show && allowImageSearch) {
            mImageSearchButton.setVisibility(VISIBLE);
        } else {
            mImageSearchButton.setVisibility(GONE);
        }
    }

    /**
     * Call this method and pass the menu item so this class can handle click events for the Menu Item.
     *
     * @param menuItem
     */
    public void setMenuItem(MenuItem menuItem) {
        this.mMenuItem = menuItem;
        mMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSearch();
                if (finishOnClose) {
                    setFinishOnClose(false);
                }
                initShowCase();
                return true;
            }
        });
    }

    /**
     * Return true if search is open
     *
     * @return
     */
    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    /**
     * Sets animation duration. ONLY FOR PRE-LOLLIPOP!!
     *
     * @param duration duration of the animation
     */
    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    /**
     * Open Search View. This will animate the showing of the view.
     */
    public void showSearch() {
        showSearch(true);
    }

    public void showSearch(boolean finishOnClose, boolean animate, SearchParameter searchParameter) {
        if(mSuggestionFragment != null) {
            mSuggestionFragment.setSearchParameterOwner(this);
        }

        setSearchParameter(searchParameter);

        setLastQuery(searchParameter.getSearchQuery());
        showSearch(finishOnClose, animate);
    }

    public void showSearch(boolean finishOnClose, boolean animate) {
        this.finishOnClose = finishOnClose;
        showSearch(animate);
        initShowCase();
    }

    private void initShowCase() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startShowCase();
            }
        }, IMAGE_SEARCH_SHOW_CASE_DIALOG_DELAY);
    }

    public boolean isFinishOnClose() {
        return finishOnClose;
    }

    public void setFinishOnClose(boolean finishOnClose) {
        this.finishOnClose = finishOnClose;
    }

    /**
     * Open Search View. If animate is true, Animate the showing of the view.
     *
     * @param animate true for animate
     */
    public void showSearch(boolean animate) {
        if (isSearchOpen()) {
            return;
        }

        textViewRequestFocus();

        if (animate) {
            setVisibleWithAnimation();
        } else {
            setVisibleWithoutAnimation();
        }

        mIsSearchOpen = true;
    }

    private void textViewRequestFocus() {
        mSearchSrcTextView.setText(lastQuery);
        mSearchSrcTextView.requestFocus();
        mSearchSrcTextView.setSelection(mSearchSrcTextView.getText().length());
    }

    public void showSearch(boolean animate, int tab) {
        showSearch(animate);

        switch (tab) {
            case TAB_SHOP_SUGGESTION:
                mSuggestionFragment.setCurrentTab(SearchMainFragment.PAGER_POSITION_SHOP);
                break;
            case TAB_PRODUCT_SUGGESTION:
                mSuggestionFragment.setCurrentTab(SearchMainFragment.PAGER_POSITION_PRODUCT);
                break;
            default:
                mSuggestionFragment.setCurrentTab(SearchMainFragment.PAGER_POSITION_PRODUCT);
                break;
        }

    }

    private void setVisibleWithAnimation() {
        AnimationUtil.AnimationListener animationListener = new AnimationUtil.AnimationListener() {
            @Override
            public boolean onAnimationStart(View view) {
                return false;
            }

            @Override
            public boolean onAnimationEnd(View view) {
                if (mSearchViewListener != null) {
                    mSearchViewListener.onSearchViewShown();
                }
                return false;
            }

            @Override
            public boolean onAnimationCancel(View view) {
                return false;
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSearchLayout.setVisibility(View.VISIBLE);
            AnimationUtil.reveal(mSearchContainer, animationListener);

        } else {
            AnimationUtil.fadeInView(mSearchLayout, mAnimationDuration, animationListener);
        }
    }

    private void setVisibleWithoutAnimation() {
        mSearchLayout.setVisibility(VISIBLE);
        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewShown();
        }
    }

    public SearchMainFragment getSuggestionFragment() {
        return mSuggestionFragment;
    }

    /**
     * Close search view.
     */

    public void closeSearch() {
        if (!isSearchOpen()) {
            return;
        }

        mSearchSrcTextView.setText(null);
        dismissSuggestions();
        clearFocus();

        mSearchLayout.setVisibility(GONE);
        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewClosed();
        }
        mIsSearchOpen = false;
    }

    public void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }

    public String getLastQuery() {
        return lastQuery;
    }

    public boolean getIsOfficial() {
        return searchParameter.getBoolean(SearchApiConst.OFFICIAL);
    }

    @Override
    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    @Override
    public SearchParameter getSearchParameter() {
        return searchParameter;
    }

    /**
     * Set this listener to listen to Query Change events.
     *
     * @param listener
     */
    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    /**
     * Set this listener to listen to Search View open and close events
     *
     * @param listener
     */
    public void setOnSearchViewListener(SearchViewListener listener) {
        mSearchViewListener = listener;
    }


    public void setOnImageSearchClickListener(ImageSearchClickListener imageSearchClickListener) {
        mImageSearchClickListener = imageSearchClickListener;
    }

    /**
     * Ellipsize suggestions longer than one line.
     *
     * @param ellipsize
     */
    public void setEllipsize(boolean ellipsize) {
        this.ellipsize = ellipsize;
    }

    @Override
    public void onFilterComplete(int count) {
        if (count > 0) {
            showSuggestions();
        } else {
            dismissSuggestions();
        }
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        // Don't accept focus if in the middle of clearing focus
        if (mClearingFocus) return false;
        // Check if SearchView is focusable.
        if (!isFocusable()) return false;
        return mSearchSrcTextView.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        hideKeyboard(this);
        super.clearFocus();
        mSearchSrcTextView.clearFocus();
        mClearingFocus = false;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        mSavedState = new SavedState(superState);
        mSavedState.query = mUserQuery != null ? mUserQuery.toString() : null;
        mSavedState.isSearchOpen = this.mIsSearchOpen;
        mSavedState.allowImageSearch = this.allowImageSearch;

        return mSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        mSavedState = (SavedState) state;

        if (mSavedState.isSearchOpen) {
            showSearch(false);
            setQuery(mSavedState.query, false);
        }

        setImageSearch(mSavedState.allowImageSearch);

        super.onRestoreInstanceState(mSavedState.getSuperState());
    }

    static class SavedState extends BaseSavedState {
        String query;
        boolean isSearchOpen;
        boolean allowImageSearch;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.query = in.readString();
            this.isSearchOpen = in.readInt() == 1;
            this.allowImageSearch = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(query);
            out.writeInt(isSearchOpen ? 1 : 0);
            out.writeInt(allowImageSearch ? 1 : 0);
        }

        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    public interface OnQueryTextListener {

        /**
         * Called when the user submits the query. This could be due to a key press on the
         * keyboard or due to pressing a submit button.
         * The listener can override the standard behavior by returning true
         * to indicate that it has handled the submit request. Otherwise return false to
         * let the SearchView handle the submission by launching any associated intent.
         *
         * @param query the query text that is to be submitted
         * @return true if the query has been handled by the listener, false to let the
         * SearchView perform the default action.
         */
        boolean onQueryTextSubmit(SearchParameter searchParameter);

        /**
         * Called when the query text is changed by the user.
         *
         * @param newText the new content of the query text field.
         * @return false if the SearchView should perform the default action of showing any
         * suggestions if available, true if the action was handled by the listener.
         */
        boolean onQueryTextChange(String newText);
    }

    public interface SearchViewListener {
        void onSearchViewShown();

        void onSearchViewClosed();
    }

    public interface ImageSearchClickListener {
        void onImageSearchClicked();
    }


}