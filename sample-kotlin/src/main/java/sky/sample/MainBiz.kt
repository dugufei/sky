package sky.sample

import android.util.Log
import sky.Background
import sky.BackgroundType
import sky.core.SKYBiz
import sky.core.SKYHelper

/**
 * @author sky
 * @version 1.0 on 2017-12-06 下午4:36
 * @see MainBiz
 */
open class MainBiz : SKYBiz<MainActivity>() {


    @Background(BackgroundType.WORK) open fun a() {

        Thread.sleep(3000)

        SKYHelper.toast().show("弹框")

    }

    open fun b(name: String, number: Int) {
        Log.i("mainbiz", "名字 ${name} 年龄 ${number}")
    }

}
