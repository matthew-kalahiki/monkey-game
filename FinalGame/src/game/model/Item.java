package game.model;

import game.model.DataStructures.ArrayQueue;

import java.util.ArrayList;
import java.util.Random;

public enum Item {

    BIG{
        @Override
        public boolean isPermanent(){return false;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc(){
            return("Makes your coconut bigger");
        }
    },
    SHIELD{
        @Override
        public boolean isPermanent(){return true;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc(){
            return("creates a barrier in front of your that only you coconut can pass through");
        }
    },
    BANANA{
        @Override
        public boolean isPermanent(){return false;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc(){
            return("adds 2 health points");
        }
    },
    ROCK{
        @Override
        public boolean isPermanent(){return false;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc() {
            return ("replaces coconut with a rock: does 1 more damage");
        }
    },
    HIT{
        @Override
        public boolean isPermanent(){return false;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc() {
            return ("automatically hits your opponent for your turn");
        }
    },
    ARMOR{
        @Override
        public boolean isPermanent(){return true;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc() {
            return ("only take half damage while wearing");
        }
    },
    THING{
        @Override
        public boolean isPermanent(){return false;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc() {
            return ("if you have the THING, take another turn if you successfully hit your opponent");
        }
    },
    POWPOW{
        @Override
        public boolean isPermanent(){return false;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc() {
            return ("gives you the right power based on your current angle");
        }
    },
    TRACER{
        @Override
        public boolean isPermanent(){return false;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc() {
            return ("gives you an angle that will hit your opponent based on your current power");
        }
    },
    SPICY{
        @Override
        public boolean isPermanent(){return false;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc() {
            return ("If coconut successfully hits, it burns the enemy for 3 turns");
        }
    },
    CRAZYCOCONUT{
        @Override
        public boolean isPermanent(){return false;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc() {
            return ("makes your coconut so crazy, it confuses the opponent for 3 turns if they are hit");
        }
    },
    STICKYCOCONUT{
        @Override
        public boolean isPermanent(){return false;}

        @Override
        public boolean isCombatItem(){return true;}

        @Override
        public String desc() {
            return ("glues opponent to tree for 3 turns if hit");
        }
    };

    public abstract boolean isPermanent();
    public abstract boolean isCombatItem();
    public abstract String desc();

    public static ArrayQueue<Item> getRandomItems(){
        ArrayQueue<Item> deck = new ArrayQueue<>();
        ArrayList<Item> items = new ArrayList<>();
        for(int i = 0;i < 2;i++) {
            for (int j = 0; j < Item.values().length; j++){
                if (Item.values()[j] != Item.SHIELD) {
                    items.add(Item.values()[j]);
                }
            }
        }
        while(deck.size() < 2 * (Item.values().length - 1)){
            Random rand = new Random();
            int r = rand.nextInt(items.size());
            if (items.get(r) != null){
                deck.add(items.get(r));
                items.remove(r);
            }
        }
        return deck;
    }

}
