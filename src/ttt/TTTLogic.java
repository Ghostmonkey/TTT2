package ttt;

import java.net.UnknownHostException;

public class TTTLogic {

    private TTT view;
    private TTTClient client;
    private TTTServer server;
    private Thread t;
    private TTTGame game;
    private String from;
    private String to;
    private int player;

    public TTTLogic(TTT view) {
        this.view = view;
    }

    //Starts server part
    public void listen(String ipAddress, int port) {
        try {
            server = new TTTServer(this, ipAddress, port);
        } catch (UnknownHostException ex) {
            systemMessage(ex.getMessage().toString());
        }
        runServer();
    }
    
    //Starts Client part
    public void connect(String ipAddress, int port) {
        try {
            client = new TTTClient(this, ipAddress, port);
        } catch (UnknownHostException ex) {
            systemMessage(ex.getMessage().toString());
        }
        runClient();
    }

    public void runServer() {
        t = new Thread(server, "t");
        t.start();
    }

    public void runClient() {
        t = new Thread(client, "t");
        t.start();
    }
    
    //Kan effektiviseras
    public void sendChatMessage(String message) {
        if (server != null) {
            server.send("999:" + message);
            view.appendChatMessage("Me: " + message);
        } else if (client != null) {
            client.send("999:" + message);
            view.appendChatMessage("Me: " + message);
        } else {
        }
    }

    public void sendSystemMessage(String message) {
        if (server != null) {
            System.out.println("Logic class sent: " + message);
            view.appendSystemMessage("Serversysmessage:" + message);
            server.send(message);
        } else if (client != null) {
            System.out.println("Logic class sent: " + message);
            view.appendSystemMessage("Clientsysmessage:" + message);
            client.send(message);
        } else {
            view.appendSystemMessage("Not connected!");
        }
    }

    public void setRole() {
        if (server != null) {
            view.setRole("server");
        } else if (client != null) {
            view.setRole("client");
        } else {
        }
    }

    //Updates view system field
    public void systemMessage(String message) {
        view.appendSystemMessage("Opponent: " + message);
    }

    //Updates view chat field
    public void chatMessage(String message) {
        view.appendChatMessage("Opponent: " + message);
    }
    
    //Client send move
    public void sendMove(){
        if(client != null){
            sendSystemMessage("101:MoveFrom:" + getMoveFrom() + "MoveTo:" + getMoveTo());
            //logik kolla så att det är ett giltigt drag
        }
        if(server != null){
            //makemove
            sendField();
        }
    }
    
    //Send indication that server is waiting for your move
    public void clientMove(){
        view.enableField(false);
        view.appendSystemMessage("Waiting for client to move");
        sendSystemMessage("104:Make your move!");       
    }
    
    //Server sends the current playfield to the client
    public void sendField(){
        sendSystemMessage("100:x0y0=3" + game.getClientBank() + "x1y1=" + getX1y1() + "x1y2=" + getX1y2() + "x1y3=" + getX1y3() + "x2y1=" + getX2y1() + "x2y2=" + getX2y2() + "x2y3=" + getX2y3() + "x3y1=" + getX3y1() + "x3y2=" + getX3y2() + "x3y3=" + getX3y3());
    }
    
    //Server Winner Declared
    public void checkForWinner(){
        if(game.checkWin() == 1){
            sendSystemMessage("199:Game over opponent wins!!!");
            view.appendSystemMessage("Game over you win!!!");
        }
        if(game.checkWin() == 2){
            sendSystemMessage("199:Game over, you win!!!");
            view.appendSystemMessage("Game over opponent wins!!");
        }
    }
    
    //Takes in message from either client or server and processes the information
    public void parseMessage(String message){
        int messageCode;
        messageCode = Integer.parseInt(message.substring(0,3));
        switch(messageCode){
            case 100:
                //Client only: Parse the field and update the view of it
                if (server == null){
                view.appendSystemMessage("Board recieved");
                game.setClientBank(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x0y0=")+5))));
                System.out.println(game.getClientBank());
                setX1y1(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x1y1=")+5))));
                System.out.println(getX1y1());
                setX1y2(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x1y2=")+5))));
                System.out.println(getX1y2());
                setX1y3(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x1y3=")+5))));
                System.out.println(getX1y3());
                setX2y1(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x2y1=")+5))));
                System.out.println(getX2y1());
                setX2y2(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x2y2=")+5))));
                System.out.println(getX2y2());
                setX2y3(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x2y3=")+5))));
                System.out.println(getX2y3());
                setX3y1(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x3y1=")+5))));
                System.out.println(getX3y1());
                setX3y2(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x3y2=")+5))));
                System.out.println(getX3y2());
                setX3y3(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x3y3=")+5))));
                System.out.println(getX3y3());
                view.updateField();
                }else{
                    server.send("900:Bad call. Server does not accept board data");
                }
                break;
            case 101:
                //Server Only
                server.send("102:Ok new game starting");
                game = new TTTGame(1);
                player = 1;
                view.setRole("server");
                sendField();
                view.updateField();
                //Your move
                //else
                //server.send("201: Error! Game already in progress");
                break;
            case 102:
                game = new TTTGame(2);
                player = 2;
                view.setRole("client");
                break;
            case 103:
                //I win
                view.appendSystemMessage(message.substring(4));
                gameOver();
                break;
            case 104:
                //Recieved move
                view.enableField(true);
                break;
            case 998://System cosmetic message
                systemMessage(message.substring(4));
                break;
            case 999://Chat message cosmetic
                chatMessage(message.substring(4));
                break;
        }
        //view.appendChatMessage(message);
    }

    private void gameOver() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //For the view to check bank status does not affect game logic
    public int getBank(){
        int bank;
        bank = 0;
        if(server != null){
            bank = game.getServerBank();
        }
        if(client!= null){
            bank = game.getClientBank();
        }
        return bank;
    }
    
    public int getX1y1() {
        return game.getX1y1();
    }

    public void setX1y1(int x1y1) {
        game.setX1y1(x1y1);
    }

    public int getX1y2() {
        return game.getX1y2();
    }

    public void setX1y2(int x1y2) {
        game.setX1y2(x1y2);
    }

    public int getX1y3() {
        return game.getX1y3();
    }

    public void setX1y3(int x1y3) {
        game.setX1y3(x1y3);
    }

    public int getX2y1() {
        return game.getX2y1();
    }

    public void setX2y1(int x2y1) {
        game.setX2y1(x2y1);
    }

    public int getX2y2() {
        return game.getX2y2();
    }

    public void setX2y2(int x2y2) {
        game.setX2y2(x2y2);
    }

    public int getX2y3() {
        return game.getX2y3();
    }

    public void setX2y3(int x2y3) {
        game.setX2y3(x2y3);
    }

    public int getX3y1() {
        return game.getX3y1();
    }

    public void setX3y1(int x3y1) {
        game.setX3y1(x3y1);
    }

    public int getX3y2() {
        return game.getX3y2();
    }

    public void setX3y2(int x3y2) {
        game.setX3y2(x3y2);
    }

    public int getX3y3() {
        return game.getX3y3();
    }

    public void setX3y3(int x3y3) {
        game.setX3y3(x3y3);
    }
    
    public String getMoveFrom(){
        return from;
    }
    
    public void setMoveFrom(String from){
        this.from = from;
    }
    
    public String getMoveTo(){
        return to;
    }
    
    public void setTo(String to){
        this.to = to;
    }

    public int getPlayer(){
        return player;
    }
    
}
