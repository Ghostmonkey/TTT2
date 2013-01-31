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
    private String playerBank;
    
    public TTTLogic(TTT view) {
        this.view = view;
        to = "";
        from = "";
    }

    //Starts server thread
    public void listen(String ipAddress, int port) {
        try {
            server = new TTTServer(this, ipAddress, port);
        } catch (UnknownHostException ex) {
            systemMessage(ex.getMessage().toString());
        }
        runServer();
    }
    
    //Starts Client thread
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
            view.appendSystemMessage("Not connected!");
        }
    }

    public void sendSystemMessage(String message) {
        if (server != null) {
            view.appendSystemMessage("Server:" + message);
            server.send(message);
        } else if (client != null) {
            view.appendSystemMessage("Client:" + message);
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
    public void makeMove(){
        if(client != null){
            sendSystemMessage("201:From:" + getFrom() + "To:" + getTo());
            from = "";
            to = "";
        }
        if(server != null){
            if(game.makeMove(1, from, to)){
                checkForWinner();
                sendField();
                view.updateField();
                view.enableField(false);
                clientMove();
            }else{
                systemMessage("Invalid move. Try again!");
                from = "";
                to = "";
                view.updateField();
            }
            
        }
    }
    
    //Send indication that server is waiting for your move
    public void clientMove(){
        view.enableField(false);
        view.appendSystemMessage("Waiting for client to move");
        sendSystemMessage("200:Make your move!");       
    }
    
    //Server sends the current playfield to the client
    public void sendField(){
        sendSystemMessage("100:x0y0=" + game.getClientBank() + "x1y1=" + getX1y1() + "x1y2=" + getX1y2() + "x1y3=" + getX1y3() + "x2y1=" + getX2y1() + "x2y2=" + getX2y2() + "x2y3=" + getX2y3() + "x3y1=" + getX3y1() + "x3y2=" + getX3y2() + "x3y3=" + getX3y3());
    }
    
    //Server Winner Declared
    public void checkForWinner(){
        if(game.checkWin() == 1){
            sendSystemMessage("199:Game over, opponent wins!");
            //view.appendSystemMessage("Game over you win!!!");
            WinDialogue winDialogue = new WinDialogue(view, true, "Congratulations you win!");
            winDialogue.setLocationRelativeTo(view);
            winDialogue.setVisible(true);
            view.enableField(false);
        }
        if(game.checkWin() == 2){
            sendSystemMessage("199:Congratulations you win!");
            //view.appendSystemMessage("Game over opponent wins!!");
            WinDialogue winDialogue = new WinDialogue(view, true, "Game over opponent wins!");
            winDialogue.setLocationRelativeTo(view);
            winDialogue.setVisible(true);
            view.enableField(false);
        }
    }
    
    //Takes in message from either client or server and processes the information
    public void parseMessage(String message){
        systemMessage(message.substring(4));
        int messageCode;
        messageCode = Integer.parseInt(message.substring(0,3));
        switch(messageCode){
            case 100:
                //Client only: Parse the field and update the view of it
                if (server == null){
                    view.appendSystemMessage("Board recieved");
                    game.setClientBank(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x0y0=")+5))));
                    System.out.println(game.getClientBank());
                    game.setX1y1(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x1y1=")+5))));
                    System.out.println(getX1y1());
                    game.setX1y2(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x1y2=")+5))));
                    System.out.println(getX1y2());
                    game.setX1y3(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x1y3=")+5))));
                    System.out.println(getX1y3());
                    game.setX2y1(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x2y1=")+5))));
                    System.out.println(getX2y1());
                    game.setX2y2(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x2y2=")+5))));
                    System.out.println(getX2y2());
                    game.setX2y3(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x2y3=")+5))));
                    System.out.println(getX2y3());
                    game.setX3y1(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x3y1=")+5))));
                    System.out.println(getX3y1());
                    game.setX3y2(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x3y2=")+5))));
                    System.out.println(getX3y2());
                    game.setX3y3(Integer.parseInt(Character.toString(message.charAt(message.indexOf("x3y3=")+5))));
                    System.out.println(getX3y3());
                    view.enableField(true);
                    view.updateField();
                }else{
                    server.send("900:Invalid message. Server does not accept board data");
                }
                break;
            case 101:
                //Parse new game request. Start new game 
                server.send("102:Ok new game starting");
                game = new TTTGame(1);
                player = 1;
                playerBank = "x9y9";
                setRole();
                sendField();
                view.updateField();
                //Your move
                sendSystemMessage("200:Your move");
                view.enableField(false);
                //else
                //server.send("201: Error! Game already in progress");
                break;
            case 102:
                //Parse acknowledgement of new game
                game = new TTTGame(2);
                player = 2;
                playerBank = "x0y0";
                setRole();
                break;
            case 200: //Your move incoming
                systemMessage(message);
                
                view.enableField(true);
                view.updateField();
                break;
            case 201:
                //Parse Move
                systemMessage(message);
                //Strängen funkar inte From:x0y0To:x1y2
                //String moveTo = message.substring(message.charAt(message.indexOf(("From:")+1)),message.charAt(message.indexOf(("From:")+5)));
                String moveFrom = message.substring(message.indexOf("From:")+5, message.indexOf("From:")+9);
                String moveTo = message.substring(message.indexOf("To:")+3, message.indexOf("To:")+7);
                systemMessage("To:" + moveTo);
                if(game.makeMove(2, moveFrom, moveTo)){
                    sendSystemMessage("202:Move confirmed");
                    checkForWinner();
                    view.enableField(true);
                    view.updateField();
                    
                }else{
                    sendSystemMessage("299:Invalid move, try again");
                }
                break;
            case 202: //Move Confirmed
                view.enableField(false);
                break;
            case 299: //Invalid move
                to = "";
                from = "";
                view.updateField();
                break;
            case 199://Winner declared
                WinDialogue winDialogue = new WinDialogue(view, true, message.substring(4));
                winDialogue.setLocationRelativeTo(view);
                winDialogue.setVisible(true);
            view.enableField(false);
            game = null;
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

    //Methods for the view to update game status
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
    
    //Get other bank. For debugging only
    public int getOBank(){
        int o;
        o = -1;
        if(server != null){
            o = game.getClientBank();
        }
        return o;
    }
    
    public String getPlayerBank(){
        return playerBank;
    }
    
    public int getX1y1() {
        return game.getX1y1();
    }

    public int getX1y2() {
        return game.getX1y2();
    }

    public int getX1y3() {
        return game.getX1y3();
    }

    public int getX2y1() {
        return game.getX2y1();
    }

    public int getX2y2() {
        return game.getX2y2();
    }

    public int getX2y3() {
        return game.getX2y3();
    }

    public int getX3y1() {
        return game.getX3y1();
    }

    public int getX3y2() {
        return game.getX3y2();
    }
    
    public int getX3y3() {
        return game.getX3y3();
    }

    public String getFrom(){
        return from;
    }
    
    public void setFrom(String from){
        this.from = from;
    }
    public void setTo(String to){
        this.to = to;
    }
    
    public String getTo(){
        return to;
    }
    public int getPlayer(){
        return player;
    }
    
    public int getButtonNr(String buttonName){
        return game.get(buttonName);
    }
    
}
