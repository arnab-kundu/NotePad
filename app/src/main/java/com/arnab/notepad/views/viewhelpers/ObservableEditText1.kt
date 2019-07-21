package com.arnab.notepad.views.viewhelpers

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class ObservableEditText1 : BaseObservable() {

    @get:Bindable
    var text = ""
}
