package com.jason.qiniu_spring_boot_starter.api.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;

import java.io.File;
import java.io.FileInputStream;

public interface QiniuService {

    String uploadFileAndReturnFilePath(FileInputStream fileInputStream,String key) throws QiniuException;

    /**
     * 将Response 返回的Json数据转换为对象
     * @param t
     * @param <T>
     * @return
     */
    <T> T converter(Response response,Class<T> t) throws QiniuException;

    /**
     * <h2>上传文件</h2>
     * @param file 上传文件的类型
     * @param key  上传文件的key值
     * @param existed 是否已经存在
     * @return com.qiniu.http.Response
     * @throws QiniuException 抛出QiniuException异常
     */
    Response uploadFile(FileInputStream file, String key, boolean existed) throws QiniuException ;

    /**
     * <h2>上传文件</h2>
     * @param file 上传文件的类型
     * @param key  上传文件的key值
     * @param existed 是否已经存在
     * @return com.qiniu.http.Response
     * @throws QiniuException 抛出QiniuException异常
     */
    Response uploadFile(File file, String key, boolean existed) throws QiniuException ;

    /**
     * 上传文件
     * <h2>文件路径上传</h2>
     *
     * @param filePath 填写上传文件的位置
     * @param key 添加上传的key值
     * @param existed 是否已经存在
     * @return 返回com.qiniu.http.Response
     * @throws QiniuException 抛出QiniuException异常
     */
    Response uploadFile(String filePath, String key, boolean existed) throws QiniuException;

    /**
     * 删除
     *
     * @param key 添加上传的key值
     * @throws QiniuException 抛出QiniuException异常
     */
    void deleteFile(String key) throws QiniuException;

}
