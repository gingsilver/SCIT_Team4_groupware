package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPart {
    String pj_part_code;
    String pj_code;
    String employee_id;
    String employee_name;
    String pj_part_content;
    String pj_part_completion;
}
