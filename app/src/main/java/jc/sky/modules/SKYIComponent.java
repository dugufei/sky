package jc.sky.modules;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @创建人 sky
 * @创建时间 16/9/28 下午4:44
 * @类描述
 */
@Singleton
@Component(modules = Sky_Module.class)
public interface SKYIComponent {
    void inject(SKYModulesManage skyModulesManage);
}
