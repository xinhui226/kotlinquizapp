package com.xinhui.quizapp.ui.screen.base.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel:ViewModel() {
    protected val _error= MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error
    protected val _success= MutableSharedFlow<String>()
    val success : SharedFlow<String> = _success
    protected val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit> = _finish
    protected val _isLoading= MutableSharedFlow<Boolean>()
    val isLoading : SharedFlow<Boolean> = _isLoading

    suspend fun <T>safeApiCall(callback: suspend ()->T?):T? {
        return try {
            callback()
        }catch (e:Exception){
            e.printStackTrace()
            _error.emit(e.message ?: "Something went wrong")
            _isLoading.emit(false)
            null
        }
    }
}