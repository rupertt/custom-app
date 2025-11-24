package com.example;

import static spark.Spark.get;
import static spark.Spark.port;
import java.io.IOException;

import com.launchdarkly.sdk.*;
import com.launchdarkly.sdk.server.*;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class App {
	private static final Gson gson = new Gson();

    // Set SDK_KEY to your LaunchDarkly SDK key.
    static String SDK_KEY = "YOUR_SDK_KEY";

    // Set FEATURE_FLAG_KEY to the feature flag key you want to evaluate.
    static String FEATURE_FLAG_KEY = "YOUR_FEATURE_FLAG_KEY";

    static final AtomicBoolean flagOn = new AtomicBoolean(false);

	public static void main(String[] args) {
		port(8080);


        final LDContext context = LDContext.builder("example-user-key")
            .name("john")
            .set("email", "john.smith@example.com")
            .set("ip", "192.0.2.1")
            .build();

        LDConfig config = new LDConfig.Builder().build();

        final LDClient client = new LDClient(SDK_KEY, config);

        if (client.isInitialized()) {
            client.track("69236feea9ec6709b5b48a6b", context);
            error = "SDK successfully initialized!";
          }
          else {
            error = "SDK failed to initialize.  Please check your internet connection and SDK credential for any typo.";
          }
          


        // Initial evaluation of the feature flag for this context.
        flagOn.set(client.boolVariation(FEATURE_FLAG_KEY, context, false));

        // Listen for changes to the flag's value for this context and update state.
        client.getFlagTracker().addFlagValueChangeListener(FEATURE_FLAG_KEY, context, event -> {
            // Re-evaluate to a boolean for simplicity
            boolean newVal = client.boolVariation(FEATURE_FLAG_KEY, context, false);
            flagOn.set(newVal);
        });
 



		get("/message", (req, res) -> {
            String message = "";
            if (flagOn.get()) {
                message = "Flag is on! (New Feature Enabled!)";
            } else {
                message = "Flag is off! (old feature)";
            }
			res.type("application/json");

			
			Map<String, String> response = new HashMap<>();
			response.put("message", message);
			return gson.toJson(response);
		});

		get("/", (req, res) -> {
			res.type("text/html");
			return """
				<!doctype html>
				<html lang="en">
				  <head>
				    <meta charset="utf-8">
				    <meta name="viewport" content="width=device-width, initial-scale=1" />
				    <title>Sample Java App</title>
				    <style>
				      body { font-family: system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif; margin: 2rem; }
				      #msg { font-size: 1.5rem; }
				    </style>
				  </head>
				  <body>
				    <div id="msg">Loading...</div>
				    <button id="track">Track Event</button>
				    <script>
				      async function refresh() {
				        try {
				          const r = await fetch('/message', { cache: 'no-store' });
				          const data = await r.json();
				          document.getElementById('msg').textContent = data.message;
				        } catch (e) {
				          // ignore errors for demo
				        }
				      }
				      async function track() {
				        try {
				          await fetch('/track', { cache: 'no-store' });
				        } catch (e) {
				          // ignore
				        }
				      }
				      refresh();
				      setInterval(refresh, 1000);
				      document.getElementById('track').addEventListener('click', track);
				    </script>
				  </body>
				</html>
			""";
		});

		get("/track", (req, res) -> {
			// Fire-and-forget tracking event for the current demo context
			res.type("application/json");
			// Use the same context and client as initialized above
			// Note: in production, pass an actual user/context from the request
			// This demo triggers a single event key as requested
			try {
				client.track("79236feea9ec6709b5b48a6b", context);
			} catch (Exception ignored) {
			}
			Map<String, String> response = new HashMap<>();
			response.put("status", "ok");
			return gson.toJson(response);
		});
	}
}


