package breakout;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;

/**
 * The game of Breakout.
 * 
 */
public class BreakoutGame {
    private static final int CANVAS_WIDTH = 600;
    private static final int CANVAS_HEIGHT = 800;
    private static final double INTIAL_BALL_SPEED = 0.5; 
    private CanvasWindow canvas;
    private Paddle paddle;
    private BrickManager brickManager;
    private int round = 0;
    private Ball ball;


    public BreakoutGame() {
        canvas = new CanvasWindow("Breakout!", CANVAS_WIDTH, CANVAS_HEIGHT);
        paddle = new Paddle(canvas);
        brickManager = new BrickManager(canvas); 
        canvas.onMouseMove(event -> paddle.movePaddle(event.getPosition()));
        run();
    }

    public void run(){
        
        ball = new Ball(canvas.getWidth()/2, canvas.getHeight()/2, INTIAL_BALL_SPEED);
        ball.addToCanvas(canvas);
        restartRound();
        

       canvas.animate(() -> {
            if (brickManager.getTotalBricks() != brickManager.getNumOfBricksRemoved()){
                gameInProgress();
                if (brickManager.getTotalBricks() == brickManager.getNumOfBricksRemoved()){
                   tellUserGameHasEnded("You won!", ball);
                }
        }});
    } 

    public static void main(String[] args){
        new BreakoutGame();
    }


    /**
     * Takes in a string input and shows this to the user as GraphicText. It also
     * removes the ball that it takes in as a parameter too.
     */
    private void tellUserGameHasEnded(String string, Ball ball){
        GraphicsText text = new GraphicsText(string);
        text.setCenter(canvas.getCenter());
        canvas.add(text);
        ball.removeFromCanvas(canvas);
    }

    /**
     * This method restart round or resume rounds depending on the ball's postion.
     * It will inform the user that they have lost if 3 rounds have been played.
     */
    private void gameInProgress(){
        if (round < 3){
            if (!(ball.getCenterY() > paddle.getPaddleShape().getY() + canvas.getHeight() * .1)){
                for (int i = 0; i<100; i++){
                    ball.updatePosition(canvas, 0.1, brickManager, paddle); //making the physics time step smaller.
                }

            } else {
                restartRound();
                round = round + 1;
            }
            if (round == 3){
                tellUserGameHasEnded("You lost!", ball);
            }
        }
    }

    /**
     * This method check that if less than 2 rounds have been played, it will 
     * reposition the ball into the middle of the canvas and also pauses for 
     * 3s.
     */
    private void restartRound(){
        if (round <2){
            ball.resetPosition(canvas);
            canvas.draw();
            canvas.pause(3000);
        }
    }

}