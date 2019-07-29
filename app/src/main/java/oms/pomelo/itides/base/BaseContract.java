package oms.pomelo.itides.base;

/**
 * NAME: Sherry
 * DATE: 2019-07-28
 */

//面向视图View的接口，和前面的Presenter配合使用
public interface BaseContract {

    //定义一个最基础的接口，里面就包含一个出错信息的回调，负责所有网络请求的错误回调
    void onError(String result);
}
