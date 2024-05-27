package com.example.task.config;

import com.example.task.domain.Info;
import com.jcraft.jsch.JSch;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {
    @Bean
    public JSch jSch(){
        return new JSch();
    }

    @Bean
    public Info info(){
        return new Info();
    }
}
