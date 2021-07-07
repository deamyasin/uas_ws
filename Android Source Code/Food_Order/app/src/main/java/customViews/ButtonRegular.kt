package customViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

class ButtonRegular : androidx.appcompat.widget.AppCompatButton {

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
        val tf = Typeface.createFromAsset(context.assets, "fonts/NunitoSans-Regular.ttf")
        typeface = tf
    }

}
