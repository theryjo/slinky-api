package com.theryjo.slinky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("/api/words")
public class WordsController {
    private final String DATAMUSE_API = "https://api.datamuse.com/";

    @Autowired
    public WordsController() {
    }

    @GetMapping("related")
    public String getRelatedWords(@RequestParam String word,
                                  @RequestParam(defaultValue = "10") int max) throws IOException, InterruptedException {
        var url = DATAMUSE_API + "words";
        var query = "?ml=" + URLEncoder.encode(word, UTF_8) + "&max=" + max;
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(url + query))
                .header("accept", "application/json")
                .build();
        var apiResponse= client.send(request, HttpResponse.BodyHandlers.ofString());

        return apiResponse.body();
    }
}
