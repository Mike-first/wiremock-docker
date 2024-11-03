package com.hill;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class WireMockSetupDocker {
    private static final int PORT = 8080;
    private static WireMockServer wireMockServer;

    private WireMockSetupDocker() {
        throw new IllegalStateException("This class contains static method only");
    }

    public static void start() {
        wireMockServer = new WireMockServer(PORT);
        log.info("wiremock start");
        wireMockServer.start();
        configureStubs(readCitiesFromFile());
    }

    private static Map<String, String> readCitiesFromFile() {
        try (Stream<String> lines = Files.lines(Paths.get("/app/resources/cities.txt"))) {
            return lines.map(line -> line.split("\\|"))
                    .collect(Collectors.toMap(parts -> parts[0], parts -> parts[1]));
        } catch (IOException e) {
            log.error("error reading file");
            throw new UncheckedIOException(e);
        }
    }

    private static void configureStubs(Map<String, String> cities) {
        cities.forEach((code, json) -> {
            log.info("WireMock.stub setup:");
            log.info("Configuring stub for CODE: {} with JSON: {}", code, json);
            WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/local/ajax/current-city.php?CODE=" + code))
                    .willReturn(WireMock.aResponse().withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(json)));});
        log.info("WireMock.stub setup finished");
    }

    public static void stop() {
        log.info("wiremock stop");
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
