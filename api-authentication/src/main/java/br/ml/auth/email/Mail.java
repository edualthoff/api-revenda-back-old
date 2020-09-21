package br.ml.auth.email;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class Mail {
	
    private String from;
    private String to;
    private String subject;
    private String content;
    
}
