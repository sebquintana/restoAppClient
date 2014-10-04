package com.http.cliente;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.model.carta.Carta;

public class HttpClientTest {

	private static final String CARTA_SERVLET_CONTEXT = "/carta";
	private static final Logger LOGGER = LogManager.getLogger();
	private String serverAddress;
	private HttpClient httpClient;
	private Gson gson;

	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"httpClient-ApplicationContext.xml");
		HttpClientTest client = (HttpClientTest) context.getBean("httpClient");
		client.pedirCarta();
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