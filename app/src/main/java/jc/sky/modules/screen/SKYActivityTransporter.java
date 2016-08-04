package jc.sky.modules.screen;

import java.util.ArrayList;

/**
 * @创建人 sky
 * @创建时间 16/2/27
 * @类描述
 */
public class SKYActivityTransporter {

    private Class<?> toClazz;
    private ArrayList<SKYActivityExtra> extras;

    public SKYActivityTransporter(Class<?> toClazz) {
        this.toClazz = toClazz;
    }

    /**
     * It is only possible to send strings as extra.
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
