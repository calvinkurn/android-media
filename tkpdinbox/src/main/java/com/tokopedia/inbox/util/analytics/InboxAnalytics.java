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
        String RESOLUTION_CENTER_CHAT = "resolution chat";
        String RESOLUTION_PRODUCT_COMPLAIN = "resolution detail";
    }

    interface ResoDimension {
        String RESOLUTION_ID = "resolutionId";
    }
    
    private interface Action {
        String IMPRESSION_SAVE_INPUT_AWB_CHAT = "impression from masukkan no resi";
        String IMPRESSION_SAVE_EDIT_AWB_CHAT = "impression from edit resi";
        String IMPRESSION_SOLUTION_APPEAL_DETAIL_PAGE_CHAT = "impression solusi detail naik banding";
        String IMPRESSION_SOLUTION_EDIT_DETAIL_PAGE_CHAT = "impression solusi detail ubah solusi";
        String IMPRESSION_SOLUTION_EDIT_LIST_PAGE_CHAT = "impression solusi ubah solusi";
        String IMPRESSION_SOLUTION_APPEAL_LIST_PAGE_CHAT = "impression solusi detail naik banding";

        String CLICK_SAVE_INPUT_AWB_CHAT = "click simpan from masukkan no resi";
        String CLICK_SAVE_SOLUTION_EDIT_REFUND = "click simpan from solusi refund from ubah solusi";
        String CLICK_SAVE_SOLUTION_APPEAL_REFUND = "click simpan from solusi refund from naik banding";
        String CLICK_CANCEL_INPUT_AWB_CHAT = "click kembali from masukkan no resi";
        String CLICK_SAVE_EDIT_AWB_CHAT = "click simpan from edit resi";
        String CLICK_CANCEL_EDIT_AWB_CHAT = "click kembali from edit resi";
        String CLICK_CLOSE_APPEAL_PAGE_CHAT = "click x from naik banding";
        String CLICK_SOLUTION_APPEAL_PAGE_CHAT = "click solusi naik banding";
        String CLICK_CLOSE_EDIT_PAGE_CHAT = "click x from ubah solusi";
        String CLICK_SOLUTION_APPEAL_DETAIL_PAGE_CHAT = "click solusi detail naik banding";
        String CLICK_SOLUTION_EDIT_DETAIL_PAGE_CHAT = "click solusi detail ubah solusi";
        String CLICK_CLOSE_APPEAL_DETAIL_PAGE_CHAT = "click x solusi detail naik banding";
        String CLICK_CLOSE_EDIT_DETAIL_PAGE_CHAT = "click x solusi detail ubah solusi";
        String CLICK_BACK_ARROW_CHATBOX_DETAIL = "click back arrow from chatbox";
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

    public static EventTracking eventResoDetailClickChatBox(String resolutionId) {
        return new EventTracking(
                Event.EVENT_RESOLUTION_CENTER,
                Category.RESOLUTION_PRODUCT_COMPLAIN,
                Action.CLICK_BACK_ARROW_CHATBOX_DETAIL,
                ""
        ).setCustomEvent(ResoDimension.RESOLUTION_ID, resolutionId);
    }
}
