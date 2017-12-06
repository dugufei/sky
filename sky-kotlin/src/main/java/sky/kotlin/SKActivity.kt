package sky.kotlin

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

/**
 * @author sky
 * @version 1.0 on 2017-12-06 上午10:56
 * @see SKActivity
 */
abstract class SKActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        /** 初始化核心 **/
        initCore()
        /** 初始化堆栈 **/
    }

    abstract fun a()

    fun initCore(){}


}