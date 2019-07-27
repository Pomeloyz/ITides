package oms.pomelo.itides.utils;

import android.content.Context;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import oms.pomelo.itides.api.ApiService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * NAME: Sherry
 * DATE: 2019-07-27
 */
public class RetrofitUtil {

    private Context mContext;
    private ApiService apiService;
    private Retrofit mRetrofit;
    OkHttpClient client = new OkHttpClient();
    GsonConverterFactory factory = GsonConverterFactory.create();

    private volatile static RetrofitUtil instance;

    //使用单例模式获取该对象
    public static RetrofitUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil(context);
                }
            }
        }
        return instance;
    }

    public RetrofitUtil(Context context) {
        mContext = context;
        initRetrofit();
    }

    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        apiService = mRetrofit.create(ApiService.class);
    }

    public ApiService getApiService(){
        return apiService;
    }
}
