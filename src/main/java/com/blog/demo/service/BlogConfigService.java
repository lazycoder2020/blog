package com.blog.demo.service;

import java.util.Map;

public interface BlogConfigService {
    Map<String,String> getAllConfigs();
    int updateConfig(String configName, String configValue);
}
