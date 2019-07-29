package oms.pomelo.itides.base;

import rx.subscriptions.CompositeSubscription;

/**
 * NAME: Sherry
 * DATE: 2019-07-28
 */
public abstract class BasePresenter<T extends BaseContract> implements Presenter {

    //使用protected修饰符，便于子类进行调用
    protected CompositeSubscription mCompositeSubscription;

    @Override
    public void init() {
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void release() {
        //释放CompositeSubscription，否则会造成内存泄漏
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    //与具体视图进行绑定
    public abstract void bindPresenterView(T presentView);

}
