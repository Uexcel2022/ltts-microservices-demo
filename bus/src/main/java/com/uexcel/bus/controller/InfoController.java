package com.uexcel.bus.controller;

import com.uexcel.bus.info.AppBuiltInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")

public class InfoController {
    private final AppBuiltInfo appBuiltInfo;
    private final Environment env;
    @Value("${build.version}")
    private String version;

    public InfoController(AppBuiltInfo appBuiltInfo, Environment env) {
        this.appBuiltInfo = appBuiltInfo;
        this.env = env;
    }

    @GetMapping("info")
    public ResponseEntity<AppBuiltInfo> getCustomerInfo(){
        return ResponseEntity.ok().body(appBuiltInfo);
    }

    @GetMapping("/build-version")
    public ResponseEntity<Map<String,String>> getAppVersion(){
        Map<String, String> map = new HashMap<>();
        map.put("Build version", version);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("java")
    public ResponseEntity<Map<String,String>> getJavaVersion(){
        Map<String, String> map = new HashMap<>();
        map.put("Java version", env.getProperty("java.version"));
        return ResponseEntity.ok().body(map);
    }


}



