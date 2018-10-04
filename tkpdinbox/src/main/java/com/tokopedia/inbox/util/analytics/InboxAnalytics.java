package com.tokopedia.inbox.util.analytics;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;

/**
 * @author by yfsx on 07/03/18.
 */

public class InboxAnalytics {
    
    private interface Event {
        String EVENT_RESOLUTION_CENTER = "resolutionCenter";
        String EVENT_RESOLUTION_CENTER_VIEW = "resolutionCenterView";
    }
    
    private interface Category {
        String RESOLUTION_CENTER = "resolution center";
        String RESOLUTION_CENTER_CHAT = "resolution chat";
        String RESOLUTION_DETAIL = "resolution detail";
        String RESOLUTION_PRODUCT_COMPLAIN = "resolution detail";
    }

    interface ResoDimension {
        String RESOLUTION_ID = "resolutionId";
    }
    
    private interface Action {
        String IMPRESSION_FINISH_COMPLAINT = "impression selesaikan komplain";
        String IMPRESSION_ACCEPT_SOLUTION = "impression terima solusi";
        String IMPRESSION_CANCEL_COMPLAINT = "impression batalkan komplain";
        String IMPRESSION_CHANGE_SOLUTION = "impression ubah solusi";
        String IMPRESSION_ASK_HELP = "impression minta bantuan";
        String IMPRESSION_APPEAL = "impression naik banding";
        String IMPRESSION_INPUT_ADDRESS = "impression masukkan alamat";
        String IMPRESSION_INPUT_AWB = "impression masukkan no resi";
        String IMPRESSION_RECOMPLAINT = "impression komplain ulang";
        String IMPRESSION_GREEN_ARROW = "impression green arrow";
        String IMPRESSION_SAVE_INPUT_AWB_CHAT = "impression from masukkan no resi";
        String IMPRESSION_SAVE_EDIT_AWB_CHAT = "impression from edit resi";
        String IMPRESSION_ACCEPT_SOLUTION_DIALOG_CHAT = "impression from terima solusi";
        String IMPRESSION_CANCEL_COMPLAINT_DIALOG_CHAT = "impression from batalkan komplain";
        String IMPRESSION_SOLUTION_APPEAL_DETAIL_PAGE_CHAT = "impression solusi detail naik banding";
        String IMPRESSION_SOLUTION_EDIT_DETAIL_PAGE_CHAT = "impression solusi detail ubah solusi";
        String IMPRESSION_SOLUTION_EDIT_LIST_PAGE_CHAT = "impression solusi ubah solusi";
        String IMPRESSION_SOLUTION_APPEAL_LIST_PAGE_CHAT = "impression solusi detail naik banding";

