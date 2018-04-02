package sky.kotlin.core

import android.content.Context
import android.support.v4.app.FragmentManager

/**
 * @author sky
 * @version 1.0 on 2018-01-25 下午4:34
 * @see SKView
 */
internal class SKView {

    val STATE_ACTIVITY = 99999

    val STATE_FRAGMENT = 88888

    val STATE_DIALOGFRAGMENT = 77777

    val STATE_NOTVIEW = 66666

    private var state: Int = 0

    private var mSKActivity: SKActivity<*>? = null

    private var context: Context? = null

    private var fragmentManager: FragmentManager? = null

    fun initUI(mSKYActivity: SKActivity<*>) {
        this.state = STATE_ACTIVITY
        this.mSKActivity = mSKYActivity
        this.context = mSKYActivity
    }

    fun context(): Context? {
        return context
    }

    fun <A : SKActivity<*>> activity(): A {
        return mSKActivity as A
    }

    fun detach() {
        this.state = 0
        this.context = null
        this.fragmentManager = null
    }
}