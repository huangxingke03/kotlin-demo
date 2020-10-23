package com.fitgreat.doctorface_top.signin

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.view.View
import androidx.print.PrintHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitgreat.doctorface_top.R
import com.fitgreat.doctorface_top.base.BaseMvpActivity
import com.fitgreat.doctorface_top.constants.ConstantsString.CHECK_APPOINTMENT_CONDITION
import com.fitgreat.doctorface_top.model.Appointment
import com.fitgreat.doctorface_top.signin.adapter.AppointmentAdapter
import com.fitgreat.doctorface_top.util.ToastUtils
import com.fitgreat.doctorface_top.view.BaseTitleView
import com.fitgreat.doctorface_top.view.MyDialogYesOrNo
import kotlinx.android.synthetic.main.activity_sign_in.*


/**
 * 病人签到
 */
class SignInActivity : BaseMvpActivity<SignInPresenterImp, SignInView>(),
    BaseTitleView.BaseBackListener, SignInView {
    private val TAG = "SignInActivity"
    var baseDialogYesOrNo: MyDialogYesOrNo? = null

    override fun getLayoutResource(): Int {
        return R.layout.activity_sign_in
    }

    override fun initData() {
        //查询病人预约信息
        val checkCondition = intent.getCharSequenceExtra(CHECK_APPOINTMENT_CONDITION)
        checkCondition?.let {
            mPresenter?.checkAppointment(it.toString())
        }
        //添加页面点击事件
        sign_title.setBackKListener(this)
    }

    override fun initPresenter(): SignInPresenterImp {
        return SignInPresenterImp()
    }

    /**
     * 签到成功弹窗提示
     */
    override fun signInSuccess() {
        baseDialogYesOrNo = MyDialogYesOrNo.newInstance(
            getString(R.string.tip_title),
            getString(R.string.sign_dialog_content),
            getString(R.string.answer_yes),
            getString(R.string.answer_no)
        )
        baseDialogYesOrNo?.setMyCheckChangeListener(object :
            MyDialogYesOrNo.MyCheckChangeListener {
            override fun checkYes() {
//                baseDialogFragment?.dismiss()
//                ToastUtils.showSmallToast("点击了yes")
//                baseDialogFragment = MyDialogFragment.newInstance(
//                    getString(R.string.tip_title),
//                    getString(R.string.navigation_dialog_content),
//                    getString(R.string.answer_yes),
//                    getString(R.string.answer_no)
//                )
//                baseDialogFragment?.setMyCheckChangeListener(object :MyDialogFragment.MyCheckChangeListener{
//                    override fun checkYes() {
//                        baseDialogFragment?.dismiss()
//                    }
//
//                    override fun checkNo() {
//                        baseDialogFragment?.dismiss()
//                    }
//                })
//                baseDialogFragment?.show(supportFragmentManager, "navigationDialog")
//                printDocument()
            }

            override fun checkNo() {
                baseDialogYesOrNo?.dismiss()
                ToastUtils.showSmallToast("点击了no")
            }
        })
        baseDialogYesOrNo?.show(supportFragmentManager, "signInDialog")
    }

    /**
     * 打印文档
     */
    fun printDocument() {
//        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
//        val jobName = "${getString(R.string.app_name)} Document"
//        printManager.print(jobName, MyPrintDocumentAdapter(this), null)
        //打印图片
        PrintHelper(this).apply {
            scaleMode = PrintHelper.SCALE_MODE_FIT
        }.also { printHelper ->
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bt_home_move_selected)
            printHelper.printBitmap("bt_home_move_selected.png - test print", bitmap)
        }
    }

//    inner class MyPrintDocumentAdapter(context: Context) : PrintDocumentAdapter() {
//        override fun onLayout(
//            oldAttributes: PrintAttributes?,
//            newAttributes: PrintAttributes?,
//            cancellationSignal: CancellationSignal?,
//            callback: LayoutResultCallback?,
//            extras: Bundle?
//        ) {
//            var mPdfDocument = PrintedPdfDocument(this@SignInActivity, newAttributes)
//            // Respond to cancellation request
//            if (cancellationSignal?.isCanceled == true) {
//                callback?.onLayoutCancelled()
//                return
//            }
//            // Compute the expected number of printed pages
////            val pages = computePageCount(newAttributes)
//            if (pages > 0) {
//                // Return print information to print framework
////                PrintDocumentInfo.Builder("print_output.pdf")
////                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
////                    .setPageCount(pages)
////                    .build()
////                    .also { info ->
////                        // Content layout reflow is complete
////                        callback?.onLayoutFinished(info, true)
////                    }
//            } else {
//                // Otherwise report an error to the print framework
//                callback?.onLayoutFailed("Page count calculation failed.")
//            }
//        }
//
//        override fun onWrite(
//            pages: Array<out PageRange>?,
//            destination: ParcelFileDescriptor?,
//            cancellationSignal: CancellationSignal?,
//            callback: WriteResultCallback?
//        ) {
//            TODO("Not yet implemented")
//        }
////        private fun computePageCount(printAttributes: PrintAttributes?): Int {
////            var itemsPerPage = 4 // default item count for portrait mode
////
////            val pageSize = printAttributes?.mediaSize
////            if (!pageSize!!.isPortrait) {
////                // Six items per page in landscape orientation
////                itemsPerPage = 6
////            }
////            // Determine number of print items
////            val printItemCount: Int = getPrintItemCount()
////            return Math.ceil((printItemCount / itemsPerPage.toDouble())).toInt()
////        }
//
//    }

    /**
     * 病人预约信息展示
     */
    override fun showAppointment(data: ArrayList<Appointment.AppointmentItem>) {
        if (data.size == 0) {
            appointment_list.visibility = View.GONE
            linearLayout_no_data.visibility = View.VISIBLE
        } else {
            appointment_list.visibility = View.VISIBLE
            linearLayout_no_data.visibility = View.GONE
            val appointmentAdapter = AppointmentAdapter(data, this)
            //签到点击事件
            appointmentAdapter.addChildClickViewIds(R.id.sign_in_bt)
            appointmentAdapter.setOnItemChildClickListener { adapter, view, position ->
                ToastUtils.showSmallToast("点击了\t\t$position")
//                val appointmentItem = adapter.data[position] as Appointment.AppointmentItem
//                mPresenter?.signIn(appointmentItem.id.toString())
                printDocument()
            }
            val linearLayoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
            appointment_list.layoutManager = linearLayoutManager
            appointment_list.addItemDecoration(SpacesItemDecoration(30))
            appointment_list.adapter = appointmentAdapter
        }
    }

    override fun getContext(): Context {
        return this
    }

    /**
     * 左上角返回按钮
     */
    override fun back() {
        ToastUtils.showSmallToast("返回")
        finish()
    }

    /**
     * 预约列表添加间隔
     */
    class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
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