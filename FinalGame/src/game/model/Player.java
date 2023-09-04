package game.model;

import game.model.ComputerAI.AI;

import java.util.ArrayList;
import java.util.Hashtable;

public class Player {

    private String name;
    private double hp;
    private double damage;
    private Ammo a;
    private ArrayList<Item> items;
    private Hashtable<StatusEffect,Integer>se = new Hashtable<>();
    private AI ai;
    private Integer curP;
    private double newY;
    private double curY;

    public Player(String name, Ammo a){
        this.name = name;
        hp = 10;
        this.a = a;
        curP = 1;
        newY = 0;
        curY = newY;
        items = new ArrayList<>();
        damage = 2;
        for (StatusEffect s : StatusEffect.values()){
            se.put(s,0);
        }
        ai = new AI(a.getCharacter().getR(),a.getCharacter().getr(),10);
    }

    public Ammo getA(){return a;}

    public double getHp(){
        return hp;
    }

    public double getDamage(){return damage;}

    public ArrayList<Item>getItems(){return items;}

    public Hashtable<StatusEffect,Integer>getSe(){return se;}

    public Integer getCurP(){return curP;}

    public AI getAi(){return ai;}

    public void setHp(double hp){this.hp = hp;}

    public void setDamage(double damage){this.damage = damage;}

    public void setCurP(Integer curP){this.curP = curP;}

    public void setNewY(double newY){this.newY = newY;}


    public void updateHp(double damage){
        hp -= damage;
    }
    public void gotHit(boolean computerAiUpdatedForTurn){
        if (!computerAiUpdatedForTurn) {
            ai.updatePos(true,se.get(StatusEffect.GLUE) == 0);
        }
            updateHp(damage);
    }

    public void move(){
        if(se.get(StatusEffect.GLUE) == 0) {

            double curY = a.getCharacter().getr().getTranslateY();
           if (curY > newY) {
                a.getCharacter().setDy(-1);
                a.getCharacter().setCanMove(true);
            }else if (curY < newY){
                a.getCharacter().setDy(1);
                a.getCharacter().setCanMove(true);

            }else{
                a.getCharacter().setCanMove(false);
            }


        }
    }

}
