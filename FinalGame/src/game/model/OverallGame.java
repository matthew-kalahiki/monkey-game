package game.model;

import game.model.DataStructures.ArrayQueue;
import game.model.DataStructures.Queue;

import java.util.ArrayList;

public class OverallGame {
    private String playerName;
    private Player player;
    private ArrayList<Item>playerPermItems;
    private Queue<Item>gameItems;
    private Queue<Enemy>gameEnemies;
    private Enemy currEnemy;
    private ArrayList<Enemy>defeatedEnemies;
    private double defaultHealth;
    private double defaultDamage;
    private int round;
    private int gameSize;


    public OverallGame(String playerName,Ammo playerAmmo){
        defaultDamage = 2;
        defaultHealth = 10;
        round = 0;
        playerPermItems = new ArrayList<>();
        defeatedEnemies = new ArrayList<>();
        this.playerName = playerName;
        player = new Player(playerName,playerAmmo);
        gameItems = Item.getRandomItems();
        gameEnemies = new ArrayQueue<>();
        for (Enemy e : Enemy.values()){
            gameEnemies.add(e);
        }
        gameSize = gameEnemies.size();
    }

    public void addPlayerItem(){player.getItems().add(gameItems.remove());}

    public String getPlayerName(){return playerName;}

    public Player getPlayer(){return player;}

    public int getRound(){return round;}

    public ArrayList<Item>getPlayerPermItems(){return playerPermItems;}

    public Queue<Enemy>getGameEnemies(){return gameEnemies;}

    public ArrayList<Enemy>getDefeatedEnemies(){return defeatedEnemies;}

    public Enemy getCurrEnemy(){return currEnemy;}

    public int getGameSize(){return gameSize;}

    public void setGameSize(int gameSize){this.gameSize = gameSize;}

    public Enemy nextRound(){
        Enemy c = gameEnemies.remove();
        if (player.getHp() < defaultHealth) {
            player.setHp(player.getHp() + ((defaultHealth - player.getHp())/2));
        }
        player.setDamage(defaultDamage);
        if (round != 0){
            defeatedEnemies.add(currEnemy);
        }
        round++;
        if (round == 2){
            player.getItems().add(Item.SHIELD);
        }
        for (int i = 0; i < 2; i++) {
            if (player.getItems().size() < 8) {
                addPlayerItem();
            }
        }
        currEnemy = c;
        return c;
    }


}
