package com.fitgreat.doctorface_top.base

interface BasePresenter<V : BaseView> {
    fun attachView(view: V)

    fun detachView()
}