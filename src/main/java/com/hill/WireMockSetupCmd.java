package com.hill;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class WireMockSetupCmd {
    private static final int PORT = 8080;
    private static WireMockServer wireMockServer;
    private static ObjectMapper objectMapper;

    private WireMockSetupCmd() {
        throw new IllegalStateException("This class contains static method only");
    }

    public static void start(String stubsFilePath) {
        objectMapper = new ObjectMapper();
        wireMockServer = new WireMockServer(PORT);
        log.info("wiremock start");
        wireMockServer.start();
        configureStubs(readStubsFromFile(stubsFilePath));
    }

    private static List<StubMapping> readStubsFromFile(String stubsFilePath) {
        try {
            return objectMapper.readValue(Files.readAllBytes(Paths.get(stubsFilePath)),
                    new TypeReference<List<StubMapping>>() {
                    });
        } catch (IOException e) {
            log.error("Error reading file: {}", stubsFilePath);
            throw new UncheckedIOException(e);
        }
    }

    private static void configureStubs(List<StubMapping> stubs) {
        stubs.forEach(stub -> {
            log.info("Configuring stub for URL: {}", stub.getUrl());
            WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(stub.getUrl()))
                    .willReturn(WireMock.aResponse()
                            .withStatus(stub.getResponse().getStatus())
                            .withHeader("Content-Type", stub.getResponse().getContentType())
                            .withBody(stub.getResponse().getBody())));
        });
        log.info("WireMock.stub setup finished");
    }

    @Getter
    @Setter
    public static class StubMapping {
        private String url;
        private Response response;

        @Getter
        @Setter
        public static class Response {
            private int status;
            private String contentType;
            private String body;
        }
    }

    public static void stop() {
        log.info("wiremock stop");
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}

