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
    board.put("serverBank", 1);
    board.put("clientBank", 3);
    board.put("x1y1", 0);
    board.put("x1y1", 1);
    board.put("x1y2", 0);
    board.put("x1y3", 1);
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
        if(getX1y1() + getX2y1() + getX3y1() == 3 || getX1y2() + getX2y2() + getX3y2() == 3 || getX1y3() + getX2y3() + getX3y3() == 3){
            winner = 1;
        }
        if(getX1y1() + getX2y1() + getX3y1() == 6 || getX1y2() + getX2y2() + getX3y2() == 6 || getX1y3() + getX2y3() + getX3y3() == 6){
            winner = 2;
        }
        //Cols
        if(getX1y1() + getX1y2() + getX1y3() == 3 || getX2y1() + getX2y2() + getX2y3() == 3 || getX3y1() + getX3y2() + getX3y3() == 3){
            
        }
        if(getX1y1() + getX1y2() + getX1y3() == 3 || getX2y1() + getX2y2() + getX2y3() == 3 || getX3y1() + getX3y2() + getX3y3() == 3){
            winner = 1;
        }
        if(getX1y1() + getX1y2() + getX1y3() == 6 || getX2y1() + getX2y2() + getX2y3() == 6 || getX3y1() + getX3y2() + getX3y3() == 6){
            winner = 2;
        }
        //Diagonals
        if(getX1y1() + getX2y2() + getX3y3() == 3 || getX3y1() + getX2y2() + getX1y3() == 3){
            winner = 1;
        }
        if(getX1y1() + getX2y2() + getX3y3() == 6 || getX3y1() + getX2y2() + getX1y3() == 6){
            winner = 2;
        }
        return winner;
    }

    public boolean makeMove(int player, String from, String to){
        boolean valid = true;
        if(player == 1){
            
        }
        if(player == 2){
            
        }
        if(board.get(from) == player && board.get(to) == 0){
            valid = true;
        }
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
        return board.get("serverBank");
    }

    public void setServerBank(int serverBank) {
        board.put("serverBank", serverBank);
    }

    public int getClientBank() {
        return board.get("clientBank");
    }

    public void setClientBank(int clientBank) {
        board.put("clientBank", clientBank);
    }
}
