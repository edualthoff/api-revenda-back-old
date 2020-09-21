package br.ml.api.config.elk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import br.edx.exception.config.ApiMessageSource;
import br.edx.exception.type.ApiNotFoundException;

@Component
public class TenantInterceptorService implements HandlerInterceptor {

	@Autowired
	private IndexTenantDynamic indexTenant;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("interceptor "+request.getHeader(IndexTenantDynamic.TENANT_VAR));
		if (!request.getHeader(IndexTenantDynamic.TENANT_VAR).isEmpty()) {
			indexTenant.setTenantID(request.getHeader(IndexTenantDynamic.TENANT_VAR));
			return true;
		};
		throw new ApiNotFoundException(ApiMessageSource.toMessageSetObject("tenantID.not.valid", "Link Produto"),
				ApiMessageSource.toMessage("tenantID.error.code"),
				ApiMessageSource.toMessage("tenantID.error.interceptor"));
	}

}
