package com.fitgreat.doctorface_top.base

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.fitgreat.doctorface_down.speech.SpeechManager

abstract class BaseMvpActivity<T : BasePresenterImp<V>, V : BaseView> : AppCompatActivity(), BaseView {
    var mPresenter: T? = null
    var speechManager:SpeechManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        //初始化presenter
        mPresenter = initPresenter()
        //presenter关联view
        mPresenter?.attachView(this as V)
        //设置页面显示全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //初始化SpeechManager
        speechManager= SpeechManager.instance(this)
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }
    /**
     * 获取页面布局xml文件id
     */
    abstract fun getLayoutResource(): Int

    /**
     * 页面创建初始化
     */
    abstract fun initData()

    /**
     * 获取实现presenter对象
     */
    abstract fun initPresenter(): T
}