package com.canadainformation.view.information;

import com.canadainformation.model.Information;

import rx.functions.Action0;

/**
 * Created By Akash
 * on 17,Dec,2019 : 8:23 PM
 */

/**
 * A Information that should be displayed as an item in a list of Information.
 * Contains the Information and the action that should be triggered when taping on the item a
 */

final public class InfoItem {
    private Information mInformation;

    private Action0 mOnClickAction;

    public InfoItem(Information information,
                    Action0 onClickAction) {
        mInformation = information;
        mOnClickAction = onClickAction;
    }

    public Information getmInformation() {
        return mInformation;
    }

    /**
     * @return the action to be triggered on click events
     */
    public Action0 getOnClickAction() {
        return mOnClickAction;
    }
}