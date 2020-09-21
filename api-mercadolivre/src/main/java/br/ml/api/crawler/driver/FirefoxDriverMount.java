package br.ml.api.crawler.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FirefoxDriverMount {

	private FirefoxDriverMount() {
	}
	
	public WebDriver mountDriver() {
		FirefoxProfile prof = new FirefoxProfile();
		prof.setAcceptUntrustedCertificates(true);
		prof.setAssumeUntrustedCertificateIssuer(false);
		prof.setPreference("javascript.enabled", true);
		prof.setPreference("network.proxy.type", 0);
		prof.setPreference("general.useragent.override", "Mozilla/5.0 (X11; CrOS x86_64 8172.45.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.64 Safari/537.36");

		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		options.addArguments("--width=1900");
		options.addArguments("--height=1000");
		options.setProfile(prof);
		
        return new FirefoxDriver(options);
	}
	
	
	public static Builder driver() {
		return new Builder();
	}
	
	public static final class Builder { 
		private Builder() {}
		
		public WebDriver build() {
			return new FirefoxDriverMount().mountDriver();
		}
	}
}
