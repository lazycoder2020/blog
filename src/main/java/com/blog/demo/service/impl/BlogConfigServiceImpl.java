package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.demo.bean.BlogConfig;
import com.blog.demo.dao.BlogConfigMapper;
import com.blog.demo.service.BlogConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogConfigServiceImpl implements BlogConfigService {

    @Autowired
    BlogConfigMapper configMapper;

    @Override
    public Map<String, String> getAllConfigs() {
        List<BlogConfig> configs = configMapper.selectList(null);
        Map<String,String> map = new HashMap<>();
        for (BlogConfig config : configs ){
            map.put(config.getConfigName(), config.getConfigValue());
        }
        return map;
    }

    @Override
    public int updateConfig(String configName, String configValue) {

        QueryWrapper<BlogConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("config_name", configName);
        BlogConfig config = configMapper.selectOne(wrapper);
        if (config != null) {
             config.setConfigValue(configValue);
             return configMapper.updateById(config);
        }

        return 0;
    }

}
