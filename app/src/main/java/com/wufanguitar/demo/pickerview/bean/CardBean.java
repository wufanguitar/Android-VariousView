package com.wufanguitar.demo.pickerview.bean;

import com.wufanguitar.semi.callback.IWheelViewData;

public class CardBean implements IWheelViewData {
    int id;
    String cardNo;

    public CardBean(int id, String cardNo) {
        this.id = id;
        this.cardNo = cardNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public Object getWheelViewData() {
        return cardNo;
    }
}

