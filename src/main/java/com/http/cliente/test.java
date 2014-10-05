package com.http.cliente;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.carta.ItemCarta;
import com.model.pedido.Pedido;

public class test {

	private static String cartaFilePath = "/home/seba/dev/Workspace/restoAppClient/src/main/resources/test.json";

	public static void main(String[] args) {
		deJavaAJson();
		deJsonAJava();
		deArchivoAJson();
	}

	private static void deArchivoAJson() {
		Gson gson = new Gson();

		String cartaString = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(cartaFilePath));
			Pedido pedido = gson.fromJson(br, Pedido.class);
			GsonBuilder gbuilder = new GsonBuilder();
			gbuilder.enableComplexMapKeySerialization();
			cartaString = gbuilder.create().toJson(pedido);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Pedido: " + cartaString);
	}

	private static void deJsonAJava() {
		Gson gson = new Gson();
		try {
	 
			BufferedReader br = new BufferedReader(new FileReader(cartaFilePath));
			Pedido obj = gson.fromJson(br, Pedido.class);
			Iterator<Entry<ItemCarta, Integer>> it = obj.getPedido().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<ItemCarta, Integer> item = (Map.Entry<ItemCarta, Integer>) it.next();
				System.out.println("Nombre " + item.getKey().getNombre() + " Cantidad " + item.getValue());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	 
	}

	private static void deJavaAJson() {
		ItemCarta item = new ItemCarta("Coca cola", "1 Litro", 20);
		ItemCarta item2 = new ItemCarta("ensalada mixta", "tomate y  lechuga", 20);
		Pedido pedido = new Pedido();
		pedido.agregarItem(item, 2);
		pedido.agregarItem(item2, 2);
		GsonBuilder gbuilder = new GsonBuilder();
		gbuilder.enableComplexMapKeySerialization().setPrettyPrinting();
		String json = gbuilder.create().toJson(pedido);
		try {
			FileWriter writer = new FileWriter(cartaFilePath);
			writer.write(json);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(json);
	}

}
