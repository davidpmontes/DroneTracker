package com.example.dronetracker2.ui.server;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dronetracker2.MainActivity;
import com.example.dronetracker2.MyWebsocketListener;
import com.example.dronetracker2.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class ServerFragment extends Fragment {

    private ServerViewModel serverViewModel;

    private OkHttpClient client;
    private WebSocket ws;

    Button loginButton;
    EditText serverEditText;
    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_server, container, false);

        loginButton = view.findViewById(R.id.button_login);
        serverEditText = view.findViewById(R.id.edit_server);
        usernameEditText = view.findViewById(R.id.edit_username);
        passwordEditText = view.findViewById(R.id.edit_password);

        serverEditText.setText("ws://10.0.2.2:9003/test-ws");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginButton.getText().toString().equals("Connect"))
                {
                    loginButton.setText("Disconnect");
                    loginButton.setBackgroundResource (R.drawable.login_button_background_disconnect);
                    ConnectToWebsocket();
                }
                else
                {
                    loginButton.setText("Connect");
                    loginButton.setBackgroundResource(R.drawable.login_button_background_connect);
                    DisconnectWebsocket();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void DisconnectWebsocket()
    {
        ws.close(1000, "closing websocket");
    }

    private void ConnectToWebsocket()
    {
        String server = serverEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        client = new OkHttpClient();
        Request request = new Request.Builder().url(server).build();
        MyWebsocketListener listener = new MyWebsocketListener((MainActivity) getActivity());

        ws = client.newWebSocket(request, listener);
        ws.send("username:" + username);
        ws.send("password:" + password);

        client.dispatcher().executorService().shutdown();
    }
}
