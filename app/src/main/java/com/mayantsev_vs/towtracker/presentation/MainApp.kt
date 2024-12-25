package com.mayantsev_vs.towtracker.presentation

import android.app.Application
import com.mayantsev_vs.towtracker.data.db.MainDb

class MainApp : Application() {
    val database by lazy { MainDb.getDatabase(this) }
}