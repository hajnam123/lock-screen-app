package com.example.lockscreenapp.ultis.drawing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.lockscreenapp.R
import kotlin.math.abs


class CanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var path: Path
    private var paint: Paint
    private val paths = ArrayList<DataPath>()
    private val undonePaths = ArrayList<DataPath>()
    private var onDraw = {}

    private var currentX: Float = 0f
    private var currentY: Float = 0f
    private val fillPaint = Paint().apply {
        color = Color.WHITE
    }
    private val strokePaint = Paint().apply {
        color = Color.BLACK
    }
    private var drawCircle = false
    private var sizeCircle = 4f

    inner class DataPath(val path: Path, val paint: Paint)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        for (p in paths) {
            canvas.drawPath(p.path, p.paint)
        }
        if (drawCircle) {
            canvas.drawCircle(currentX, currentY, sizeCircle / 2 + 16, strokePaint)
            canvas.drawCircle(currentX, currentY, sizeCircle / 2 + 15, fillPaint)
        }
    }

    private var mX = 0f
    private var mY = 0f

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CanvasView,
            0, 0
        )
        path = Path()
        paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 9f
        paint.alpha = 255
        paint.color = a.getColor(R.styleable.CanvasView_paintColor, 0)
        paint.strokeWidth = a.getColor(R.styleable.CanvasView_paintStroke, 0).toFloat()
    }

    fun setSizeCircleTouchEraser(size: Float) {
        sizeCircle = size + 30f
    }
    fun setSizeCircleTouch(size: Float) {
        sizeCircle = size + 10f
    }

    fun getSizePath(): Int = paths.size
    fun getSizeUndonePath(): Int = undonePaths.size
    fun setOnDraw(draw: () -> Unit) {
        this.onDraw = draw
    }

    /**
     * Touch Down event handler
     */
    private fun touchStart(x: Float, y: Float) {
        val newPaint = Paint().apply {
            color = paint.color
            strokeWidth = paint.strokeWidth
            style = paint.style
            strokeJoin = paint.strokeJoin
            strokeCap = paint.strokeCap
            isAntiAlias = paint.isAntiAlias
            isDither = paint.isDither
        }
        path = Path()
        paths.add(DataPath(path, newPaint))
        /**Avoid Out Memory*/
        if (paths.size > 5000) {
            val removeCount = 5
            paths.subList(0, removeCount).clear()
        }
        undonePaths.clear()
        path.reset()
        path.moveTo(x, y)
        mX = x
        mY = y
        currentX = x
        currentY = y
        drawCircle = true
    }

    /**
     * Touch Move event handler
     **/
    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - mX)
        val dy = abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
        currentX = mX
        currentY = mY
    }

    /**
     * Touch Up event handler
     **/
    private fun touchUp() {
        path.lineTo(mX, mY)
        onDraw.invoke()
        drawCircle = false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    @Suppress("DEPRECATION")
    fun getBitmap(): Bitmap {
        this.isDrawingCacheEnabled = true
        this.buildDrawingCache()
        val bmp = Bitmap.createBitmap(this.drawingCache)
        this.isDrawingCacheEnabled = false
        return bmp
    }

    /**
     * Clear Canvas View and invalidate */
    fun clear() {
        invalidate()
        System.gc()
    }

    fun onClickUndo() {
        if (paths.size > 0) {
            undonePaths.add(paths.removeAt(paths.size - 1))
            invalidate()
        }
    }

    fun onClickRedo() {
        if (undonePaths.size > 0) {
            paths.add(undonePaths.removeAt(undonePaths.size - 1))
            invalidate()
        }
    }

    /**
     * change path color here*/
    fun paintColor(color: Int) {
        paint.color = color
    }

    fun paintAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    /**
     * Stroke size*/
    fun paintStroke(width: Int) {
        paint.strokeWidth = width.toFloat()
        sizeCircle = width.toFloat()
    }

    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }
}
