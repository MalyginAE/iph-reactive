package com.hse.iphreactive.services;

import com.hse.iphreactive.entity.CustomerEntity;
import com.hse.iphreactive.entity.dto.SavedDataSuccessResponse;
import com.hse.iphreactive.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.hse.iphreactive.controllers.StartController.basePath;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository repository;

    public Mono<ServerResponse> saveCustomerData(ServerRequest request) {
        var mapMono = request.multipartData().log();
        return mapMono.flatMap(stringPartMultiValueMap -> {
                    FilePart file = (FilePart) stringPartMultiValueMap.toSingleValueMap().get("file");
                    log.debug("size : {}", stringPartMultiValueMap.toSingleValueMap().values().size());

                    var destPath = basePath.resolve(file.filename());
                    file.transferTo(destPath).subscribe();

                    CustomerEntity andrey = new CustomerEntity(destPath.toUri().toString(), "Andrey");

                    return repository.save(andrey).log()
                            .flatMap(it -> Mono.just(
                                    new SavedDataSuccessResponse(it.getId(), it.getCustomerName(), file.filename()))
                            );
                })
                .flatMap(x -> ServerResponse.ok().body(BodyInserters.fromValue(x)));
    }



}
