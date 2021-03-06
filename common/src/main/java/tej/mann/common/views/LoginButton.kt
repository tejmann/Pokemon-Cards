package tej.mann.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.layout_login_button.view.*
import tej.mann.common.R

class LoginButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        const val ENABLED_ALPHA = 1.0f
        const val DISABLED_ALPHA = 0.25f
        const val ALPHA_DURATION_MS = 200L
    }

    enum class LoginButtonState {
        ENABLED,
        LOADING,
        DISABLED

    }

    var buttonText: CharSequence? = null
        set(value) {
            field = value
            updateText(value)
        }

    var buttonState: LoginButtonState =
        LoginButtonState.ENABLED
        set(value) {
            field = value
            updateButtonState(value)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_login_button, this, true)
        var givenText: String? = null
        var givenState =
            LoginButtonState.ENABLED

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.LoginButton, 0, 0
            )
            try {
                val state = typedArray.getInt(
                    R.styleable.LoginButton_loginButtonState,
                    LoginButtonState.ENABLED.ordinal
                )
                givenState = if (state in (0 until LoginButtonState.values().size)) {
                    LoginButtonState.values()[state]
                } else {
                    LoginButtonState.ENABLED
                }

                givenText = typedArray.getString(R.styleable.LoginButton_loginButtonText)

            } finally {
                typedArray.recycle()
            }
        }
        buttonText = givenText
        buttonState = givenState
    }

    private fun updateButtonState(buttonState: LoginButtonState) = when (buttonState) {
        LoginButtonState.ENABLED -> setEnableState(enabled = true)
        LoginButtonState.LOADING -> setLoadingState()
        LoginButtonState.DISABLED -> setEnableState(enabled = false)
    }

    private fun setEnableState(enabled: Boolean) {
        val shouldAnimate = isEnabled != enabled
        val finalAlpha = if (enabled) ENABLED_ALPHA else DISABLED_ALPHA

        if (shouldAnimate) {
            animate()
                .alpha(finalAlpha)
                .duration =
                ALPHA_DURATION_MS
        } else {
            alpha = finalAlpha
        }

        login_button_spinner.visibility = View.GONE
        isEnabled = enabled
        login_button_text.visibility = View.VISIBLE
    }

    private fun setLoadingState() {
        login_button_spinner.visibility = View.VISIBLE
        isEnabled = false
        login_button_text.isEnabled = false
        login_button_text.visibility = View.INVISIBLE
    }

    private fun updateText(buttonText: CharSequence?) {
        login_button_text.setTextSafe(buttonText)
    }
}

