package br.com.stelo.cloud.upload;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import br.com.stelo.cloud.bean.FileItem;
import br.com.stelo.cloud.bean.Result;
import br.com.stelo.cloud.constant.Constants;
import br.com.stelo.cloud.factory.SteloStorageFactory;
import br.com.stelo.cloud.utils.JsonUtil;
import br.com.stelo.cloud.utils.PropertiesUtils;

import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.oauth.OAuthService;
import com.google.appengine.api.oauth.OAuthServiceFactory;
import com.google.appengine.api.users.User;

@SuppressWarnings("serial")
public class FileUpload extends HttpServlet {
	
	/**
	 * LOG.
	 */
	private static final Logger LOG = Logger.getLogger(FileUpload.class.getName());

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		try {

			String version = req.getParameter("version");
			if (req.getParameter("auth").equals("S")) {

				if (req.getParameter("errorCode") != null) {
					res.sendError(
							Integer.parseInt(req.getParameter("errorCode")),
							req.getParameter("errorMessage"));
					return;
				}

				if (version.equals("IEN")) {
					res.setContentType("application/json; charset=UTF-8");
				}

				if (req.getHeader("Content-Type") != null
						&& req.getHeader("Content-Type").startsWith(
								"multipart/form-data")) {

					String siglaSistemaID = (String) req.getSession()
							.getServletContext()
							.getAttribute(Constants.SIGLA_SISTEMA_ID);

					ServletFileUpload upload = new ServletFileUpload();
					upload.setHeaderEncoding("UTF-8"); 
					
					FileItemIterator iterator = upload.getItemIterator(req);
					
					int fileSize = Integer.parseInt(req.getHeader("Content-Length"));
					LOG.warning("tamanho do arquivo: "+fileSize);
					
					boolean fileExist = false;

					if (SteloStorageFactory.limiteMaximoUpload(siglaSistemaID)) {
						while (iterator.hasNext()) {
							FileItemStream item = iterator.next();	
													
							if (fileSize > 6000000) {
								LOG.warning("tamanho do arquivo: "+fileSize);
								JsonUtil.addMensagemResponse(res, Constants.MENSAGEM,
										PropertiesUtils.getMessage(Constants.TAMANHO_ARQUIVO_NAO_SUPORTADO));
								return;
							}
							
							Result result = SteloStorageFactory.list(siglaSistemaID, req);							
							List<FileItem> listFileItem = result.results;							
							ListIterator<FileItem> itr = listFileItem.listIterator();
						    while(itr.hasNext()) {
						    	FileItem element = itr.next();
						    	if(item.getName().contains(element.fileName)){
						    		fileExist = true;
								}
						    }							
							
							if (item.getName() != null) {
								SteloStorageFactory.insert(siglaSistemaID,
										item, version);
							}
							
						}
						
						if(fileExist){
							if (version.equals("IES")) {
								PrintWriter out = res.getWriter();
								out.print(Constants.FILE_EXIST);
							} else {
								JsonUtil.addMensagemResponse(res, Constants.CODE,
										Constants.FILE_EXIST);
							}							
							
						} else {
							if (version.equals("IES")) {
								PrintWriter out = res.getWriter();
								out.print(Constants.CODE_SUCESSO);
							} else {
								JsonUtil.addMensagemResponse(res, Constants.CODE,
										Constants.CODE_SUCESSO);
							}							
							
						}

					} else {
						JsonUtil.addMensagemResponse(
								res,
								Constants.MENSAGEM,
								PropertiesUtils
										.getMessage(Constants.MSG_UPLOAD_EXCEDIDO));
					}
				}

			} else if (req.getParameter("auth").equals("N")) {
				if (isUserLoggedIn()) {
					JsonUtil.addMensagemResponse(res, Constants.CODE,
							Constants.CODE_SUCESSO);
				} else {
					JsonUtil.addMensagemResponse(res, Constants.MENSAGEM,
							PropertiesUtils
									.getMessage(Constants.MSG_TOKEN_EXPIRADO));
				}
			}

		} catch (FileUploadException e) {
			LOG.severe("Erro ao realizar upload: " + e.getMessage());
		} catch (Exception e) {
			LOG.severe("Erro ao realizar upload: " + e.getMessage());
		}

	}

	/**
	 * isUserLoggedIn.
	 * 
	 * @return boolean userLoggedIn
	 */
	private boolean isUserLoggedIn() {

		User user = null;
		try {
			OAuthService oauth = OAuthServiceFactory.getOAuthService();
			user = oauth.getCurrentUser(Constants.EMAIL_SCOPE);

		} catch (OAuthRequestException e) {
			LOG.severe("Erro na autenticação: " + e.getMessage());
		}

		Boolean userLoggedIn = Boolean.FALSE;
		if (user != null) {
			userLoggedIn = Boolean.TRUE;
		}

		return userLoggedIn;
	}

}
