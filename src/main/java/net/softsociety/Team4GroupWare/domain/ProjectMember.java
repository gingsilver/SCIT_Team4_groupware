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
    String pj_member_id;
    String pj_member_name;
    String pj_member_department;
    String pj_member_position;
    int pj_part_step;
}
