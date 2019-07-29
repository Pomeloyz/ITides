package oms.pomelo.itides.utils;

import android.content.Context;

import okhttp3.OkHttpClient;
import oms.pomelo.itides.api.ApiService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * NAME: Sherry
 * DATE: 2019-07-27
 */
public final class RetrofitUtil {

    private Context mContext;
    private ApiService mApiService;
    private OkHttpClient mClient = new OkHttpClient();
    private GsonConverterFactory mFactory = GsonConverterFactory.create();

    private volatile static RetrofitUtil sInstance;

    //使用单例模式获取该对象
    public static RetrofitUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitUtil(context);
                }
            }
        }
        return sInstance;
    }

    private RetrofitUtil(Context context) {
        mContext = context;
        initRetrofit();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(mClient)
                .addConverterFactory(mFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mApiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService(){
        return mApiService;
    }
}
