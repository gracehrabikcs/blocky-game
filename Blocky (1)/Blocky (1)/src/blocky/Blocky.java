package blocky;

public class Blocky
{
    /*
    Grace Hrabik
    12/19/24
    Blocky Assignment - Final Project
    
    fully implemented blocky game
    strategy is to get the highest score possible
    click on a block, use the up arrow to go up a level
    use l to rotate children blocks left and r to rotate children blocks right
    use s to smash the highlighted block
    
    points are calculated based on the perimeter and blobs
    at the beginnning of every game, you get a perimeter game color and a blob game color
    one of your goals is to get as many unit cells to the perimeter as possible
    each unit cell on the perimeter counts for one point, and unit cells in the corners count as 2 points
    your other goal is to connect unit cells of the blob game color, as bigger blobs get you more points
    each unit cell of the blob game color counts as one point
    
    your total score is the combination of the perimeter score and blob score
    */
    public static void main(String[] args)
    {
        Game game = new Game();
        GameRenderer gameRenderer = new GameRenderer(game);
        gameRenderer.display();
    }
}
