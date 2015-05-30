package br.com.stelo.cloud.factory;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;

public class GCSFactory {

	private static Storage storage;
	private static HttpTransport httpTransport;

//	private static final String PROJECT_NAME = "PocGoogleCloudStorage";
//
//	private static final String KEY_P12 = "PocGoogleCloudStorage-05089dfca855.p12";
//
//	private static final String SERVICE_ACCOUNT_EMAIL = "90233399647-q61b9plt3d541g9c46etcaoe0rdaqnsa@developer.gserviceaccount.com";
//
//	private static final String STORAGE_SCOPE = "https://www.googleapis.com/auth/devstorage.read_write";
	
	private static final String PROJECT_NAME = "Stelo-GestorDOCDispustas";

	private static final String KEY_P12 = "Stelo-GestorDOCDisputas-bd3f21dd6d64.p12";

	private static final String SERVICE_ACCOUNT_EMAIL = "111286042768-5pl11brjpaanj14n4hgegr1r9bnbjapp@developer.gserviceaccount.com";

	private static final String STORAGE_SCOPE = "https://www.googleapis.com/auth/devstorage.read_write";

	public static Storage initialize() throws GeneralSecurityException,
			IOException {

		if (storage != null) {
			return storage;
		}

		/*
		 * httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		 * AppIdentityCredential credential = new
		 * AppIdentityCredential(Collections
		 * .singleton(Constants.STORAGE_SCOPE));
		 * 
		 * storage = new Storage.Builder(httpTransport,
		 * JacksonFactory.getDefaultInstance(), credential)
		 * .setApplicationName(Constants.PROJECT_NAME).build();
		 */

		File p12File = new File(GCSFactory.class.getClassLoader()
				.getResource(KEY_P12).getPath());

		httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(httpTransport)
				.setJsonFactory(JacksonFactory.getDefaultInstance())
				.setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
				.setServiceAccountScopes(Collections.singleton(STORAGE_SCOPE))
				.setServiceAccountPrivateKeyFromP12File(p12File).build();

		storage = new Storage.Builder(httpTransport,
				JacksonFactory.getDefaultInstance(), credential)
				.setApplicationName(PROJECT_NAME).build();

		return storage;
	}
}