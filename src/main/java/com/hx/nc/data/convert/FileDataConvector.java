package com.hx.nc.data.convert;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.service.spi.ServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author XingJiajun
 * @Date 2019/4/1 14:33
 * @Description
 */
public class FileDataConvector {
    public String getBase64String(byte[] data) throws ServiceException {
        return getBase64String(data, true);
    }

    public String getBase64String(byte[] data, boolean compress)
            throws ServiceException {
        if (data == null)
            return null;
        if (compress) {
            data = compress(data);
        }
        return Base64.encodeBase64String(data);
    }

    public byte[] getByteArrayFromBase64String(String source)
            throws ServiceException {
        return getByteArrayFromBase64String(source, false);
    }

    public byte[] getByteArrayFromBase64String(String source, boolean compress)
            throws ServiceException {
        byte[] data = Base64.decodeBase64(source);
        if (compress) {
            return uncompress(data);
        }
        return data;
    }

    /**
     * 对byte[]进行压缩
     *
     *
     * @return 压缩后的数据
     * @throws ServiceException
     */

    private byte[] compress(byte[] data) throws ServiceException {
        GZIPOutputStream gzip = null;
        ByteArrayOutputStream baos = null;
        byte[] newData = null;
        try {
            baos = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(baos);
            gzip.write(data);
            gzip.finish();
            gzip.flush();
            newData = baos.toByteArray();
        } catch (IOException e) {
            throw new ServiceException("压缩文件二进制数组失败!", e);
        } finally {

            try {

                if (baos != null) {
                    baos.close();
                }
                if (gzip != null) {
                    gzip.close();
                }
            } catch (IOException e) {
                throw new ServiceException("压缩文件二进制数组失败!", e);
            }
        }
        return newData;
    }

    public byte[] uncompress(byte[] data) throws ServiceException {
        GZIPInputStream gzip = null;
        ByteArrayInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new ByteArrayInputStream(data);
            gzip = new GZIPInputStream(in);
            out = new ByteArrayOutputStream();
            int len;
            byte[] buf = new byte[2048];
            while ((len = gzip.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new ServiceException("解压缩文件二进制数组失败!", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (gzip != null) {
                    gzip.close();
                }
            } catch (IOException e) {
                throw new ServiceException("解压缩文件二进制数组失败!", e);
            }
        }
    }
}
