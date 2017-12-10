package sky.sample

import android.app.Application
import retrofit2.Retrofit
import sky.core.ISky
import sky.core.SKYHelper
import sky.core.SKYModulesManage
import sky.core.SKYPlugins

/**
 * @author sky
 * @version 1.0 on 2017-12-06 下午3:49
 * @see SampleApplication
 */
class SampleApplication : Application(), ISky {

    override fun onCreate() {
        super.onCreate()
        SKYHelper.newSky().Inject(this)
    }

    override fun isLogOpen(): Boolean {
        return true
    }

    override fun httpAdapter(builder: Retrofit.Builder): Retrofit.Builder {
        builder.baseUrl("http://www.baidu.com")
        return builder
    }

    override fun pluginInterceptor(builder: SKYPlugins.Builder): SKYPlugins.Builder {
        return builder
    }

    override fun modulesManage(): SKYModulesManage {
        return SKYModulesManage()
    }


}