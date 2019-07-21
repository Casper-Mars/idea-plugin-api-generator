package org.r.idea.plugin.generator.core.upload;

import org.r.idea.plugin.generator.core.exceptions.UplodaException;

public interface Deliveryman {


    /**
     * 获取远端的ip地址
     *
     * @return ip地址字符串
     */
    String getDistanceIp();

    /**
     * 获取本地待上传的文件的文件名
     */
    String getLocalFilename();

    /**
     * 获取本地待上传的文件的文件路径
     */
    String getLocalFilePath();

    /**
     * 获取上传到远端的文件的文件名
     *
     * @return
     */
    String getRemoteFilename();

    /**
     * 获取上传到远端的文件的文件路径
     *
     * @return
     */
    String getRemoteFilePath();


    /**
     * 上传
     */
    void doDeliver() throws UplodaException;


}
