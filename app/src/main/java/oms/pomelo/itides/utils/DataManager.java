package oms.pomelo.itides.utils;

import android.content.Context;

import oms.pomelo.itides.api.ApiService;
import oms.pomelo.itides.daliy.DailyInfo;
import rx.Observable;

/**
 * NAME: Sherry
 * DATE: 2019-07-28
 * 该类用来管理ApiService中对应的各种API接口，
 * 当做Retrofit和presenter中的桥梁，Activity就不用直接和retrofit打交道了
 */
public class DataManager {

    private ApiService apiService;
    private volatile static DataManager instance;

    private DataManager(Context context) {
        this.apiService = RetrofitUtil.getInstance(context).getApiService();
    }

    public static DataManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager(context);
                }
            }
        }
        return instance;
    }

    //将retrofit的业务方法映射到DataManager中，以后统一用该类来调用业务方法
    public Observable<DailyInfo> getDailyInfo() {
        return apiService.getDailyInfo();
    }

}
