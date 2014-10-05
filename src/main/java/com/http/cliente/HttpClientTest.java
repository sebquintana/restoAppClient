package com.http.cliente;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.carta.Carta;
import com.model.pedido.Pedido;

public class HttpClientTest {

	private static final String CARTA_SERVLET_CONTEXT = "/carta";
	private static final String PEDIDO_SERVLET_CONTEXT = "/pedido";
	private static String pedidoFilePath = "/home/seba/dev/Workspace/restoAppClient/src/main/resources/pedido.json";
	private static final Logger LOGGER = LogManager.getLogger();
	private String serverAddress;
	private HttpClient httpClient;
	private Gson gson;

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"httpClient-ApplicationContext.xml");
		HttpClientTest client = (HttpClientTest) context.getBean("httpClient");
		client.pedirCarta();
		client.enviarPedido();
	}

	public void pedirCarta() {
		GetMethod getMethod = null;
		int responseCode = -1;
		try {
			LOGGER.info("=================================================");
			LOGGER.info("=====  	Pidiendo la carta... 	       ======");
			LOGGER.info("=================================================");
			getMethod = new GetMethod(serverAddress + CARTA_SERVLET_CONTEXT);
			LOGGER.info("Server URL: " + serverAddress + CARTA_SERVLET_CONTEXT);
			LOGGER.debug("Enviando el request HTTP GET");
			responseCode = httpClient.executeMethod(getMethod);
			LOGGER.info("HTTP GET Response Code: " + responseCode);
			String respuesta = getMethod.getResponseBodyAsString();
			LOGGER.info("Respuesta: " + respuesta);
			Carta carta = gson.fromJson(respuesta, Carta.class);
			LOGGER.info("Restaurant: " + carta.getRestaurant());
		} catch (Exception e) {
			LOGGER.error("Exception => ", e);
		} finally {
			getMethod.releaseConnection();
		}
	}
	
	public void enviarPedido(){
		PostMethod postMethod = null;
		int responseCode = -1;
		try {
			LOGGER.info("=================================================");
			LOGGER.info("=====  	Enviando el pedido... 	       ======");
			LOGGER.info("=================================================");
			postMethod = new PostMethod(serverAddress + PEDIDO_SERVLET_CONTEXT);
			LOGGER.info("Server URL: " + serverAddress + PEDIDO_SERVLET_CONTEXT);
			StringRequestEntity requestEntity = new StringRequestEntity(crearPedido(pedidoFilePath), "application/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);
			LOGGER.debug("Enviando el request HTTP POST");
			responseCode = httpClient.executeMethod(postMethod);
			LOGGER.info("HTTP POST Response Code: " + responseCode);
		} catch (Exception e) {
			LOGGER.error("Exception => ", e);
		} finally {
			postMethod.releaseConnection();
		}
	}
	
	
	private String crearPedido(String jsonFilePath) {
		 String pedido = "";
		 try {
				BufferedReader br = new BufferedReader(new FileReader(pedidoFilePath));
				Pedido aux = gson.fromJson(br, Pedido.class);
				GsonBuilder gbuilder = new GsonBuilder();
				gbuilder.enableComplexMapKeySerialization();
				pedido = gbuilder.create().toJson(aux);
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 return pedido;
	}

	// Setters 
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	public void setGson(Gson gson){
		this.gson = gson;
	}

}