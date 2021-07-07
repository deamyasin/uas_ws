package customViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

class TextViewBold : androidx.appcompat.widget.AppCompatTextView {

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
        val tf = Typeface.createFromAsset(context.assets, "fonts/NunitoSans-Bold.ttf")
        typeface = tf
    }

}
