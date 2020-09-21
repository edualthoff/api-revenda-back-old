package br.ml.api.crawler.mercadolivre;

public enum TagsAnuncioEnum {

	PAGO("pago"),
	GRATIS("gratis"),
	RECONDICIONADO("recondicionado"),
	USADO("usado"),
	NOVO("novo");
	
	private String name;
	
	TagsAnuncioEnum(String name) {
		this.name = name;
	}
	public String getValue() {
		return this.name;
	}
	
	public static String getTagsValue(String tags) {
		switch (tags.toLowerCase()) {
		case "pago":
			return TagsAnuncioEnum.PAGO.getValue();
		case "gratis":
			return TagsAnuncioEnum.GRATIS.getValue();
		case "recondicionado":
			return TagsAnuncioEnum.RECONDICIONADO.getValue();
		case "usado":
			return TagsAnuncioEnum.USADO.getValue();
		case "novo":
			return TagsAnuncioEnum.NOVO.getValue();
		default:
			return "";
		}
	}
}
