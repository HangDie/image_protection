package cn.hd.service;

import cn.hd.core.converter.Converter;
import cn.hd.core.converter.DctConverter;
import cn.hd.core.dencoder.Decoder;
import cn.hd.core.dencoder.Encoder;
import cn.hd.core.dencoder.TextEncoder;
import cn.hd.core.phash.ImagePHash;
import cn.hd.dao.FileInfoMapper;
import cn.hd.dao.PhotosMapper;
import cn.hd.entity.FileInfo;
import cn.hd.entity.PhotoModel;
import cn.hd.entity.Photos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PhotoServiceImpl {

    @Autowired
    PhotosMapper photosMapper;
    @Autowired
    FileInfoMapper fileInfoMapper;

    private final static String FILE_PATH = "/Users/linxiaohang/Documents/temp/image/";
    private final static String FILE_URL_PATH = "localhost:8088/upload/";

    private static int STANDARD_DISTANCE = 3;

    public void insert(Photos photos) {
        FileInfo fileInfo = fileInfoMapper.selectByPrimaryKey(photos.getFileId());
        photos.setFileUrl(fileInfo.getFileUrl());
        // 计算哈希值
        ImagePHash pHashUtil = new ImagePHash();
        String code = pHashUtil.getHashCode(fileInfo.getFilePath());
        fileInfo.setImageCode(code);
        fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
        photos.setCreateTime(new Date());
        photosMapper.insert(photos);
    }

    public List<PhotoModel> check(Integer fileId) {
        List<PhotoModel> list = fileInfoMapper.queryAll();
        FileInfo checkFile = fileInfoMapper.selectByPrimaryKey(fileId);
        List<PhotoModel> result = new ArrayList<>();
        if(list.size() < 1 || checkFile == null)
            return null;
        // 计算哈希值
        ImagePHash pHashUtil = new ImagePHash();
        String imgCode = pHashUtil.getHashCode(checkFile.getFilePath());
        for(PhotoModel pm : list) {
            if(pHashUtil.distance(pm.getImageCode(), imgCode) <= STANDARD_DISTANCE) {
                result.add(pm);
            }
        }
        if(result.size() > 0) {
            return result;
        }
        return null;
    }

    public FileInfo getWatermark(Integer fileId) {
        FileInfo checkFile = fileInfoMapper.selectByPrimaryKey(fileId);
        Converter converter = new DctConverter();
        Decoder decoder = new Decoder(converter);
        String filePath = checkFile.getFileName().substring(0, 2) + "/" + checkFile.getFileName().substring(2, 4) + "/";
        decoder.decode(checkFile.getFilePath(), FILE_PATH + filePath + "water-" + checkFile.getFileName());
        FileInfo watermarkFile = new FileInfo();
        watermarkFile.setFileName("watermark-" + checkFile.getFileName());
        watermarkFile.setFilePath(FILE_PATH + filePath + watermarkFile.getFileName());
        watermarkFile.setTempFile(true);
        watermarkFile.setWatermark(true);
        watermarkFile.setFileUrl(FILE_URL_PATH + filePath + "water-" + checkFile.getFileName());
        watermarkFile.setCreateTime(new Date());
        watermarkFile.setFileOriginName("watermark-" + checkFile.getFileOriginName());
        watermarkFile.setExtension(checkFile.getExtension());
        fileInfoMapper.insert(watermarkFile);
        return watermarkFile;
    }

    public FileInfo getOriginImage(Integer fileId, String userName, String ipAddress) {
        FileInfo checkFile = fileInfoMapper.selectByPrimaryKey(fileId);
        Converter converter = new DctConverter();
        Encoder encoder = new TextEncoder(converter);
        String filePath = checkFile.getFileName().substring(0, 2) + "/" + checkFile.getFileName().substring(2, 4) + "/";
        encoder.encode(checkFile.getFilePath(), "u " + userName + " ip " + ipAddress, FILE_PATH + filePath + "water-img-" + checkFile.getFileName());
        FileInfo watermarkFile = new FileInfo();
        watermarkFile.setFileName("water-img-" + checkFile.getFileName());
        watermarkFile.setFilePath(FILE_PATH + filePath + watermarkFile.getFileName());
        watermarkFile.setTempFile(true);
        watermarkFile.setWatermark(true);
        watermarkFile.setFileUrl(FILE_URL_PATH + filePath + "water-img-" + checkFile.getFileName());
        watermarkFile.setCreateTime(new Date());
        watermarkFile.setFileOriginName("water-img-" + checkFile.getFileOriginName());
        watermarkFile.setExtension(checkFile.getExtension());
        fileInfoMapper.insert(watermarkFile);
        return watermarkFile;
    }

    public List<PhotoModel> myPhoto(Integer userId) {
        List<PhotoModel> list = fileInfoMapper.queryAll();
        List<PhotoModel> result = new ArrayList<>();
        for(PhotoModel pm : list) {
            if(pm.getUserId().equals(userId)){
                result.add(pm);
            }
        }
        if(result.size() > 0) {
            return result;
        }
        return null;
    }

    public List<Photos> query(String search) {
        return photosMapper.query(search);
    }

    public List<PhotoModel> queryPhotoModels(String search) {
        List<PhotoModel> list = fileInfoMapper.queryAll();
        List<PhotoModel> result = new ArrayList<>();
        for(PhotoModel pm : list) {
            if(pm.getPhotoName().contains(search) || search.equals("")) {
                result.add(pm);
            }
        }
        if(result.size() > 0) {
            return result;
        }
        return null;
    }

}
