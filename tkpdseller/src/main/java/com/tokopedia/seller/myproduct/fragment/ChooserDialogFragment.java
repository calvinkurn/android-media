package com.tokopedia.seller.myproduct.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.tokopedia.core.R;
import com.tokopedia.seller.myproduct.ProductActivity;
import com.tokopedia.seller.myproduct.adapter.SimpleFragmentPagerAdapter;
import com.tokopedia.seller.myproduct.model.SimpleTextModel;
import com.tokopedia.core.util.MethodChecker;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 12/9/15.
 * best practice : set title at tablayout
 */
public class ChooserDialogFragment extends android.support.v4.app.DialogFragment {
    public static final String FRAGMENT_TAG = "ChooserDialogFragment";
    public static final String CHOOSER_DIALOG_TYPE = "type";
    public static final String CHOOSER_DIALOG_STYLE = "num";
    public static final String CHOOSER_DIALOG_TITLE = "title";
    public static final String CHOOSER_DIALOG_DATA = "data";

    int mNum;
    String title;
    int type;

    List<SimpleTextModel> datas;
    List <SimpleTextModel> dataClone;

    Button mOk;
    Button mButton;
    ChooserDialogFragment.onSubmitListener mListener;
    ViewPager viewPager;
    SimpleFragmentPagerAdapter adapter;
    TabLayout tabLayout;
    SearchView searchView;

    public static ChooserDialogFragment newInstance(int type, String title, List<SimpleTextModel> data){
        ChooserDialogFragment f = new ChooserDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(CHOOSER_DIALOG_TYPE, type);
        args.putInt(CHOOSER_DIALOG_STYLE, DialogFragment.STYLE_NO_FRAME);
        args.putString(CHOOSER_DIALOG_TITLE, title);
        args.putParcelable(CHOOSER_DIALOG_DATA, Parcels.wrap(data));
        f.setArguments(args);

        return f;
    }



    @Deprecated
    public static ChooserDialogFragment newInstance(String title, List<SimpleTextModel> data){
        ChooserDialogFragment f = new ChooserDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(CHOOSER_DIALOG_STYLE, DialogFragment.STYLE_NO_TITLE);
        args.putString(CHOOSER_DIALOG_TITLE, title);
        args.putParcelable(CHOOSER_DIALOG_DATA, Parcels.wrap(data));
        f.setArguments(args);

        return f;
    }

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    @Deprecated
    static ChooserDialogFragment newInstance(int num) {
        ChooserDialogFragment f = new ChooserDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(CHOOSER_DIALOG_STYLE, num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(CHOOSER_DIALOG_TYPE, ProductActivity.ADD_PRODUCT_CATEGORY);
        mNum = getArguments().getInt(CHOOSER_DIALOG_STYLE);
        title = getArguments().getString(CHOOSER_DIALOG_TITLE);
        datas = Parcels.unwrap(getArguments().getParcelable(CHOOSER_DIALOG_DATA));
        dataClone = ((List) ((ArrayList) datas).clone());

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum - 1) % 6) {
            case 1:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 2:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            case 3:
                style = DialogFragment.STYLE_NO_INPUT;
                break;
            case 4:
                style = DialogFragment.STYLE_NORMAL;
                break;
            case 5:
                style = DialogFragment.STYLE_NORMAL;
                break;
            case 6:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 7:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            case 8:
                style = DialogFragment.STYLE_NORMAL;
                break;
        }
        switch ((mNum - 1) % 6) {
            case 4:
                theme = android.R.style.Theme_Holo;
                break;
            case 5:
                theme = android.R.style.Theme_Holo_Light_Dialog;
                break;
            case 6:
                theme = android.R.style.Theme_Holo_Light;
                break;
            case 7:
                theme = android.R.style.Theme_Holo_Light_Panel;
                break;
            case 8:
                theme = android.R.style.Theme_Holo_Light;
                break;
        }
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_dialog, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        adapter = new SimpleFragmentPagerAdapter(getChildFragmentManager(),
                getActivity(), new ArrayList<String>(){{add(title);}}, dataClone);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        mButton = (Button) v.findViewById(R.id.btn_cancel);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        searchView = (SearchView) v.findViewById(R.id.search_action);
        String searchHint = "";
        if(title.toLowerCase().contains("kategori")){
            searchHint = getString(R.string.hint_search_categories);
        } else if (title.toLowerCase().contains("etalase")){
            searchHint = getString(R.string.hint_search_etalase);
        } else {
            searchHint = getString(R.string.hint_search_status);
        }
        searchView.setQueryHint(MethodChecker.fromHtml("<font color = #888888>" + searchHint + "</font>"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dataClone.clear();
                for (SimpleTextModel text : datas) {
                    text.setQuery(query);
                    if(text.getText().toLowerCase().contains(query.toLowerCase())){
                        dataClone.add(text);
                    }
                }
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dataClone.clear();
                for (SimpleTextModel text : datas) {
                    text.setQuery(newText);
                    if(text.getText().toLowerCase().contains(newText.toLowerCase())){
                        dataClone.add(text);
                    }
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dataClone = ((List) ((ArrayList) datas).clone());
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
//        searchView.requestFocusFromTouch();

        mOk = (Button)v.findViewById(R.id.btn_ok);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // currently handled by adapter
//                mListener.setOnSubmitListener();
                dismiss();
            }
        });

        return v;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    interface onSubmitListener {
        void setOnSubmitListener(String arg);
        void setOnSubmitListener(SimpleTextModel arg);
    }

}
