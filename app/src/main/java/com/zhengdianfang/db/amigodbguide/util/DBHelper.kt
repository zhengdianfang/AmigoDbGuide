package com.zhengdianfang.db.amigodbguide.util


import com.zhengdianfang.db.amigodbguide.bean.ConnectionInfo
import com.zhengdianfang.db.amigodbguide.bean.Database
import com.zhengdianfang.db.amigodbguide.bean.Table
import com.zhengdianfang.db.amigodbguide.bean.TableProperty
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.sql.Connection
import java.sql.DriverManager


/**
 * 实际上这里的Connection是以库为单位的。

 * @author zheng
 */
class DBHelper(val connectionInfo: ConnectionInfo) {

    var conn: Connection? = null
        get() {
            if (field == null || field!!.isClosed){
                Class.forName("com.mysql.jdbc.Driver")//指定连接类型
                DriverManager.setLoginTimeout(10*1000)
                field = DriverManager.getConnection("jdbc:mysql://${connectionInfo.host}:${connectionInfo.port}/", connectionInfo.username, connectionInfo.password)//获取连接
            }
            return field
        }

    fun connnection(): Observable<Boolean> {
        return Observable.create<Boolean> { sub->
                Class.forName("com.mysql.jdbc.Driver")//指定连接类型
                DriverManager.setLoginTimeout(10*1000)
                conn = DriverManager.getConnection("jdbc:mysql://${connectionInfo.host}:${connectionInfo.port}/", connectionInfo.username, connectionInfo.password)//获取连接
                sub.onNext(conn != null && conn!!.isClosed.not())
                sub.onComplete()
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())


    }

    /**
     * get all databases name
     */
    fun getDatabases():MutableList<Database>{
        val prepareStatement = conn!!.prepareStatement("SHOW DATABASES")
        val executeQuery = prepareStatement!!.executeQuery()
        val databasesArray = mutableListOf<Database>()
        while (executeQuery.next()){
            val database = Database()
            database.name = executeQuery.getString(1)
            databasesArray.add(database)
        }
        return databasesArray
    }

    /**
     * select one database
     */
    fun useDatabase(tableName:String){
        val prepareStatement = conn!!.prepareStatement("USE $tableName")
        prepareStatement!!.executeQuery()
    }

    /**
     * get all tables name
      */
    fun getAllTableNames():MutableList<Table>{
        val prepareStatement = conn!!.prepareStatement("SHOW TABLES")
        val executeQuery = prepareStatement!!.executeQuery()
        val tablesArray = mutableListOf<Table>()
        while (executeQuery.next()){
            val table = Table()
            table.name = executeQuery.getString(1)
            tablesArray.add(table)
        }
        return tablesArray
    }

    /**
     * get table columns name
     */
    fun getTableColumnNames(tableName: String):MutableList<TableProperty>{
        val prepareStatement = conn!!.prepareStatement("SHOW COLUMNS FROM $tableName")
        val executeQuery = prepareStatement!!.executeQuery()
        val columnsArray = mutableListOf<TableProperty>()
        while (executeQuery.next()){
            val tableProperty = TableProperty()
            tableProperty.field = executeQuery.getString(1) ?: ""
            tableProperty.type = executeQuery.getString(2) ?: ""
            tableProperty._null = executeQuery.getString(3) ?: ""
            tableProperty.key = executeQuery.getString(4) ?: ""
            tableProperty._default = executeQuery.getString(5) ?: ""
            tableProperty.extra = executeQuery.getString(6) ?: ""
            columnsArray.add(tableProperty)
        }
        return columnsArray
    }

    fun getTableDatas(tableName: String):MutableList<Map<TableProperty, String>>{
        val tableColumnNames = getTableColumnNames(tableName)
        val prepareStatement = conn!!.prepareStatement("SELECT * FROM $tableName")
        val executeQuery = prepareStatement!!.executeQuery()
        val datas = mutableListOf<Map<TableProperty, String>>()
        while (executeQuery.next()){
            val data = mutableMapOf<TableProperty, String>()
            tableColumnNames.forEach {
                data.put(it, executeQuery.getString(executeQuery.findColumn(it.field)) ?: "")
            }
            datas.add(data)
        }
        return datas
    }

    fun executeSql(sql:String){
        val pst = conn!!.prepareStatement(sql)//准备执行语句
    }


}

