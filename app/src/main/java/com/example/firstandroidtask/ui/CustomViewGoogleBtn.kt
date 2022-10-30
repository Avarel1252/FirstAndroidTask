package com.example.firstandroidtask.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.example.firstandroidtask.utils.Constants
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.firstandroidtask.R
import com.example.firstandroidtask.utils.extensions.dpToPx
import com.example.firstandroidtask.utils.extensions.spToPx
import kotlin.properties.Delegates

class CustomViewGoogleBtn @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    //to calculate and store the position of elements in View
    data class LocationOfElements(
        val textXPos: Float,
        val textYPOs: Float,
        val iconLeftBound: Int,
        val iconTopBound: Int,
        val iconRightBound: Int,
        val iconBottomBound: Int,
    )

    //View custom attr
    private lateinit var attrText: String
    private var attrCornerRadius by Delegates.notNull<Float>()
    private var attrIconSize by Delegates.notNull<Float>()

    //tools for draw
    private val drawableLogo = ResourcesCompat.getDrawable(resources, R.drawable.google_logo, null)
    private val paintButtonBox = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)

    //const measurement
    private val padding = Constants.DEFAULT_INNER_PADDING.dpToPx(context)
    private val iconGap = Constants.DEFAULT_ICON_GAP.dpToPx(context)

    //global obj for save position of elements on View
    private lateinit var locationOfEl: LocationOfElements

    init {
        initializationAttrs(attrs, defStyleAttr, defStyleRes)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val (widthText, heightText) = calculateMeasure()
        setMeasuredDimension(
            resolveSize(widthText, widthMeasureSpec),
            resolveSize(heightText, heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        locationOfEl = calcLocations()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val (textXPos, textYPos, iconLeftBound, iconTopBound, iconRightBound, iconBottomBound) = locationOfEl
        canvas?.let {
            with(canvas) {
                drawRoundRect(
                    0f,
                    0f,
                    width.toFloat(),
                    height.toFloat(),
                    attrCornerRadius,
                    attrCornerRadius,
                    paintButtonBox
                )
                drawText(
                    attrText,
                    textXPos,
                    textYPos,
                    paintText
                )
            }
            drawableLogo?.setBounds(
                iconLeftBound,
                iconTopBound,
                iconRightBound,
                iconBottomBound
            )
            drawableLogo?.draw(it)
        }
    }

    private fun calcLocations(): LocationOfElements {
        val centerXPos = (width - paintText.measureText(attrText)) / 2f
        val centerTextYPos = (height + paintText.textSize - paintText.descent()) / 2f
        return LocationOfElements(
            textXPos = centerXPos + iconGap,
            textYPOs = centerTextYPos,
            iconLeftBound = (centerXPos - attrIconSize).toInt(),
            iconTopBound = ((height - attrIconSize) / 2).toInt(),
            iconRightBound = centerXPos.toInt(),
            iconBottomBound = ((height + attrIconSize) / 2).toInt(),
        )
    }

    private fun calculateMeasure(): Pair<Int, Int> {
        val heightText =
            (paintText.textSize + paddingTop + paddingBottom + padding).toInt()
        val widthText =
            (paintText.measureText(attrText) + paddingStart + paddingEnd + attrIconSize * 2 + padding).toInt()
        if (attrIconSize.isNaN()) {
            attrIconSize = paintText.textSize
        }
        return Pair(widthText, heightText)
    }

    private fun initializationAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomViewGoogleBtn,
            defStyleAttr, defStyleRes
        )
        with(typedArray) {
            //save attrs to paint
            paintText.color =
                getColor(
                    R.styleable.CustomViewGoogleBtn_textColor,
                    Constants.DEFAULT_TEXT_COLOR
                )
            paintText.textSize =
                getDimension(
                    R.styleable.CustomViewGoogleBtn_textSize,
                    Constants.DEFAULT_TEXT_SIZE.spToPx(context)
                )
            paintButtonBox.color =
                getColor(
                    R.styleable.CustomViewGoogleBtn_boxButtonColor,
                    Constants.DEFAULT_BOX_BUTTON_COLOR
                )

            //save attrs values
            attrText = getString(R.styleable.CustomViewGoogleBtn_text) ?: Constants.DEFAULT_TEXT
            attrCornerRadius =
                getDimension(
                    R.styleable.CustomViewGoogleBtn_cornerRadius,
                    Constants.DEFAULT_CORNER_RADIUS
                ).dpToPx(context)
            attrIconSize = getDimension(
                R.styleable.CustomViewGoogleBtn_iconSize,
                Float.NaN
            ).dpToPx(context)
        }
        typedArray.recycle()
    }
}