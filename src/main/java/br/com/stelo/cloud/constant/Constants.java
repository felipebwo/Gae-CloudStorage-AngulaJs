package br.com.stelo.cloud.constant;

/**
 * Contains the client IDs and scopes for allowed clients consuming your API.
 */
public class Constants {

//	public static final String WEB_CLIENT_ID = "90233399647-q61b9plt3d541g9c46etcaoe0rdaqnsa.apps.googleusercontent.com";
//	public static final String WEB_AUDIENCE = WEB_CLIENT_ID;
//	public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
//	public static final String STORAGE_SCOPE = "https://www.googleapis.com/auth/devstorage.read_write";
//	public static final String PROJECT_NAME = "PocGoogleCloudStorage";
	
	public static final String WEB_CLIENT_ID = "111286042768-5pl11brjpaanj14n4hgegr1r9bnbjapp.apps.googleusercontent.com";
	public static final String WEB_AUDIENCE = WEB_CLIENT_ID;
	public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
	public static final String STORAGE_SCOPE = "https://www.googleapis.com/auth/devstorage.read_write";
	public static final String PROJECT_NAME = "Stelo-GestorDOCDisputas";

	/** Constants do sistema **/
	public static final int CODE_SUCESSO = 200;
	public static final int LIMITE_UPLOAD = 10;
	public static final String SIGLA_SISTEMA_ID = "siglaSistemaID";
	public static final String TOKEN = "token";
	public static final int FILE_EXIST = 201;
	
	/** properties**/
	public static final String ENCONDE_KEY = "encode.key";
	public static final String MENSAGEM = "mensagem";
	public static final String CODE = "code";
	
	/**message**/
	public static final String MSG_UPLOAD_EXCEDIDO = "msg.upload.excedido";
	public static final String MSG_TOKEN_EXPIRADO = "msg.token.expirado";
	public static final String MSG_REGISTRO_DELETADO = "msg.registro.deletado";
	public static final String TAMANHO_ARQUIVO_NAO_SUPORTADO = "msg.tamanho.arquivo.nao.suportado";	

}
