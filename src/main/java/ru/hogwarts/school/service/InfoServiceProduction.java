package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InfoServiceProduction implements InfoService {

    @Value("${server.port}")
    private Integer serverPort;

    @Override
    public Integer getPort() {
        return serverPort;
    }

}
