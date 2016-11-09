package com.zhengdianfang.db.amigodbguide.ui.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.zhengdianfang.db.amigodbguide.R
import com.zhengdianfang.db.amigodbguide.bean.TableProperty
import com.zhengdianfang.db.amigodbguide.ui.RowView
import kotlin.properties.Delegates

/**
 * Created by zheng on 2016/11/9.
 */
class ValuleAdapter(val datas:MutableList<Map<TableProperty, String>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var headerView:RowView by Delegates.notNull<RowView>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.table_value_item_layout, parent, false)) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val rowView = holder?.itemView as ViewGroup
        val item = datas[position]
        item.values.forEachIndexed { i, s ->
            val childAt = headerView.getChildAt(i)
            val textView = TextView(holder?.itemView?.context)
            textView.text = s
            textView.setTextColor(Color.BLACK)
            val layoutParams = ViewGroup.MarginLayoutParams(childAt.measuredWidth, childAt.measuredHeight)
            textView.layoutParams =  layoutParams
            textView.gravity = Gravity.CENTER
            textView.setPadding(16, 16, 16, 16)
            textView.setLines(1)
            textView.maxEms  = 10
            textView.setBackgroundResource(R.drawable.value_background)
            rowView.addView(textView)
        }


    }

    override fun getItemCount(): Int {
       return datas.count()
    }
}