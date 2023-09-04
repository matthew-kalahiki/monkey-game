package game.model.ComputerAI;

public class Probability {
    int numerator;
    int denominator;

    public Probability(){
        numerator = 1;
        denominator = 2;
    }

    public void updateProbability(boolean success){
        if(success){
            numerator++;
        }
        denominator++;
    }
    public double getAsDouble(){
        return numerator/(double)denominator;
    }
}
