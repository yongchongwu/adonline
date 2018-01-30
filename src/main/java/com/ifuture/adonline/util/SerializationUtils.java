package com.ifuture.adonline.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * ClassName: SerializationUtils
 *
 * @author JornTang
 * @Description: 对象序列化工具类
 * @date 2017年7月22日
 */
public class SerializationUtils {

    public SerializationUtils() {
    }

    /**
     * @return byte[]
     * @Description: 序列化对象
     * @author JornTang
     * @date 2017年7月22日
     */
    public static byte[] serialize(Object state) {
        ObjectOutputStream oos = null;
        byte abyte[];
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(state);
            oos.flush();
            abyte = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        if (oos != null) {
            try {
                oos.close();
            } catch (IOException ioexception) {
            }
        }
        return abyte;
    }

    /**
     * @return Object
     * @Description: 反序列化对象
     * @author JornTang
     * @date 2017年7月22日
     */
    public static Object deserialize(byte byteArray[]) {
        ObjectInputStream oip = null;
        Object obj;
        try {
            oip = new ObjectInputStream(new ByteArrayInputStream(byteArray));
            Object result = oip.readObject();
            obj = result;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        if (oip != null) {
            try {
                oip.close();
            } catch (IOException ioexception) {
            }
        }
        return obj;
    }
}
