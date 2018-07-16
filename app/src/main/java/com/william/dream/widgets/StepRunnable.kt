package com.william.dream.widgets

data class StepRunnable(val no: Int) : Runnable {

    override fun run() {
        StepManager.check(this.no)
    }

}