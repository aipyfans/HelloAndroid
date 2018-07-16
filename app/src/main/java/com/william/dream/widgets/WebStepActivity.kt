package com.william.dream.widgets

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.william.dream.R
import kotlinx.android.synthetic.main.activity_web_step.*

class WebStepActivity : AppCompatActivity(), StepCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_step)
    }


    override fun onStep(step: Step) {
        StepManager.end()
        tvLog.text = "当这一步操作超时的时候会进行回调 \n 应该先调用end()方法结束操作步骤的流程 \n 而后结合业务进行处理 \n $step"
        StepManager.end()
    }


    fun onClick(view: View) {
        when (view.id) {
            R.id.btnSuccess -> {
                autoExeOk()
            }
            R.id.btnFailed -> {
                autoExeNo()
            }
        }
    }


    /**
     * 模拟CIB自动执行成功的情况
     */
    private fun autoExeOk() {
        val handler = Handler()
        // 模拟执行每一步操作

        StepManager.start(this)
        for (times in 1..3) {
            handler.postDelayed({ StepManager.next("desc$times") }, times * 1000L)
            if (times == 3) {
                handler.postDelayed({
                    StepManager.end()
                    tvLog.text = "所有的操作步骤顺利执行完成"
                    // 模拟完成任务关闭当前页面
                    // finish()
                }, (times + 1) * 1000L)

            }
        }

    }


    /**
     * 模拟CIB自动执行成功的情况
     */
    private fun autoExeNo() {
        val handler = Handler()
        // 模拟执行每一步操作
        StepManager.start(this)
        for (times in 1..3) {
            if (times == 2) {
                handler.postDelayed({ StepManager.next("desc$times") }, times * 6000L)
                break
            }
            handler.postDelayed({ StepManager.next("desc$times") }, times * 1000L)
        }
    }

}
