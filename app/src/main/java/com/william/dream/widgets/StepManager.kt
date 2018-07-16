package com.william.dream.widgets

import android.os.Handler

object StepManager {

    private const val delayMillis = 4000L
    private var stepNo = 0

    private var currentStep: Step? = null
    private var stepCallback: StepCallback? = null

    private val handler: Handler = Handler()
    private var stepRunnable: Runnable? = null


    fun start(callback: StepCallback?) {
        stepNo = 0
        currentStep = null
        stepRunnable = null
        stepCallback = callback
    }


    fun next(desc: String) {
        val stepNoTemp = ++stepNo
        val step = Step.newInstance(stepNoTemp, desc)
        if (null == currentStep) {
            currentStep = step
        } else {
            step.previousStep = currentStep
            currentStep = step
        }

        // 取消前一个任务,同时开启超时任务
        stepRunnable?.let { handler.removeCallbacks(it) }
        stepRunnable = StepRunnable(stepNoTemp)
        handler.postDelayed(stepRunnable, delayMillis)
    }

    fun end() {
        stepRunnable?.let { handler.removeCallbacksAndMessages(null) }
        start(null)
    }


    fun check(no: Int) {
        currentStep?.let {
            if (no == it.no) stepCallback?.onStep(it)
        }
    }


}