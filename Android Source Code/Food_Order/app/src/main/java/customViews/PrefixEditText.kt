package customViews

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.*

/**
 * Created on 26-11-2019.
 */
class PrefixEditText : AppCompatEditText {
    var mOriginalLeftPadding = -1f
    var textWidth = 0f

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    constructor(
        context: Context?, attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculatePrefix()
    }

    private fun calculatePrefix() {
        if (mOriginalLeftPadding == -1f) {
            val prefix = tag as String
            val widths = FloatArray(prefix.length)
            paint.getTextWidths(prefix, widths)
            textWidth = 0f
            for (w in widths) {
                textWidth += w
            }
            if (Locale.getDefault().language.equals("ar", ignoreCase = true)) {
                mOriginalLeftPadding = compoundPaddingRight.toFloat()
                setPadding(
                    paddingLeft,
                    (textWidth + mOriginalLeftPadding).toInt(), paddingTop,
                    paddingBottom
                )
            } else {
                mOriginalLeftPadding = compoundPaddingLeft.toFloat()
                setPadding(
                    (textWidth + mOriginalLeftPadding).toInt(),
                    paddingRight, paddingTop,
                    paddingBottom
                )
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val prefix = tag as String
        if (Locale.getDefault().language.equals("ar", ignoreCase = true)) {
            canvas.drawText(
                prefix, width - textWidth,
                getLineBounds(0, null).toFloat(), paint
            )
        } else {
            canvas.drawText(
                prefix, mOriginalLeftPadding,
                getLineBounds(0, null).toFloat(), paint
            )
        }
    }

}