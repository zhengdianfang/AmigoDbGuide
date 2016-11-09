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
import kotlin.properties.Delegates


/**
 * 实际上这里的Connection是以库为单位的。

 * @author zheng
 */
class DBHelper() {

    companion object{
        val instance:DBHelper by lazy { DBHelper() }
    }

    var connectionInfo: ConnectionInfo by Delegates.notNull<ConnectionInfo>()

    var conn: Connection? = null
        get() {
            if (field == null || field!!.isClosed){
                Class.forName("com.mysql.jdbc.Driver")//指定连接类型
                DriverManager.setLoginTimeout(10*1000)
                field = DriverManager.getConnection("jdbc:mysql://${connectionInfo.host}:${connectionInfo.port}/", connectionInfo.username, connectionInfo.password)//获取连接
            }
            return field
        }

    fun connnection(connectionInfo: ConnectionInfo): Observable<Boolean> {
        this.connectionInfo = connectionInfo
        return Observable.create<Boolean> { sub->
                sub.onNext(conn != null && conn!!.isClosed.not())
                sub.onComplete()
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())


    }

    /**
     * get all databases name
     */
    fun getDatabases(): Observable<MutableList<Database>>? {
        return Observable.create<MutableList<Database>> { sub->
            val prepareStatement = conn!!.prepareStatement("SHOW DATABASES")
            val executeQuery = prepareStatement!!.executeQuery()
            val databasesArray = mutableListOf<Database>()
            while (executeQuery.next()){
                val database = Database()
                database.name = executeQuery.getString(1)
                databasesArray.add(database)
            }
            sub.onNext(databasesArray)
            sub.onComplete()
        }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * select one database
     */
    fun useDatabase(tableName:String): Observable<Boolean>? {
        return Observable.create<Boolean> {sub->
            val prepareStatement = conn!!.prepareStatement("USE $tableName")
            val executeQuery = prepareStatement!!.executeQuery()
            sub.onNext(executeQuery != null)
            sub.onComplete()
        }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

    }

    /**
     * get all tables name
      */
    fun getAllTableNames(): Observable<MutableList<Table>>? {
        return Observable.create<MutableList<Table>> {sub->
            val prepareStatement = conn!!.prepareStatement("SHOW TABLES")
            val executeQuery = prepareStatement!!.executeQuery()
            val tablesArray = mutableListOf<Table>()
            while (executeQuery.next()){
                val table = Table()
                table.name = executeQuery.getString(1)
                tablesArray.add(table)
            }
            sub.onNext(tablesArray)
            sub.onComplete()
        } .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * get table columns name
     */
    fun getTableColumnNames(tableName: String): Observable<MutableList<TableProperty>>? {
        return Observable.create<MutableList<TableProperty>> { sub->
            val columnsArray = getTableColunsNames(tableName)
            sub.onNext(columnsArray)
            sub.onComplete()
        }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getTableColunsNames(tableName: String): MutableList<TableProperty> {
        val prepareStatement = conn!!.prepareStatement("SHOW COLUMNS FROM $tableName")
        val executeQuery = prepareStatement!!.executeQuery()
        val columnsArray = mutableListOf<TableProperty>()
        while (executeQuery.next()) {
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

    fun getTableDatas(tableName: String): Observable<MutableList<Map<TableProperty, String>>>? {
        return Observable.create<MutableList<Map<TableProperty, String>>> { sub->
            val tableColumnNames = getTableColunsNames(tableName)
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
            sub.onNext(datas)
            sub.onComplete()
        }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun executeSql(sql:String){
        val pst = conn!!.prepareStatement(sql)//准备执行语句
    }

    fun destory() {
        if (conn != null && conn!!.isClosed.not()){
            conn!!.close()
            conn = null
        }
    }


}

