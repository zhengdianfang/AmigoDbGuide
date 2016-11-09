package com.zhengdianfang.db.amigodbguide.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhengdianfang.db.amigodbguide.R
import com.zhengdianfang.db.amigodbguide.bean.Table
import com.zhengdianfang.db.amigodbguide.bean.TableProperty
import com.zhengdianfang.db.amigodbguide.ui.adapter.ValuleAdapter
import com.zhengdianfang.db.amigodbguide.util.DBHelper
import kotlinx.android.synthetic.main.content_main.*

/**
 * Created by zheng on 2016/11/9.
 */
class ResultDatasFragment : Fragment() {

    val values = mutableListOf<Map<TableProperty, String>>()
    val valueAdapter by lazy { ValuleAdapter(values) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun changeTable(table:Table){
        values.clear()
        valueAdapter.notifyDataSetChanged()
        DBHelper.instance.getTableColumnNames(table.name)?.subscribe { datas->
            if (null != datas){
                headerView.addTableFieldRowViews(datas)
                valueAdapter.headerView = headerView
                datasRecyclerView.adapter = valueAdapter


                DBHelper.instance.getTableDatas(table.name)?.subscribe { datas ->
                    if (null != datas) {
                        values.clear()
                        values.addAll(datas)
                        valueAdapter.notifyDataSetChanged()
                    }
                }

            }
        }

    }
}