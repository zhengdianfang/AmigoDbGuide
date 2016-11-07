package com.zhengdianfang.db.amigodbguide.bean

import java.io.Serializable

/**
 * Created by zheng on 2016/11/4.
 * 数据库链接参数
 */
class ConnectionInfo : Serializable {

    var name = ""

    var host = ""
    var port = ""
    var username = ""
    var password = ""
}
