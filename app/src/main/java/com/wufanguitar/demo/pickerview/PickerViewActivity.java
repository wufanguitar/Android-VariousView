package com.wufanguitar.demo.pickerview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wufanguitar.demo.R;
import com.wufanguitar.demo.pickerview.bean.CardBean;
import com.wufanguitar.demo.pickerview.bean.ProvinceBean;
import com.wufanguitar.semi.ActionSheet;
import com.wufanguitar.semi.ContentView;
import com.wufanguitar.semi.OptionsWheelView;
import com.wufanguitar.semi.TimeWheelView;
import com.wufanguitar.semi.callback.ICustomLayout;
import com.wufanguitar.semi.listener.OnDismissListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: Frank Wu
 * @Time: 2018/01/01 on 22:56
 * @Email: wu.fanguitar@163.com
 * @Description:
 */

public class PickerViewActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    /* private ArrayList<ArrayList<ArrayList<IWheelViewData>>> options3Items = new ArrayList<>(); */
    private Button btn_Time, btn_Options, btn_CustomOptions, btn_CustomTime, btn_no_linkage, btn_to_Fragment, btn_ContentView_default,
            btn_ContentView_reject, btn_ContentView_back, btn_approve_select, btn_reject_approve_comment, btn_show_loading, btn_loading_option_comment;

    private TimeWheelView pvTime, pvCustomTime, pvCustomLunar;
    private OptionsWheelView pvOptions, pvCustomOptions, pvNoLinkOptions, cvBack, pvSelectApprove, optionToComment;
    private ContentView cvDefault;
    private ContentView cvReject, cvRejectApproveComment;
    private ActionSheet cvLoading;
    private ArrayList<CardBean> cardItem = new ArrayList<>();

    private ArrayList<String> food = new ArrayList<>();
    private ArrayList<String> clothes = new ArrayList<>();
    private ArrayList<String> computer = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_view);

        // 等数据加载完毕再初始化并显示Picker，以免还未加载完数据就显示，造成APP崩溃。
        getOptionData();

        initLunarPicker();
        initTimePicker();
        initOptionPicker();
        initCustomTimePicker();
        initCustomOptionPicker();
        initNoLinkOptionsPicker();
        initContentViewDefault();
        initContentViewReject();
        initContentViewBackToPrestep();
        initCustomOptionSelectApprove();
        initRejectToApproveComment();
        initShowLoadingDialog();
        initLoadingOptionComment();


        btn_Time = (Button) findViewById(R.id.btn_Time);
        btn_Options = (Button) findViewById(R.id.btn_Options);
        btn_CustomOptions = (Button) findViewById(R.id.btn_CustomOptions);
        btn_CustomTime = (Button) findViewById(R.id.btn_CustomTime);
        btn_no_linkage = (Button) findViewById(R.id.btn_no_linkage);
        btn_to_Fragment = (Button) findViewById(R.id.btn_fragment);
        btn_ContentView_default = (Button) findViewById(R.id.btn_Content);
        btn_ContentView_reject = (Button) findViewById(R.id.btn_Content_edit);
        btn_ContentView_back = (Button) findViewById(R.id.btn_Content_back);
        btn_approve_select = (Button) findViewById(R.id.btn_Option_approve_select);
        btn_reject_approve_comment = (Button) findViewById(R.id.btn_reject_approve_comment);
        btn_show_loading = (Button) findViewById(R.id.btn_show_loading);
        btn_loading_option_comment = (Button) findViewById(R.id.btn_loading_option_comment);

        btn_Time.setOnClickListener(this);
        btn_Options.setOnClickListener(this);
        btn_CustomOptions.setOnClickListener(this);
        btn_CustomTime.setOnClickListener(this);
        btn_no_linkage.setOnClickListener(this);
        btn_to_Fragment.setOnClickListener(this);
        btn_ContentView_default.setOnClickListener(this);
        btn_ContentView_reject.setOnClickListener(this);
        btn_ContentView_back.setOnClickListener(this);
        btn_approve_select.setOnClickListener(this);
        btn_reject_approve_comment.setOnClickListener(this);
        btn_show_loading.setOnClickListener(this);
        btn_loading_option_comment.setOnClickListener(this);

        findViewById(R.id.btn_GotoJsonData).setOnClickListener(this);
        findViewById(R.id.btn_lunar).setOnClickListener(this);
    }

    private void initLunarPicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomLunar = new TimeWheelView.Builder(this, new TimeWheelView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) { // 选中事件回调
                Toast.makeText(PickerViewActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
            }
        }).setSelectDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_lunar, new ICustomLayout() {

                    @Override
                    public void customLayout(final View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.returnData();
                                pvCustomLunar.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomLunar.dismiss();
                            }
                        });
