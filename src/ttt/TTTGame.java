/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ttt;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dahle
 */
public class TTTGame {
    
    //Methods assume that server is player 1 and client is player 2
    private Map<String, Integer> board;
        
    public TTTGame(int player){
    board = new HashMap();
    board.put("x0y0", 3);//Client bank
    board.put("x9y9", 3);//Server bank
    board.put("x1y1", 0);
    board.put("x1y1", 0);
    board.put("x1y2", 0);
    board.put("x1y3", 0);
    board.put("x2y1", 0);
    board.put("x2y2", 0);
    board.put("x2y3", 0);
    board.put("x3y1", 0);
    board.put("x3y2", 0);
    board.put("x3y3", 0);
    }
    
    //Check for win
    
    public int checkWin(){
        //Does not work
        int winner;
        winner = 0;
        //Rows
        if(getX1y1() == getX2y1() && getX1y1() == getX3y1() && getX1y1() != 0){
            winner = getX1y1();
        }
        if(getX1y2() == getX2y2() && getX1y2() == getX3y2() && getX1y1() != 0){
            winner = getX1y2();
        }
        if(getX1y3() == getX2y3() && getX1y3() == getX3y3() && getX1y3() != 0){
            winner = getX1y3();
        }
        
        //Columns
        if(getX1y1() == getX1y2() && getX1y1() == getX1y3() && getX1y1() != 0){
            winner = getX1y1();
        }
        if(getX2y1() == getX2y2() && getX2y1() == getX2y3() && getX2y1() != 0){
            winner = getX2y1();
        }
        if(getX3y1() == getX3y2() && getX3y1() == getX3y3() && getX3y1() != 0){
            winner = getX3y1();
        }
        
        //Diagonals
        if(getX1y1() == getX2y2() && getX1y1() == getX3y3() && getX1y1() != 0){
            winner = getX1y1();
        }
        if(getX3y1() == getX2y2() && getX3y1() == getX1y3() && getX3y1() != 0){
            winner = getX3y1();
        }
        return winner;
    }

    public boolean makeMove(int player, String from, String to){
        boolean valid = true;
        
        if(!(board.get(to) == 0)){
            valid = false;
            System.out.println("Till är upptagen");
        }
        
        //Kollar om det är en bank och i så fall om den är 0 eller mindre 
        if((from.equalsIgnoreCase("x0y0") || from.equalsIgnoreCase("x9y9")) && board.get(from) <= 0){
            valid = false;
            System.out.println("Bank men tom");
            if(!(board.get(from) == player)){//Problem för banken är inte spelarens nummer
                valid = false;
                System.out.println("Från är inte din");
            }   
        }
        
        //Genomför draget
        if(from.equalsIgnoreCase("x0y0") || from.equalsIgnoreCase("x9y9")){
            board.put(from, (board.get(from) - 1));
        }else{
            board.put(from, 0);
        }
        board.put(to, player);
        return valid;
    }
    
    public int getX1y1() {
        return board.get("x1y1");
    }

    public void setX1y1(int x1y1) {
        board.put("x1y1", x1y1);
    }

    public int getX1y2() {
        return board.get("x1y2");
    }

    public void setX1y2(int x1y2) {
        board.put("x1y2", x1y2);
    }

    public int getX1y3() {
        return board.get("x1y3");
    }

    public void setX1y3(int x1y3) {
        board.put("x1y3", x1y3);
    }

    public int getX2y1() {
        return board.get("x2y1");
    }

    public void setX2y1(int x2y1) {
        board.put("x2y1", x2y1);
    }

    public int getX2y2() {
        return board.get("x2y2");
    }

    public void setX2y2(int x2y2) {
        board.put("x2y2", x2y2);
    }

    public int getX2y3() {
        return board.get("x2y3");
    }

    public void setX2y3(int x2y3) {
        board.put("x2y3", x2y3);
    }

    public int getX3y1() {
        return board.get("x3y1");
    }

    public void setX3y1(int x3y1) {
        board.put("x3y1", x3y1);
    }

    public int getX3y2() {
        return board.get("x3y2");
    }

    public void setX3y2(int x3y2) {
        board.put("x3y2", x3y2);
    }

    public int getX3y3() {
        return board.get("x3y3");
    }

    public void setX3y3(int x3y3) {
        board.put("x3y3", x3y3);
    }

    public int getServerBank() {
        return board.get("x9y9");
    }

    public void setServerBank(int serverBank) {
        board.put("x9y9", serverBank);
    }

    public int getClientBank() {
        return board.get("x0y0");
    }

    public void setClientBank(int clientBank) {
        board.put("x0y0", clientBank);
    }
    
    public int get(String name){
        int nr;
        nr = board.get(name);
        return nr;
    }
}
