package ro.pub.cs.systems.pdsd.practicaltest02;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private int port;
    private String address;
    private EditText wordEditText;
    private TextView response;

    public ClientThread(int port, String address, EditText wordEditText, TextView response) {
        this.port = port;
        this.address = address;
        this.wordEditText = wordEditText;
        this.response = response;
    }

    @Override
    public void run() {
        try {

            Socket socket = new Socket(address, port);
            BufferedReader bufferedReader = Util.getReader(socket);
            PrintWriter printWriter = Util.getWriter(socket);
            String text = wordEditText.getText().toString();
            printWriter.println(text);

            String res = bufferedReader.readLine();
            Log.d("Client ", "Received data: " + res);

            response.post(new Runnable() {
                @Override
                public void run() {
                    response.setText(res);
                }
            });
        } catch (IOException e) {
            Log.e("Client error", "Socket error on client for ip " + address + " and port " + port);
            e.printStackTrace();
        }
    }
}

