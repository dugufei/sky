package sky.kotlin.core

import android.content.Intent
import android.os.Bundle

/**
 * @author sky
 * @version 1.0 on 2018-01-26 上午10:21
 * @see SKMVPManager  MVP管理
 */
class SKMVPManager(view: Object, intent: Intent) {

    val view: Object = view

    val intent: Intent = intent

    private var key: Int

    private var skBuilder: SKBuilder? = null

    private var bundle: Bundle? = null

    private var service: Class<*>? = null

    private var impl: Any? = null

    init {
        key = view.hashCode()
        bundle = intent.extras
        service = SKUtils.getClassGenricType(view.`class`, 0)
        if(service != null){
            initProxy()
        }
    }

    fun initProxy() {
        impl = SKUtils.createClassGenricType(service!!)
    }
}