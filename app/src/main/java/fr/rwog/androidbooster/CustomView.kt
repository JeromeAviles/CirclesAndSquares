package fr.rwog.androidbooster

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class CustomView : View, View.OnTouchListener {
    val mPaint = Paint()
    var tabCircles: ArrayList<MagicCircle> = arrayListOf()
    var tabSquares: ArrayList<MagicSquare> = arrayListOf()

    companion object {
        val DELTA = 8
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y
            tabCircles.add(MagicCircle(width.toFloat(), height.toFloat()))
            tabSquares.add(MagicSquare(width.toFloat(), height.toFloat()))
            for (circle in tabCircles) circle.gather(x, y)
            for (square in tabSquares) square.gather(x, y)

        }
        if (event?.action == MotionEvent.ACTION_UP) {
            for (square in tabSquares) square.escape()
            for (circle in tabCircles) circle.escape()
        }
        return true
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        super.setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (mCircle in tabCircles) {
            with(mCircle) {
                mPaint.color = mColor
                move()
                canvas?.drawCircle(cx, cy, size.toFloat(), mPaint)
                invalidate()
            }
        }

        for (mSquare in tabSquares) {
            with(mSquare) {
                mPaint.color = mColor
                move()
                canvas?.drawRect(cl, ct, cr, cb, mPaint)
                invalidate()
            }
        }

        var textPainter = Paint()
        textPainter.typeface = Typeface.MONOSPACE
        textPainter.textSize = 35F
        textPainter.color = Color.BLACK
        canvas?.drawText("Nombre d'éléments : "+(MagicSquare.NB_SQUARES+MagicCircle.NB_CIRCLES), width /3.toFloat(), height / 2.toFloat(), textPainter)


    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        tabCircles.add(MagicCircle(width.toFloat(), height.toFloat()))
        tabSquares.add(MagicSquare(width.toFloat(), height.toFloat()))
    }

}