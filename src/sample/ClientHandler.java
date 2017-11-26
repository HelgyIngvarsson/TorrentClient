package sample;

import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public  class ClientHandler extends Thread{
        Socket socket;
        Client client;
        File output;
        File torrentPath;
        String status ="";
        BufferedWriter out;

public ClientHandler(String outputDir, String torrentFile) throws IOException {
    this.output = new File(outputDir);
    this.torrentPath = new File(torrentFile);
    this.socket = new Socket("localhost", 4444);
    out= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        }
@Override
    public void run(){
    try {
        out.write(torrentPath.getAbsolutePath());
        out.flush();
        out.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    try {
        SharedTorrent torrent = SharedTorrent.fromFile(torrentPath, output);
        client = new Client(socket.getInetAddress(),torrent);
    } catch (IOException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    client.setMaxDownloadRate(50.0);
    client.setMaxUploadRate(50.0);

    client.download();

    while (!Client.ClientState.DONE.equals(client.getState())) {
        status ="Name: "+ client.getTorrent().getName()+" state -"+client.getState()+" bytes down:"+client.getTorrent().getDownloaded()+
                "bytes up:"+client.getTorrent().getUploaded()+" Left: "+client.getTorrent().getLeft()+" peers: "+client.getPeers().size();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

String getStatus(){
        return status;
}

void del(){
    client.stop();
}
        }