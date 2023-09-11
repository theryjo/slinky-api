package com.theryjo.slinky.controller;

import com.theryjo.slinky.model.Link;
import com.theryjo.slinky.repository.LinkRepository;
import com.theryjo.slinky.service.JSoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/links")
public class LinksController {
    private final LinkRepository linkRepository;
    private final JSoupService jSoupService;

    @Autowired
    public LinksController(LinkRepository linkRepository, JSoupService jSoupService) {
        this.linkRepository = linkRepository;
        this.jSoupService = jSoupService;
    }

    @GetMapping
    public Iterable<Link> list() {
        return linkRepository.findAll();
    }
}

