package game.model;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.File;

public class GameController{

    @FXML
    private TextField p1Name;

    @FXML
    private TextField p2Name;

    @FXML
    private Pane pane;

    @FXML
    private TextField p1HP;

    @FXML
    private TextField p2HP;

    @FXML
    private TextField power;

    @FXML
    private TextField angle;

    @FXML
    private TextField desc;

    @FXML
    private Button begin;

    @FXML
    private Button up;

    @FXML
    private Button down;

    @FXML
    private Button fire;

    @FXML
    private Button items;

    @FXML
    private Button b1;

    @FXML
    private Button b2;

    @FXML
    private Button b3;

    @FXML
    private Button b4;

    @FXML
    private Button b5;

    @FXML
    private Button b6;

    @FXML
    private Button b7;

    @FXML
    private Button b8;

    @FXML
    private Rectangle p1R;

    @FXML
    private Rectangle p2R;

    @FXML
    private Label pow;

    @FXML
    private Label ang;

    @FXML
    private ImageView title;

    @FXML
    private Button cheat;

    private TextArea info;

    private Ammo p1A;

    private Ammo p2A;

    private Game game;

    private OverallGame overallGame;

    private Button[]b;

    private Rectangle p1Shield;

    private Rectangle p2Shield;

    private Circle c1;

    private Circle c2;

    private boolean isP1ShieldActive = false;

    private boolean isP2ShieldActive = false;

    private boolean upBool = false;

    private boolean downBool = false;

    private boolean itemsVisible;

    private Movement clock;

    File p1monkey = new File("src/game/resources/Monkey5.png.png");

    File p2monkey = new File("src/game/resources/Monkey6.png.png");

    private File j = new File("src/game/resources/Jungle.PNG");

    private File k = new File("src/game/resources/title.PNG");

    private File l = new File("src/game/resources/victory.PNG");

    private File m = new File("src/game/resources/gameover.PNG");




   private class Movement extends AnimationTimer {

        private long FRAMES_PER_SEC = 500L;
        private long INTERVAL = 1000000000L / FRAMES_PER_SEC;

        private long last = 0;
        private Character p1C;
        private Character p2C;
        private Ammo p1A;
        private Ammo p2A;
        private Game game;

        public void setItems(Character p1C,Character p2C,Ammo p1A,Ammo p2A,Game game) {
            this.p1C = p1C;
            this.p2C = p2C;
            this.p1A = p1A;
            this.p2A = p2A;
            this.game = game;
        }

        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                if(!p1A.getFireOff() && !p2A.getFireOff() && !game.gameOver() && game.getCurrent().equals(game.getP1())){

                    //If the neither of the players are in the process of firing and the game is not over and it is Player 1's turn,
                    //all of the buttons should be visible

                    seeButtons();
                }
                if(game.getCurrent().equals(game.getP2()) && !p1A.getFireOff() && !game.gameOver()){

                    //If it is Player 2's turn and first player is not in the process of firing and the game is not over
                    //Player 2 proceeds with their turn
                    computerMove(game);
                    game.addEnemyItems();
                    computerTurn(game);
                }
                if (!game.gameOver()){
                    if (!upBool && !downBool) {
                       game.getP1().move();
                    }
                    itemActions();
                }
                p1Fire(game);
                p2Fire(game);

                p1C.move();
                p1C.draw();
                p2C.move();
                p2C.draw();
                if (p1A.move()){
                    if(!game.getComputerAiUpdatedForTurn()) {
                        game.getP2().getAi().updatePos(false,game.getP2().getSe().get(StatusEffect.GLUE) == 0);
                        game.setComputerAiUpdatedForTurn(true);
                    }
                }
                p1A.draw();
               if (p2A.move()){
                    game.getP2().getAi().updateTraj(game.getP2().getAi().getCurrentPosition() - game.getP1().getCurP(),false);
               }

