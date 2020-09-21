package br.ml.api.config.elk;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.logging.log4j.core.config.Order;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;

@Configuration
@Order(0)
public class ElasticsearchConfig {

	@Value("${elastich.rest.uris}")
	private String elastickHost;
	@Value("${elastich.rest.port}")
	private int elastickPort;
	@Value("${elastich.rest.password}")
	private String password;
	@Value("${elastich.rest.username}")
	private String username;

	private RestHighLevelClient client;
	
	/*
	  @Bean(destroyMethod = "close") public RestHighLevelClient client() {
	  System.out.println("teste: "+elastickHost); TrustStrategy
	  acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
	  
	  SSLContext sslContext = null; try { sslContext =
	  org.apache.http.ssl.SSLContexts.custom() .loadTrustMaterial(null,
	  acceptingTrustStrategy) .build(); } catch (KeyManagementException e) {
	  e.printStackTrace(); } catch (NoSuchAlgorithmException e) {
	  e.printStackTrace(); } catch (KeyStoreException e) { e.printStackTrace(); }
	  final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
	  .connectedTo(elastickHost+":"+elastickPort) .usingSsl(sslContext)
	  .withBasicAuth(username, password) .withSocketTimeout(Duration.ofSeconds(30))
	  .withConnectTimeout(Duration.ofSeconds(15)) .build();
	  
	  return RestClients.create(clientConfiguration).rest(); }
	 */

	//@Bean(destroyMethod = "close")
	@Bean
	public RestHighLevelClient client() {
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
					.build();
			RestClientBuilder builder = RestClient.builder(new HttpHost(elastickHost, elastickPort, "https"))
					.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
							.setSSLContext(sslContext)
							.setDefaultCredentialsProvider(credentialsProvider)
							//.setConnectionManagerShared(true)
							.setMaxConnPerRoute(30)
							.setMaxConnTotal(50)
							.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build()))
					.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
						@Override
						public RequestConfig.Builder customizeRequestConfig(
								RequestConfig.Builder requestConfigBuilder) {
							return requestConfigBuilder
									.setConnectTimeout(5000)
									.setSocketTimeout(60000)
									.setConnectionRequestTimeout(0);
						}
					});
			client = new RestHighLevelClient(builder);
           return client;
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		return null;
	}
    /*
     * it gets called when bean instance is getting removed from the context if
     * scope is not a prototype
     */
    /*
     * If there is a method named shutdown or close then spring container will try
     * to automatically configure them as callback methods when bean is being
     * destroyed
     */
    @PreDestroy
    public void clientClose() {
        try {
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate(ElasticsearchConverter elasticsearchConverter) {
        return new ElasticsearchRestTemplate(client(), elasticsearchConverter);
    }

}
