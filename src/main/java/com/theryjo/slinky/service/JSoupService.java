package com.theryjo.slinky.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class JSoupService {
    @Value("${linkExcludes}")
    private String linkExcludes = "";
    private Set<String> excludeSet = new HashSet<>();

    @PostConstruct
    private void init() {
        loadExcludeSet();
    }

    public List<String> scrapeStrings(String url, String cssQuery, Function<Element,String> extractor) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements els = doc.select(cssQuery);
        return els.stream()
                .map(el -> extractor.apply(el))
                .distinct()
                .filter(stringExclusionFilter())
                .collect(Collectors.toList());
    }

    public List<String> scrapeTopSites() throws IOException {
        var TOP_SITES_URL = "https://www.alexa.com/topsites";
        var TOP_SITES_QUERY = ".site-listing a";
        var TOP_SITES_EXTRACTOR = (Function<Element,String>)(Element el) -> el.text();

        return scrapeStrings(TOP_SITES_URL, TOP_SITES_QUERY, TOP_SITES_EXTRACTOR);
    }

    public List<String> scrapeLinks(String url) throws IOException {
        var LINKS_QUERY = "a";
        var LINKS_EXTRACTOR = (Function<Element,String>)(Element el) -> el.attr("href");

        return scrapeStrings(url, LINKS_QUERY, LINKS_EXTRACTOR);
    }

    private Predicate<String> stringExclusionFilter() {
        return s -> {
            String normalized = s.toLowerCase();
            return !excludeSet.contains(normalized);
        };
    }

    private void loadExcludeSet() {
        excludeSet = new HashSet<>(
                Arrays.asList(linkExcludes.split(",")).stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList())
        );
    }
}
