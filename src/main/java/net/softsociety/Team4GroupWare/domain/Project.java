package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    String pj_code;
    String employee_code;
    String company_code;
    String pj_name;
    String pj_startdate;
    String pj_enddate;
    String pj_leader_id;
    String pj_leader_name;
    String pj_content;
    int pj_state;
    int pj_percent;
}
