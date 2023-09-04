package game.model;

import java.util.ArrayList;

public enum Enemy {
    EASYMONKEY{
        @Override
        public ArrayList<Item>getItems(int counter){
            ArrayList<Item>items = new ArrayList<>();
            return items;
        }
        @Override
        public String enemyName(){return "Easy Monkey";}
        @Override
        public String desc(){return "a monkey with no items";}
    },BIGCOCONUTMONKEY{
        @Override
        public ArrayList<Item>getItems(int counter){
            ArrayList<Item>items = new ArrayList<>();
            if (counter == 0) {
                items.add(Item.SHIELD);
                items.add(Item.BIG);
            }
            return items;
        }
        @Override
        public String enemyName(){return "Big Coconut Monkey";}
        @Override
        public String desc(){return "a monkey with a shield and a big coconut";}
    },SPICYMONKEY{
        @Override
        public ArrayList<Item>getItems(int counter){
            ArrayList<Item>items = new ArrayList<>();
            if (counter == 0) {
                items.add(Item.SHIELD);
            }
            items.add(Item.SPICY);
            return items;
        }
        @Override
        public String enemyName(){return "Spicy Monkey";}
        @Override
        public String desc(){return "a monkey that only throws spicy coconuts";}
    },CRAZYMONKEY{
        @Override
        public ArrayList<Item>getItems(int counter){
            ArrayList<Item>items = new ArrayList<>();
            if (counter == 0) {
                items.add(Item.SHIELD);
            }
            items.add(Item.CRAZYCOCONUT);
            return items;
        }
        @Override
        public String enemyName(){return "Crazy Monkey";}
        @Override
        public String desc(){return "a monkey that only throws crazy coconuts";}
    },STICKYMONKEY{
        @Override
        public ArrayList<Item>getItems(int counter){
            ArrayList<Item>items = new ArrayList<>();
            if (counter == 0) {
                items.add(Item.SHIELD);
            }
            items.add(Item.STICKYCOCONUT);
            return items;
        }
        @Override
        public String enemyName(){return "Sticky Monkey";}
        @Override
        public String desc(){return "a monkey that only throw sticky coconuts";}
    },STRONGMONKEY{
        @Override
        public ArrayList<Item>getItems(int counter){
            ArrayList<Item>items = new ArrayList<>();
            if (counter == 0) {
                items.add(Item.SHIELD);
                items.add(Item.ARMOR);
                items.add(Item.ROCK);
            }
            return items;
        }
        @Override
        public String enemyName(){return "Strong Monkey";}
        @Override
        public String desc(){return "a monkey with armor that throws rocks";}
    };

    public abstract ArrayList<Item>getItems(int counter);
    public abstract String enemyName();
    public abstract String desc();
}

