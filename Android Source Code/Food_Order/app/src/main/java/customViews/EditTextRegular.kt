package customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.foodorder.R
import java.util.*

class EditTextRegular : androidx.appcompat.widget.AppCompatEditText {

    internal var mOriginalLeftPadding = -1f
    var textWidth = 0f
    var contexts: Context? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        contexts = context
        val tf = Typeface.createFromAsset(context.assets, "fonts/NunitoSans-Regular.ttf")
        typeface = tf
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (tag != null) {
            calculatePrefix()
        }
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
        if (tag != null) {
            var textPaint: TextPaint = paint
            if (contexts != null) {
                textPaint.color = ContextCompat.getColor(contexts!!, R.color.colorGreenLight)
            }
            val prefix = tag as String
            if (Locale.getDefault().language.equals("ar", ignoreCase = true)) {
                canvas.drawText(
                    prefix, width - textWidth,
                    getLineBounds(0, null).toFloat(), textPaint
                )
            } else {
                canvas.drawText(
                    prefix, mOriginalLeftPadding,
                    getLineBounds(0, null).toFloat(), textPaint
                )
            }
        }
    }

}
