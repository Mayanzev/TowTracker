package com.mayantsev_vs.towtracker.data.utils

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mayantsev_vs.towtracker.R

fun Fragment.openFragment(f: Fragment) {
    (activity as AppCompatActivity).supportFragmentManager
        .beginTransaction()
        .replace(R.id.fragments_container, f).commit()
}

fun AppCompatActivity.openFragment(f: Fragment) {
    if (supportFragmentManager.fragments.isNotEmpty()) {
        if (supportFragmentManager.fragments[0].javaClass == f.javaClass) return
    }
    supportFragmentManager
        .beginTransaction()
        .replace(R.id.fragments_container, f).commit()
}

fun Fragment.showToast(s: String) {
    Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showToast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}

fun Fragment.checkPermission(p: String): Boolean {
    return when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(activity as AppCompatActivity, p) -> true
        else -> false
    }
}