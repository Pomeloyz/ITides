package oms.pomelo.itides.base;

import rx.subscriptions.CompositeSubscription;

/**
 * NAME: Sherry
 * DATE: 2019-07-30
 */
public class BasePresenter<T extends BaseMvpView> implements Presenter<T> {

    protected CompositeSubscription mCompositeSubscription;
    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mCompositeSubscription = new CompositeSubscription();
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        //释放CompositeSubscription，否则会造成内存泄漏
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public T getMvpView() {
        return mMvpView;
    }
}
