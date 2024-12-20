package com.mayantsev_vs.towtracker

import android.app.Application
import com.mayantsev_vs.towtracker.db.MainDb

class MainApp : Application {
    val database by lazy { MainDb.getDatabase(this) }
}