package com.jason.qiniu_spring_boot_starter.api.service.impl;

import com.google.gson.Gson;
import com.jason.qiniu_spring_boot_starter.api.service.QiniuService;
import com.jason.qiniu_spring_boot_starter.config.QiNiuProperties;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;

public class QiniuServiceImpl implements QiniuService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuServiceImpl.class) ;
    @Autowired
    private UploadManager uploadManager ;
    @Autowired
    private BucketManager bucketManager ;
    @Autowired
    private Auth auth ;
    @Autowired
    private QiNiuProperties qiNiuProperties ;


    @Override
    public String uploadFileAndReturnFilePath(FileInputStream fileInputStream, String key) throws QiniuException {

        Response response = uploadFile(fileInputStream,key,false);
        // 解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        //返回上传后文件地址
        return qiNiuProperties.getDomain() + "/" + putRet.key;
    }

    @Override
    @SuppressWarnings("all")
    public <T> T converter(Response response,Class<T> t) throws QiniuException {
        return (T) new Gson().fromJson(response.bodyString(), t);
    }

    @Override
    public Response uploadFile(FileInputStream file, String key, boolean existed) throws QiniuException {
        Response response ;
        if(existed){
            response = this.uploadManager.put(file,key,getUploadToken(key),null,null) ;
        }else{

            response = this.uploadManager.put(file,key,getUploadToken(),null,null) ;

            //尝试3次
            int retry = 0 ;
            while(response.needRetry() && retry < 3){
                response = this.uploadManager.put(file,key,getUploadToken(),null,null) ;
                retry ++ ;
            }
        }
        return response ;
    }

    @Override
    public Response uploadFile(File file, String key, boolean existed) throws QiniuException {
        Response response ;
        if(existed){
            response = this.uploadManager.put(file,key,getUploadToken(key)) ;
        }else{

            response = this.uploadManager.put(file,key,getUploadToken()) ;
            //尝试3次
            int retry = 0 ;
            while(response.needRetry() && retry < 3){
                response = this.uploadManager.put(file,key,getUploadToken()) ;
                retry ++ ;
            }
        }
        return response ;
    }

    @Override
    public Response uploadFile(String filePath, String key, boolean existed) throws QiniuException {
        Response response ;
        if(existed){
            response = this.uploadManager.put(filePath,key,getUploadToken(key)) ;
        }else{
            response = this.uploadManager.put(filePath,key,getUploadToken()) ;
            int retry = 0 ;
            while (response.needRetry() && retry < 3){
                response = this.uploadManager.put(filePath,key,getUploadToken()) ;
                retry ++ ;
            }
        }
        return response ;
    }

    @Override
    public void deleteFile(String key) throws QiniuException {

    }

    /**
     * 获取上传凭证，覆盖上传
     * @param fileName
     * @return
     */
    private String getUploadToken(String fileName){
        return this.auth.uploadToken(qiNiuProperties.getBucketName(),fileName) ;
    }

    /**
     * 获取上传凭证，普通上传
     * @return
     */
    private String getUploadToken(){
        return this.auth.uploadToken(qiNiuProperties.getBucketName()) ;
    }
}
