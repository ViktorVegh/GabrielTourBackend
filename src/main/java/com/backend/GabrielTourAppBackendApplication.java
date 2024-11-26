package com.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GabrielTourAppBackendApplication {
    public static void main(String[] args) {
        // Enable full SOAP message dumping
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

        // Increase the dump threshold to a large value (e.g., 10 MB)
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dumpTreshold", "10485760"); // 10 MB
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpTreshold", "10485760"); // 10 MB

        SpringApplication.run(GabrielTourAppBackendApplication.class, args);
    }
}
