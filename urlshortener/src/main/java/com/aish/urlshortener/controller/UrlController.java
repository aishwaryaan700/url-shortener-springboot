package com.aish.urlshortener.controller;

import com.aish.urlshortener.model.Url;
import com.aish.urlshortener.service.UrlService;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    // Create Short URL
    @PostMapping("/shorten")
    public String createShortUrl(@RequestBody String originalUrl) {

        Url url = urlService.createShortUrl(originalUrl);

        return "http://localhost:8081/api/" + url.getShortCode();
    }

    // Redirect to Original URL + Count Click
    @GetMapping("/{shortCode}")
    public void redirectToOriginal(@PathVariable String shortCode,
                                   HttpServletResponse response) throws Exception {

        Optional<Url> urlOptional = urlService.getOriginalUrl(shortCode);

        if (urlOptional.isPresent()) {

            Url url = urlOptional.get();

            // increase click count
            url.setClicks(url.getClicks() + 1);

            // save updated clicks
            urlService.save(url);

            // redirect user
            response.sendRedirect(url.getOriginalUrl());

        } else {

            response.sendError(404, "URL not found");

        }
    }

    // Get Analytics / Stats API
    @GetMapping("/stats/{shortCode}")
    public Url getStats(@PathVariable String shortCode) {

        Optional<Url> urlOptional = urlService.getOriginalUrl(shortCode);

        if (urlOptional.isPresent()) {

            return urlOptional.get();

        } else {

            throw new RuntimeException("Short URL not found");

        }
    }
}