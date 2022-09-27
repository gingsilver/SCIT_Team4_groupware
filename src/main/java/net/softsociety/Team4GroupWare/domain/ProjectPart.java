package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPart {
    String pj_part_code;
    String pj_member_code;
    String pj_part_member_id;
    String pj_part_name;
    String pj_part_content;
    String pj_part_savedate;
    String pj_part_completion;
}
