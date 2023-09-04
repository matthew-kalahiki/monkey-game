package game.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;

public class Character {

    private Rectangle r;
    private Rectangle R;
    private ImageView monkey;
    private ImageView tree;
    private Pane pane;
    private double y;
    private double dy;
    private boolean canMove;

    private boolean isFirstMonkey;
    private File f = new File("src/game/resources/Monkey1.png.png");
    private File g = new File("src/game/resources/Monkey2.png.png");
    private File h = new File("src/game/resources/Monkey3.png.png");
    private File i = new File("src/game/resources/Monkey4.png.png");
    private File t = new File("src/game/resources/TreeTop.png.png");


    public Character(Rectangle R,Pane pane){

        ImageView tree = new ImageView();
        tree.setImage(new Image(t.toURI().toString()));
        pane.getChildren().add(tree);
        tree.setX(R.getLayoutX() - tree.getImage().getWidth() / 2 + R.getWidth() / 2);
        tree.setY(R.getLayoutY() - tree.getImage().getHeight() + 5);

        if (R.getLayoutX() < pane.getWidth() / 2){
            Rectangle r = new Rectangle(R.getLayoutX() + R.getWidth(),R.getLayoutY(),R.getWidth(),R.getHeight() / 5);

            ImageView monkey = new ImageView();
            monkey.setImage(new Image(f.toURI().toString()));
            pane.getChildren().add(monkey);
            monkey.setX(r.getX() - R.getWidth());
            monkey.setY(r.getY());
            isFirstMonkey = true;
            this.monkey = monkey;

            r.setFill(Color.BLACK);
            pane.getChildren().add(r);
            this.r = r;
        }
        else{
            Rectangle r = new Rectangle(R.getLayoutX() - R.getWidth(),R.getLayoutY(),R.getWidth(),R.getHeight() / 5);

            ImageView monkey = new ImageView();
            monkey.setImage(new Image(h.toURI().toString()));
            pane.getChildren().add(monkey);
            monkey.setX(r.getX() - monkey.getImage().getWidth() + r.getWidth() * 2);
            monkey.setY(r.getY());
            isFirstMonkey = true;
            this.monkey = monkey;


            r.setFill(Color.BLACK);
            pane.getChildren().add(r);
            this.r = r;
        }
        this.R = R;
        y = 0;
        dy = 1;
        canMove = false;
        this.pane = pane;
    }

    public void setDy(double dy){this.dy = dy;}

    public void setCanMove(boolean canMove){this.canMove = canMove;}

    public Pane getPane(){return pane;}

    public Rectangle getR(){return R;}

    public Rectangle getr(){return r;}

    public double getY(){return y;}

    public boolean getCanMove(){return canMove;}

    public ImageView getMonkey(){return monkey;}

    public void toggleMonkey(){
        if (R.getLayoutX() < pane.getWidth() / 2) {
            if (isFirstMonkey) {
                monkey.setImage(new Image(f.toURI().toString()));
            } else {
                monkey.setImage(new Image(g.toURI().toString()));
            }
        }else{
            if (isFirstMonkey) {
                monkey.setImage(new Image(h.toURI().toString()));
            } else {
                monkey.setImage(new Image(i.toURI().toString()));
            }
        }
        isFirstMonkey = !isFirstMonkey;
    }

    public void move(){
        if (canMove){
            if (y % 7 == 0 && y > 0 && y < (R.getLayoutY()+R.getHeight() - 108 - r.getHeight())) {
                toggleMonkey();
            }
            if (Math.signum(dy) < 0) {
                if (y > 0) {
                    y += dy;
                }
            }
            if (Math.signum(dy) > 0) {
                if (y < (R.getLayoutY()+R.getHeight() - 108 - r.getHeight())) {
                    y += dy;
                }
            }
        }
    }

    public void draw(){
        r.setTranslateY(y);
        monkey.setTranslateY(y);
    }
}
