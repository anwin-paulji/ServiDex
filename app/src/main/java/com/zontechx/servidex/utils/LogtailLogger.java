package com.zontechx.servidex.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogtailLogger {

    private static final String LOGTAG = "LogtailLogger";
    private static final String INGEST_URL = "https://s1467015.eu-nbg-2.betterstackdata.com";
    private static final String SOURCE_TOKEN = "EuSog2pfdX33bAtcA2kD42Kt";
    private static final int BATCH_SIZE = 50;

    private static final Queue<JSONObject> logQueue = new ConcurrentLinkedQueue<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    static {
        // Start sending logs every 5 seconds
        scheduler.scheduleAtFixedRate(LogtailLogger::flushLogs, 5, 5, TimeUnit.SECONDS);
    }

    public static void log(String level, String message, JSONObject meta) {
        try {
            JSONObject logEntry = new JSONObject();
            logEntry.put("dt", getCurrentISOTime());
            logEntry.put("level", level);
            logEntry.put("message", message);

            if (meta != null) {
                for (Iterator<String> it = meta.keys(); it.hasNext(); ) {
                    String key = it.next();
                    logEntry.put(key, meta.get(key));
                }
            }

            logQueue.add(logEntry);

            // Flush early if batch size is reached
            if (logQueue.size() >= BATCH_SIZE) {
                flushLogs();
            }

        } catch (Exception e) {
            Log.e(LOGTAG, "Failed to log event", e);
        }
    }

    private static void flushLogs() {
        if (logQueue.isEmpty()) return;

        JSONArray batch = new JSONArray();
        for (int i = 0; i < BATCH_SIZE && !logQueue.isEmpty(); i++) {
            batch.put(logQueue.poll());
        }

        sendLogs(batch);
    }

    private static void sendLogs(JSONArray batch) {
        new Thread(() -> {
            try {
                URL url = new URL(INGEST_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + SOURCE_TOKEN);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(batch.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                int code = conn.getResponseCode();
                if (code >= 200 && code < 300) {
                    Log.d(LOGTAG, "Sent batch of logs: " + batch.length());
                } else {
                    Log.e(LOGTAG, "Logtail send failed. Response code: " + code);
                }

                conn.disconnect();
            } catch (Exception e) {
                Log.e(LOGTAG, "Error sending logs to Logtail", e);
            }
        }).start();
    }

    private static String getCurrentISOTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
}
