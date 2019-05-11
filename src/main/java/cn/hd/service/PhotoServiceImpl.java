package cn.hd.service;

import cn.hd.core.phash.ImagePHash;
import cn.hd.dao.FileInfoMapper;
import cn.hd.dao.PhotosMapper;
import cn.hd.entity.FileInfo;
import cn.hd.entity.Photos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PhotoServiceImpl {

    @Autowired
    PhotosMapper photosMapper;
    @Autowired
    FileInfoMapper fileInfoMapper;

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

    public List<Photos> query(String search) {
        return photosMapper.query(search);
    }

}
