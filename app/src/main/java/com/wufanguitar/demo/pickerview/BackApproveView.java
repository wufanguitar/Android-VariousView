package com.wufanguitar.demo.pickerview;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wufanguitar.demo.R;
import com.wufanguitar.semi.OptionsWheelView;
import com.wufanguitar.semi.base.BaseView;
import com.wufanguitar.semi.callback.ICustomLayout;

/**
 * @Author: Frank Wu
 * @Time: 2018/01/27 on 23:49
 * @Email: wu.fanguitar@163.com
 * @Description:
 */

public class BackApproveView implements ICustomLayout {
    private Context mContext;
    private String mOptionSelected;
    private BaseView mBaseView;
    // option
    private ViewGroup mOptionLayout;
    private AppCompatTextView mOptionTitleNextStep;
    private AppCompatTextView mOptionTitleCancel;

    // comment
    private ViewGroup mCommentLayout;
    private AppCompatTextView mConfirmBack;
    private AppCompatTextView mCommentTitleClose;
    private AppCompatTextView mCommentTitlePreStep;
    private AppCompatEditText mCommentEt;

    public BackApproveView(Context context) {
        this.mContext = context;
    }

    @Override
    public void customLayout(View v) {
        mOptionLayout = (ViewGroup) v.findViewById(R.id.option_selecet);
        mOptionLayout.setTag("option");
        mCommentLayout = (ViewGroup) v.findViewById(R.id.comment_back);
        mCommentLayout.setTag("comment");
        mCommentEt = (AppCompatEditText) v.findViewById(R.id.comment_et);
        mConfirmBack = (AppCompatTextView) v.findViewById(R.id.bottom);
        mConfirmBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOptionSelected != null) {
                    mBaseView.dismiss();
                    Toast.makeText(mContext, mCommentEt.getText().toString() + "-" + mOptionSelected, Toast.LENGTH_LONG).show();
                }
            }
        });
        mOptionTitleNextStep = (AppCompatTextView) v.findViewById(R.id.right);
        mCommentTitlePreStep = (AppCompatTextView) v.findViewById(R.id.to_option);
        mCommentTitlePreStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTo("option");
            }
        });
        mCommentTitleClose = (AppCompatTextView) v.findViewById(R.id.close);
        mCommentTitleClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseView.dismiss();
            }
        });
    }

    public void switchTo(String tag) {
        switch (tag) {
            case "option":
                mOptionLayout.setVisibility(View.VISIBLE);
                mCommentLayout.setVisibility(View.GONE);
                break;
            case "comment":
                mOptionLayout.setVisibility(View.GONE);
                mCommentLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void optionSelected(String selected) {
        this.mOptionSelected = selected;
    }

    public void setBaseView(OptionsWheelView optionsWheelView) {
        mBaseView = optionsWheelView;
    }
}
