package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.ThemeService;
import ua.edu.ratos.service.dto.in.ThemeInDto;
import ua.edu.ratos.service.dto.in.patch.StringInDto;
import ua.edu.ratos.service.dto.out.ThemeExtOutDto;
import ua.edu.ratos.service.dto.out.ThemeMapOutDto;
import ua.edu.ratos.service.dto.out.ThemeMinOutDto;
import ua.edu.ratos.service.dto.out.ThemeOutDto;

import jakarta.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @PostMapping(value = "/instructor/themes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThemeOutDto> save(@Valid @RequestBody ThemeInDto dto) {
        ThemeOutDto themeOutDto = themeService.save(dto);
        log.debug("Saved Theme, themeId = {}", themeOutDto.getThemeId());
        return ResponseEntity.ok().body(themeOutDto);
    }

    @PutMapping(value = "/instructor/themes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThemeOutDto> update(@Valid @RequestBody ThemeInDto dto) {
        ThemeOutDto themeOutDto = themeService.update(dto);
        log.debug("Updated Theme, themeId = {}", themeOutDto.getThemeId());
        return ResponseEntity.ok().body(themeOutDto);
    }

    @PatchMapping(value = "/instructor/themes/{themeId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable Long themeId, @Valid @RequestBody StringInDto dto) {
        String name = dto.getValue();
        themeService.updateName(themeId, name);
        log.debug("Updated Theme's name themeId = {}, new name = {}", themeId, name);
    }

    @DeleteMapping("/instructor/themes/{themeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long themeId) {
        try {
            themeService.deleteById(themeId);
            log.debug("Deleted Theme, themeId = {}", themeId);
        } catch (Exception e) {
            themeService.deleteByIdSoft(themeId);
            log.debug("Soft deleted Theme, themeId = {}", themeId);
        }
    }

    //-------------------------------------------Staff min drop-down----------------------------------------------------

    @GetMapping(value="/department/themes-dropdown/all-themes-by-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ThemeMinOutDto> findAllForDropDownByStaffId() {
        return themeService.findAllForDropdownByStaffId();
    }

    @GetMapping(value="/department/themes-dropdown/all-themes-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ThemeMinOutDto> findAllForDropDownByDepartmentId() {
        return themeService.findAllForDropdownByDepartmentId();
    }

    @GetMapping(value="/fac-admin/themes-dropdown/all-themes-by-department", params = "depId", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ThemeMinOutDto> findAllForDropDownByDepartmentId(@RequestParam final Long depId) {
        return themeService.findAllForDropdownByDepartmentId(depId);
    }

    //----------------------------------------------Staff table---------------------------------------------------------

    @GetMapping(value="/department/themes-table/all-themes-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ThemeOutDto> findAllForTableByDepartment() {
        return themeService.findAllForTableByDepartment();
    }

    @GetMapping(value="/fac-admin/themes-table/all-themes-by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ThemeOutDto> findAllForTableByDepartmentId(@RequestParam Long depId) {
        return themeService.findAllForTableByDepartmentId(depId);
    }

    //---------------------------------------------Scheme creating support with levels----------------------------------
    /**
     * Peek functionality;
     * Use this endpoint for scheme creating support to obtain all existing types and levels in this theme.
     * It works rather fast (100-500ms) with questions per theme = 200-500 pieces,
     * but requires to fetch all questions from theme for calculations.
     * So it will presumably work slower with questions per theme >1000. Be careful to use it in this case!
     * @param themeId theme you want to add to scheme
     * @return DTO with details on question types and levels present in this theme.
     * Front-end has to prevent a user from setting values greater than these limits.
     */
    @GetMapping(value = "/instructor/themes-map/{themeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ThemeMapOutDto getQuestionTypeLevelMapByThemeId(@PathVariable Long themeId) {
        return themeService.getQuestionTypeLevelMapByThemeId(themeId);
    }

    /**
     * You may want to find out how many questions of which type the theme contains;
     * Use this endpoint for this purpose.
     * @param themeId themeId
     * @return extended DTO
     */
    @GetMapping(value = "/instructor/theme-questions-details/{themeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ThemeExtOutDto findByIdForQuestionsDetails(@PathVariable Long themeId) {
        return themeService.findByIdForQuestionsDetails(themeId);
    }

}
