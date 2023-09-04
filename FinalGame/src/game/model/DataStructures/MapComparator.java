package game.model.DataStructures;

import game.model.ComputerAI.Probability;

import java.util.Comparator;
import java.util.Map;

public class MapComparator implements Comparator<Integer> {

    private Map<Integer,Probability>positions;

    public MapComparator(Map<Integer,Probability>positions){this.positions = positions;}

    @Override
    public int compare(Integer a,Integer b){
        if (positions.get(a).getAsDouble() < positions.get(b).getAsDouble()){return -1;}
        if (positions.get(a).getAsDouble() > positions.get(b).getAsDouble()){return 1;}
        return 0;
    }
}
