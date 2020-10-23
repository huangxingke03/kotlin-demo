package com.fitgreat.doctorface_top.base

open class BasePresenterImp<V : BaseView> : BasePresenter<V> {
    var mView: V? = null
    override fun attachView(view: V) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }
}