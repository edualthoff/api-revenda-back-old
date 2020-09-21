package br.ml.api.crawler.mercadolivre;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edx.exception.config.ApiMessageSource;
import br.edx.exception.type.ApiNotFoundException;
import br.ml.api.buscas.mercadolivre.itens.ItemMercadoLivre;
import br.ml.api.crawler.BuscaCrawler;
import br.ml.api.crawler.driver.FirefoxDriverMount;
import lombok.Getter;

@Getter
public class BuscarItemMercadoLivre implements Serializable, BuscaCrawler<ItemMercadoLivre> {
	private static final long serialVersionUID = 4872395434408005507L;
	private static final Logger log = LoggerFactory.getLogger(BuscarItemMercadoLivre.class);

	private List<ItemMercadoLivre> itens = new ArrayList<>();
	private WebDriver driver;
	
	@Override
	public List<ItemMercadoLivre> analisarUrl(String url) {
        driver  = FirefoxDriverMount.driver().build();
		try {
			driver.get(url);

			//if(doc.head().toString().contains("prefix")) {
			if(isElementPresent(".//div/section/ol//li")) {
				System.out.println("desktop");
				this.verificarPagina("desktop", this.driver );
			} else {
				log.debug("mobile");
				log.debug("dox: \n "+this.driver.getPageSource());
			}
			
			if (url.contains(TagsRequestEnum.ORDER_PRICE.getValue())) {
				tipoDeAnuncio();
			}

		} catch (IOException e) {
			log.error(url, e);
			throw new ApiNotFoundException(ApiMessageSource.toMessageSetDiversesObject("objeto.add.error",
					new String[] { url, "Mercado Livre item" }));
		}
		finally {
			System.out.println("close driver");
			driver.close();
		}
		return itens;
	}

