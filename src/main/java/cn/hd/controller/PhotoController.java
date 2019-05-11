package cn.hd.controller;

import cn.hd.entity.Photos;
import cn.hd.service.PhotoServiceImpl;
import cn.hd.utils.EntityUtil;
import cn.hd.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/query")
    @ResponseBody
    public ServerResponse query(String search) {
        if(search == null || search.isEmpty()) {
            search = "";
        }
        return ServerResponse.createBySuccess(search);
    }

}
