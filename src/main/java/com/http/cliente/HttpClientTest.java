package com.http.cliente;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HttpClientTest {

	private static final String CARTA_SERVLET_CONTEXT = "/carta";
	private static final Logger LOGGER = LogManager.getLogger();
	private String message;
	private String serverAddress;
	private HttpClient httpClient;
	private long sendMessageDelay;
	private long sendMessagePeriod;
	private int messageCounter = 0;
	
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("httpClient-ApplicationContext.xml");
		HttpClientTest client = (HttpClientTest) context.getBean("httpClient");
		System.out.println("Hola 0");
		client.pedirCarta();
	}
	
	public void pedirCarta() {

		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				GetMethod getMethod = null;
				int responseCode = -1;
				try {
					LOGGER.info("=====================================================");
					LOGGER.info("=====  	Pidiendo la carta... 				======");
					LOGGER.info("=====================================================");
					getMethod = new GetMethod(serverAddress + CARTA_SERVLET_CONTEXT);
					messageCounter++;
					System.out.println(serverAddress + CARTA_SERVLET_CONTEXT);
					LOGGER.debug("Sending the HTTP GET");
					responseCode = httpClient.executeMethod(getMethod);
					LOGGER.info("HTTP GET Response Code: " + responseCode);
					LOGGER.info("Restaurant: " + getMethod.getResponseBodyAsString());
				} catch (Exception e) {
					LOGGER.error("Exception => ", e);
				} finally {
					getMethod.releaseConnection();
				}
			}
		};
		timer.scheduleAtFixedRate(timerTask, sendMessageDelay, sendMessagePeriod);
	}
	
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setSendMessageDelay(long sendMessageDelay) {
		this.sendMessageDelay = sendMessageDelay;
	}

	public void setSendMessagePeriod(long sendMessagePeriod) {
		this.sendMessagePeriod = sendMessagePeriod;
	}
	
}