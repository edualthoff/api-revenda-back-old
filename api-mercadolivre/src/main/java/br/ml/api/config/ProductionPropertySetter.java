package br.ml.api.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProductionPropertySetter {

	//@Value("${webdriver.firefox}")
	//private String firefoxDrive;
	
	@Autowired
	private Environment env;
	
	@PostConstruct
	public void setProperty() {
		//System.out.println("JRE Architecture --> "+System.getProperty("os.name")+" bit.");
		switch (System.getProperty("os.name").split(" ")[0].toLowerCase()) {
		case "windows": {
			System.setProperty("webdriver.gecko.driver", env.getProperty("webdriver.windows.firefox"));
			break;
		}
		case "linux": {
			System.setProperty("webdriver.gecko.driver", env.getProperty("webdriver.linux.firefox"));
			break;
		}
		default:
			throw new IllegalArgumentException("sistema não está configurado: " + System.getProperty("os.name"));
		}
	}
}
