package ua.edu.ratos.web;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ratos.service.StrategyService;
import ua.edu.ratos.service.dto.out.StrategyOutDto;

import java.util.Set;

@RestController
@AllArgsConstructor
public class StrategyController {

    private final StrategyService strategyService;

    @GetMapping(value="/department/strategies-dropdown/all-strategies-by-ratos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<StrategyOutDto> findAll() {
        return strategyService.findAll();
    }
}
