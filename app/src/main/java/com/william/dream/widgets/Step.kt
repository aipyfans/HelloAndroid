package com.william.dream.widgets

data class Step(val no: Int, val desc: String) {

    var previousStep: Step? = null

    companion object {

        fun newInstance(no: Int, desc: String): Step {
            return Step(no, desc)
        }

    }

    override fun toString(): String {
        return "Step(no=$no, desc='$desc', previousStep=${previousStep.toString()})"
    }

}