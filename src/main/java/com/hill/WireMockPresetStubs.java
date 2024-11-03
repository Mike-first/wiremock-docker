package com.hill;

public class WireMockPresetStubs {
    public static void main(String[] args) {
        WireMockSetupDocker.start();

        Runtime.getRuntime().addShutdownHook(new Thread(WireMockSetupDocker::stop));
    }
}