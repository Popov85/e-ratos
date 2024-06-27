package ua.edu.ratos.service.dto.out;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ua.edu.ratos.service.transformer.mapper.SchemeInfoMapper;

/**
 * See also:
 * {@link SchemeInfoMapper}
 */

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SchemeInfoOutDto {

    private Long schemeId;

    private String name;

    private String strategy;

    // Quantity of questions
    private int questions;

    // time limit per question in sec, negative value for unlimited
    private long timings;

    // Is there time limit for batch?
    private boolean batchTimeLimited;

    private ModeMinOutDto mode;

    private String course;

    private String staff;

    private Boolean active;
}
