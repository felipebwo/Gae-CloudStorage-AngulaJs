package br.com.stelo.cloud.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.stelo.cloud.bean.Parameter;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * @author felipeb
 *
 */
public class Utils {

	/**
	 * Split Parameter.
	 * 
	 * @param bcc
	 *            String
	 * @return parameter ParameterBcc
	 * @throws Exception
	 */
	public static Parameter splitParameter(String bcc) throws Exception {

		String[] values = bcc.split(";");

		Parameter parameter = new Parameter();

		if (values.length == 2) {
			parameter.bucketName = values[0];
			parameter.token = values[1];

		} else {
			throw new Exception("Parametros invalidos.");
		}

		return parameter;
	}

	public static String http(String url) throws JsonSyntaxException,
			IOException {

		URL urlDestino = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlDestino
				.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");

		JsonElement jsonElement = null;

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlDestino.openStream()));
			StringBuffer res = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				res.append(line);
			}
			reader.close();

			com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
			Object resultObject = parser.parse(res.toString());
			com.google.gson.JsonObject obj = (com.google.gson.JsonObject) resultObject;
			jsonElement = obj.get("mediaLink");

		}

		return jsonElement.getAsString();
	}

	public static byte[] getBytes(InputStream is) throws IOException {

		int len;
		int size = 1024;
		byte[] buf;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			buf = bos.toByteArray();
		}
		return buf;
	}

	public static int getLength(BufferedInputStream in) {
		int sizenew = 0;
		try {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;		
			while ((len = in.read(buf)) > 0) {

				bos.write(buf, 0, len);
			}
			byte[] b = bos.toByteArray();
			sizenew = b.length;

		} catch (IOException e) {
			System.err.println(e);
		}
		return sizenew;

	}

	public static void main(String[] args) throws IOException {

		URL url = new URL(
				"https://www.googleapis.com/storage/v1/b/disputa_01/o/Lighthouse.jpg");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// OK
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			StringBuffer res = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				res.append(line);
			}
			reader.close();

			com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
			Object resultObject = parser.parse(res.toString());
			com.google.gson.JsonObject obj = (com.google.gson.JsonObject) resultObject;
			JsonElement jsonElement = obj.get("mediaLink");

			System.out.println(jsonElement.getAsString());
		}

	}

}
