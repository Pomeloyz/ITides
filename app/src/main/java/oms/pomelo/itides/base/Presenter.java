package oms.pomelo.itides.base;


/**
 * NAME: Sherry
 * DATE: 2019-07-30
 */
public interface Presenter<T extends BaseMvpView> {

    void attachView(T mvpView);

    void detachView();

}
