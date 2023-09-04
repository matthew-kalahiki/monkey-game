package game.model;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Ammo {

    private double x;
    private double y;
    private double a;
    private double b;
    private double dx;
    private double dy;
    private double radius;
    private boolean canMove;
    private Circle c;
    private Pane pane;
    private Rectangle or;
    private Character character;
    private boolean isTurn;
    private boolean fireOff;

    public Ammo(double radius, Circle c,Rectangle R,Pane pane){
        Character character = new Character(R,pane);
        fireOff = false;
        x = 0;
        y = 0;
        a = 0;
        b = 50;
        dx = 3;
        dy = x;
        this.radius = radius;
        canMove = false;
        this.c = c;
        or = new Rectangle();
        this.pane = character.getPane();
        this.character = character;
        isTurn = false;
        if(isPlayer1()) {
            c.setCenterX(R.getLayoutX() + R.getWidth());
        }else{
            c.setCenterX(R.getLayoutX());
        }
        c.setCenterY(R.getLayoutY());
    }

    public void setOr(Rectangle or){this.or = or;}

    public void setIsTurn(boolean isTurn){this.isTurn = isTurn;}

    public void setFireOff(boolean fireOff){this.fireOff = fireOff;}

    public void setA(double a){this.a = a;}

    public void setB(double b){this.b = b;}

    public void setRadius(double radius){this.radius = radius;}

    public void setX(double x){this.x = x; }

    public void setY(double y){this.y = y;}

    public boolean getFireOff(){return fireOff;}

    public double getA(){return a;}

    public Character getCharacter(){return character;}

    public void toggleMovement(){canMove = !canMove;}

    public boolean isPlayer1(){
        if (character.getR().getLayoutX() < (pane.getWidth() / 2)){
            return true;
        }
        return false;
    }

    public boolean move(){
        boolean bool = false;
        if (canMove) {
            if (isPlayer1()) {
                //dy = 2.0 * ((1.0/200.0)*((x + c.getCenterX())-((1.0/2.0)*(pane.getWidth())))) + 1 - (a * 0.2);
                double theta = Math.toRadians(a);
                if (b < 0.5){b = 0.5;}
                double velocity = b * 10;
                dy = (-3)*(Math.tan(theta) - (((2 * 9.81) * ((x + c.getCenterX())-105))/((2 * Math.pow(velocity,2)) * Math.pow(Math.cos(theta),2))));

                if (shouldReturn()) {

                    x = 0;
                    y = 0;
                    c.setVisible(false);
                    toggleMovement();
                    setIsTurn(false);
                    fireOff = false;


                    character.toggleMonkey();
                    bool = true;
                }

                x += dx;

            }
            if (!isPlayer1()) {
                //dy = 3 * (-1 *((1.0/200.0)*((x + c.getCenterX())-(1.0/2.0)*(pane.getWidth())))) + 1 - (a * 0.2);

                double theta = Math.toRadians(a);
                if (b < 0.5){b = 0.5;}
                double velocity = b * 10;
                dy = (3)*((-1)*Math.tan(theta) - (((-2 * 9.81) * (((-1)*x - c.getCenterX())+478))/((2 * Math.pow(velocity,2)) * Math.pow(Math.cos(theta),2))));
                double newDx = dx * -1;

                if (shouldReturn()) {

                    x = 0;
                    y = 0;
                    c.setVisible(false);
                    toggleMovement();
                    setIsTurn(false);
                    fireOff = false;

                    character.toggleMonkey();

                    bool = true;

                }

                x += newDx;

            }
           y += dy;

        }else{
            c.setCenterY(character.getY() + character.getr().getY());
        }
        return bool;
    }

    public Rectangle getOr(){return or;}

    public Circle getC(){return c;}

    public void draw(){
        c.setRadius(radius);
        c.setTranslateX(x);
        c.setTranslateY(y);
    }

    public double getPower(double angle){
        double theta = Math.toRadians(angle);
        double power = Math.sqrt((9.81 * Math.pow(363,2))/(2 * Math.pow(Math.cos(theta),2)*(((363) * Math.tan(theta)) + (or.getY() + or.getTranslateY() + (.5 * or.getHeight())) - (c.getTranslateY() + c.getCenterY())))) / 10;
        if(power > 0){
            return power;
        }
        return 0;
    }

    public double getAngle(double power){

        power = power * 10;

        double part1 = (-1)*(363);

        double part2 = Math.pow(363, 2);

        double part3 = (-9.81 * Math.pow(363,2))/(2*Math.pow(power,2));

        double part4 = (part3) - c.getCenterY() - c.getTranslateY() + (or.getY() + or.getTranslateY() + (or.getHeight()/2));

        double part5 = (-2 * 9.81 * Math.pow(363,2))/(2*Math.pow(power,2));

        double root = Math.sqrt(part2 - (4*part3*part4));

        double ff = Math.atan((part1 + root)/part5);

        double fg = Math.atan((part1 - root)/part5);

        double returnedNumber = Math.toDegrees(Math.min(ff, fg));

        if (returnedNumber >= -180 && returnedNumber <= 180) {

            return returnedNumber;
        }else{
            return 0;
        }
    }

    public boolean shouldReturn(){
        if (isPlayer1()){
            return (x + c.getCenterX() + radius) > or.getX() || (y + c.getCenterY() + radius > pane.getHeight() || (y + c.getCenterY() - radius < 0));
        }else{
            return ((x + c.getCenterX()) - radius) < (or.getX() + or.getWidth()) || (y + c.getCenterY() + radius > pane.getHeight() || (y + c.getCenterY() - radius < 0));
        }
    }
}


