package com.zhengdianfang.db.amigodbguide.bean

import java.io.Serializable

/**
 * Created by zheng on 2016/11/4.
 */

class TableProperty : Serializable {

    var field = ""
    var type = ""
    var _null = ""
    var key = ""
    var _default = ""
    var extra = ""
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as TableProperty

        if (field != other.field) return false
        if (type != other.type) return false
        if (_null != other._null) return false
        if (key != other.key) return false
        if (_default != other._default) return false
        if (extra != other.extra) return false

        return true
    }

    override fun hashCode(): Int {
        var result = field.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + _null.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + _default.hashCode()
        result = 31 * result + extra.hashCode()
        return result
    }


}
