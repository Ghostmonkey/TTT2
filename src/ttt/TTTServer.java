package ttt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Dahle
 */
public class TTTServer implements Runnable{
   
    private TTTLogic logic;
    private InetAddress bindAddress; 
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;
    
    public TTTServer(TTTLogic logic, String ipAddress, String port) throws UnknownHostException{
        this.logic = logic;
        this.bindAddress = InetAddress.getByName(ipAddress);
        this.port = Integer.parseInt(port);
    }
    
    public TTTServer(TTTLogic logic, InetAddress ipAddress, String port){
        this.logic = logic;
        this.bindAddress = ipAddress;
        this.port = Integer.parseInt(port);
    }
    
    public TTTServer(TTTLogic logic, String ipAddress, int port) throws UnknownHostException{
        this.logic = logic;
        this.bindAddress = InetAddress.getByName(ipAddress);
        this.port = port;
    }
    
    public TTTServer(TTTLogic logic, InetAddress ipAddress, int port){
        this.logic = logic;
        this.bindAddress = ipAddress;
        this.port = port;
    }
    
    private void listen(){
        String clientMessage;
        try {
            serverSocket = new ServerSocket(port, 10, bindAddress);
            logic.systemMessage("Connection open on: " + serverSocket.getInetAddress().getHostAddress().toString() + ":" + Integer.toString(serverSocket.getLocalPort()));
            Socket connectionSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            out = new PrintWriter(connectionSocket.getOutputStream(), true);
            logic.systemMessage(connectionSocket.getRemoteSocketAddress().toString().substring(1) + " Connected");
            send("998:Welcome to" + serverSocket.getInetAddress().getHostAddress().toString() + ":" + Integer.toString(serverSocket.getLocalPort()));
            while(true){
                String line;
                line = in.readLine();
                System.out.println("From client:" + line);
                logic.parseMessage(line);
            }
        }catch(IOException e) {
            logic.systemMessage(e.getMessage());
        }
    }
    
    public void send(String message){
        try{
            System.out.println("Serverclass sent:" + message);
            out.println(message);
        }catch(Exception e){
            logic.systemMessage(e.getMessage().toString());
        }
    }
    
    @Override
    public void run() {
        listen();
    }

}
