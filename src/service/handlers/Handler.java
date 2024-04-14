package service.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class Handler implements HttpHandler {
    void sendResponse(HttpExchange exchange, String responseText, int code) throws IOException {
        byte[] response = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
    }
}
