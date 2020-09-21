package br.ml.auth.session;

public class SessionServiceImp implements SessionService{

	private String tenanteId;
	private Long idUser;
	private String session;
	
	
	public SessionServiceImp(String tenanteId, Long idUser) {
		super();
		this.tenanteId = tenanteId;
		this.idUser = idUser;
	}
	
	public SessionServiceImp(String session) {
		super();
		this.session = session;
	}

	@Override
	public String gerarSession() {
		this.session = this.tenanteId +"/"+ Long.toHexString(this.idUser);
		return this.session;
	}
}
