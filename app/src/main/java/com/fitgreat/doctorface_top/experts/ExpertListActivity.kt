package com.fitgreat.doctorface_top.experts

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.fitgreat.doctorface_top.R
import com.fitgreat.doctorface_top.base.BaseMvpActivity
import com.fitgreat.doctorface_top.constants.ConstantsString.EXPERT_DATA
import com.fitgreat.doctorface_top.constants.ConstantsString.EXPERT_DATA_BUNDLE
import com.fitgreat.doctorface_top.experts.adapter.ExpertAdapter
import com.fitgreat.doctorface_top.experts.info.ExpertInfoActivity
import com.fitgreat.doctorface_top.model.ExpertListData
import com.fitgreat.doctorface_top.util.LogUtils
import com.fitgreat.doctorface_top.view.BaseTitleView
import kotlinx.android.synthetic.main.activity_expert_list.*

/**
 * 专家一览
 */
class ExpertListActivity : BaseMvpActivity<ExpertListPresenterImp, ExpertListView>(),
    BaseTitleView.BaseBackListener, ExpertListView {
    private val TAG = "ExpertListActivity"

    override fun getLayoutResource(): Int {
        return R.layout.activity_expert_list
    }

    override fun initData() {
        //获取专家列表数据
        mPresenter?.getExpertList()
        //返回键点击事件
        expert_list_title.setBackKListener(this)
    }

    override fun initPresenter(): ExpertListPresenterImp {
        return ExpertListPresenterImp()
    }

    /**
     * 页面专家列表数据更新
     */
    override fun showExpertsSuccess(msg: MutableList<ExpertListData.Msg>) {
        LogUtils.json(TAG, "showExpertsSuccess\t\t${JSON.toJSONString(msg)}")
        val expertAdapter = ExpertAdapter(msg, this)
        expertAdapter.addChildClickViewIds(R.id.item_expert)
        expertAdapter.setOnItemChildClickListener { adapter, view, position ->
            val msg = adapter.data[position] as ExpertListData.Msg
            var intent = Intent(this@ExpertListActivity, ExpertInfoActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(EXPERT_DATA, msg)
            intent.putExtra(EXPERT_DATA_BUNDLE, bundle)
            startActivity(intent)
        }
        val gridLayoutManager = GridLayoutManager(this, 2)
        expert_list_data.layoutManager = gridLayoutManager
        expert_list_data.addItemDecoration(SpacesItemDecoration(10))
        expert_list_data.adapter = expertAdapter
    }

    override fun getContext(): Context {
        return this
    }

    override fun back() {
        finish()
    }

    class SpacesItemDecoration(private val space: Int) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space
        }
    }
}