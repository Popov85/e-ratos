package ua.edu.ratos.web.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ratos.config.properties.AppProperties;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.cache.CacheSupport;

import java.security.Principal;

@Slf4j
@RestController
public class CacheSupportController {

    private final CacheSupport cacheSupport;

    private final SecurityUtils securityUtils;

    @Autowired
    public CacheSupportController(CacheSupport cacheSupport, SecurityUtils securityUtils) {
        this.cacheSupport = cacheSupport;
        this.securityUtils = securityUtils;
    }

    /**
     * Any department staff can load a scheme to cache.
     * @param schemeId scheme
     */
    @GetMapping("/department/cache/load-one/{schemeId}")
    public void loadScheme(@PathVariable Long schemeId) {
        cacheSupport.loadScheme(schemeId);
    }

    /**
     * Any instructor can load the whole course into cache
     * @param courseId course
     */
    @GetMapping("/instructor/cache/load-course/{courseId}")
    public void loadCourse(@PathVariable Long courseId) {
        cacheSupport.loadCourse(courseId);
    }

    /**
     * Only department admin can load the whole department questions into the cache
     */
    @GetMapping("/dep-admin/cache/load-department")
    public void loadDepartment() {
        cacheSupport.loadDepartment(securityUtils.getAuthDepId());
    }

    /**
     * Single-threaded option to launch loading multiple schemes into cache based on selected strategy
     * @param strategy selected strategy
     */
    @GetMapping("/org-admin/cache/load-many/{strategy}")
    public void loadSchemes(@PathVariable AppProperties.Init.Caching strategy) {
        cacheSupport.loadMany(strategy);
    }
}
