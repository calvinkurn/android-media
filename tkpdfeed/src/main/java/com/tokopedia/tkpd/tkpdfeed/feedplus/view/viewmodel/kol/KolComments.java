package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import java.util.ArrayList;

/**
 * @author by nisie on 10/31/17.
 */

public class KolComments {

    ArrayList<KolCommentViewModel> listComments;

    public KolComments(ArrayList<KolCommentViewModel> listComments) {
        this.listComments = listComments;
    }

    public ArrayList<KolCommentViewModel> getListComments() {
        return listComments;
    }
}
