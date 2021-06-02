package com.blog.demo.controller.admin;

import com.blog.demo.service.BlogConfigService;
import com.blog.demo.util.Result;
import com.blog.demo.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ConfigurationController {

    @Autowired
    BlogConfigService configService;

    @GetMapping("/configurations")
    public String list(HttpServletRequest request){
        request.setAttribute("path","configurations");
        request.setAttribute("configurations",configService.getAllConfigs());
        return "admin/configuration";
    }

    @PostMapping({"/configurations/website", "/configurations/userInfo", "/configurations/footer"})
    @ResponseBody
    public Result website(@RequestParam Map<String,String> params){

        int result = 0;
        for (String key : params.keySet()){
            System.out.println(key + " : " + params.get(key));
            if (!params.get(key).isEmpty()){
                result += configService.updateConfig(key,params.get(key));
            }
        }

        return ResultGenerator.genSuccessResult(result > 0);
    }
}
