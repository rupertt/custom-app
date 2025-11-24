package com.example;

import static spark.Spark.get;
import static spark.Spark.port;

import com.google.gson.Gson;
import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.LDClient;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App {
	private static LDClient ldClient;
	private static final Gson gson = new Gson();
	private static final String DEFAULT_FLAG_KEY = "release-and-remediate";
	private static final String ENV_FLAG_KEY = getEnvOrNull("LD_FLAG_KEY");
	private static final String FLAG_KEY = (ENV_FLAG_KEY != null && !ENV_FLAG_KEY.isEmpty())
		? ENV_FLAG_KEY
		: DEFAULT_FLAG_KEY;

	public static void main(String[] args) {
		port(8080);

		// Initialize LaunchDarkly client if SDK key is provided via environment
		String sdkKey = "sdk-e645e583-c7a3-4006-97fb-b4160bc1961f";
		if (sdkKey == null || sdkKey.isEmpty()) {
			sdkKey = System.getenv("LAUNCHDARKLY_SDK_KEY");
		}
		if (sdkKey != null && !sdkKey.isEmpty()) {
			ldClient = new LDClient(sdkKey);
			// Wait briefly for initialization so first requests can evaluate flags
			long startMs = System.currentTimeMillis();
			while (!ldClient.isInitialized() && (System.currentTimeMillis() - startMs) < 5000) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
			if (!ldClient.isInitialized()) {
				System.err.println("LaunchDarkly client not initialized after 5s; using default variations until connected.");
			}
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				if (ldClient != null) {
					try {
						ldClient.close();
					} catch (IOException e) {
						System.err.println("Error closing LaunchDarkly client: " + e.getMessage());
					}
				}
			}));
		} else {
			System.err.println("LaunchDarkly SDK key not set (LD_SDK_KEY or LAUNCHDARKLY_SDK_KEY). Feature flags disabled.");
		}

		get("/message", (req, res) -> {
			res.type("application/json");
			String message = "Hello from the sample Java app!";

			if (ldClient != null) {
				// Set up the evaluation context.
				String contextKey = req.queryParams("key");
				if (contextKey == null || contextKey.isEmpty()) {
					contextKey = "example-context-key";
				}
				String contextName = req.queryParams("name");

				LDContext context;
				if (contextName != null && !contextName.isEmpty()) {
					context = LDContext.builder(contextKey).name(contextName).build();
				} else {
					context = LDContext.builder(contextKey).name("Sandy").build();
				}

				// Evaluate the feature flag for this context.
				boolean flagValue = ldClient.boolVariation(FLAG_KEY, context, false);
				if (flagValue) {
					message = "New feature rolled out!";
				}
			}

			Map<String, String> response = new HashMap<>();
			response.put("message", message);
			return gson.toJson(response);
		});
	}

	private static String getEnvOrNull(String name) {
		try {
			return System.getenv(name);
		} catch (SecurityException ignored) {
			return null;
		}
	}
}


