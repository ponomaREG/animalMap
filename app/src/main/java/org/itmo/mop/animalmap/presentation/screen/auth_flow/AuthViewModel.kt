package org.itmo.mop.animalmap.presentation.screen.auth_flow

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.itmo.mop.animalmap.R
import org.itmo.mop.animalmap.domain.repository.UserRepository
import org.itmo.mop.animalmap.presentation.base.BaseViewModel
import org.itmo.mop.animalmap.presentation.base.Event
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel<AuthState, AuthEvent>() {

    override val initialState: AuthState
        get() = AuthState()

    fun onPhoneChanged(phone: String) {
        updateState {
            copy(
                phone = phone,
                isButtonNextPageEnabled = validatePhone(phone),
            )
        }
    }

    fun onOtpCodeChanged(otpCode: String) {
        updateState {
            copy(
                otpCode = otpCode,
                isButtonNextPageEnabled = validateOtpCode(otpCode),
            )
        }
    }

    fun onLoginChanged(login: String) {
        updateState {
            copy(
                login = login,
                isButtonNextPageEnabled = validateLogin(login)
            )
        }
    }

    fun onEnterLoginScreenShowed() {
        updateState {
            copy(
                isLoading = false,
                isButtonNextPageEnabled = validateLogin(login),
                currentStateFlow = AuthFlowState.Login,
                errorMessage = ""
            )
        }
    }

    fun onEnterOtpCodeScreenShowed() {
        updateState {
            copy(
                isLoading = false,
                isButtonNextPageEnabled = validateOtpCode(otpCode),
                currentStateFlow = AuthFlowState.OtpCode
            )
        }
    }

    fun onEnterPhoneScreenShowed() {
        updateState {
            copy(
                isLoading = false,
                isButtonNextPageEnabled = validatePhone(phone),
                currentStateFlow = AuthFlowState.Phone
            )
        }
    }

    fun onButtonNextPageClicked() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            when (state.value.currentStateFlow) {
                AuthFlowState.Login -> {
                    val isLoginExists = userRepository.checkIfLoginAlreadyExists(state.value.login)
                    if (isLoginExists) {
                        updateState {
                            copy(
                                isLoading = false,
                                errorMessage = "Такой логин уже существует"
                            )
                        }
                    } else {
                        submitEvent(AuthEvent.ShowNextPage(R.id.action_enter_login_to_enter_otp_code))
                    }
                }
                AuthFlowState.OtpCode -> {
                    Log.e("state", state.value.toString())
                    with(state.value) {
                        val authenticateUser = userRepository.authenticateUser(
                            phone = phone,
                            otpCode = otpCode,
                            login = login,
                        )
                        val nextEvent: AuthEvent = if (authenticateUser) {
                            AuthEvent.GoToMainGraph
                        } else {
                            AuthEvent.ShowToast("Что-то произошло не так")
                        }
                        submitEvent(nextEvent)
                    }
                }
                AuthFlowState.Phone -> {
                    val isUserExists = userRepository.checkIfUserAlreadyExists(state.value.phone)
                    if (isUserExists) {
                        submitEvent(AuthEvent.ShowNextPage(R.id.action_enter_phone_to_enter_otp_code))
                    } else {
                        submitEvent(AuthEvent.ShowNextPage(R.id.action_enter_phone_to_enter_login))
                    }
                }
            }
        }
    }

    //TODO: Добавить логику какую-нибудь
    private fun validatePhone(phone: String): Boolean {
        return phone.length == 10
    }

    private fun validateOtpCode(otpCode: String): Boolean {
        return otpCode.isNotEmpty()
    }

    private fun validateLogin(login: String): Boolean {
        return login.isNotEmpty()
    }

}

data class AuthState(
    val login: String = "",
    val otpCode: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isButtonNextPageEnabled: Boolean = false,
    val currentStateFlow: AuthFlowState = AuthFlowState.Phone,
)

sealed class AuthEvent: Event {
    class ShowToast(val message: String): AuthEvent()
    class ShowNextPage(val actionId: Int): AuthEvent()
    object GoToMainGraph: AuthEvent()
}

sealed class AuthFlowState {

    object Phone: AuthFlowState()
    object OtpCode: AuthFlowState()
    object Login: AuthFlowState()
}