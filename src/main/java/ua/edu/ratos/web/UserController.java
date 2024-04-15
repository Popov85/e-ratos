package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.UserService;
import ua.edu.ratos.service.dto.in.EmailInDto;
import ua.edu.ratos.service.dto.in.patch.StringInDto;

import jakarta.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping(value = "/dep-admin/users/{userId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateName(@PathVariable Long userId, @Valid @RequestBody StringInDto dto) {
        String name = dto.getValue();
        userService.updateName(userId, name);
        log.debug("Updated User's name, userId = {}, new name = {}", userId, name);
    }

    @PatchMapping(value = "/dep-admin/users/{userId}/surname")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateSurname(@PathVariable Long userId, @Valid @RequestBody StringInDto dto) {
        String surname = dto.getValue();
        userService.updateSurname(userId, surname);
        log.debug("Updated User's surname, userId = {}, new surname = {}", userId, surname);
    }

    @PatchMapping(value = "/dep-admin/users/{userId}/email")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateEmail(@PathVariable Long userId, @Valid @RequestBody EmailInDto email) {
        userService.updateEmail(userId, email);
        log.debug("Updated User's email, userId = {}, new email = {}", userId, email);
    }

}
