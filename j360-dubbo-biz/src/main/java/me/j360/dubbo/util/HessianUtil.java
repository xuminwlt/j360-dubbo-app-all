/**
 * pajk.com Inc.
 * Copyright (c) 2014-2014 All Rights Reserved.
 */
package me.j360.dubbo.util;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.alibaba.com.caucho.hessian.io.SerializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

/**
 * 
 * @author yuewenxin
 * @version v 0.1 2014年9月28日 下午3:55:21 aaronyue Exp $
 */
public class HessianUtil {
    private final static Logger            logger            = LoggerFactory
                                                                 .getLogger(HessianUtil.class);

    /**
     * hessian serializer factory
     */
    private static final SerializerFactory serializerFactory = new SerializerFactory();

    public static byte[] encode(Object obj) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        Hessian2Output h2o = new Hessian2Output(os);
        h2o.setSerializerFactory(serializerFactory);

        try {
            h2o.writeObject(obj);
        } finally {
            closeableQuietly(os);
            closeQuietly(h2o);
        }

        return os.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> T decode(byte[] data) throws IOException {
        Object obj = null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Hessian2Input hessian2Input = new Hessian2Input(in);
        hessian2Input.setSerializerFactory(serializerFactory);

        try {
            obj = hessian2Input.readObject();
        } finally {
            closeableQuietly(in);
            closeQuietly(hessian2Input);
        }
        return (T) obj;
    }

    private static void closeQuietly(Hessian2Output hessian2Output) {
        try {
            if (hessian2Output != null) {
                hessian2Output.close();
            }
        } catch (IOException e) {
            logger.warn("close hessian2Output failed");
        }
    }

    private static void closeQuietly(Hessian2Input hessian2Input) {
        try {
            if (hessian2Input != null) {
                hessian2Input.close();
            }
        } catch (IOException e) {
            logger.warn("close hessian2Input failed");
        }
    }

    private static void closeableQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

}
