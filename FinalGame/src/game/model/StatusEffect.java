package game.model;

public enum StatusEffect {

    GLUE{
        @Override
        public String desc(){return "player is unable to move";}
    },BURN{
        @Override
        public String desc(){return "player takes 1 damage every time they fire";}
    },CONFUSION{
        @Override
        public String desc(){return "there is a chance that player's angle is changed";}
    };

    public abstract String desc();

}
