package jc.sky.modules.screen;

import java.util.ArrayList;

/**
 * @author sky
 * @version 版本
 */
public class SKYActivityTransporter {

    private Class<?> toClazz;
    private ArrayList<SKYActivityExtra> extras;

    public SKYActivityTransporter(Class<?> toClazz) {
        this.toClazz = toClazz;
    }

    /**
     * It is only possible to send strings as extra.
     * @param key      参数
     * @param value 参数
     * @return 返回值
     */
    public SKYActivityTransporter addExtra(String key, String value) {
        if (extras == null)
            extras = new ArrayList<>();

        extras.add(new SKYActivityExtra(key, value));
        return this;
    }

    public Class<?> toClazz() {
        return toClazz;
    }

    public ArrayList<SKYActivityExtra> getExtras() {
        return extras;
    }
}
