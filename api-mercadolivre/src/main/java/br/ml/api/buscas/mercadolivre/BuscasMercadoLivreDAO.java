package br.ml.api.buscas.mercadolivre;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuscasMercadoLivreDAO extends ElasticsearchRepository<BuscasMercadoLivre, String>{

	Iterable<BuscasMercadoLivre> findByStatus(boolean status);
	
	Iterable<BuscasMercadoLivre> findAllByStatusAndErrorLink(boolean status, boolean errorLink);
	
	List<BuscasMercadoLivre> findByStatusAndErrorLink(boolean status, boolean errorLink);
	
	List<BuscasMercadoLivre> findByStatusAndErrorLinkAndSchedulingTimeNextDateLessThanEqual(boolean status, boolean errorLink, Instant dateTime);
	
	@Query("{\"bool\": {\r\n" + 
			"      \"must\": [\r\n" + 
			"          {\"match\": {\"status\": ?0}},\r\n" + 
			"          {\"match\": {\"errorLink\": ?1}}\r\n" + 
			"      ],\r\n" + 
			"      \"filter\": [\r\n" + 
			"        {\r\n" + 
			"          \"nested\": {\r\n" + 
			"            \"path\": \"schedulingTime\",\r\n" + 
			"            \"query\": {\r\n" + 
			"              \"range\": {\r\n" + 
			"                \"schedulingTime.nextDate\": {\r\n" + 
			"                  \"lte\": \"?2\"\r\n" + 
			"                }\r\n" + 
			"              }\r\n" + 
			"            }\r\n" + 
			"          }\r\n" + 
			"        }\r\n" + 
			"      ]\r\n" + 
			"    }\r\n" + 
			"  }}")
	Page<BuscasMercadoLivre> findBySchedulingTimeNextDateLessThanEqual(boolean status, boolean errorLink, Instant dateTime, PageRequest pageRequest);

	
	//Iterable<BuscasMercadoLivre> findByErrorLink(boolean status, boolean errorLink);
	//findByStatusAndErrorLinkAndSchedulingTimeNextDateIsLessThanEqual
}
