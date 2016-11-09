package com.zhengdianfang.db.amigodbguide.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.zhengdianfang.db.amigodbguide.R
import com.zhengdianfang.db.amigodbguide.bean.Database

/**
 * Created by zheng on 2016/11/8.
 */
class DBSpinnerAdapter(val databases:MutableList<Database>) : BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var textView:TextView? = convertView as? TextView
        if (textView == null){
           textView = LayoutInflater.from(parent?.context).inflate(R.layout.db_item_layout, parent, false) as TextView
        }
        if (position == 0){
            textView.text = "请选择数据库"
        }else{
            textView.text = databases[position -1].name
        }
        return textView
    }

    override fun getItem(position: Int): Database {
        return databases[position - 1]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return databases.count() + 1
    }


}