package ua.edu.ratos.web.report;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ratos.service.dto.in.report.ReportOnContentInDto;
import ua.edu.ratos.service.dto.out.report.ReportOnContent;
import ua.edu.ratos.service.report.ReportOnContentService;
import ua.edu.ratos.service.validator.ReportOnContentInDtoValidator;

import jakarta.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class ReportOnContentController {

    private final ReportOnContentService reportOnContentService;

    private final ReportOnContentInDtoValidator validator;

    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        binder.addValidators(validator);
    }


    @PostMapping(value = "/department/report-on-content", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReportOnContent getReportOnContent(@Valid @RequestBody ReportOnContentInDto dto) {
        return reportOnContentService.getReportOnContent(dto);
    }

}