        String CLICK_ACCEPT_SOLUTION = "click terima solusi";
        String CLICK_CANCEL_COMPLAINT = "click batalkan komplain";
        String CLICK_CHANGE_SOLUTION = "click ubah solusi";
        String CLICK_ASK_HELP = "click minta bantuan";
        String CLICK_APPEAL = "click naik banding";
        String CLICK_INPUT_ADDRESS = "click masukkan alamat";
        String CLICK_INPUT_AWB = "click masukkan no resi";
        String CLICK_RECOMPLAINT = "click komplain ulang";
        String CLICK_FINISH_COMPLAINT = "click selesaikan komplain";
        String CLICK_GREEN_ARROW = "click green arrow next conversation";
        String CLICK_NEXT_ACTION = "click drop down langkah selanjutnya";
        String CLICK_PRODUCT_CHAT = "click produk yang dikomplain on chatbox";
        String CLICK_CHANGE_ADDRESS_CHAT = "click ubah alamat on chatbox";
        String CLICK_CHANGE_AWB_CHAT = "click edit resi on chatbox";
        String CLICK_TRACK_CHAT = "click lacak paket on chatbox";
        String CLICK_SAVE_INPUT_AWB_CHAT = "click simpan from masukkan no resi";
        String CLICK_SAVE_SOLUTION_EDIT_REFUND = "click simpan from solusi refund from ubah solusi";
        String CLICK_SAVE_SOLUTION_APPEAL_REFUND = "click simpan from solusi refund from naik banding";
        String CLICK_CANCEL_INPUT_AWB_CHAT = "click kembali from masukkan no resi";
        String CLICK_SAVE_EDIT_AWB_CHAT = "click simpan from edit resi";
        String CLICK_CANCEL_EDIT_AWB_CHAT = "click kembali from edit resi";
        String CLICK_CLOSE_APPEAL_PAGE_CHAT = "click x from naik banding";
        String CLICK_SOLUTION_APPEAL_PAGE_CHAT = "click solusi naik banding";
        String CLICK_CLOSE_EDIT_PAGE_CHAT = "click x from ubah solusi";
        String CLICK_SOLUTION_EDIT_PAGE_CHAT = "click solusi ubah solusi";
        String CLICK_SOLUTION_APPEAL_DETAIL_PAGE_CHAT = "click solusi detail naik banding";
        String CLICK_SOLUTION_EDIT_DETAIL_PAGE_CHAT = "click solusi detail ubah solusi";
        String CLICK_CLOSE_APPEAL_DETAIL_PAGE_CHAT = "click x solusi detail naik banding";
        String CLICK_CLOSE_EDIT_DETAIL_PAGE_CHAT = "click x solusi detail ubah solusi";
        String CLICK_ACCEPT_SOLUTION_YES_DIALOG_CHAT = "click ya from terima solusi";
        String CLICK_ACCEPT_SOLUTION_BACK_DIALOG_CHAT = "click kembali from terima solusi";
        String CLICK_CANCEL_COMPLAINT_YES_DIALOG_CHAT = "click ya from batalkan komplain";
        String CLICK_CANCEL_COMPLAINT_BACK_DIALOG_CHAT = "click kembali from batalkan komplain";
        String CLICK_SUBMIT_INPUT_ADDRESS_PAGE_DETAIL = "click submit from masukkan alamat";
        String CLICK_CANCEL_INPUT_ADDRESS_PAGE_DETAIL = "click cancel from masukkan alamat";
        String CLICK_BACK_INPUT_ADDRESS_PAGE_DETAIL = "click back arrow from masukkan alamat";
        String CLICK_SUBMIT_EDIT_ADDRESS_PAGE_DETAIL = "click submit from ubah alamat";
        String CLICK_CANCEL_EDIT_ADDRESS_PAGE_DETAIL = "click cancel from ubah alamat";
        String CLICK_BACK_EDIT_ADDRESS_PAGE_DETAIL = "click back arrow from ubah alamat";
        String CLICK_DETAIL_CHAT = "click detail";
        String CLICK_BACK_ARROW_CHATBOX_DETAIL = "click back arrow from chatbox";
        String CLICK_ADD_NEW_AWB_DETAIL = "click tambah on detail";
        String CLICK_RESO_FREE_RETURN_DETAIL = "click disini on detail";
        String CLICK_RESO_COPY_INVOICE_DETAIL = "click invoice on detail";
        String CLICK_RESO_TRACK_DETAIL = "click lacak on detail";
        String CLICK_RESO_SEE_RESO_HISTORY_DETAIL = "click lihat selengkapnya on detail";
        String CLICK_RESO_ASK_HELP_DETAIL = "click minta bantuan on detail";
        String CLICK_RESO_SEE_ADDRESS_HISTORY_DETAIL = "click lihat riwayat alamat pengembalian on detail";
        String CLICK_RESO_SEE_AWB_HISTORY_DETAIL = "click lihat riwayat no resi on detail";
        String CLICK_RESO_EDIT_SOLUTION_DETAIL = "click ubah on detail";
        String CLICK_RESO_BUYER_NAME_DETAIL = "click nama pembeli on detail";
        String CLICK_RESO_ARROW_BACK = "click arrow back";
        String CLICK_RESO_DISCUSSION = "click diskusi";
    }

