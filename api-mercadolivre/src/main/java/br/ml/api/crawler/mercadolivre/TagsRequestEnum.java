package br.ml.api.crawler.mercadolivre;

public enum TagsRequestEnum {

	PAGINATION("_Desde_"),
	ORDER_PRICE("_OrderId_PRICE"),
	PRICE_RANGE("_PriceRange_"),
	USADO("usado"),
	NOVO("novo");
	
	
	private String name;
	
	TagsRequestEnum(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.name;
	}
	
	public static String getTagsValue(String tags) {
		switch (tags.toLowerCase()) {
		case "pagina":
			return TagsRequestEnum.PAGINATION.getValue();
		case "ordenar_price":
			return TagsRequestEnum.ORDER_PRICE.getValue();
		case "price_range":
			return TagsRequestEnum.PRICE_RANGE.getValue();
		case "usado":
			return TagsRequestEnum.USADO.getValue();
		case "novo":
			return TagsRequestEnum.NOVO.getValue();
		default:
			return "";
		}
	}
}
