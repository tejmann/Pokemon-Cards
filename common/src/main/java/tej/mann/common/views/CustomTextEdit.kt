package tej.mann.common.views

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * https://issuetracker.google.com/issues/37051832
 * https://gist.github.com/siyamed/e7276009121d775c0d74ea7d6b01fc25
 *
 * There is a known issue in google issue tracker where the input edit text does not reveal the
 * error that is being covered by the keyboard, this is a workaround as per google I/O'19
 */
class CustomTextInputEditText : TextInputEditText {
    // Note: Cannot use JvmOverloads for this class as of Kotlin 1.3.50
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus()
            return true
        }
        return super.onKeyPreIme(keyCode, event)
    }

    private val parentRect = Rect()

    override fun getFocusedRect(rect: Rect?) {
        super.getFocusedRect(rect)
        rect?.let {
            getParentView().getFocusedRect(parentRect)
            rect.bottom = parentRect.bottom
        }
    }

    override fun getGlobalVisibleRect(rect: Rect?, globalOffset: Point?): Boolean {
        val result = super.getGlobalVisibleRect(rect, globalOffset)
        rect?.let {
            getParentView().getGlobalVisibleRect(parentRect, globalOffset)
            rect.bottom = parentRect.bottom
        }
        return result
    }

    override fun requestRectangleOnScreen(rect: Rect?): Boolean {
        val result = super.requestRectangleOnScreen(rect)
        val parent = getParentView()
        parentRect.set(0, parent.height - RECTANGLE_HEIGHT, parent.right, parent.height)
        parent.requestRectangleOnScreen(parentRect, true)
        return result
    }

    private fun getParentView(): View {
        var parentView = parent

        while (parentView !is TextInputLayout && parentView != null) {
            parentView = parentView.parent
        }

        return (parentView as? View) ?: this
    }
}

private const val RECTANGLE_HEIGHT = 10