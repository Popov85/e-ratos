package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ratos.service.InfoService;
import ua.edu.ratos.service.SchemeService;
import ua.edu.ratos.service.dto.out.SchemeInfoOutDto;
import ua.edu.ratos.service.dto.out.UserInfoOutDto;

@Slf4j
@RestController
@RequestMapping("/info")
@AllArgsConstructor
public class InfoController {

    private final SchemeService schemeService;

    private final InfoService infoService;


    //-----------------------------------------------------User Info----------------------------------------------------
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoOutDto> findUserForInfo(){
        UserInfoOutDto dto = infoService.getUserInfo();
        log.debug("UserInfo = {}", dto);
        return ResponseEntity.ok(dto);
    }

    //----------------------------------------------------Scheme Info---------------------------------------------------
    @GetMapping(value = "/schemes/{schemeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SchemeInfoOutDto> findSchemeForInfo(@PathVariable Long schemeId){
        log.debug("SchemeInfo for ID = {} requested..", schemeId);
        SchemeInfoOutDto info = schemeService.findByIdForInfo(schemeId);
        log.debug("SchemeInfo = {}", info);
        return ResponseEntity.ok(info);
    }
}
