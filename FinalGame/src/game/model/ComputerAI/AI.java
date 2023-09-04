package game.model.ComputerAI;

import game.model.DataStructures.MapComparator;
import game.model.DataStructures.MinHeap;
import javafx.scene.shape.Rectangle;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AI {
    Integer currentPosition;

    Map<Integer,Probability> positions;

    Map<Integer,Boolean>anglesHit;

    Map<Integer,Double>angles;

    Position p;

    MinHeap<Integer> heap;

    Comparator<Integer>comparator;

    Rectangle tree;
    Rectangle character;
    int oneLessThenNumPositions;


    public AI(Rectangle tree,Rectangle character,int oneLessThanNumPositions){

        positions = new HashMap<>();

        anglesHit = new HashMap<>();

        angles = new HashMap<>();

        p = new Position(tree.getHeight(),character.getHeight(),oneLessThanNumPositions);

        currentPosition = 1;

        for (Integer i = 1;i <= oneLessThanNumPositions + 1;i++){
            positions.put(i,new Probability());
        }
        comparator = new MapComparator(positions);
        heap = new MinHeap<>(comparator);
        for (Integer i = 1;i <= oneLessThanNumPositions + 1;i++){
            heap.add(i);
        }
        for (Integer i = 0 ; i <= 2 * oneLessThanNumPositions;i++){
            anglesHit.put(i - 10,false);
            angles.put(i - 10, 40.0);
        }
        this.tree = tree;
        this.character = character;
        this.oneLessThenNumPositions = oneLessThanNumPositions;
    }

    public Position getP(){return p;}

    public Integer getCurrentPosition(){return currentPosition;}

    public double getPositionYCoordinate(){return tree.getLayoutY() + p.distanceFromTop(currentPosition);}

    public double getAngle(Integer posDif){
        return angles.get(posDif);
    }

    public int getOneLessThenNumPositions(){return oneLessThenNumPositions;}

    public void oppActivatedShield(){
        anglesHit.replaceAll((key,bool) -> false);

    }

    public void updatePos(boolean wasHit,boolean isntGlued){
        positions.get(currentPosition).updateProbability(wasHit);
        if (isntGlued) {
            heap.update(currentPosition);
            currentPosition = heap.element();
        }

    }

    public void updateTraj(Integer posDif, boolean hit) {


        if (!anglesHit.get(posDif)) {
            if (hit) {
                anglesHit.replace(posDif, true);
            }else{
                Double a = angles.get(posDif);
                Double b = a + 5.0;
                angles.replace(posDif,b);
            }
        }
    }
}
