package sky.sample

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import sky.core.SKYActivity
import sky.core.SKYBuilder

/**
 * @author sky
 * @version 1.0 on 2017-12-06 下午3:50
 * @see MainActivity
 */
open class MainActivity : SKYActivity<MainBiz>() {

    override fun build(initialSKYBuilder: SKYBuilder): SKYBuilder {
        initialSKYBuilder.layoutId(R.layout.activity_main)
        return initialSKYBuilder
    }

    override fun initData(savedInstanceState: Bundle?) {
        button2.setOnClickListener { biz().a() }
        button3.setOnClickListener { biz().b("aa",1) }
    }

}
