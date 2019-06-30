package com.arnab.notepad

import android.app.Application
import com.facebook.stetho.Stetho

class NotepadApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this);
    }
}