	private void verificarPagina(String value, WebDriver element ) throws IOException {
		boolean paginar = true;
		switch (value) {
		case "desktop": {
			do {
				this.varrerHtmlDesktop(element); //.attr("href"));
				if (this.isElementPresent("//div/section/div[2]//*[contains(@class, 'next')]/a")) {
					log.debug("link paginacao:: " + element.findElement(By.xpath("//div/section/div[2]//*[contains(@class, 'next')]/a")).getAttribute("href"));
					try {
						Thread.sleep(500);
						this.driver.get(element.findElement(By.xpath("//div/section/div[2]//*[contains(@class, 'next')]/a")).getAttribute("href")); 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					element = this.driver;
				} else {
					paginar = false;
				};
				paginar = false;
			} while (paginar);
			break;
		}
		case "mobile": {
			do {
				this.varrerHtmlDesktop(element); //.attr("href"));
				//this.varrerHtmlMobile(doc);
				if (this.isElementPresent(".//div/section/div[2]//*[contains(@class, 'next')]/a")) {
					log.debug("link paginacao:: " + element.findElement(By.xpath("//div/section/div[2]//*[contains(@class, 'next')]/a")).getAttribute("href"));
					this.driver.get(element.findElement(By.xpath("//div/section/div[2]//*[contains(@class, 'next')]/a")).getAttribute("href")); 
					element = this.driver;
				} else {
					paginar = false;
				};
				paginar = false;
			} while (paginar);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + value);
		}
	}
	/**
	 * Passa o Documento/Elemento qual contem o HTML com a lista dos itens a ser -- Site Desktop
	 * salvo
	 * 
	 * @param ee
	 */
	private void varrerHtmlDesktop(WebDriver e) {
		log.debug("varrerHtmlItens doc a ser analisado");
		log.debug("driver2 size: \n {}",e.findElements(By.xpath(".//div/section/ol//li")).size());
		try {
		for(int i = 1; i <= e.findElements(By.xpath(".//div/section/ol//li")).size(); i++) {
			//log.debug("Tag name web: {}, {}, \n {} ",e.toString(), e.findElement(By.xpath("//section/ol/li["+i+"]//a[contains(@href,'')]")).getAttribute("href"), e.findElement(By.xpath("//section/ol/li["+i+"]//a[contains(@href,'')]")).getAttribute("href"));
			ItemMercadoLivre it = new ItemMercadoLivre();
			it.setTitulo(e.findElement(By.xpath("//div/section/ol/li["+i+"]//h2")).getText());
			it.setLinkUrl(e.findElement(By.xpath("//div/section/ol/li["+i+"]//a[contains(@href,'')]")).getAttribute("href"));
			
			it.setCondicao((this.isElementPresent("//div/section/ol/li["+i+"]//*[contains(translate(normalize-space(text()), \"\", \"\"), \"Usado\") or contains(translate(normalize-space(text()), \"\", \"\"), \"Recondicionado\")]")) ? 
					TagsAnuncioEnum.getTagsValue(e.findElement(By.xpath("//div/section/ol/li["+i+"]//*[contains(translate(normalize-space(text()), \"\", \"\"), \"Usado\") or contains(translate(normalize-space(text()), \"\", \"\"), \"Recondicionado\")]"))
							.getText().split(" ")[0].trim())
							: TagsAnuncioEnum.getTagsValue("novo"));
			
			it.setCidade((e.findElement(By.xpath("//div/section/ol/li["+i+"]//*[contains(translate(normalize-space(text()), \" \", \"\"), \"Usado\")]")).getText().split("-").length > 1
					? e.findElement(By.xpath("//div/section/ol/li["+i+"]//*[contains(translate(normalize-space(text()), \" \", \"\"), \"Usado\")]")).getText().split("-")[1].trim()
					: ""));
			
			//div/section/ol/li[1] //*[contains(text(), 'R$')]/ancestor::*[position()=1]/*[contains(@class, 'fraction')]
			it.setValor(Double.parseDouble(e.findElement(By.xpath("//div/section/ol/li["+i+"] //*[contains(text(), 'R$')]/ancestor::*[position()=1]/*[contains(@class, 'fraction')]")).getText().replace(".", "") + "."
					+ (this.isElementPresent("//div/section/ol/li["+i+"] //*[contains(text(), 'R$')]/ancestor::*[position()=1]/*[contains(@class, 'decimals')]") ?
							e.findElement(By.xpath("//div/section/ol/li["+i+"] //*[contains(text(), 'R$')]/ancestor::*[position()=1]/*[contains(@class, 'decimals')]")).getText() : "")));
			
			//System.out.println("valor: "+it.getValor());
			it.setAtivo(true);
			Matcher m = Pattern.compile("((.com.*\\/)([^?#]*))").matcher(it.getLinkUrl());
			if (m.find()) {
				it.setId(m.group().replace(".com.br/", "").trim());
			}

			itens.add(it);
		
			}
		} catch (NoSuchElementException noSuchElementException) {
			log.error("Error no for elemento não encontrado, varrerHtmlDesktop - noSuchElementException ");
			log.error(e.getCurrentUrl(), e);
		}
	}
	/**
	 * Passa o Documento/Elemento qual contem o HTML com a lista dos itens a ser -- Site MOBILW
	 * salvo
	 * 
	 * @param element
	
	private void varrerHtmlMobile(Element element) {
		log.debug("varrerHtmlMobile doc a ser analisado");

		for (Element e : element.getElementById("ui-search-results").getElementsByClass("ui-search-result__wrapper")) {
			ItemMercadoLivre it = new ItemMercadoLivre();
			it.setTitulo(e.getElementsByClass("ui-search-item__title").text());
			it.setLinkUrl(e.getElementsByTag("a").attr("href"));
			it.setCondicao(
					TagsAnuncioEnum.getTagsValue(e.getElementsByClass("ui-search-item__details").text().split("-")[0].trim()));
			it.setCidade((e.getElementsByClass("ui-search-item__details").text().split("-").length > 1
					? e.getElementsByClass("ui-search-item__details").text().split("-")[1].trim()
					: ""));
			it.setValor(Double.parseDouble(e.getElementsByClass("price-tag-fraction").text().replace(".", "") + "."
					+ e.getElementsByClass("price-tag-fraction-decimals").text()));

			it.setAtivo(true);
			Matcher m = Pattern.compile("((.com.*\\/)([^?#]*))").matcher(it.getLinkUrl());
			if (m.find()) {
				it.setId(m.group().replace(".com.br/", "").trim());
			}

			itens.add(it);
		}
	} */

	/**
	 * Varre a lista de itens e classifica o item como anuncio Pago ou Gratis.. É
	 * efetivo quando o o LINK do anuncio está ordenado por valor do Menor para o
	 * Maior
	 */
	private void tipoDeAnuncio() {
		double vBase = 0.0;
		double vAtual = 0.0;
		boolean gratis = true;
		log.debug("BuscarItemMercadoLivre - tipoDeAnuncio - tomanho lista de itens organizar(pago, gratis): {}",
				itens.size());
		for (int i = 0; i < itens.size(); i++) {
			vAtual = itens.get(i).getValor();
			// System.out.println("valor base: " + vBase + " valor atual: " + vAtual);
			if (vAtual >= vBase && gratis) {
				// System.out.println("if valor base: " + vBase + " valor atual: " + vAtual);
				itens.get(i).setTipoAnuncio(TagsAnuncioEnum.PAGO.getValue());
				vBase = vAtual;
			} else {
				itens.get(i).setTipoAnuncio(TagsAnuncioEnum.GRATIS.getValue());
				gratis = false;
			}

		}
	}
	
	private boolean isElementPresent(String xPath){
		//log.debug("isElementPresent value: {}",xPath);
        try{
        	this.driver.findElement(By.xpath(xPath));
            return true;
        }
        catch(NoSuchElementException e){
            return false;
        }
    }
}