    public static EventTracking eventResoChatImpressionGetSolution(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_ACCEPT_SOLUTION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionCancelComplaint(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_CANCEL_COMPLAINT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionChangeSolution(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_CHANGE_SOLUTION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionAskHelp(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_ASK_HELP,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionAppeal(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_APPEAL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionInputAddress(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_INPUT_ADDRESS,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionInputAWB(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_INPUT_AWB,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionRecomplaint(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_RECOMPLAINT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickAcceptSolution(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_ACCEPT_SOLUTION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickCancelComplaint(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CANCEL_COMPLAINT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickChangeSolution(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CHANGE_SOLUTION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickAskHelp(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_ASK_HELP,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickAppeal(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_APPEAL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickInputAddress(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_INPUT_ADDRESS,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionGreenArrow(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_GREEN_ARROW,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickGreenArrow(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_GREEN_ARROW,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickNextAction(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_NEXT_ACTION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickProductOnChat(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_PRODUCT_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickChangeAddress(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CHANGE_ADDRESS_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickEditAwb(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CHANGE_AWB_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickTrack(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_TRACK_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickSaveInputAWB(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_SAVE_INPUT_AWB_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickCancelInputAWB(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CANCEL_INPUT_AWB_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionSaveInputAWB(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_SAVE_INPUT_AWB_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickSaveEditAWB(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_SAVE_EDIT_AWB_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickCancelEditAWB(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CANCEL_EDIT_AWB_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionSaveEditAWB(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_SAVE_EDIT_AWB_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickCloseAppealPage(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CLOSE_APPEAL_PAGE_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickCloseEditPage(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CLOSE_EDIT_PAGE_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickSolutionAppealPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_SOLUTION_APPEAL_PAGE_CHAT,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickSolutionEditPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_SOLUTION_EDIT_LIST_PAGE_CHAT,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionSolutionEditPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_SOLUTION_EDIT_LIST_PAGE_CHAT,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionSolutionAppealPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_SOLUTION_APPEAL_LIST_PAGE_CHAT,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionAcceptSolutionDialog(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_ACCEPT_SOLUTION_DIALOG_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickYesAcceptSolutionDialog(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_ACCEPT_SOLUTION_YES_DIALOG_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickBackAcceptSolutionDialog(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_ACCEPT_SOLUTION_BACK_DIALOG_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionCancelComplaintDialog(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_CANCEL_COMPLAINT_DIALOG_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickYesCancelComplaintDialog(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CANCEL_COMPLAINT_YES_DIALOG_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickBackCancelComplaintDialog(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CANCEL_COMPLAINT_BACK_DIALOG_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickSolutionAppealDetailPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_SOLUTION_APPEAL_DETAIL_PAGE_CHAT,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickSolutionEditDetailPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_SOLUTION_EDIT_DETAIL_PAGE_CHAT,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickSolutionContinueEditDetailPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_SAVE_SOLUTION_EDIT_REFUND,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickSolutionContinueAppealDetailPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_SAVE_SOLUTION_APPEAL_REFUND,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionSolutionAppealDetailPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_SOLUTION_APPEAL_DETAIL_PAGE_CHAT,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatImpressionSolutionEditDetailPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_CENTER_CHAT,
                Action.IMPRESSION_SOLUTION_EDIT_DETAIL_PAGE_CHAT,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatCloseSolutionAppealDetailPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CLOSE_APPEAL_DETAIL_PAGE_CHAT,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatCloseSolutionEditDetailPage(String resolutionId, String solution) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_CLOSE_EDIT_DETAIL_PAGE_CHAT,
                solution
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoChatClickDetail(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_CENTER_CHAT,
                Action.CLICK_DETAIL_CHAT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickChatBox(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_PRODUCT_COMPLAIN,
                Action.CLICK_BACK_ARROW_CHATBOX_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailImpressionAcceptSolution(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_DETAIL,
                Action.IMPRESSION_ACCEPT_SOLUTION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailImpressionFinishComplaint(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_DETAIL,
                Action.IMPRESSION_FINISH_COMPLAINT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailImpressionRecomplaint(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_DETAIL,
                Action.IMPRESSION_RECOMPLAINT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailImpressionCancelComplaint(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_DETAIL,
                Action.IMPRESSION_CANCEL_COMPLAINT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailImpressionChangeSolution(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_DETAIL,
                Action.IMPRESSION_CHANGE_SOLUTION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailImpressionAppealSolution(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_DETAIL,
                Action.IMPRESSION_APPEAL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailImpressionAskHelp(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_DETAIL,
                Action.IMPRESSION_ASK_HELP,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailImpressionInputAddress(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_DETAIL,
                Action.IMPRESSION_INPUT_ADDRESS,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailImpressionInputAwb(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER_VIEW,
                Category.RESOLUTION_DETAIL,
                Action.IMPRESSION_INPUT_AWB,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickAcceptSolution(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_ACCEPT_SOLUTION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickCancelComplaint(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_CANCEL_COMPLAINT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickEditSolution(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_CHANGE_SOLUTION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickAskHelp(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_ASK_HELP,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickInputAddress(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_INPUT_ADDRESS,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickInputAwb(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_INPUT_AWB,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickRecomplaint(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RECOMPLAINT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickFinish(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_FINISH_COMPLAINT,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickAddAWB(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_ADD_NEW_AWB_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickFreeReturn(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_FREE_RETURN_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickCopyInvoice(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_COPY_INVOICE_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickTrack(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_TRACK_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickSeeAllResoHistory(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_SEE_RESO_HISTORY_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickAskHelpButton(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_ASK_HELP_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickAppealSolution(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_APPEAL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickNextAction(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_NEXT_ACTION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickAddressHistory(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_SEE_ADDRESS_HISTORY_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickAWBHistory(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_SEE_AWB_HISTORY_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickEditSolutionView(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_EDIT_SOLUTION_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickBuyerName(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_BUYER_NAME_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoClickDetailBack(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_ARROW_BACK,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoClickDiscussion(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_DETAIL,
                Action.CLICK_RESO_DISCUSSION,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }
}
