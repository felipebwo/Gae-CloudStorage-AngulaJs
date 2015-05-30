package br.com.stelo.cloud.factory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemStream;

import br.com.stelo.cloud.bean.FileItem;
import br.com.stelo.cloud.bean.Result;
import br.com.stelo.cloud.constant.Constants;
import br.com.stelo.cloud.utils.PropertiesUtils;
import br.com.stelo.cloud.utils.Utils;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;
import com.google.gson.JsonSyntaxException;

/**
 * Classe que fornece os servicos do google cloud storage.
 * @author felipeb
 */
public class SteloStorageFactory {

	/**
	 * LOG.
	 */
	private static final Logger LOG = Logger.getLogger(SteloStorageFactory.class.getName());
	
	/**
	 * PROJECT_ID.
	 */
	private static final String PROJECT_ID = "ativos-digitais";

	/**
	 * storage.
	 */
	protected static Storage storage;

	/**
	 * list.
	 * 
	 * @param bucketName
	 * @param req 
	 * @return
	 * @throws JsonSyntaxException 
	 * @throws IOException
	 */
	public static Result list(String bucketName, HttpServletRequest req) throws JsonSyntaxException, IOException {

		Result response = new Result();
		List<FileItem> results = null;

		results = new ArrayList<FileItem>();
		if (storage == null) {
			initStorage();
		}
		
		com.google.api.services.storage.model.Objects objects = null;
		try {
			objects = storage.objects().list(bucketName).execute();
		} catch (IOException e) {
			response.code = Constants.CODE_SUCESSO;
			response.results = results;
			return response;
		}
		
		if(objects != null){
			if (null == objects.getItems() || objects.getItems().isEmpty()) {
				LOG.warning("Bucket \"" + bucketName + "\" empty or invalid.");
			} else {
				for (StorageObject object : objects.getItems()) {
					FileItem item = new FileItem();
					String dataFormatada = new SimpleDateFormat("dd/MM/yyyy hh:mm")
							.format(new Date(object.getUpdated().getValue()));
					item.date = dataFormatada;
					item.fileName = object.getName();
					item.link = Utils.http(object.getSelfLink());
					results.add(item);
				}
			}
		}		

		response.code = Constants.CODE_SUCESSO;
		response.results = results;

		return response;

	}

	/**
	 * Insert.
	 * 
	 * @param bucketName
	 * @param item
	 * @throws Exception 
	 */
	public static void insert(String bucketName, FileItemStream item, String version)throws Exception {

		if (storage == null) {
			initStorage();
		}	
		
		try {
			storage.objects().list(bucketName).execute();
		} catch (IOException e) {
			createBucket(bucketName);
		}

		StorageObject objectMetadata = new StorageObject();
		objectMetadata.setBucket(bucketName);

		InputStreamContent content = new InputStreamContent(item.getContentType(), item.openStream());
		
		Storage.Objects.Insert insert = storage.objects().insert(bucketName, objectMetadata, content);
		insert.setPredefinedAcl("publicRead");
		
		if (version.equals("IEN")) {
			insert.setName(item.getName());
			
		} else if (version.equals("IES")) {
			insert.setName(item.getName().substring(
					item.getName().lastIndexOf("\\") + 1));
		}

		insert.execute();

	}
	

	/**
	 * Delete.
	 * 
	 * @param bucketName
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Result delete(String bucketName, String fileName)
			throws IOException {

		if (storage == null) {
			initStorage();
		}
		
		Result response = new Result();
		if (fileName != null && !fileName.equals("")) {
			storage.objects().delete(bucketName, fileName).execute();
			response.code = Constants.CODE_SUCESSO;
		} else {
			response.mensagem = PropertiesUtils.getMessage(Constants.MSG_REGISTRO_DELETADO);
		}

		return response;
	}
	
	/**
	 * Valida o limite de Upload.
	 * 
	 * @param bucketName
	 * @return
	 * @throws IOException
	 */
	public static boolean limiteMaximoUpload(String bucketName) throws IOException {
		
		Result result = SteloStorageFactory.list(bucketName, null);

		if(result.results.size() == Constants.LIMITE_UPLOAD){
			return Boolean.FALSE;	
		}
		
		return Boolean.TRUE;
	}

	/**
	 * Metodo responsavel por inicializar o Storage.
	 */
	private static void initStorage() {
		LOG.info("Inicializando o storage.");

		if (storage == null) {
			try {
				storage = GCSFactory.initialize();
			} catch (GeneralSecurityException | IOException e) {
				LOG.severe("Erro ao inicializar o Storage: " + e.getMessage());
			}
		}
	}
	
	/**
    * Metodo simples para criacao de um bucket.
    *
    * @param bucketName Name of bucket to create
    * @throws Exception
    */
    public static void createBucket(String bucketName) throws Exception {
 
        Bucket bucket = new Bucket();
        bucket.setName(bucketName); 
        storage.buckets().insert(PROJECT_ID, bucket).execute();
        
    }

}
