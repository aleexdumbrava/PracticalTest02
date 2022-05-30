package ro.pub.cs.systems.pdsd.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PracticalTest02MainActivity extends AppCompatActivity {
    private EditText portEditTextServer;
    private Button startServerButton;
    private ServerThread serverThread;
    private EditText addressEditText;
    private EditText portEditTextClient;
    private TextView responseTextView;
    private ClientThread clientThread;
    private EditText wordEditText;
    private Button startClientButton;
    private boolean isClientStarted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        portEditTextServer = findViewById(R.id.PortEditText);
        startServerButton = findViewById(R.id.ConnectButton);
        addressEditText = findViewById(R.id.address);
        portEditTextClient = findViewById(R.id.portClient);
        responseTextView = findViewById(R.id.response);
        wordEditText = findViewById(R.id.word);
        startClientButton = findViewById(R.id.send_request);
        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int port = Integer.parseInt(portEditTextServer.getText().toString());
                serverThread = new ServerThread(port);
                serverThread.start();
            }
        });

        startClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String address = addressEditText.getText().toString();
                int port = Integer.parseInt(portEditTextClient.getText().toString());
                clientThread = new ClientThread(port, address, wordEditText, responseTextView);
                clientThread.start();


            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.i("Destroy", "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}