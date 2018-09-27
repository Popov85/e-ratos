package ua.edu.ratos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ratos.domain.entity.Language;
import ua.edu.ratos.domain.repository.LanguageRepository;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/global-admin")
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    @GetMapping("/")
    public List<Language> get(Principal principal) {
        log.debug("Principal :: {}", principal);
        return languageRepository.findAll();
    }
}