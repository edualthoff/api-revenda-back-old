package br.ml.api.base.mercadolivre;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import br.ml.api.buscas.mercadolivre.BuscasMercadoLivre;
import br.ml.api.buscas.mercadolivre.BuscasMercadoLivreService;
import br.ml.api.buscas.mercadolivre.itens.ItemMercadoLivre;
import br.ml.api.buscas.mercadolivre.itens.ItensMercadoLivreService;
import br.ml.api.config.elk.IndexTenantDynamic;
import br.ml.api.crawler.BuscaCrawler;
import br.ml.api.crawler.mercadolivre.BuscarItemMercadoLivre;
import br.ml.api.mail.MailService;
import br.ml.api.produto.item.ItemProduto;
import br.ml.api.produto.item.ItemProdutoService;

@Component
@Scope(value="prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseCrawlerAnalise {
	private static final Logger log = LoggerFactory.getLogger(BaseCrawlerAnalise.class);

	@Autowired
	private ItensMercadoLivreService itensMercadoLivreService;
	@Autowired
	private BuscasMercadoLivreService buscasMLService;
	@Autowired
	private ApplicationContext context;
	@Autowired
	private MailService mailService;
	@Autowired
	private ItemProdutoService itemProdutoService;

	public BaseCrawlerAnalise(ItensMercadoLivreService itensMercadoLivreService,
			BuscasMercadoLivreService buscasMLService, ApplicationContext context, MailService mailService,
			ItemProdutoService itemProdutoService) {
		super();
		this.itensMercadoLivreService = itensMercadoLivreService;
		this.buscasMLService = buscasMLService;
		this.context = context;
		this.mailService = mailService;
		this.itemProdutoService = itemProdutoService;
	}

	public BaseCrawlerAnalise() {
	}

	public void build(BuscasMercadoLivre buscasMercadoLivre, String urlLink) {
		log.debug("Thread - baseCrawlerAnaliseThread - metodod: baseCrawlerAnaliseThread START -- id: {}, link: {}", buscasMercadoLivre.getId(), urlLink);
		this.context.getBean(IndexTenantDynamic.class).setTenantID(buscasMercadoLivre.getTenantID());

		BuscaCrawler<ItemMercadoLivre> buscaCrawler = new BuscarItemMercadoLivre();
		List<ItemMercadoLivre> itensList = new ArrayList<>();
		itensList.addAll(buscaCrawler.analisarUrl(urlLink));

		List<ItemMercadoLivre> listaProduto = itensMercadoLivreService.buscarPorIdUriBuscasAndStatus(buscasMercadoLivre.getId(), true);
		log.debug("Thread - baseCrawlerAnaliseThread - entrou Try {}, {}, {}, {}", buscasMercadoLivre.getId(), urlLink,
				listaProduto.size(), itensList.size());
		if (listaProduto.isEmpty()) {
			log.debug("Thread - baseCrawlerAnaliseThread - entrou Try lista null -- ");
			for (ItemMercadoLivre result : itensList) {
				// System.out.println("result: "+result.getId());
				result.setIdUriBuscas(Arrays.asList(buscasMercadoLivre.getId()));
				result.setTenantID(Arrays.asList(buscasMercadoLivre.getTenantID()));
				itensMercadoLivreService.adicionar(result);
			}
		} else {
			if ((itensList.isEmpty())) {
				log.debug("Thread - baseCrawlerAnaliseThread - ItensList vazia (null) - não retornou nenhuma itens do crawler MercadoLivre");
				buscasMercadoLivre.setErrorLink(true);
				buscasMLService.atualizar(buscasMercadoLivre.getId(), buscasMercadoLivre);
				Thread.currentThread().interrupt();
			} else {
				List<ItemMercadoLivre> newList = new ArrayList<>();
				log.debug("Thread - baseCrawlerAnaliseThread - ItensList -- retornou itens do crawler - idBusca: ");
				newList.addAll(this.verificarItemsVendidosAndEnviados(buscasMercadoLivre, listaProduto, itensList));
				// System.out.println("Lista baseCrawlerAnaliseThread - "+itensList.size()+"
				// "+newList.size());
				itensList.clear();
				itensList.addAll(newList);
			}
		}
		this.mountMailEnviar(buscasMercadoLivre, urlLink, itensList);
	}

	private List<ItemMercadoLivre> verificarItemsVendidosAndEnviados(BuscasMercadoLivre buscasMercadoLivre,
			List<ItemMercadoLivre> listaProdutoDB, List<ItemMercadoLivre> listAtual) {
		log.debug("Thread - baseCrawlerAnaliseThread - Metodo - verificarItemsVendidosAndEnviados START, IDBUSCA: {}", listaProdutoDB.size());
		List<ItemMercadoLivre> itensListBD = new ArrayList<>();
		//itensMercadoLivreService.atualizarTodos(listaProduto);
		/*
		 * if (listaProduto.isEmpty()) { log.
		 * debug("verificarItemsVendidosAndEnviados - nenhum resultado salvo no BancoDados - Lista Nova"
		 * ); return listAtual; } else {
		 */
		log.debug("verificarItemsVendidosAndEnviados - resultado salvo no BancoDados - entrou else");
		for (Iterator<ItemMercadoLivre> itemDB = listaProdutoDB.iterator(); itemDB.hasNext();) {
			ItemMercadoLivre item = itemDB.next();
			// log.debug("verificarItemsVendidosAndEnviados - Entrou no FOR - Validar
			// itens");
			// System.out.println("entrei for lista DB id: "+ item.getId());
			//item.setAtivo(false);
			for (Iterator<ItemMercadoLivre> atual = listAtual.iterator(); atual.hasNext();) {
				ItemMercadoLivre itemAtual = atual.next();
				// System.out.println("entrei for lista DB id: "+ itemAtual.getId());
				if (item.getId().equals(itemAtual.getId())) {
					// System.out.println("entrei for lista Atual Valido if id igual
					// "+item.getId()+" "+itemAtual.getId());
					if (item.getValor() == itemAtual.getValor()) {
						// System.out.println("entrei for lista valor true "+item.getValor()+" "+itemAtual.getValor());
						atual.remove();
					}
					itemDB.remove();
				}
			}
		}
		
		log.debug("verificarItemsVendidosAndEnviados - FOR Concluido Sucesso - listas db e atua size: {} e {}", listaProdutoDB.size(), itensListBD.size());
		if (listaProdutoDB.isEmpty() && listAtual.isEmpty()) {
			log.debug("---- Lista Empety -----");
			return listAtual;
		}
		for (Iterator<ItemMercadoLivre> itemDB = listaProdutoDB.iterator(); itemDB.hasNext();) {
			ItemMercadoLivre item = itemDB.next();
			item.setAtivo(false);
			//System.out.println("ativo: "+item.isAtivo());
			itensListBD.add(item);
		}
		for (Iterator<ItemMercadoLivre> atual = listAtual.iterator(); atual.hasNext();) {
			ItemMercadoLivre itemAtual = atual.next();
			itemAtual.setIdUriBuscas(Arrays.asList(buscasMercadoLivre.getId()));
			itemAtual.setTenantID(Arrays.asList(buscasMercadoLivre.getTenantID()));
			itemAtual.setDate_created(Instant.now());
			//itemAtual.getIdUriBuscas().containsAll(itemAtual.getIdUriBuscas());
			/** Old Logica Aparetemente desnecessario
			if(Optional.ofNullable(itemAtual.getIdUriBuscas()).isEmpty() || !itemAtual.getIdUriBuscas().contains(buscasMercadoLivre.getId())) {
				System.out.println("optional isempty id uri");
				itemAtual.setIdUriBuscas(Arrays.asList(buscasMercadoLivre.getId()));
				if(Optional.ofNullable(itemAtual.getTenantID()).isEmpty() || !itemAtual.getTenantID().contains(buscasMercadoLivre.getTenantID())) {
					itemAtual.setTenantID(Arrays.asList(buscasMercadoLivre.getTenantID()));
					itemAtual.setDate_created(Instant.now());
				}
			}*/
			
			itensListBD.add(itemAtual);
		}
		log.debug("verificarItemsVendidosAndEnviados - Atualizar Lista de Itens no BD");
		itensMercadoLivreService.atualizarTodos(itensListBD);
		return listAtual;
		// }
	}

	private void mountMailEnviar(BuscasMercadoLivre buscasMercadoLivre, String urlLink, List<ItemMercadoLivre> itensList) {
		log.debug("Metodo mountMailEnviar - Start - list index size {}" + itensList.size());
		ItemProduto itemProduto = this.itemProdutoService.buscarItensPorId(buscasMercadoLivre.getIdProduto());
		StringBuilder strBuilder = new StringBuilder();
		StringBuilder strAssuntoBuilder = new StringBuilder(
				itemProduto.getModelo() + " - " + buscasMercadoLivre.getCondicao().getCondicao());
		// String assunto = itemProduto.getModelo() + " - " +
		// buscasMercadoLivre.getCondicao().getCondicao();

		if (itensList.isEmpty()) {
			strAssuntoBuilder.append(" - Vazio");
			strBuilder.append("Não tem nenhum item novo ou a lista esta com error");
		} else {
			for (ItemMercadoLivre item : itensList) {
				strBuilder.append(item.getLinkUrl() +  "\n Titulo: " + item.getTitulo() + "\n Condicao: " + item.getCondicao() + "\n Tipo: "
						+ item.getTipoAnuncio() + "\n Valor: " + item.getValor() + "\n \n");
			}
		}

		// assunto = itemProduto.getModelo() + " - " +
		// buscasMercadoLivre.getCondicao().getCondicao();
		String mensagem = buscasMercadoLivre.getDescricao() + "\n \n" + urlLink + "\n \n" + strBuilder.toString();
		// log.debug("Metodo mountMailEnviar - mensagem {}", mensagem);
		this.mailService.sendMail("revendascerta@gmail.com", strAssuntoBuilder.toString(), mensagem);
	}
}
