package sky.kotlin.core

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @author sky
 * @version 1.0 on 2017-12-06 上午10:56
 * @see SKActivity 参数
 */
abstract class SKActivity<T> : AppCompatActivity() {

    protected abstract fun build(skBuilder: SKBuilder): SKBuilder



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        /** 初始化数据 **/
        initData(intent.extras)
    }

    abstract fun initView()

    abstract fun initData(bundle: Bundle?)
}