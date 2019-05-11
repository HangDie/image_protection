package cn.hd.service;

import cn.hd.dao.FileInfoMapper;
import cn.hd.entity.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl {

    private final static String FILE_PATH = "/Users/linxiaohang/Documents/temp/image/";
    private final static String FILE_URL_PATH = "localhost:8088/upload/";

    @Autowired
    FileInfoMapper fileInfoMapper;

    public FileInfo saveFile(MultipartFile file) throws Exception {
        String originName = file.getOriginalFilename();
        String extension = originName.substring(originName.lastIndexOf("."), originName.length());
        String fileName = UUID.randomUUID().toString() + extension;
        String filePath = fileName.substring(0, 2) + "/" + fileName.substring(2, 4) + "/";
        Integer fileSize = (int) file.getSize();
        // 保存文件
        byte[] bytes = file.getBytes();
        File dir = new File(FILE_PATH + filePath);
        if (!dir.exists())
            dir.mkdirs();
        File saveFile = new File(FILE_PATH + filePath + fileName);
        if (!saveFile.exists())
            saveFile.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(saveFile);
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
        // 设置文件信息
        FileInfo fileinfo = new FileInfo();
        fileinfo.setFileName(fileName);
        fileinfo.setFileOriginName(originName);
        fileinfo.setExtension(extension);
        fileinfo.setFilePath(FILE_PATH + filePath + fileName);
        fileinfo.setFileUrl(FILE_URL_PATH + filePath + fileName);
        fileinfo.setCreateTime(new Date());
        fileinfo.setWatermark(false);
        fileinfo.setTempFile(false);
        fileInfoMapper.insert(fileinfo);
        return fileinfo;
    }
}
