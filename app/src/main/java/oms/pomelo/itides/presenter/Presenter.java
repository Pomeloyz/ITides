package oms.pomelo.itides.presenter;

/**
 * NAME: Sherry
 * DATE: 2019-07-28
 * BasePresenter继承该Presenter
 */
public interface Presenter {

    void init();

    void release();

    void BindPresenterView(BaseContract baseContract); //绑定视图

}
