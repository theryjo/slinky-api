package com.theryjo.slinky.controller;

import com.theryjo.slinky.enums.LinkType;
import com.theryjo.slinky.model.Link;
import com.theryjo.slinky.service.JSoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scrape")
public class ScrapeController {
    private final JSoupService jSoupService;

    @Autowired
    public ScrapeController(JSoupService jSoupService) {
        this.jSoupService = jSoupService;
    }

    @GetMapping("links")
    public Iterable<String> scrapeLinks(@RequestParam String url) throws IOException {
        return jSoupService.scrapeLinks(url);
    }

    @GetMapping("topsites")
    public Iterable<Link> scrapeTopSites() throws IOException {
        var links = jSoupService.scrapeTopSites().stream()
                .map(s -> new Link(null, LinkType.URL, s))
                .collect(Collectors.toList());
        return links;
    }
}
