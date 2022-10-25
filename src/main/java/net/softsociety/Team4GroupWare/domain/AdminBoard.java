package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminBoard {
	String company_code;
	String admin_board_code;
	String admin_board_type;
	String admin_board_title;
	String admin_board_contents;
	String admin_board_inputdate;
	String admin_board_updatedate;
	String admin_board_hits;
	String writer_code;
}
