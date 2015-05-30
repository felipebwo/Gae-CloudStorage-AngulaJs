package br.com.stelo.cloud.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;


/**
 * 
 * Classe reponsavel por gerar o Json e adicionar no Response.
 * 
 * @author felipeb
 *
 */
public class JsonUtil {

	/**
	 * addMensagemResponse [String].
	 * @param res
	 * @param param
	 * @param mensagem
	 * @throws IOException
	 */
	public static void addMensagemResponse(HttpServletResponse res,
			String param, String mensagem) throws IOException {

		JsonObject json = new JsonObject();
		json.addProperty(param, mensagem);

		PrintWriter out = res.getWriter();
		out.print(json.toString());

	}

	/**
	 * addMensagemResponse [int].
	 * @param res
	 * @param param
	 * @param codeSucesso
	 * @throws IOException
	 */
	public static void addMensagemResponse(HttpServletResponse res,
			String param, int codeSucesso) throws IOException {
		
		JsonObject json = new JsonObject();
		json.addProperty(param, codeSucesso);

		PrintWriter out = res.getWriter();
		out.print(json.toString());
		
	}

}
