package tk.samgrogan.al;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by ghost on 7/11/2017.
 */

public class TcpClient extends AsyncTask<Void, Void, Void> {

    String ip;
    int port;
    String message;

    public TcpClient(String ip, int port, String message){
        this.ip = ip;
        this.port = port;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Socket socket = new Socket(ip, port);
            BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String msg = message;

            outputStream.write(msg);
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
