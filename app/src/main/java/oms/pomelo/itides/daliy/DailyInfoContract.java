package oms.pomelo.itides.daliy;

import oms.pomelo.itides.presenter.BaseContract;

/**
 * NAME: Sherry
 * DATE: 2019-07-28
 * 面向具体业务的回调，此处暂时只定义访问成功的回调
 */
public interface DailyInfoContract extends BaseContract {

    void onSuccess(DailyInfo dailyInfo); //请求成功的回调

}
