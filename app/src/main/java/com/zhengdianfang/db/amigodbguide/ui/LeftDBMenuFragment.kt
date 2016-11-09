package com.zhengdianfang.db.amigodbguide.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.zhengdianfang.db.amigodbguide.R
import com.zhengdianfang.db.amigodbguide.bean.Database
import com.zhengdianfang.db.amigodbguide.bean.Table
import com.zhengdianfang.db.amigodbguide.ui.adapter.DBSpinnerAdapter
import com.zhengdianfang.db.amigodbguide.ui.adapter.TableAdapter
import com.zhengdianfang.db.amigodbguide.util.DBHelper
import kotlinx.android.synthetic.main.left_db_menu_layout.*

/**
 * Created by zheng on 2016/11/8.
 */
class LeftDBMenuFragment : Fragment(),  AdapterView.OnItemSelectedListener {
    val databases = mutableListOf<Database>()
    val tables = mutableListOf<Table>()
    val dbSpinnerAdapter by lazy { DBSpinnerAdapter(databases) }
    val tableAdapter by lazy { TableAdapter(tables) }
    var selectedDatabase:Database? = null
    var  onSelectedTableListener: (Table)->Unit = {}

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.left_db_menu_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dbnameSpinner.adapter = dbSpinnerAdapter
        dbnameSpinner.dropDownVerticalOffset = 32
        dbnameSpinner.onItemSelectedListener = this

        tableRecyclerView.adapter = tableAdapter
        tableAdapter.onClickItem = {table->onSelectedTableListener(table)}
        getDatabases()
    }

    private fun getDatabases(){
        DBHelper.instance.getDatabases()?.subscribe{
            datas->
                if (datas != null){
                    databases.clear()
                    databases.addAll(datas)
                    dbSpinnerAdapter.notifyDataSetChanged()
                }
        }
    }

    private fun getTables(){
        DBHelper.instance.getAllTableNames()?.subscribe{
            datas->
            if (datas != null){
                tables.clear()
                tables.addAll(datas)
                tableAdapter.notifyDataSetChanged()
                loadingView.visibility = View.GONE
                tableRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position > 0){
            selectedDatabase = dbSpinnerAdapter.getItem(position)
            loadingView.visibility = View.VISIBLE
            tableRecyclerView.visibility = View.GONE
            DBHelper.instance.useDatabase(selectedDatabase!!.name)?.subscribe {res->
                if (res){
                    getTables()
                }else{
                    Toast.makeText(context, "数据库连接失败，请重试！", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


}