//                        公农历切换
                        CheckBox cb_lunar = (CheckBox) v.findViewById(R.id.cb_lunar);
                        cb_lunar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                pvCustomLunar.setLunarCalendar(!pvCustomLunar.isLunarCalendar());
                                //自适应宽
                                setTimePickerChildWeight(v, 0.8f, isChecked ? 1f : 1.1f);
                            }
                        });
                    }

                    /**
                     * 公农历切换后调整宽
                     */
                    private void setTimePickerChildWeight(View v, float yearWeight, float weight) {
                        ViewGroup timepicker = (ViewGroup) v.findViewById(R.id.wheel_time);
                        View year = timepicker.getChildAt(0);
                        LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams) year.getLayoutParams());
                        lp.weight = yearWeight;
                        year.setLayoutParams(lp);
                        for (int i = 1; i < timepicker.getChildCount(); i++) {
                            View childAt = timepicker.getChildAt(i);
                            LinearLayout.LayoutParams childLp = ((LinearLayout.LayoutParams) childAt.getLayoutParams());
                            childLp.weight = weight;
                            childAt.setLayoutParams(childLp);
                        }
                    }
                })
                .setTimeType(new boolean[]{true, true, true, false, false, false})
                .setCenterLabel(false) // 是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .build();
    }

    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2019, 11, 28);
        //时间选择器
        pvTime = new TimeWheelView.Builder(this, new TimeWheelView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                Button btn = (Button) v;
                btn.setText(getTime(date));
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setTimeType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .setCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentTextSize(21)
                .setSelectDate(selectedDate)
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .build();
    }

    private void initOptionPicker() { // 条件选择器初始化
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */
        pvOptions = new OptionsWheelView.Builder(this, new OptionsWheelView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String tx = options1Items.get(options1).getWheelViewData()
                        + options2Items.get(options1).get(options2);
                btn_Options.setText(tx);
            }
        })
                .setTitleStr("城市选择")
                .setContentTextSize(20) // 设置滚轮文字大小
                .setDividerColor(Color.LTGRAY) // 设置分割线的颜色
                .setOptionsSelectedPosition(0, 1) // 默认选中项
                .setWheelViewBgColor(Color.BLACK)
                .setTopBarBgColor(Color.DKGRAY)
                .setTitleStrColor(Color.LTGRAY)
                .setLeftBtnStrColor(Color.YELLOW)
                .setRightBtnStrColor(Color.YELLOW)
                .setCenterTextColor(Color.LTGRAY)
                .setCenterLabel(false) // 是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("省", "市", "区")
                .setBackgroundColor(0x66000000) // 设置外部遮罩颜色
                .build();
        pvOptions.setRelatedOptions(options1Items, options2Items); // 二级选择器
    }

    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimeWheelView.Builder(this, new TimeWheelView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                btn_CustomTime.setText(getTime(date));
            }
        }).setSelectDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new ICustomLayout() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        final TextView tvCenter = (TextView) v.findViewById(R.id.tv_center_custom);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                        tvCenter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tvCenter.setText("改变");
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setTimeType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTimeXOffset(0, 0, 0, 40, 0, -40)
                .setCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
    }

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsWheelView.Builder(this, new OptionsWheelView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = (String) cardItem.get(options1).getWheelViewData();
                btn_CustomOptions.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new ICustomLayout() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        TextView tvCenter = (TextView) v.findViewById(R.id.tv_option_center);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                        tvAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getCardData();
                                pvCustomOptions.setRelatedOptions(cardItem);
                            }
                        });

                        tvCenter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(PickerViewActivity.this, "option center click", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .setDialogStyle(true)
                .build();

        pvCustomOptions.setRelatedOptions(cardItem);//添加数据
    }

    private void initNoLinkOptionsPicker() {// 不联动的多级选项
        pvNoLinkOptions = new OptionsWheelView.Builder(this, new OptionsWheelView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String str = "food:" + food.get(options1)
                        + "\nclothes:" + clothes.get(options2)
                        + "\ncomputer:" + computer.get(options3);

                Toast.makeText(PickerViewActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        }).build();
        pvNoLinkOptions.setNoRelatedOptions(food, clothes, computer);
    }

    private void initContentViewDefault() {
        cvDefault = new ContentView.Builder(this)
                .setTitleStr("我的上下班时间")
                .setBottomBtnStr("知道了")
                .setContentStr("早上9:00到下午18:00")
                .build();
    }

    private void initContentViewReject() {
        cvReject = new ContentView.Builder(this).setLayoutRes(R.layout.view_content_reject)
                .setTitleStr("驳回审核备注")
                .setRightBtnStr("取消")
                .setBottomBtnStr("确认驳回")
                .build();
    }

    private void initContentViewBackToPrestep() {
        List<String> list = new ArrayList();
        list.add("chenjie(hrdp)");
        list.add("zhengjing01(直属上级审批)");
        list.add("liangshixiong48441(一级目录审批)");
        cvBack = new OptionsWheelView.Builder(this, new OptionsWheelView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int optionsFirst, int optionsSecond, int optionsThird, View view) {
                cvBack.dismissImmediately();
                cvReject = new ContentView.Builder(PickerViewActivity.this).setLayoutRes(R.layout.view_content_reject)
                        .setTitleStr("驳回审核备注")
                        .setRightBtnStr("×")
                        .setLeftBtnStr("上一步")
                        .setBottomBtnStr("确认驳回")
                        .setOnClickListener(new ContentView.OnClickListener() {
                            @Override
                            public void onLeftClick(View view) {
                                cvReject.dismissImmediately();
                                if (cvBack != null) {
                                    cvBack.show(false);
                                }
                            }

                            @Override
                            public void onRightClick(View view) {
                                cvReject.dismiss();
                            }

                            @Override
                            public void onBottomClick(View view) {
                                cvReject.dismiss();
                            }
                        })
                        .build();
                cvReject.show(false);
            }
        }).setCenterLabel(true)
                .setLabels("√", "", "")
                .build();
        cvBack.setNoRelatedOptions(list, null, null);
    }

    private void initCustomOptionSelectApprove() {
        ArrayList<String> data = new ArrayList<>();
        data.add("何爽(深圳)-heshuang01");
        data.add("常振(北京)-changzhen");
        data.add("刘洋(北京)-liuyang");
        data.add("刁学禹(北京)-diaoxueyu");
        data.add("郭毅(北京)-guoyi");
        pvSelectApprove = new OptionsWheelView.Builder(this, new OptionsWheelView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int optionsFirst, int optionsSecond, int optionsThird, View view) {

            }
        }).setLayoutRes(R.layout.select_approve_layout, new ICustomLayout() {
            @Override
            public void customLayout(View v) {
                AppCompatTextView left = (AppCompatTextView) v.findViewById(R.id.left);
                AppCompatTextView right = (AppCompatTextView) v.findViewById(R.id.right);
                AppCompatTextView title = (AppCompatTextView) v.findViewById(R.id.title);
                title.setText("下一节点审批人选择");
                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvSelectApprove.dismiss();
                    }
                });
                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvSelectApprove.returnData();
                        pvSelectApprove.dismiss();
                    }
                });
            }
        }).setDividerColor(Color.WHITE)
                .build();
        pvSelectApprove.setNoRelatedOptions(data, null, null);
    }

    public void initRejectToApproveComment() {
        cvRejectApproveComment = new ContentView.Builder(this)
                .setLayoutRes(R.layout.reject_approve_comment_layout, new ICustomLayout() {
                    @Override
                    public void customLayout(View v) {

                    }
                }).setTitleStr("驳回审核备注")
                .setRightBtnStr("关闭")
                .setBottomBtnStr("确认驳回")
                .build();
    }

    public void initShowLoadingDialog() {
//        cvLoading = new ContentView.Builder(this).setDialogStyle(true)
//                .setLayoutRes(R.layout.show_loading, new ICustomLayout() {
//                    @Override
//                    public void customLayout(View v) {
//                        ImageView iv = (ImageView) v.findViewById(R.id.loading_iv);
//                        RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                        AccelerateInterpolator lin = new AccelerateInterpolator();
//                        rotate.setInterpolator(lin);
//                        rotate.setDuration(2000);//设置动画持续周期
//                        rotate.setRepeatCount(1);//设置重复次数
//                        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//                        rotate.setStartOffset(10);//执行前的等待时间
//                        iv.setAnimation(rotate);
//                    }
//                })
//                .build();

        List<String> arrayList = new ArrayList<>();
        arrayList.add("武汉01");
        arrayList.add("武汉02");
        arrayList.add("武汉03");
        arrayList.add("武汉04");
        arrayList.add("武汉05");
        arrayList.add("武汉06");
        arrayList.add("武汉07");
        arrayList.add("武汉08");
        arrayList.add("武汉09");
        arrayList.add("武汉10");
        arrayList.add("武汉11");
        arrayList.add("武汉12");
        arrayList.add("武汉13");
        arrayList.add("武汉14");
        arrayList.add("武汉15");
        arrayList.add("武汉16");
        arrayList.add("武汉17");
        arrayList.add("武汉18");

        cvLoading = new ActionSheet.Builder(PickerViewActivity.this)
//                .setActionSheetPadding(new float[]{0, 0, 0, 0})
//                .setCornerRadius(0)
                .setItemStrColor(Color.BLACK)
                .setBottomBtnStrColor(Color.BLACK)
//                .setItemStrSize(18)
//                .setBottomBtnStrSize(18)
//                .setItemHeight(56)
//                .setBottomBtnHeight(56)
//                .setBottomBtnTopMargin(7)
                .setKeybackDismiss(true)
                .setOutsideDismiss(true)
                .setItemStrList(arrayList)
                .build();
    }

    public void initLoadingOptionComment() {
        final ArrayList<String> data = new ArrayList<>();
        data.add("何爽(深圳)-heshuang01");
        data.add("常振(北京)-changzhen");
        data.add("刘洋(北京)-liuyang");
        data.add("刁学禹(北京)-diaoxueyu");
        data.add("郭毅(北京)-guoyi");
        final BackApproveView backApproveView = new BackApproveView(this);
        optionToComment = new OptionsWheelView.Builder(this,
                new OptionsWheelView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int optionsFirst, int optionsSecond, int optionsThird, View view) {
                        backApproveView.switchTo("comment");
                        backApproveView.optionSelected(data.get(optionsFirst));
                    }
                }).setBackgroundColor(Color.BLUE)
                .setShareCommonLayout(true)
                .setLeftBtnStr("取消")
                .setRightBtnStr("确定")
                .setOutsideDismiss(true)
                .setKeybackDismiss(true)
                .setLayoutRes(R.layout.approve_loading_option_comment, backApproveView)
                .setOnClickListener(new OptionsWheelView.OnClickListener() {
                    @Override
                    public void onLeftClick(View view) {
                        optionToComment.dismiss();
                    }

                    @Override
                    public void onRightClick(View view) {
                        optionToComment.returnData();
                    }
                })
                .build();
        backApproveView.setBaseView(optionToComment);
