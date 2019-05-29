package cn.hd.controller;

import cn.hd.entity.FileInfo;
import cn.hd.entity.PhotoModel;
import cn.hd.entity.Photos;
import cn.hd.service.PhotoServiceImpl;
import cn.hd.utils.EntityUtil;
import cn.hd.utils.ServerResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    PhotoServiceImpl photoService;

    @PostMapping("/add")
    @ResponseBody
    public ServerResponse add(@RequestBody Map<String, Object> params){
        Photos photos = null;
        try {
            photos = EntityUtil.convert(Photos.class, params);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByFailure("参数错误");
        }
        photoService.insert(photos);
        return ServerResponse.createBySuccess();
    }

    @PostMapping("/check")
    @ResponseBody
    public ServerResponse check(@RequestBody Map<String, Object> params) {
        Integer fileId = null;
        try {
            fileId = Integer.parseInt(params.get("fileId").toString());
            List<PhotoModel> photos = photoService.check(fileId);
            if(photos == null || photos.size() < 1) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createBySuccess(photos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByFailure("参数错误");
        }

    }

    @PostMapping("/getWatermark")
    @ResponseBody
    public ServerResponse getWatermark(@RequestBody Map<String, Object> params) {
        Integer fileId = null;
        try {
            fileId = Integer.parseInt(params.get("fileId").toString());
            FileInfo result = photoService.getWatermark(fileId);
            return ServerResponse.createBySuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByFailure("参数错误");
        }

    }

    @GetMapping("/getOriginImage")
    @ResponseBody
    public ResponseEntity<byte[]> getOriginImage(Integer fileId, String userName, String ipAddress) {
        try {
            FileInfo result = photoService.getOriginImage(fileId, userName, ipAddress);
            try {
                File file = new File(result.getFilePath());
                HttpHeaders headers = new HttpHeaders();
                // 下载显示的文件名，解决中文名称乱码问题
                String downloadFielName = new String(result.getFileOriginName().getBytes("UTF-8"), "iso-8859-1");
                // 通知浏览器以attachment（下载方式）打开文件
                // headers.setContentDispositionFormData("content-Type",
                // fileInfo.getOriginName());
                headers.set("Content-Disposition", "filename=" + downloadFielName);
                if(result.getExtension().equals("png")) {
                    headers.setContentType(MediaType.valueOf("image/png"));
                } else{
                    headers.setContentType(MediaType.valueOf("image/jpeg"));
                }
                return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/myPhoto")
    @ResponseBody
    public ServerResponse myPhoto(@RequestBody Map<String, Object> params){
        Integer userId = null;
        try {
            userId = Integer.parseInt(params.get("userId").toString());
            List<PhotoModel> photos = photoService.myPhoto(userId);
            if(photos == null || photos.size() < 1) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createBySuccess(photos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByFailure("参数错误");
        }

    }

    @GetMapping("/query")
    @ResponseBody
    public ServerResponse query(String search) {
        if(search == null || search.isEmpty()) {
            search = "";
        }
        return ServerResponse.createBySuccess(photoService.queryPhotoModels(search));
    }

}
