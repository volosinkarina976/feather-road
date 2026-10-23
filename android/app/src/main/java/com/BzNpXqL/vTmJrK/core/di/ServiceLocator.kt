package com.BzNpXqL.vTmJrK.core.di

import android.content.Context
import com.BzNpXqL.vTmJrK.data.local.PreferencesStore
import com.BzNpXqL.vTmJrK.data.repository.ProgressRepositoryImpl

object ServiceLocator {

    private var viewModelFactory: AppViewModelFactory? = null

    fun init(context: Context) {
        if (viewModelFactory == null) {
            viewModelFactory = AppViewModelFactory(
                ProgressRepositoryImpl(PreferencesStore(context))
            )
        }
    }

    val factory: AppViewModelFactory
        get() = viewModelFactory ?: throw IllegalStateException("ServiceLocator.init was not called")
}
