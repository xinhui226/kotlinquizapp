package com.xinhui.quizapp.ui.screen.tabContainer.viewModel

import androidx.lifecycle.viewModelScope
import com.xinhui.quizapp.data.model.User
import com.xinhui.quizapp.data.repo.UserRepo
import com.xinhui.quizapp.ui.screen.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabContainerViewModel @Inject constructor(
    private val userRepo: UserRepo,
): BaseViewModel() {

    private val _user: MutableSharedFlow<User> = MutableSharedFlow()
    val user: SharedFlow<User> = _user

    init {
        viewModelScope.launch {
            _isLoading.emit(true)
            safeApiCall { userRepo.getUser() }?.let {
                _isLoading.emit(false)
                _user.emit(it)
            }
        }
    }

}