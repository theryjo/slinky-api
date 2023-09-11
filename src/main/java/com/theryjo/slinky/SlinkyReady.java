package com.theryjo.slinky;

import com.theryjo.slinky.enums.ImportType;
import com.theryjo.slinky.enums.LinkType;
import com.theryjo.slinky.model.Link;
import com.theryjo.slinky.model.LinkImport;
import com.theryjo.slinky.repository.LinkRepository;
import com.theryjo.slinky.repository.LinkImportRepository;
import com.theryjo.slinky.service.JSoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class SlinkyReady implements ApplicationListener<ApplicationReadyEvent> {
    private final LinkRepository linkRepository;
    private final LinkImportRepository linkImportRepository;
    private final JSoupService jSoupService;

    @Autowired
    public SlinkyReady(LinkRepository linkRepository, LinkImportRepository linkImportRepository, JSoupService jSoupService) {
        this.linkRepository = linkRepository;
        this.linkImportRepository = linkImportRepository;
        this.jSoupService = jSoupService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (linkRepository.count() == 0) {
            System.out.println("Performing startup scrape...");
            doScrape();
            System.out.println("Startup scrape completed");
        }
    }

    /**
     * Do demo scrape and populate repository
     */
    private void doScrape() {
        LinkImport imp = new LinkImport(ImportType.SCRAPE, "Startup Scrape - Top Sites at www.alexa.com",
                LocalDateTime.now(), null, false, null);
        linkImportRepository.save(imp);
        try {
            var links = jSoupService.scrapeTopSites().stream()
                    .map(s ->  new Link(imp, LinkType.URL, s))
                    .collect(Collectors.toList());
            linkRepository.saveAll(links);

            imp.setCount(links.size());
            imp.setFinishedAt(LocalDateTime.now());
            imp.setCompleted(true);
            linkImportRepository.save(imp);
        // TODO handle hibernate errors
        } catch (IOException ioe) {
            System.out.println("Error performing startup scrape");
        }
    }
}
