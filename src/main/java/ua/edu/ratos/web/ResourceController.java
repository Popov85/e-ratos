package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.ResourceService;
import ua.edu.ratos.service.dto.in.ResourceInDto;
import ua.edu.ratos.service.dto.in.patch.StringInDto;
import ua.edu.ratos.service.dto.in.patch.UrlInDto;
import ua.edu.ratos.service.dto.out.ResourceOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(value = "/instructor/resources", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceOutDto> save(@Valid @RequestBody ResourceInDto dto) {
        ResourceOutDto resourceOutDto = resourceService.save(dto);
        log.debug("Saved Resource, resId = {}", resourceOutDto.getResourceId());
        return ResponseEntity.ok(resourceOutDto);
    }

    @PutMapping(value = "/instructor/resources", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceOutDto> update(@Valid @RequestBody ResourceInDto dto) {
        ResourceOutDto resourceOutDto = resourceService.update(dto);
        log.debug("Updated Resource = {}", dto);
        return ResponseEntity.ok(resourceOutDto);
    }

    @PatchMapping(value = "/instructor/resources/{resId}/link", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateLink(@PathVariable Long resId, @Valid @RequestBody UrlInDto dto) {
        String link = dto.getValue();
        resourceService.updateLink(resId, link);
        log.debug("Updated Resource's link, resId = {}, new link = {}", resId, link);
    }

    @PatchMapping(value = "/instructor/resources/{resId}/description", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateDescription(@PathVariable Long resId, @Valid @RequestBody StringInDto dto) {
        String description = dto.getValue();
        resourceService.updateDescription(resId, description);
        log.debug("Updated Resource's description, resId = {}, new description = {}", resId, description);
    }


    @DeleteMapping("/instructor/resources/{resId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long resId) {
        try {
            resourceService.deleteById(resId);
            log.debug("Deleted Resource, resId = {}", resId);
        } catch (Exception e) {
            resourceService.deleteByIdSoft(resId);
            log.debug("Soft deleted Resource, resId = {}", resId);
        }
    }

    //----------------------------------------------------Staff table (all)---------------------------------------------
    @GetMapping(value = "/department/resources-table/all-res-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ResourceOutDto> findAllByDepartment() {
        return resourceService.findAllByDepartment();
    }

    //---------------------------------------------Staff table for future references------------------------------------
    @GetMapping(value = "/department/resources/by-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ResourceOutDto> findAllByStaffId(@PageableDefault(sort = {"lastUsed"}, direction = Sort.Direction.DESC, value = 50) Pageable pageable) {
        return resourceService.findByStaffId(pageable);
    }

    @GetMapping(value = "/department/resources/by-staff", params = "letters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ResourceOutDto> findAllByStaffIdAndDescriptionLettersContains(@RequestParam String letters, @PageableDefault(sort = {"description"}, value = 50) Pageable pageable) {
        return resourceService.findByStaffIdAndDescriptionLettersContains(letters, pageable);
    }

    @GetMapping(value = "/department/resources/by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ResourceOutDto> findAllByDepartmentId(@PageableDefault(sort = {"lastUsed"}, direction = Sort.Direction.DESC, value = 50) Pageable pageable) {
        return resourceService.findByDepartmentId(pageable);
    }

    @GetMapping(value = "/department/resources/by-department", params = "letters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ResourceOutDto> findAllByDepartmentIdAndDescriptionLettersContains(@RequestParam String letters, @PageableDefault(sort = {"description"}, value = 50) Pageable pageable) {
        return resourceService.findByDepartmentIdAndDescriptionLettersContains(letters, pageable);
    }
}
