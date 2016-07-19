package com.huimang.ganhuo.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ronnie
 * @copyright 2016 (c) huimang.com
 * @since 2016/7/19 21:07
 */
public class Const {
    public static final String INFOQ_HOST = "http://www.infoq.com";
    private static Properties props;

    /**
     * 获取属性
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        if (props == null) {
            props = new Properties();
            try {
                InputStream in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + "/db.properties"));
                props.load(in);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return props.getProperty(key);
    }
}
