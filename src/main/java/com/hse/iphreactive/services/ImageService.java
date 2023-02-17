package com.hse.iphreactive.services;

import com.hse.iphreactive.openCV.ImageAnalyzeUtil;
import com.hse.iphreactive.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final CustomerRepository customerRepository;


    public Mono<ServerResponse> getAnalyzingBlueImage(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        var state = request.queryParam("state");
        switch (state.orElse("original")) {
            case "original" -> {
                return getImageResponse(request);
            }
            case "analyze" ->{
                 return getBlueImageResponse(request);
            }
        }


        return null;
    }


    private Mono<ServerResponse> getImageResponse(ServerRequest request ) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return customerRepository.findById(id)
                .flatMap(customerEntity -> {
                    byte[] bytes = null;
                    try {
                        bytes = Files.readAllBytes(new File(customerEntity.getUri()).toPath());
//                        bytes = ImageAnalyzeUtil.getAnalyzingImage(customerEntity.getUri());
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                    return Mono.just(bytes);
                })
                .flatMap(x -> ServerResponse.ok().body(BodyInserters.fromValue(x)));

    }

    private Mono<ServerResponse> getBlueImageResponse(ServerRequest request ) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return customerRepository.findById(id)
                .flatMap(customerEntity -> {
                    byte[] bytes = null;
                    try {
                        // bytes = Files.readAllBytes(new File(customerEntity.getUri()).toPath());
                        bytes = ImageAnalyzeUtil.getAnalyzingImage(customerEntity.getUri());
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                    return Mono.just(bytes);
                })
                .flatMap(x -> ServerResponse.ok().body(BodyInserters.fromValue(x)));

    }
}

