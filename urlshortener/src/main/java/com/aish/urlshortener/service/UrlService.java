package com.aish.urlshortener.service;

import com.aish.urlshortener.model.Url;
import com.aish.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String generateShortCode() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            shortCode.append(characters.charAt(random.nextInt(characters.length())));
        }

        return shortCode.toString();
    }

    public Url createShortUrl(String originalUrl) {
        String shortCode = generateShortCode();

        Url url = new Url(originalUrl, shortCode);
        return urlRepository.save(url);
    }

    public Optional<Url> getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }
    public Url save(Url url) {
        return urlRepository.save(url);
    }
}