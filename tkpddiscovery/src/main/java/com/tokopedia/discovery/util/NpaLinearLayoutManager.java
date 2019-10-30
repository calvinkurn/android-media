package com.tokopedia.discovery.util;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.tkpd.library.ui.view.LinearLayoutManager;

/**
 * Created by nakama on 09/12/16.
 */

public class NpaLinearLayoutManager extends LinearLayoutManager {

    public NpaLinearLayoutManager(Context context) {
        super(context);
    }

    public NpaLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NpaLinearLayoutManager(RecyclerView view) {
        super(view);
    }

    public NpaLinearLayoutManager(RecyclerView view, int orientation, boolean reverseLayout) {
        super(view, orientation, reverseLayout);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
