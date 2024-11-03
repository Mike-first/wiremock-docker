package com.hill;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WireMockExternalStubs {
    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("Usage: java -jar wiremock-docker-<version>.jar <path-to-stubs-json>");
            System.exit(1);
        }

        String stubsFilePath = args[0];
        WireMockSetupCmd.start(stubsFilePath);

        Runtime.getRuntime().addShutdownHook(new Thread(WireMockSetupCmd::stop));
    }
}
