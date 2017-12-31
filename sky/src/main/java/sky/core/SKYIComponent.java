package sky.core;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author sky
 * @version 版本
 */
@Singleton
@Component(modules = SKYModule.class)
public interface SKYIComponent {

	void inject(SKYModulesManage skyModulesManage);
}
