package com.zhengdianfang.db.amigodbguide.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.zhengdianfang.db.amigodbguide.MainActivity
import com.zhengdianfang.db.amigodbguide.R
import com.zhengdianfang.db.amigodbguide.bean.ConnectionInfo
import com.zhengdianfang.db.amigodbguide.util.DBHelper
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        connectionButton.setOnClickListener {
            val connectionInfo = ConnectionInfo()
            connectionInfo.host = ipEdit.text.toString()
            connectionInfo.port = portEdit.text.toString()
            connectionInfo.username = usernameEidt.text.toString()
            connectionInfo.password = passwordEdit.text.toString()
            val dbHelper = DBHelper(connectionInfo)
            dbHelper.connnection().subscribe { result->
                if (result){
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }else{
                    Toast.makeText(this@LoginActivity, "连接失败", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}
