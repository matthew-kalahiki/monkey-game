package game.model.ComputerAI;

public class Position {
    private double totalHeight;
    private double charHeight;
    private int oneLessThanNumPositions;
    private double segmentSize;

    public Position(double totalHeight, double charHeight, int oneLessThanNumPositions){
        this.totalHeight = totalHeight;
        this.charHeight = charHeight;
        this.oneLessThanNumPositions = oneLessThanNumPositions;
        segmentSize = (totalHeight - charHeight) / oneLessThanNumPositions;
    }


    public double distanceFromTop(int whichPosition){
        return segmentSize * (whichPosition - 1);
    }

    public Integer getWhichPosition(double distanceFromTop,boolean goingUp){
        int a = (int)(distanceFromTop / segmentSize);
        if(goingUp || a + 2 > oneLessThanNumPositions + 1){
            return a + 1;
        }else{
            return a + 2;
        }
    }

}
