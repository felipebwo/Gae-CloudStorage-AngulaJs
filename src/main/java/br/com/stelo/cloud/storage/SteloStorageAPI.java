package br.com.stelo.cloud.storage;


import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.stelo.cloud.bean.Parameter;
import br.com.stelo.cloud.bean.Result;
import br.com.stelo.cloud.constant.Constants;
import br.com.stelo.cloud.criptografia.AES;
import br.com.stelo.cloud.factory.SteloStorageFactory;
import br.com.stelo.cloud.utils.PropertiesUtils;
import br.com.stelo.cloud.utils.Utils;

import com.google.api.client.util.Base64;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.services.storage.Storage;
import com.google.appengine.api.users.User;

@Api(name = "steloStorageAPI",
     version = "v1",
     namespace = @ApiNamespace(ownerDomain = "br.com.stelo",
                                ownerName = "br.com.stelo",
                                packagePath=""))
public class SteloStorageAPI {

	/**
	 * LOG.
	 */
	private static final Logger LOG = Logger.getLogger(SteloStorageAPI.class.getName());

	/**
	 * storage.
	 */
	protected Storage storage;
	
	@ApiMethod(
			name = "listar", 
			httpMethod = "get",
			scopes = {Constants.EMAIL_SCOPE, Constants.STORAGE_SCOPE},
		    clientIds = {Constants.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
		    audiences = {Constants.WEB_AUDIENCE})
	public Result listar(HttpServletRequest req, User user)throws UnauthorizedException {
		LOG.info("Lista todos os arquivos Storage API");
		
		if (user == null) {
			throw new UnauthorizedException(PropertiesUtils.getMessage(Constants.MSG_TOKEN_EXPIRADO));
		}
		
		String bucketName =  (String) req.getSession().getServletContext().getAttribute(Constants.SIGLA_SISTEMA_ID); 

		Result response = null;
		try{
			response = SteloStorageFactory.list(bucketName, req);
		} catch (Exception e) {
			addLogMensagemErro(response, "Erro ao listar arquivos: " + e.getCause());
		}	
		
		return response;
	}

	@ApiMethod(
			name = "deletar", 
			httpMethod = "get",
			scopes = {Constants.EMAIL_SCOPE, Constants.STORAGE_SCOPE},
		    clientIds = {Constants.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
		    audiences = {Constants.WEB_AUDIENCE})
	public Result deletar(HttpServletRequest req, @Named("fileName") String fileName, User user) throws UnauthorizedException {
		LOG.info("Delete arquivo Storage API");
		
		if (user == null){
			throw new UnauthorizedException(PropertiesUtils.getMessage(Constants.MSG_TOKEN_EXPIRADO));
		}

		String bucketName =  (String) req.getSession().getServletContext().getAttribute(Constants.SIGLA_SISTEMA_ID);
		
		Result response = null;
		try{
			response = SteloStorageFactory.delete(bucketName, fileName);
		} catch (Exception e) {
			addLogMensagemErro(response, "Erro ao deletar o arquivo: " + e.getMessage());
		}

		return response;
	}
	
	
	@ApiMethod(
			name = "getToken", 
			httpMethod = "get",
			scopes = {Constants.EMAIL_SCOPE, Constants.STORAGE_SCOPE},
		    clientIds = {Constants.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
		    audiences = {Constants.WEB_AUDIENCE})
	public Result getToken(HttpServletRequest req, @Named("id") String id) throws Exception {
		LOG.info("get Token");

		String parametrosDecripty = new String(AES.decrypt(
				Base64.decodeBase64(id.getBytes()),
				PropertiesUtils.getMessage(Constants.ENCONDE_KEY)));
		
		Parameter parameter = Utils.splitParameter(parametrosDecripty);
		
		HttpSession session = req.getSession(false);
	    if(session == null){
	        session = req.getSession(true);
	    }
		
		session.getServletContext().setAttribute(Constants.SIGLA_SISTEMA_ID, parameter.bucketName);
		session.getServletContext().setAttribute(Constants.TOKEN, parameter.token);
	    
		Result response = new Result();
		response.token = parameter.token; 

		return response;
	}
	
	/**
	 * Metodo responsavel por logar o erro e adiconar a mensagem de erro
	 * @param response
	 * @param mensagemErro
	 */
	private static void addLogMensagemErro(Result response, String mensagemErro) {
		response.mensagem = mensagemErro;
		LOG.severe(mensagemErro);
	}
	
}