//        optionToComment.setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss(Object o) {
//                backApproveView.switchTo("option");
//            }
//        });
        optionToComment.setNoRelatedOptions(data, null, null);
    }

    private void getOptionData() {

        /**
         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IWheelViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */

        getCardData();
        getNoLinkData();

        // 选项1
        options1Items.add(new ProvinceBean(0, "广东", "描述部分", "其他数据"));
        options1Items.add(new ProvinceBean(1, "湖南", "描述部分", "其他数据"));
        options1Items.add(new ProvinceBean(2, "广西", "描述部分", "其他数据"));

        // 选项2
        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("广州");
        options2Items_01.add("佛山");
        options2Items_01.add("东莞");
        options2Items_01.add("珠海");
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        options2Items_02.add("株洲");
        options2Items_02.add("衡阳");
        ArrayList<String> options2Items_03 = new ArrayList<>();
        options2Items_03.add("桂林");
        options2Items_03.add("玉林");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);

        /*-------- 数据源添加完毕 ---------*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Time && pvTime != null) {
            // pvTime.setDate(Calendar.getInstance());
           /* pvTime.show(); //show timePicker*/
            pvTime.show(v); // 弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
        } else if (v.getId() == R.id.btn_Options && pvOptions != null) {
            pvOptions.show(); // 弹出条件选择器
        } else if (v.getId() == R.id.btn_CustomOptions && pvCustomOptions != null) {
            pvCustomOptions.show(); // 弹出自定义条件选择器
        } else if (v.getId() == R.id.btn_CustomTime && pvCustomTime != null) {
            pvCustomTime.show(); // 弹出自定义时间选择器
        } else if (v.getId() == R.id.btn_no_linkage && pvNoLinkOptions != null) { // 不联动数据选择器
            pvNoLinkOptions.show();
        } else if (v.getId() == R.id.btn_GotoJsonData) { // 跳转到省市区解析示例页面
            startActivity(new Intent(PickerViewActivity.this, JsonDataActivity.class));
        } else if (v.getId() == R.id.btn_fragment) { // 跳转到 fragment
            startActivity(new Intent(PickerViewActivity.this, FragmentTestActivity.class));
        } else if (v.getId() == R.id.btn_lunar) {
            pvCustomLunar.show();
        } else if (v.getId() == R.id.btn_Content) {
            cvDefault.show();
        } else if (v.getId() == R.id.btn_Content_edit) {
            cvReject.show();
        } else if (v.getId() == R.id.btn_Content_back) {
            cvBack.show();
        } else if (v.getId() == R.id.btn_Option_approve_select) {
            pvSelectApprove.show();
        } else if (v.getId() == R.id.btn_reject_approve_comment) {
            cvRejectApproveComment.show();
        } else if (v.getId() == R.id.btn_show_loading) {
            cvLoading.show();
        } else if (v.getId() == R.id.btn_loading_option_comment) {
            optionToComment.show();
        }
    }

    private void getCardData() {
        for (int i = 0; i < 5; i++) {
            cardItem.add(new CardBean(i, "No.ABC12345 " + i));
        }

        for (int i = 0; i < cardItem.size(); i++) {
            if (cardItem.get(i).getCardNo().length() > 6) {
                String str_item = cardItem.get(i).getCardNo().substring(0, 6) + "...";
                cardItem.get(i).setCardNo(str_item);
            }
        }
    }

    private void getNoLinkData() {
        food.add("KFC");
        food.add("MacDonald");
        food.add("Pizza hut");

        clothes.add("Nike");
        clothes.add("Adidas");
        clothes.add("Anima");

        computer.add("ASUS");
        computer.add("Lenovo");
        computer.add("Apple");
        computer.add("HP");
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