                p2A.draw();
                last = now;
            }
        }
    }
    @FXML
   public void Start(){
        clock.start();
    }
    public void initialize(){

       //Only the names, hp, and start button will be visible upon initialization

        Image jungle = new Image(j.toURI().toString());
        BackgroundImage bi = new BackgroundImage(jungle,null,null,null,null);
        Background ba = new Background(bi);
        pane.setBackground(ba);

        Image t = new Image(k.toURI().toString());
        title.setImage(t);



        p1Name.setEditable(true);
        title.toFront();
        p1Name.setTranslateX(power.getLayoutX() - p1Name.getLayoutX());
        p1Name.setTranslateY(power.getLayoutY() - p1Name.getLayoutY());
        p1HP.setEditable(false);
        p2HP.setEditable(false);

        p1HP.setVisible(false);
        p2HP.setVisible(false);
        p2Name.setVisible(false);

        power.setEditable(false);
        itemsVisible = false;
        clearButtons();
        b = new Button[]{b1,b2,b3,b4,b5,b6,b7,b8};

        for (Button a : b){
            a.setOnMousePressed(e -> selectItem(a));
            a.setOnMouseEntered(e -> showDesc(a));
            a.setOnMouseExited(e -> hideDesc(a));
        }

    }

    private Circle newCircle(){
        Circle c = new Circle();
        c.setFill(Color.BROWN);
        c.setStroke(Color.BLACK);
        pane.getChildren().add(c);
        return c;
    }

    /* The collision test takes in a circle that is colliding with something and a
     rectangle that is being ran into. It is also taking in a boolean fromRight that indicates
     which side of the rectangle we are testing for a collision*/

    private boolean collisionTest(Circle collider,Rectangle collided,boolean fromLeft){
        if (fromLeft){

            /* This if statement checks if a circle coming from the left successfully collided with a rectangle on the right. It contains the following conditions relative to the order in which they appear:
               The the right edge of circle is past the left side of the rectangle
               and the right edge of the circle is between the left side of the rectangle and the width of the circle past it
               and the top of the circle is above the bottom of the rectangle
                and the bottom of the circle is below the top of the rectangle*/

            if (((collider.getTranslateX() + collider.getCenterX() + collider.getRadius()) > (collided.getX())) &&
                    ((collider.getTranslateX() + collider.getCenterX() + collider.getRadius()) < (collided.getX() + (2 * collider.getRadius())))&&
                    (collider.getTranslateY() + collider.getCenterY() < (collided.getTranslateY() + collided.getY() + collided.getHeight() + collider.getRadius()))
                    && ((collider.getTranslateY() + collider.getCenterY() > (collided.getTranslateY() + collided.getY() - collider.getRadius())))){
                return true;
            }
        }else if (!fromLeft){

            /* This if statement checks if a circle coming from the right successfully collided with a rectangle on the left. It contains the following conditions relative to the order in which they appear:
               The the left edge of circle is touching or past the right side of the rectangle
               and the top of the circle is above the bottom of the rectangle
                and the bottom of the circle is below the top of the rectangle*/

            if (((collider.getTranslateX() + collider.getCenterX() - collider.getRadius()) < (collided.getX() + collided.getWidth())) &&
                    ((collider.getTranslateX() + collider.getCenterX() - collider.getRadius())) > (collided.getX() - (2 * collider.getRadius()))&&
                    (collider.getTranslateY() + collider.getCenterY() < (collided.getTranslateY() + collided.getY() + collided.getHeight() + collider.getRadius()))
                    && (collider.getTranslateY() + collider.getCenterY() > (collided.getTranslateY() + collided.getY() - collider.getRadius()))){
                    return true;
            }
        }
            return false;
    }

    private void p1Fire(Game game){

       //This is what happens during Player 1's firing process


            /* What happens if Player 1's attack is successful is:
               Player 2 is hit, the HP is updated, The game ends if the opponent is out of health*/
        if (collisionTest(game.getP1().getA().getC(),game.getP1().getA().getOr(),true)){

            game.getP2().gotHit(game.getComputerAiUpdatedForTurn());
            game.setComputerAiUpdatedForTurn(true);
            p2HP.setText(String.format("%7.2f",game.getP2().getHp()));


            Over(game.getP1());


        }
    }
    private void p2Fire(Game game){

       //This is what happens during Player 2's firing process



            /* What happens if Player 2's attack is successful is:
               Player 1 is hit, the HP is updated, The game ends if the opponent is out of health*/

        if (collisionTest(game.getP2().getA().getC(),game.getP2().getA().getOr(),false)){

            game.getP1().gotHit(true);
            p1HP.setText(String.format("%7.2f",game.getP1().getHp()));
            Over(game.getP2());
            game.getP2().getAi().updateTraj(game.getP2().getAi().getCurrentPosition() - game.getP1().getCurP(), true);
        }
    }

    private void computerMove(Game game){

       //The computer's character will move up or down until it is even with its opponent

        double newY = game.getP2().getAi().getPositionYCoordinate();
        double curY = game.getCurrent().getA().getCharacter().getr().getTranslateY() + game.getCurrent().getA().getCharacter().getr().getY();
        if (curY < newY && game.getP2().getSe().get(StatusEffect.GLUE) == 0) {
            game.getCurrent().getA().getCharacter().setDy(1);
            game.getCurrent().getA().getCharacter().setCanMove(true);
        }else if (curY > newY && game.getP2().getSe().get(StatusEffect.GLUE) == 0){
            game.getCurrent().getA().getCharacter().setDy(-1);
            game.getCurrent().getA().getCharacter().setCanMove(true);
        }else{
            game.getCurrent().getA().getCharacter().setCanMove(false);
        }
    }

    private void computerTurn(Game game){

       // The computer will only begin its firing process if its character isn't still moving

        if (!game.getCurrent().getA().getCharacter().getCanMove()) {

            //trouble with hit

            if (game.getCurrent().equals(game.getP2())) {

                game.getCurrent().getA().setA(game.getP2().getAi().getAngle(game.getP2().getAi().getCurrentPosition() - game.getP1().getCurP()));


                game.getCurrent().getA().setB(game.getCurrent().getA().getPower(game.getCurrent().getA().getA()));


                statusEffectActions();

                game.getCurrent().getA().setFireOff(true);
                game.getCurrent().getA().getC().setVisible(true);
                clearButtons();
                game.getCurrent().getA().setIsTurn(true);
                game.getCurrent().getA().toggleMovement();

                //The computer's sprite changes to its fire image until its turn is over

                game.getP2().getA().getCharacter().getMonkey().setImage(new Image(p2monkey.toURI().toString()));

                if (game.getP2InPlayItems().contains(Item.THING)){
                    game.getP2InPlayItems().remove(Item.THING);
                }else {
                    game.switchTurn();
                    game.setCounter(game.getCounter() + 1);
                    game.setComputerAiUpdatedForTurn(false);
                }

            }
        }
    }

    private void clearButtons(){

       //Takes the gameplay buttons of of the screen

        fire.setVisible(false);
        power.setVisible(false);
        angle.setVisible(false);
        up.setVisible(false);
        down.setVisible(false);
        pow.setVisible(false);
        ang.setVisible(false);
        items.setVisible(false);
        desc.setVisible(false);
        cheat.setVisible(false);
        if (!title.isVisible()) {
            infoB();
            clearItems();
        }
    }
    private void seeButtons(){

       //puts the gameplay buttons back on the screen

        fire.setVisible(true);
        up.setVisible(true);
        down.setVisible(true);
        power.setVisible(true);
        angle.setVisible(true);
        pow.setVisible(true);
        ang.setVisible(true);
        items.setVisible(true);
        cheat.setVisible(true);
    }

    public void Over(Player winner){
        if(game.gameOver()){
            if (winner.equals(game.getP1())) {
                if (overallGame.getDefeatedEnemies().size() <  overallGame.getGameSize() - 1) {
                    p2HP.setText("Loser");
                    p1HP.setText("Winner");
                    begin.setText("Next");
                    clearButtons();
                    begin.setOnAction(e -> cont());
                }else{
                    clearButtons();
                    begin.setVisible(false);
                    p1Name.setVisible(false);
                    p2Name.setVisible(false);
                    p1HP.setVisible(false);
                    p2HP.setVisible(false);
                    title.setVisible(true);
                    Image v = new Image(l.toURI().toString());
                    title.setImage(v);
                    p1A.getCharacter().getMonkey().setVisible(false);
                    p2A.getCharacter().getMonkey().setVisible(false);
                    if (p1Shield != null){
                        p1Shield.setVisible(false);
                    }
                    if (p2Shield != null){
                        p2Shield.setVisible(false);
                    }
                }

            }else{
                clearButtons();
                begin.setVisible(false);
                p1Name.setVisible(false);
                p2Name.setVisible(false);
                p1HP.setVisible(false);
                p2HP.setVisible(false);
                title.setVisible(true);
                Image go = new Image(m.toURI().toString());
                title.setImage(go);
                p1A.getCharacter().getMonkey().setVisible(false);
                p2A.getCharacter().getMonkey().setVisible(false);
                if (p1Shield != null){
                    p1Shield.setVisible(false);
                }
                if (p2Shield != null){
                    p2Shield.setVisible(false);
                }
            }
        }
    }

    public void NextMonkey(OverallGame overallGame){
        if (overallGame.getRound() != 0) {
            pane.getChildren().remove(p2A.getCharacter().getr());
            pane.getChildren().remove(p2A.getCharacter().getMonkey());
        }
        if (p1Shield != null){
            pane.getChildren().remove(p1Shield);
            isP1ShieldActive = false;
        }
        if (p2Shield != null){
            pane.getChildren().remove(p2Shield);
            isP2ShieldActive = false;
        }
        p2A = new Ammo(5,c2,p2R,pane);
        p1A.setOr(p2A.getCharacter().getr());
        p2A.setOr(p1A.getCharacter().getr());
        game = new Game("Player 1","Player 2",overallGame.getPlayer(),p1A,p2A,overallGame.nextRound());
        for(Item it : overallGame.getPlayerPermItems()){
            game.getP1InPlayItems().add(it);
        }
        p2Name.setText(game.getE().enemyName());
        clock.setItems(p1A.getCharacter(),p2A.getCharacter(),p1A,p2A,game);
        p1HP.setText(String.format("%7.2f",game.getP1().getHp()));
        p2HP.setText(String.format("%7.2f",game.getP2().getHp()));
        seeButtons();
        game.getP1().getA().getCharacter().getR().setVisible(true);
        game.getP2().getA().getCharacter().getR().setVisible(true);
        game.getP1().getA().getC().setVisible(false);
        game.getP2().getA().getC().setVisible(false);
        game.getP1().getA().getCharacter().getr().setVisible(false);
        game.getP2().getA().getCharacter().getr().setVisible(false);
        game.getP1().getA().setRadius(5);
    }


   @FXML
    public void Begin(){

       // initiates the combat game

       //sets the background image

       info = new TextArea();
       pane.getChildren().add(info);
       info.setPrefWidth(power.getWidth() * 2);
       info.setMaxWidth(power.getWidth()*2);
       info.setMinWidth(power.getWidth()*2);
       info.setLayoutX((pane.getWidth()/2) - (info.getPrefWidth()/2));
       info.setLayoutY(begin.getLayoutY() + begin.getHeight());
       info.setEditable(false);
       info.setVisible(false);

       p1Name.setTranslateX(0);
       p1Name.setTranslateY(0);
       p1HP.setVisible(true);
       p2HP.setVisible(true);
       p2Name.setVisible(true);

       title.setVisible(false);

       c1 = newCircle();
       c2 = newCircle();
       clock = new Movement();
       p1A = new Ammo(5, c1,p1R,pane);

       overallGame = new OverallGame(p1Name.getText(),p1A);

       NextMonkey(overallGame);
       p1Name.setEditable(false);
       p2Name.setEditable(false);
       power.setEditable(true);
       Start();
       begin.setText("info");
       begin.setOnAction(e -> infoA());

   }
    @FXML
    public void Fire(){

       //Initiates Player 1's firing process

           if(game.getCurrent().equals(game.getP1())) {
               try {
                   game.getCurrent().getA().getC().setVisible(true);
                   double powerLevel = Double.parseDouble(power.getText());
                   double angleSlope = Double.parseDouble(angle.getText());
                   game.getCurrent().getA().setA(angleSlope);
                   game.getCurrent().getA().setB(powerLevel);
                   game.getCurrent().getA().setFireOff(true);
                   clearButtons();

                   statusEffectActions();

                   game.getCurrent().getA().setIsTurn(true);
                   game.getCurrent().getA().toggleMovement();

                   game.getCurrent().getA().getCharacter().getMonkey().setImage(new Image(p1monkey.toURI().toString()));

                   if (game.getP1InPlayItems().contains(Item.THING)){
                       game.getP1InPlayItems().remove(Item.THING);
                   }else {
                       game.switchTurn();
                   }
                   clearItems();
               }catch (NumberFormatException nfe){
                   desc.setText("Make sure you enter numbers for power and angle");
                   desc.setVisible(true);
                   desc.setPrefWidth(desc.getText().length() * 6.5);
                   desc.setLayoutX((pane.getWidth()/2) - desc.getPrefWidth() / 2);
                   desc.setLayoutY(pane.getHeight()/2);
               }
           }



    }
    @FXML
    public void UpA(){

       //moves player up while the button is being pressed
        /*if(game.getCurrent().equals(game.getP1()) && game.getP1().getSe().get(StatusEffect.GLUE) == 0 && game.getP1().getCurP() > 1) {
            game.getP1().setNewY(game.getP1().getAi().getP().distanceFromTop(game.getP1().getCurP() - 1));
            game.getP1().setCurP(game.getP1().getCurP() - 1);
            game.getP1().getA().getCharacter().setCanMove(true);
        }*/
        if(game.getCurrent().equals(game.getP1()) && game.getP1().getSe().get(StatusEffect.GLUE) == 0) {
            upBool = true;
            game.getP1().getA().getCharacter().setDy(-1);
            game.getP1().getA().getCharacter().setCanMove(true);
        }

    }
    @FXML
    public void UpB(){
       //stops the player when the up button is released
        if(game.getCurrent().equals(game.getP1()) && game.getP1().getSe().get(StatusEffect.GLUE) == 0) {
            upBool = false;
            game.getP1().setNewY(game.getP1().getAi().getP().distanceFromTop(game.getP1().getAi().getP().getWhichPosition(game.getP1().getA().getCharacter().getr().getTranslateY(),true)));
            game.getP1().setCurP(game.getP1().getAi().getP().getWhichPosition(game.getP1().getA().getCharacter().getr().getTranslateY(),true));
        }
    }

    @FXML
    public void DownA(){
       // moves player down while button is is being pressed
        /*if(game.getCurrent().equals(game.getP1()) && game.getP1().getSe().get(StatusEffect.GLUE) == 0) {
            game.getCurrent().getA().getCharacter().setDy(1);
            game.getCurrent().getA().getCharacter().toggleMovement();
        }*/

        if(game.getCurrent().equals(game.getP1()) && game.getP1().getSe().get(StatusEffect.GLUE) == 0) {
            downBool = true;
            game.getP1().getA().getCharacter().setDy(1);
            game.getP1().getA().getCharacter().setCanMove(true);
        }
    }
    @FXML
    public void DownB(){
       //stops player when down button is released
        if(game.getCurrent().equals(game.getP1()) && game.getP1().getSe().get(StatusEffect.GLUE) == 0) {
            downBool = false;
            game.getP1().setNewY(game.getP1().getAi().getP().distanceFromTop(game.getP1().getAi().getP().getWhichPosition(game.getP1().getA().getCharacter().getr().getTranslateY(),false)));
            game.getP1().setCurP(game.getP1().getAi().getP().getWhichPosition(game.getP1().getA().getCharacter().getr().getTranslateY(),false));
        }
    }

    @FXML
    public void ShowItems(){
       //displays P1's items
        if (!itemsVisible) {
            itemsVisible = true;
            for (Button a : b) {
                a.toFront();
            }

            for (int i = 0; i < game.getP1().getItems().size(); i++) {
                if (game.getP1().getItems().get(i).isCombatItem()) {
                    b[i].setVisible(true);
                    b[i].setText(game.getP1().getItems().get(i).toString());
                }
            }
        }else{
            clearItems();
        }
    }

    @FXML
    public void ShowP1Status(){
        if (fire.isVisible()) {
            TextArea status = new TextArea();
            pane.getChildren().add(status);
            status.setLayoutX(p1HP.getLayoutX());
            status.setLayoutY(p1HP.getLayoutY());
            status.setText(StatusEffect.BURN.toString() + " : " + game.getP1().getSe().get(StatusEffect.BURN) + "\n"
                    + StatusEffect.CONFUSION.toString() + " : " + game.getP1().getSe().get(StatusEffect.CONFUSION) + "\n"
                    + StatusEffect.GLUE.toString() + " : " + game.getP1().getSe().get(StatusEffect.GLUE) + "\n");
            status.setPrefWidth(p1HP.getWidth());
            status.setPrefHeight(p1HP.getHeight() * 3);
        }
    }

    @FXML
    public void HideP1Status(){
        if (fire.isVisible()) {
            pane.getChildren().remove(pane.getChildren().size() - 1);
        }
    }

    @FXML
    public void ShowP2Status(){
        if (fire.isVisible()) {
            TextArea status = new TextArea();
            pane.getChildren().add(status);
            status.setLayoutX(p2HP.getLayoutX());
            status.setLayoutY(p2HP.getLayoutY());
            status.setText(StatusEffect.BURN.toString() + " : " + game.getP2().getSe().get(StatusEffect.BURN) + "\n"
                    + StatusEffect.CONFUSION.toString() + " : " + game.getP2().getSe().get(StatusEffect.CONFUSION) + "\n"
                    + StatusEffect.GLUE.toString() + " : " + game.getP2().getSe().get(StatusEffect.GLUE) + "\n");
            status.setPrefWidth(p2HP.getWidth());
            status.setPrefHeight(p2HP.getHeight() * 3);
        }
    }

    @FXML
    public void HideP2Status(){
        if (fire.isVisible()) {
            pane.getChildren().remove(pane.getChildren().size() - 1);
        }
    }

    @FXML
    public void Cheat(){
        game.getP2().setHp(0);
        Over(game.getP1());
    }

    public void clearItems(){
       for (Button a : b){
           a.setVisible(false);
       }
       itemsVisible = false;
    }


    public void showDesc(Button clicked){
       desc.setVisible(true);
       desc.setLayoutX(clicked.getLayoutX() + clicked.getWidth());
       desc.setLayoutY(clicked.getLayoutY());
       desc.setText(Item.valueOf(clicked.getText()).desc());
       desc.setPrefWidth(desc.getText().length() * 6.5);
       desc.toFront();

    }


    public void hideDesc(Button clicked){

        desc.setVisible(false);
    }

    public void infoA(){
        info.setVisible(true);

        String a = "Round : " + overallGame.getRound() + "\n" + "Current Enemy : " + "\n" + overallGame.getCurrEnemy().enemyName() + " : " + overallGame.getCurrEnemy().desc() + "\n"  + "\n" + "Defeated Enemies :" + "\n";

        for (Enemy enemy : overallGame.getDefeatedEnemies()){
            a = a + enemy.enemyName() + " : " + enemy.desc() + "\n";
        }

        info.setText(a);
        begin.setOnAction(e -> infoB());
    }

    public void infoB(){
        info.setVisible(false);
        begin.setOnAction(e -> infoA());
    }

    public void cont(){
        NextMonkey(overallGame);
        begin.setText("info");
        begin.setOnAction(e -> infoA());
    }


    public void selectItem(Button clicked) {
        Item temp = Item.valueOf(clicked.getText());
        if (temp == Item.POWPOW || temp == Item.TRACER) {
            try {
                if (temp == Item.POWPOW && p1A.getPower(Double.parseDouble(angle.getText())) == 0) {
                    power.setText("Use a different angle");
                } else if (temp == Item.TRACER && p1A.getAngle(Double.parseDouble(power.getText())) == 0) {
                    angle.setText("Use a different power");
                } else {
                    clicked.setVisible(false);
                    game.getP1().getItems().remove(Item.valueOf(clicked.getText()));
                    game.getP1InPlayItems().add(Item.valueOf(clicked.getText()));
                }
            } catch (NumberFormatException nfe) {
                desc.setText("Make sure you enter numbers for power and angle");
                desc.setVisible(true);
                desc.setPrefWidth(desc.getText().length() * 6.5);
                desc.setLayoutX((pane.getWidth() / 2) - desc.getPrefWidth() / 2);
                desc.setLayoutY(pane.getHeight() / 2);
            }
        } else if (temp == Item.SHIELD || temp == Item.THING){
            if (!game.getP1InPlayItems().contains(temp)){
                clicked.setVisible(false);
                game.getP1().getItems().remove(Item.valueOf(clicked.getText()));
                game.getP1InPlayItems().add(Item.valueOf(clicked.getText()));
                if (temp.isPermanent()){
                    overallGame.getPlayerPermItems().add(temp);
                }
            }
        }else {

            clicked.setVisible(false);
            game.getP1().getItems().remove(Item.valueOf(clicked.getText()));
            game.getP1InPlayItems().add(Item.valueOf(clicked.getText()));
            if (temp.isPermanent()){
                overallGame.getPlayerPermItems().add(temp);
            }
        }
    }

    public void itemActions(){
       if(game.getP1InPlayItems().contains(Item.BIG)){


           game.getP1().getA().setRadius(15);


       }
       if (game.getP1InPlayItems().contains(Item.BANANA)){

           game.getP1InPlayItems().remove(Item.BANANA);
           game.getP1().setHp(game.getP1().getHp() + 2);
           p1HP.setText(String.format("%7.2f",game.getP1().getHp()));

       }
       if (game.getP1InPlayItems().contains(Item.SHIELD)){
           if (!isP1ShieldActive) {
               Rectangle shield = new Rectangle();
               pane.getChildren().add(shield);
               shield.setWidth(p1R.getWidth());
               shield.setHeight(game.getP1().getA().getCharacter().getr().getHeight() * 2);
               shield.setX(game.getP1().getA().getCharacter().getr().getX() + 100);
               shield.setY(game.getP1().getA().getCharacter().getr().getY() - (game.getP1().getA().getCharacter().getr().getHeight() / 2));
               shield.setFill(Color.BLUE);
               shield.toBack();
               isP1ShieldActive = true;
               p1Shield = shield;
               game.getP2().getAi().oppActivatedShield();

           }else if (collisionTest(game.getP2().getA().getC(),p1Shield,false)){
               p2A.setX(0);
               p2A.setY(0);
               p2A.getC().setVisible(false);

               p2A.toggleMovement();
               p2A.setIsTurn(false);
               p2A.setFireOff(false);

               p2A.getCharacter().toggleMonkey();

               game.getP2().getAi().updateTraj(game.getP2().getAi().getCurrentPosition() - game.getP1().getCurP(),false);

           }else if (isP1ShieldActive){

               p1Shield.setTranslateY(game.getP1().getA().getCharacter().getr().getTranslateY());

           }
       }
       if (game.getP1InPlayItems().contains(Item.ROCK)){
           game.getP1InPlayItems().remove(Item.ROCK);
           game.getP2().setDamage(game.getP2().getDamage() * 1.5);
       }
       if (game.getP1InPlayItems().contains(Item.HIT)){
           game.getP1InPlayItems().remove(Item.HIT);
           statusEffectActions();
           game.getP2().gotHit(game.getComputerAiUpdatedForTurn());
           game.setComputerAiUpdatedForTurn(true);
           p2HP.setText(String.format("%7.2f",game.getP2().getHp()));
           Over(game.getP1());
           clearButtons();
           clearItems();
           if (game.getP1InPlayItems().contains(Item.SPICY)){
               game.getP2().getSe().replace(StatusEffect.BURN,3);
               game.getP1InPlayItems().remove(Item.SPICY);
           }
           if (game.getP1InPlayItems().contains(Item.STICKYCOCONUT)){
               game.getP2().getSe().replace(StatusEffect.GLUE,3);
               game.getP1InPlayItems().remove(Item.STICKYCOCONUT);
           }
           if (game.getP1InPlayItems().contains(Item.CRAZYCOCONUT)){
               game.getP2().getSe().replace(StatusEffect.CONFUSION,3);
               game.getP1InPlayItems().remove(Item.CRAZYCOCONUT);
           }

           if (game.getP1InPlayItems().contains(Item.THING)){
               game.getP1InPlayItems().remove(Item.THING);
           }else{
               game.switchTurn();

           }
       }
       if (game.getP1InPlayItems().contains(Item.ARMOR)){
            game.getP1InPlayItems().remove(Item.ARMOR);
            game.getP1().setDamage(game.getP1().getDamage() / 2);

       }
       if (game.getP1InPlayItems().contains(Item.POWPOW)){
           game.getP1InPlayItems().remove(Item.POWPOW);
           double angleSlope = Double.parseDouble(angle.getText());
           double newPower = game.getCurrent().getA().getPower(angleSlope);
           if (newPower > 0) {
               power.setText(Double.toString(newPower));
           }
       }
       if (game.getP1InPlayItems().contains(Item.TRACER)){
           game.getP1InPlayItems().remove(Item.TRACER);
           double powerLevel = Double.parseDouble(power.getText());
           double newAngle = game.getCurrent().getA().getAngle(powerLevel);
           if (newAngle != 0){
               angle.setText(Double.toString(newAngle));
           }
       }
       if (game.getP1InPlayItems().contains(Item.SPICY)){
           if (p1A.shouldReturn()){
               if (collisionTest(p1A.getC(),p1A.getOr(),true)){
                   game.getP2().getSe().replace(StatusEffect.BURN,3);
               }
               game.getP1InPlayItems().remove(Item.SPICY);
           }
       }
       if (game.getP1InPlayItems().contains(Item.CRAZYCOCONUT)){
           if (p1A.shouldReturn()){
               if (collisionTest(p1A.getC(),p1A.getOr(),true)){
                   game.getP2().getSe().replace(StatusEffect.CONFUSION,3);
               }
               game.getP1InPlayItems().remove(Item.CRAZYCOCONUT);
           }
       }
       if (game.getP1InPlayItems().contains(Item.STICKYCOCONUT)){
           if (p1A.shouldReturn()){
               if (collisionTest(p1A.getC(),p1A.getOr(),true)){
                   game.getP2().getSe().replace(StatusEffect.GLUE,3);
               }
               game.getP1InPlayItems().remove(Item.STICKYCOCONUT);
           }
       }

       //start second player item actions

        if(game.getP2InPlayItems().contains(Item.BIG)){

            game.getP2().getA().setRadius(15);

        }
        if (game.getP2InPlayItems().contains(Item.BANANA)){

            game.getP2InPlayItems().remove(Item.BANANA);
            game.getP2().setHp(game.getP2().getHp() + 2);
            p2HP.setText(String.format("%7.2f",game.getP2().getHp()));

        }
        if (game.getP2InPlayItems().contains(Item.SHIELD)){
            if (!isP2ShieldActive) {
                Rectangle shield = new Rectangle();
                pane.getChildren().add(shield);
                shield.setWidth(p2R.getWidth());
                shield.setHeight(game.getP2().getA().getCharacter().getr().getHeight() * 2);
                shield.setX(game.getP2().getA().getCharacter().getr().getX() - 100);
                shield.setY(game.getP2().getA().getCharacter().getr().getY() - (game.getP2().getA().getCharacter().getr().getHeight() / 2));
                shield.setFill(Color.RED);
                shield.toBack();
                isP2ShieldActive = true;
                p2Shield = shield;

            }else if (collisionTest(game.getP1().getA().getC(),p2Shield,true)){
                p1A.setX(0);
                p1A.setY(0);
                p1A.getC().setVisible(false);

                p1A.toggleMovement();
                p1A.setIsTurn(false);
                p1A.setFireOff(false);

                p1A.getCharacter().toggleMonkey();

                game.getP2().getAi().updatePos(false,game.getP2().getSe().get(StatusEffect.GLUE) == 0);
                game.setComputerAiUpdatedForTurn(true);

            }else if (isP2ShieldActive){

                p2Shield.setTranslateY(game.getP2().getA().getCharacter().getr().getTranslateY());

            }
        }
        if (game.getP2InPlayItems().contains(Item.ROCK)){
            game.getP2InPlayItems().remove(Item.ROCK);
            game.getP1().setDamage(game.getP1().getDamage() * 1.5);
        }
        if (game.getP2InPlayItems().contains(Item.HIT)){
            game.getP2InPlayItems().remove(Item.HIT);
            statusEffectActions();
            game.getP1().gotHit(true);
            p1HP.setText(String.format("%7.2f",game.getP1().getHp()));
            Over(game.getP2());
            clearItems();
            clearButtons();
            if (game.getP2InPlayItems().contains(Item.SPICY)){
                game.getP1().getSe().replace(StatusEffect.BURN,3);
                game.getP2InPlayItems().remove(Item.SPICY);
            }
            if (game.getP2InPlayItems().contains(Item.STICKYCOCONUT)){
                game.getP1().getSe().replace(StatusEffect.GLUE,3);
                game.getP2InPlayItems().remove(Item.STICKYCOCONUT);
            }
            if (game.getP2InPlayItems().contains(Item.CRAZYCOCONUT)){
                game.getP1().getSe().replace(StatusEffect.CONFUSION,3);
                game.getP2InPlayItems().remove(Item.CRAZYCOCONUT);
            }

            if (game.getP2InPlayItems().contains(Item.THING)){
                game.getP2InPlayItems().remove(Item.THING);
            }else{
                game.switchTurn();
                game.setCounter(game.getCounter() + 1);
                game.setComputerAiUpdatedForTurn(false);
            }
        }
        if (game.getP2InPlayItems().contains(Item.ARMOR)){
            game.getP2InPlayItems().remove(Item.ARMOR);
            game.getP2().setDamage(game.getP2().getDamage() / 2);

        }
        if (game.getP2InPlayItems().contains(Item.SPICY)){
            if (p2A.shouldReturn()){
                if (collisionTest(p2A.getC(),p2A.getOr(),false)){
                    game.getP1().getSe().replace(StatusEffect.BURN,3);
                }
                game.getP2InPlayItems().remove(Item.SPICY);
            }
        }
        if (game.getP2InPlayItems().contains(Item.CRAZYCOCONUT)){
            if (p2A.shouldReturn()){
                if (collisionTest(p2A.getC(),p2A.getOr(),false)){
                    game.getP1().getSe().replace(StatusEffect.CONFUSION,3);
                }
                game.getP2InPlayItems().remove(Item.CRAZYCOCONUT);
            }
        }
        if (game.getP2InPlayItems().contains(Item.STICKYCOCONUT)){
            if (p2A.shouldReturn()){
                if (collisionTest(p2A.getC(),p2A.getOr(),false)){
                    game.getP1().getSe().replace(StatusEffect.GLUE,3);
                }
                game.getP2InPlayItems().remove(Item.STICKYCOCONUT);
            }
        }
    }

    public void statusEffectActions(){
        if (game.getCurrent().getSe().get(StatusEffect.BURN) > 0){
            game.getCurrent().setHp(game.getCurrent().getHp() - 1);

            if (game.getCurrent().equals(game.getP1())) {
                p1HP.setText(String.format("%7.2f", game.getCurrent().getHp()));
                Over(game.getP2());
            }else{
                p2HP.setText(String.format("%7.2f", game.getCurrent().getHp()));
                Over(game.getP1());
            }

            game.getCurrent().getSe().replace(StatusEffect.BURN,game.getCurrent().getSe().get(StatusEffect.BURN) - 1);
        }
        if (game.getCurrent().getSe().get(StatusEffect.CONFUSION) > 0){
            double r = Math.random();
            if (r >= 0.5){
                game.getCurrent().getA().setA(Math.random() * 100);
            }
            game.getCurrent().getSe().replace(StatusEffect.CONFUSION,game.getCurrent().getSe().get(StatusEffect.CONFUSION) - 1);
        }
        if (game.getCurrent().getSe().get(StatusEffect.GLUE) > 0){
            game.getCurrent().getSe().replace(StatusEffect.GLUE,game.getCurrent().getSe().get(StatusEffect.GLUE) - 1);
        }
    }

}