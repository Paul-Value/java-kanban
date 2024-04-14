package service.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.ManagerSaveException;
import service.ValidationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ErrorHandler {
    Gson gson = new Gson();

    public void handle(HttpExchange exchange, NumberFormatException e) throws IOException {
        e.printStackTrace();
        sendText(exchange, 400, gson.toJson(e));
    }

    public void handle(HttpExchange exchange, ManagerSaveException e) throws IOException {
        e.printStackTrace();
        sendText(exchange, 400, gson.toJson(e));
    }

    public void handle(HttpExchange exchange, ValidationException e) throws IOException {
        e.printStackTrace();
        sendText(exchange, 406, gson.toJson(e));
    }

    public void handle(HttpExchange exchange, Exception e) throws IOException {
        e.printStackTrace();
        sendText(exchange, 500, gson.toJson(e));
    }

    private void sendText(HttpExchange exchange, int code, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);

    }
}
