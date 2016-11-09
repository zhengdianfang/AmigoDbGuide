package com.zhengdianfang.db.amigodbguide.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.zhengdianfang.db.amigodbguide.R
import com.zhengdianfang.db.amigodbguide.bean.TableProperty

/**
 * Created by zheng on 2016/11/9.
 */
class RowView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : ViewGroup(context, attrs, defStyleAttr) {


    constructor(context: Context? ) : this(context, null, 0) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var w= 0
        (0..childCount - 1)
                .map { getChildAt(it) }
                .forEach {
                    measureChild(it, widthMeasureSpec, heightMeasureSpec)
                    w += it.measuredWidth + it.paddingLeft + it.paddingRight
                }


        setMeasuredDimension(w, heightSize)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = l
        for (i in 0.. childCount-1){
            val childAt = getChildAt(i)
            childAt.layout(left, t, left + childAt.measuredWidth, t + childAt.measuredHeight)
            left += childAt.measuredWidth
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    fun addTableFieldRowViews(fields:MutableList<TableProperty>){
        removeAllViews()
        fields.forEach {
            val textView = TextView(context)
            textView.text = it.field
            textView.setTextColor(Color.WHITE)
            val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            textView.layoutParams =  layoutParams
            textView.setPadding(16, 16, 16, 16)
            textView.setLines(1)
            textView.gravity = Gravity.CENTER
            textView.setBackgroundResource(R.drawable.column_background)
            addView(textView)
        }

    }


    fun addTableValueRowViews(headerView:RowView, values:MutableList<String>){
        removeAllViews()
        values.forEachIndexed { i, s ->
            val childAt = headerView.getChildAt(i)
            val textView = TextView(context)
            textView.text = s
            textView.setTextColor(Color.BLACK)
            val layoutParams = ViewGroup.MarginLayoutParams(childAt.measuredWidth, childAt.measuredHeight)
            textView.layoutParams =  layoutParams
            textView.gravity = Gravity.CENTER
            textView.setPadding(16, 16, 16, 16)
            textView.setLines(1)
            textView.maxEms  = 10
            textView.setBackgroundResource(R.drawable.value_background)
            addView(textView)
        }

    }
}