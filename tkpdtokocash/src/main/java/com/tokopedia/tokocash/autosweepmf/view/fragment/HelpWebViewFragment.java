package com.tokopedia.tokocash.autosweepmf.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tokopedia.tokocash.R;

/**
 * A Help fragment containing a WebView.
 */
public class HelpWebViewFragment extends Fragment {

    private static final String ARG_PAGE = "arg_page";

    public HelpWebViewFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given url
     */
    public static HelpWebViewFragment newInstance(String page) {
        HelpWebViewFragment fragment = new HelpWebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        WebView web = rootView.findViewById(R.id.help_web_view);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(getArguments().getString(ARG_PAGE));
        return rootView;
    }
}
