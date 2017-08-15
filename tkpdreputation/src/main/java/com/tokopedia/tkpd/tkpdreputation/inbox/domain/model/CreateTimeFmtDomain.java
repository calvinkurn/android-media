package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class CreateTimeFmtDomain {

    private String dateTimeFmt1;
    private String dateTimeFmt1x;
    private String dateTimeFmt2;
    private String dateTimeFmt3;
    private String dateTimeFmt3x;
    private String dateFmt1;

    public CreateTimeFmtDomain(String dateTimeFmt1, String dateTimeFmt1x,
                               String dateTimeFmt2, String dateTimeFmt3,
                               String dateTimeFmt3x, String dateFmt1) {
        this.dateTimeFmt1 = dateTimeFmt1;
        this.dateTimeFmt1x = dateTimeFmt1x;
        this.dateTimeFmt2 = dateTimeFmt2;
        this.dateTimeFmt3 = dateTimeFmt3;
        this.dateTimeFmt3x = dateTimeFmt3x;
        this.dateFmt1 = dateFmt1;
    }

    public String getDateTimeFmt1() {
        return dateTimeFmt1;
    }

    public String getDateTimeFmt1x() {
        return dateTimeFmt1x;
    }

    public String getDateTimeFmt2() {
        return dateTimeFmt2;
    }

    public String getDateTimeFmt3() {
        return dateTimeFmt3;
    }

    public String getDateTimeFmt3x() {
        return dateTimeFmt3x;
    }

    public String getDateFmt1() {
        return dateFmt1;
    }
}
