package com.itheima.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AliyunUtil {
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    public static String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    public static String accessKeyId = "your ak";
    public static String accessKeySecret = "your sk";
    public static String bucketName = "your bucketname";
    public static String objectName = "picture/ [your path]";

    public static void upload2AliYun(byte[] bytes, String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传文件直接流
        ossClient.putObject(bucketName,objectName+fileName, new ByteArrayInputStream(bytes));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    public static void upload2AliYun(InputStream inputStream, String fileName){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, objectName+fileName, inputStream);
        ossClient.shutdown();
    }

    public static void deleteFileFromAliYun(String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName+fileName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

}
