package br.ml.auth.usuario;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edx.exception.config.ApiMessageSource;
import br.edx.exception.type.ApiBadRequestException;
import br.ml.auth.cadastro.codigo.CodigoCadastro;
import br.ml.auth.cadastro.codigo.CodigoCadastroService;
import br.ml.auth.email.type.EnviarEmail;
import br.ml.auth.modulo.ModuloAplicacao;
import br.ml.auth.role.AuthRoles;
import br.ml.auth.role.AuthRolesService;
import br.ml.auth.role.RolesProfileEnum;
import br.ml.auth.tenant.TenantService;
import br.ml.auth.usuario.codigo.CodigoAtivar;
import br.ml.auth.usuario.codigo.CodigoAtivarUsuario;
import br.ml.auth.usuario.codigo.CodigoRecuperarSenha;
import freemarker.template.TemplateException;

@Service
public class UsuarioService {

	@Autowired private UsuarioDAO usuarioDAO;
	@Autowired private AuthRolesService authRolesService;
	@Autowired private CodigoCadastroService codigoCadastroService;
	@Autowired private PasswordEncoder passwordEncoder;
	
	@Autowired @Qualifier("emailConfirmarCadastro") private EnviarEmail<Usuario> enviarEmailCadastro;
	
	@Autowired @Qualifier("emailRecuperarSenha") private EnviarEmail<Usuario> enviarEmailRecuperarSenha;
	
	/**
	 * Criar um novo Usuaurio no sistema
	 * @param user
	 * @param codigoGerado
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional
	public Usuario createUsuarioComCodigoCadstro(Usuario user, String codigoGerado) throws MessagingException, IOException, TemplateException {
		if(this.codigoCadastroService.validCodigoCadastro(codigoGerado)) {
			System.out.println("chegou aq "+codigoGerado);
			if(!this.usuarioDAO.existsByEmail(user.getEmail())) {
				CodigoCadastro codigo = this.codigoCadastroService.buscarCodigo(codigoGerado);
				user.setSenha(passwordEncoder.encode(user.getSenha()));
				user.setTenantId(TenantService.buildIdString(usuarioDAO));
				user.getPessoa().setTenantId(user.getTenantId());
				user.setUserAtivo(true);
				user.setVerificado(false);
				System.out.println("codigo: "+codigo.getModuloAplicacao().get(0).getNome());
				user.setModuloAplicacao(new ArrayList<ModuloAplicacao>(codigo.getModuloAplicacao()));
				//BeanUtils.copyProperties(codigo.getModuloAplicacao(), user.getModuloAplicacao(), new ArrayList());
				Usuario userRole = this.usuarioDAO.save(user);
				/** Save Role no banco de dados*/
				this.authRolesService.createRole(new AuthRoles(RolesProfileEnum.ROLE_ADMIN, userRole.getId()));
				/* Enviar Email de Confirmacao Cadastro*/
				this.enviarEmailCadastro(userRole);
				return userRole;
			}
		}
		throw new ApiBadRequestException(ApiMessageSource.toMessage("bad_request.error.code.cadastro.user"), 
				ApiMessageSource.toMessage("bad_request.error.msg.cadastro.user"));
	}
	/**
	 * Email Confirmar Cadastro
	 * @param user
	 * @throws IOException
	 * @throws MessagingException
	 * @throws TemplateException
	 */
	private void enviarEmailCadastro(Usuario user) throws IOException, MessagingException, TemplateException {
		this.enviarEmailCadastro.setObjectoClzz(user);
		this.enviarEmailCadastro.sendEmail();
	}
	/**
	 * Email Recuperar Senha
	 * @param user
	 * @throws IOException
	 * @throws MessagingException
	 * @throws TemplateException
	 */
	private void enviarEmailRecuperarSenha(Usuario user) throws IOException, MessagingException, TemplateException {
		this.enviarEmailRecuperarSenha.setObjectoClzz(user);
		this.enviarEmailRecuperarSenha.sendEmail();
	}
	/**
	 * Enviar email de recuperacao de senha
	 * @param email
	 */
	public void enviarEmailParaRecuperarSenha(String email) throws IOException, MessagingException, TemplateException {
		Usuario user = this.usuarioDAO.findByEmail(email);
		if(user != null && user.isVerificado() == true) {
			this.enviarEmailRecuperarSenha(user);
		} else {
			throw new ApiBadRequestException(ApiMessageSource.toMessage("bad_request.error.cadastroVerificar.code"),
					ApiMessageSource.toMessage("bad_request.error.cadastroVerificar.msg"));
		}
	}
	public void atualizarSenha(String password, String codigo) {
		CodigoAtivar<Usuario> codigoAtivar = new CodigoRecuperarSenha(new Usuario());
		Usuario user = codigoAtivar.reveterCodigo(codigo);
		Usuario findUser = this.usuarioDAO.findById(user.getId()).get();
		this.passwordEncoder.matches(findUser.getSenha(), password);
		if(findUser.isVerificado() == true && !this.passwordEncoder.matches(findUser.getSenha(), password)) {
			findUser.setSenha( this.passwordEncoder.encode(password));
			this.usuarioDAO.save(findUser);
		}
	}
	
	public void reenviarEmailConfirmacao(String email) throws IOException, MessagingException, TemplateException {
		Usuario user = this.usuarioDAO.findByEmail(email);
		if(user != null && user.isVerificado() != true) {
			this.enviarEmailCadastro(user);
		}
	}
	/**
	 * Verificar Cadastro e confirmar o email
	 * @param codigo
	 */
	public void verificarEmail(String codigo) {
		CodigoAtivar<Usuario> codigoAtivar = new CodigoAtivarUsuario(new Usuario());
		Usuario user = codigoAtivar.reveterCodigo(codigo);
		Usuario findUser = this.usuarioDAO.findById(user.getId()).get();
		if(findUser.isVerificado() != true) {
			findUser.setVerificado(true);
			this.usuarioDAO.save(findUser);
		}
	}
	
}
