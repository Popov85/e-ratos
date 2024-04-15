package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.edu.ratos.service.UserSelfService;
import ua.edu.ratos.service.dto.in.PasswordsInDto;
import ua.edu.ratos.service.dto.in.UserMinInDto;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserSelfController {
    
    private final UserSelfService userSelfService;
    
    @PutMapping("/profile")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateProfile(@Valid @RequestBody UserMinInDto dto) {
        userSelfService.updateProfile(dto);
        log.debug("Updated user's base profile, profile = {}", dto);
    }


    @PutMapping("/profile/password")
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePassword(@Valid @RequestBody PasswordsInDto dto) {
        userSelfService.updatePassword(dto.getOldPass(), dto.getNewPass());
        log.debug("Updated user's password, passwords = {}", dto);
    }
}
