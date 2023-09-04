package game.model;

import java.util.HashSet;

public class Game {

    private Player p1;
    private Player p2;
    private Player current;
    private HashSet<Item> p1InPlayItems;
    private HashSet<Item>p2InPlayItems;
    private int counter;
    private Enemy e;
    private boolean computerAiUpdatedForTurn;

    public Game(String p1name, String p2name,Player p1,Ammo p1ammo,Ammo p2ammo, Enemy e){
        //p1 = new Player(p1name,p1ammo);
        this.p1 = p1;
        p2 = new Player(p2name,p2ammo);
        HashSet<Item>p1InPlayItems = new HashSet<>();
        HashSet<Item>p2InPlayItems = new HashSet<>();
        this.p1InPlayItems = p1InPlayItems;
        this.p2InPlayItems = p2InPlayItems;
        this.e = e;
        current = p1;
        counter = 1;
        computerAiUpdatedForTurn = false;

        for (Item i : e.getItems(0)){
            p2InPlayItems.add(i);
        }


    }
    public void setCounter(int counter){this.counter = counter;}
    public void setComputerAiUpdatedForTurn(boolean computerAiUpdatedForTurn){this.computerAiUpdatedForTurn = computerAiUpdatedForTurn;}

    public Player getCurrent(){
        return current;
    }
    public Player getP1(){
        return p1;
    }
    public Player getP2(){
        return p2;
    }
    public int getCounter(){return counter;}
    public Enemy getE(){return e;}
    public boolean getComputerAiUpdatedForTurn(){return computerAiUpdatedForTurn;}


    public HashSet<Item>getP1InPlayItems(){return p1InPlayItems;}
    public HashSet<Item>getP2InPlayItems(){return p2InPlayItems;}

    public boolean gameOver(){
        return p1.getHp() <= 0 || p2.getHp() <= 0;
    }
    public boolean p1Turn(){
        return current == p1;
    }

    public void switchTurn(){
        if(p1Turn()){
            current = p2;
        }
        else{
            current = p1;
        }
    }
    public void addEnemyItems() {
        if (!getCurrent().getA().getCharacter().getCanMove()) {
            for (Item i : e.getItems(counter)) {
                p2InPlayItems.add(i);
            }
        }
    }
}
