package com.hse.iphreactive;

import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IphReactiveApplication {

    public static void main(String[] args) {

        OpenCV.loadLocally();
        SpringApplication.run(IphReactiveApplication.class, args);
    }

}
