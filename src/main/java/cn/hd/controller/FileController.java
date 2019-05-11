package cn.hd.controller;

import cn.hd.entity.FileInfo;
import cn.hd.service.FileServiceImpl;
import cn.hd.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileServiceImpl fileService;

    @PostMapping("/upload")
    @ResponseBody
    public ServerResponse<?> upload(@RequestParam("files") MultipartFile[] files) {
        if (files.length > 0) {
            MultipartFile file = files[0];
            FileInfo fileInfo = null;
            try {
                fileInfo = fileService.saveFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fileInfo == null) {
                return ServerResponse.createByFailure("上传文件失败，文件上传大小不可超过5MB。");
            } else {
                return ServerResponse.createBySuccess(fileInfo);
            }
        }
        return ServerResponse.createByFailure("上传文件不能为空");
    }

}
