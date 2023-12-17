package com.polstat.luthfiani.state

class NormalState(val value: String? = null) :
    TextFieldState(validator = ::isValid) {
    init {
        value?.let {
            text = it
        }
    }
}

private fun isValid(text: String): Boolean {
    if (text.isNullOrEmpty()) {
        return false
    }
    return true
}

val NormalStateSaver = textFieldStateSaver(NormalState())