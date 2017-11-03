package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import java.util.ArrayList;

/**
 * @author by nisie on 10/31/17.
 */

public class KolComments {

    private final String lastcursor;
    ArrayList<KolCommentViewModel> listComments;

    public KolComments(String lastcursor, ArrayList<KolCommentViewModel> listComments) {
        this.listComments = listComments;
        this.lastcursor = lastcursor;
    }

    public ArrayList<KolCommentViewModel> getListComments() {
        return listComments;
    }

    public String getLastcursor() {
        return lastcursor;
    }
}
