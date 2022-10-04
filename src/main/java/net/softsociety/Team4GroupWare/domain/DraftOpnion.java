package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DraftOpnion {
	String opinion_code;
	String draft_code;
	String opinion_writer_code;
	String opinion_writer_name;
	String opinion_contents;
	String opinion_date;
}
