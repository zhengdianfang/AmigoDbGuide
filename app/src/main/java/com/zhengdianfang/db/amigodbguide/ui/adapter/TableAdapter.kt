package com.zhengdianfang.db.amigodbguide.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.zhengdianfang.db.amigodbguide.R
import com.zhengdianfang.db.amigodbguide.bean.Table

/**
 * Created by zheng on 2016/11/8.
 */
class TableAdapter(val tables:MutableList<Table>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClickItem:(Table)->Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val tableNameView = holder?.itemView?.findViewById(R.id.tableNameView) as TextView
        val table = tables[position]
        tableNameView?.text = table.name
        holder?.itemView?.setOnClickListener {
            onClickItem(table)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.table_item_layout, parent, false)){}
    }

    override fun getItemCount(): Int {
       return tables.count()
    }
}