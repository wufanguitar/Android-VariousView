package com.wufanguitar.demo.pickerview.bean;

import com.wufanguitar.variousview.semi.callback.IPickerViewData;

public class ProvinceBean implements IPickerViewData {
    private long id;
    private String name;
    private String description;
    private String others;

    public ProvinceBean(long id, String name, String description, String others) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.others = others;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    // 这个用来显示在 PickerView 上面的字符串，PickerView 会通过 getPickerViewText 方法获取字符串显示出来
    @Override
    public Object getPickerViewData() {
        return name;
    }
}
