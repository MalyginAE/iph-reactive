package com.hse.iphreactive.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class StartController {

    Path basePath = Paths.get("C:\\Users\\Public\\temp");

    @Bean
    public RouterFunction<ServerResponse> getImage() {
        return RouterFunctions.route()
                .POST("/image", this::getData)
                .build();
    }

    Mono<ServerResponse> getData(ServerRequest request) {
        //  var id = request.pathVariable("id");
        log.debug("hello");
        Mono<MultiValueMap<String, Part>> mapMono = request.body(BodyExtractors.toMultipartData());
      return   mapMono.flatMap(stringPartMultiValueMap -> {
          FilePart file = (FilePart)stringPartMultiValueMap.toSingleValueMap().get("file");
          log.debug("size : {}", stringPartMultiValueMap.toSingleValueMap().values().size());
          return file.transferTo(basePath.resolve(file.filename()));



        }).flatMap(x -> ServerResponse.ok().body(BodyInserters.empty()));
    }
}
