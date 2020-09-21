package br.ml.api.config.elk;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


@Component
@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode= ScopedProxyMode.TARGET_CLASS)
public class IndexTenantDynamic {

	private String index = "";
	private String tenantID;
	static String PREFIX_SIMBOL = "_";
	static String TENANT_VAR = "tenantID";
	
	public IndexTenantDynamic() {
		super();
	}

	public String getIndex() {
		//System.out.println("dynamic "+this.index);
		return index;
	}
	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.index = PREFIX_SIMBOL+tenantID;
		this.tenantID = tenantID;
		//System.out.println("setTenantID "+this.index+" "+this.tenantID);
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
}
