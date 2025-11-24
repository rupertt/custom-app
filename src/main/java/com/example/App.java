package com.example;

import static spark.Spark.get;
import static spark.Spark.port;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class App {
	private static final Gson gson = new Gson();

	public static void main(String[] args) {
		port(8080);

		get("/message", (req, res) -> {
			res.type("application/json");
			String message = "Hello from the sample Java app!";
			Map<String, String> response = new HashMap<>();
			response.put("message", message);
			return gson.toJson(response);
		});
	}
}


