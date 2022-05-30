package ro.pub.cs.systems.pdsd.practicaltest02;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class CommunicationThread extends Thread {

    private final Socket clientSocket;
    private HashMap<String, String> data;
    private ServerThread serverThread;
    public CommunicationThread(ServerThread serverThread, Socket clientSocket) {
        this.serverThread = serverThread;
        this.clientSocket = clientSocket;
        this.data = serverThread.getData();
    }

    @Override
    public void run() {

        String clientResponse;
        BufferedReader bufferedReader;
        try {
            bufferedReader = Util.getReader(clientSocket);
            String request = bufferedReader.readLine();

            if (!data.containsKey(request)) {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://api.dictionaryapi.dev/api/v2/entries/en/" +
                        request);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity == null) {
                    Log.e("Eroare", "Null response from server");
                }
                String response = EntityUtils.toString(httpEntity);

                JSONArray content = new JSONArray(response);

                String res = content.getJSONObject(0).getJSONArray("meanings").getJSONObject(0).
                        getJSONArray("definitions").getJSONObject(0).getString("definition");

                System.out.println(res);

                data.put(request, res);
                serverThread.setData(data);
            }

            clientResponse = data.get(request);


            PrintWriter printWriter = Util.getWriter(clientSocket);
            printWriter.println(clientResponse);
            printWriter.flush();
            clientSocket.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}

