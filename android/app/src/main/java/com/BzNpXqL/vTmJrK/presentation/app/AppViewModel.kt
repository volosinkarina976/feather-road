package com.BzNpXqL.vTmJrK.presentation.app

import androidx.lifecycle.ViewModel
import com.BzNpXqL.vTmJrK.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current != Screen.Splash) backStack.add(current)
        _screen.value = target
    }

    fun replaceWith(target: Screen) {
        _screen.value = target
    }

    fun backToMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun back() {
        val previous = backStack.removeLastOrNull()
        _screen.value = previous ?: Screen.Menu
    }
}
