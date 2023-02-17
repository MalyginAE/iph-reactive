package com.hse.iphreactive.controllers;

import com.hse.iphreactive.services.CustomerService;
import com.hse.iphreactive.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class StartController {
    public static final Path basePath = Paths.get("C:\\Users\\Public\\temp");//to config

    private final CustomerService service;
    private final ImageService imageService;




    @Bean
    public RouterFunction<ServerResponse> imageController() {
        return RouterFunctions.route()
                .POST("/customer", service::saveCustomerData)
                .GET("/image/{id}", imageService::getAnalyzingBlueImage)
                .build();
    }



    @SneakyThrows
    private Mono<ServerResponse> getImage(ServerRequest request) {
        service.saveCustomerData(request);
//        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
//        multipartBodyBuilder.part("image", Files.readAllBytes(new ClassPathResource("photo.jpg").getFile().toPath()), MediaType.valueOf("multipart/form-data"));
//        MultiValueMap<String, HttpEntity<?>> build = multipartBodyBuilder.build();
//        byte[] bytes = new ClassPathResource("photo.jpg").getInputStream().readAllBytes();
        return ServerResponse.ok().body(BodyInserters.empty());
    }



}
