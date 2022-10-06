package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {
    String pj_member_code;
    String pj_code;
    String employee_code;
    String position_type;
    int pj_part_step;
    String employee_id;
    String employee_name;
    String organization;
}
