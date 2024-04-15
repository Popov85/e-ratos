package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.edu.ratos.service.GroupService;
import ua.edu.ratos.service.dto.in.GroupInDto;
import ua.edu.ratos.service.dto.out.GroupExtendedOutDto;
import ua.edu.ratos.service.dto.out.GroupOutDto;

import jakarta.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/instructor")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping(value = "/groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody GroupInDto dto) {
        final Long groupId = groupService.save(dto);
        log.debug("Saved Group, groupId = {}", groupId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(groupId).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/groups/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupOutDto> findOne(@PathVariable Long groupId) {
        GroupOutDto dto = groupService.findOneForEdit(groupId);
        log.debug("Retrieved Group = {}", dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(value = "/groups/{groupId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable Long groupId, @RequestParam String name) {
        groupService.updateName(groupId, name);
        log.debug("Updated Group's name, groupId = {}, new name = {}", groupId, name);
    }

    //------------------------------------------------STUDENTS OPERATIONS-----------------------------------------------
    @PostMapping(value = "/groups/{groupId}/students/{studentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void addStudent(@PathVariable Long groupId, @PathVariable Long studentId) {
        groupService.addStudent(groupId, studentId);
        log.debug("Added Student to Group, studentId = {}, groupId = {}", studentId, groupId);
    }

    @DeleteMapping(value = "/groups/{groupId}/students/{studentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeStudent(@PathVariable Long groupId, @PathVariable Long studentId) {
        groupService.removeStudent(groupId, studentId);
        log.debug("Removed Student from Group, studentId = {}, groupId = {}", studentId, groupId);
    }

    @DeleteMapping("/groups/{groupId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long groupId) {
        groupService.deleteById(groupId);
        log.debug("Deleted Group, groupId = {}", groupId);
    }

    //---------------------------------------------------Staff table----------------------------------------------------
    @GetMapping(value = "/groups/by-staff", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GroupExtendedOutDto> findAllByStaffId(@PageableDefault(sort = {"name"}, value = 50) Pageable pageable) {
        return groupService.findAllByStaffId(pageable);
    }

    @GetMapping(value = "/groups/by-staff", params = "letters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GroupExtendedOutDto> findAllByStaffIdAndNameLettersContains(@RequestParam String letters, @PageableDefault(sort = {"name"}, value = 50) Pageable pageable) {
        return groupService.findAllByStaffIdAndNameLettersContains(letters, pageable);
    }

    @GetMapping(value = "/groups/by-department", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GroupExtendedOutDto> findAllByDepartmentId(@PageableDefault(sort = {"name"}, value = 50) Pageable pageable) {
        return groupService.findAllByDepartmentId(pageable);
    }

    @GetMapping(value = "/groups/by-department", params = "letters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GroupExtendedOutDto> findAllByDepartmentIdAndNameLettersContains(@RequestParam String letters, @PageableDefault(sort = {"name"}, value = 50) Pageable pageable) {
        return groupService.findAllByDepartmentIdAndNameLettersContains(letters, pageable);
    }
}
