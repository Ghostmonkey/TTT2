package ttt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;

public class TTTClient implements Runnable{
    
    private TTTLogic logic; // Main logic
    private Socket clientSocket; //Client socket
    private InetAddress serverIpAddress; //Server IP
    private PrintWriter out; //Socket output socket
    private BufferedReader in; //Socket input socket
    private int serverPort; // Server port
    
    //Constructor
    public TTTClient(TTTLogic logic, String ipAddress, String port) throws UnknownHostException{
        this.logic = logic;
        this.serverIpAddress = InetAddress.getByName(ipAddress);
        this.serverPort = Integer.parseInt(port);
    }
    
    //Constructor overload
    public TTTClient(TTTLogic logic, String ipAddress, int port) throws UnknownHostException{
        this.logic = logic;
        this.serverIpAddress = InetAddress.getByName(ipAddress);
        this.serverPort = port;
    }
    
    //Constructor overload
    public TTTClient(TTTLogic logic, InetAddress ipAddress, String port) throws UnknownHostException{
        this.logic = logic;
        this.serverIpAddress = ipAddress;
        this.serverPort = Integer.parseInt(port);
    }
    
    //Constructor overload
    public TTTClient(TTTLogic logic, InetAddress ipAddress, int port) throws UnknownHostException{
        this.logic = logic;
        this.serverIpAddress = ipAddress;
        this.serverPort = port;
    }
    //Connect to server
    public void connect(){
        String serverMessage;
        try {
            clientSocket = new Socket(serverIpAddress, serverPort);
            logic.systemMessage("Connected to: " + clientSocket.getInetAddress().getHostAddress().toString() + ":" + Integer.toString(clientSocket.getPort()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("101: Request game");
            while(true){
                String line;
                line = in.readLine();
                System.out.println("TTTClient recieved: " + line);
                logic.parseMessage(line);
            }
        } catch (UnknownHostException e) {
            logic.systemMessage(e.getMessage().toString());
        } catch (IOException e) {
            logic.systemMessage(e.getMessage().toString());
        }
    }
    
    public void send(String message){
        try{
            System.out.println("Clientclass sent:" + message);
            out.println(message);
        }catch(Exception e){
            logic.systemMessage(e.getMessage().toString());
        }
    }

    @Override
    public void run() {
        connect();
    }
    
}
