package com.arnab.notepad.views.viewhelpers;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ObservableEditText extends BaseObservable {

    private String text = "";

    @Bindable
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
