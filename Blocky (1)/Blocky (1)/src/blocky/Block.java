package blocky;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// A square block in the Blocky game.
public class Block
{
    public final static Color PACIFIC_POINT = new Color(1, 128, 181);
    public final static Color OLD_OLIVE = new Color(138, 151, 71);
    public final static Color REAL_RED = new Color(199, 44, 58);
    public final static Color DAFFODIL_DELIGHT = new Color(255, 211, 92);
    public final static Color[] COLORS = {DAFFODIL_DELIGHT, OLD_OLIVE, REAL_RED, PACIFIC_POINT};
    
    public final static Color MELON_MAMBO = new Color(234, 62, 112);
    public final static Color HIGHLIGHT_COLOR = new Color(75, 196, 213); //TEMPTING_TURQUOISE
    
    //=== Public Attributes ===
    //position:
    //    The (x, y) coordinates of the upper left corner of this Block.
    //    Note that (0, 0) is the top left corner of the window.
    private int xCoordinate;
    private int yCoordinate;
    
    //size:
    //    The height and width of this Block.  Since all blocks are square,
    //    we needn't represent height and width separately.
    private int size;
    
    //color:
    //    If this block is not subdivided, color stores its color.
    //    Otherwise, color is null and this block's sublocks store their
    //    individual colours.
    private Color color;
    
    //level:
    //    The level of this block within the overall block structure.
    //    The outermost block, corresponding to the root of the tree,
    //    is at level zero.  If a block is at level i, its children are at
    //    level i+1.
    private int level;
    
    //max_depth:
    //    The deepest level allowed in the overall block structure.
    public static int MAX_DEPTH = 4;
    
    // max_size:
    //    The size of the biggest Block, and thus, the entire game board.
    public final static int MAX_SIZE = 640;
    
    //highlighted:
    //    True iff the user has selected this block for action.
    private boolean highlighted;
    
    //children:
    //    The blocks into which this block is subdivided.  The children are
    //    stored in this order: upper-left child, upper-right child,
    //    lower-left child, lower-right child.
    List<Block> children;
    
    //parent:
    //    The block that this block is directly within.
    Block parent;

    /*
    === Notes ===
    - children.size() == 0 or children.size() == 4
    - If this Block has children,
        - their max_depth is the same as that of this Block,
        - their size is half that of this Block,
        - their level is one greater than that of this Block,
        - their position is determined by the position and size of this Block
    - level <= max_depth
    */

    public Block()
    {
        this(Color.WHITE, 0, MAX_SIZE, null);
    }
    
    public Block(Color inColor, int inLevel, int inSize, Block inParent)
    {
        xCoordinate = 0;
        yCoordinate = 0;
        size = inSize;
        color = inColor;
        level = inLevel;
        highlighted = false;
        children = new ArrayList<>();
        parent = inParent;
    }

    public int getXCoordinate()
    {
        return xCoordinate;
    }

    public int getYCoordinate()
    {
        return yCoordinate;
    }

    public int getSize()
    {
        return size;
    }

    public Color getColor()
    {
        return color;
    }

    public int getLevel()
    {
        return level;
    }

    public boolean isHighlighted()
    {
        return highlighted;
    }

    public List<Block> getChildren()
    {
        return children;
    }

    public Block getParent()
    {
        return parent;
    }

    public void setXCoordinate(int xCoordinate)
    {
        this.xCoordinate = xCoordinate;
    }

    public void setYCoordinate(int yCoordinate)
    {
        this.yCoordinate = yCoordinate;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setHighlighted(boolean highlighted)
    {
        this.highlighted = highlighted;
    }

    public void setChildren(List<Block> children)
    {
        this.children = children;
    }

    public void setParent(Block parent)
    {
        this.parent = parent;
    }
    
    // Static method to select a random color from the COLORS array
    public static Color getRandomColor() {
        Random random = new Random();
        int index = random.nextInt(COLORS.length);
        return COLORS[index];
    }
     
    // swaps highlighted block's children vertically and horizontally
    public void swap(boolean horizontal) {
        // if block contains children
        if (!this.getChildren().isEmpty()) {
            
            // get children
            Block upperLeftChild = this.getChildren().get(0);
            Block lowerLeftChild = this.getChildren().get(2);
            Block upperRightChild = this.getChildren().get(1);
            Block lowerRightChild = this.getChildren().get(3);

            // if horizontal is true
            if (horizontal == true) {
                // upper left child (0) swaps with upper right child
                this.getChildren().set(0, upperRightChild);
                // upper right child (1) swaps with upper left child
                this.getChildren().set(1, upperLeftChild);
                // lower left child (2) swaps with lower right child
                this.getChildren().set(2, lowerRightChild);
                // lower right child (3) swaps with lower left child
                this.getChildren().set(3, lowerLeftChild);
            }

            // if horizontal is false, do a veritcal swap
            else {
                // upper left child (0) swaps with lower left child
                this.getChildren().set(0, lowerLeftChild);
                // lower left child (2) swaps with upper left child
                this.getChildren().set(2, upperLeftChild);
                // upper right child (1) swaps with lower right child
                this.getChildren().set(1, lowerRightChild);
                // lower right child (3) swaps with upper right child
                this.getChildren().set(3, upperRightChild);
            } 
            
            // update coordinates so highlighting functionality works when clicking on a block
            updateChildCoordinates();
        }
    }
    
    // smashes a highlighted block, clears existing children for the block
    public void smash() {
        // Note: a player cannot Smash the Level 0 Block - this would generate a completely new game board.  
        // A player also cannot Smash a Block at the maximum depth (Block.MAX_DEPTH) - a Block at that level cannot be divided.  
        // Check both of these conditions before smashing a Block.
        if (this.level == 0) {
            System.out.println("Smashing this level will result in new game board.");
        }
        
        if (this.level > 0 && this.level < MAX_DEPTH) {
            // clear existing children arraylist
            this.children.clear(); 
        }
        
    
    }

    // rotates a highlighted block's children, and their children's children down the last ancestor, either clockwise or counterclockwise
    // press L key for counterclockwise and right key for clockwise
    public void rotate(boolean clockwise) {
        // use recursion
        
        // if block contains children
        if (!this.getChildren().isEmpty()) {
            
            // get children
            Block upperLeftChild = this.getChildren().get(0);
            Block upperRightChild = this.getChildren().get(1);
            Block lowerLeftChild = this.getChildren().get(2);
            Block lowerRightChild = this.getChildren().get(3);

            // if clockwise is false, rotate counter clockwise
            if (clockwise == false) {
                // upper left child (0) swaps with upper right child
                this.getChildren().set(0, upperRightChild);
                // upper right child (1) swaps with lower right child
                this.getChildren().set(1, lowerRightChild);
                // lower right child (3) swaps with lower left child
                this.getChildren().set(3, lowerLeftChild);
                // lower left child (2) swaps with upper left child
                this.getChildren().set(2, upperLeftChild);
            }
            
            // clockwise is true, rotate clockwise
            else {
                // upper left child (0) swaps with lower left child
                this.getChildren().set(0, lowerLeftChild);
                // lower left child (2) swaps with lower right child
                this.getChildren().set(2, lowerRightChild);
                // lower right child (3) swaps with upper right child
                this.getChildren().set(3, upperRightChild);
                // upper right child (1) swaps with upper left child
                this.getChildren().set(1, upperLeftChild);
                    
            }  
            
            // for every child block within the block
            for (Block child : this.getChildren()) {
                // rotate child block
                child.rotate(clockwise);
            }
            
            // update coordinates so highlighting functionality works when clicking on a block
            updateChildCoordinates();
        }  
    }
    
    // highlighting wasn't working properly after rotate and swap, I needed an updateChildCoordinates() method
    public void updateChildCoordinates() {
        // if there is no children to update, return
        if (this.children.isEmpty()) {
            return; 
        }

        int halfSize = this.size / 2;
        int x = this.xCoordinate;
        int y = this.yCoordinate;

        // update coordinates for each child based on the parent's coordinates and size
        // for upper left
        children.get(0).setXCoordinate(x);                     
        children.get(0).setYCoordinate(y);
        // for upper right 
        children.get(1).setXCoordinate(x + halfSize);
        children.get(1).setYCoordinate(y);      
        // for lower left
        children.get(2).setXCoordinate(x);
        children.get(2).setYCoordinate(y + halfSize);
        // for lower right
        children.get(3).setXCoordinate(x + halfSize);
        children.get(3).setYCoordinate(y + halfSize);

        // for every child in the children, update coordinates
        for (Block child : children) {
            child.updateChildCoordinates();
        }
    }
    
    public int calculatePerimeter(String[][] flattenedBoard, String goalColor) {
        /*
        After every turn, calculate the score for the Perimeter Goal and tell it to the player.  
        The score for the Perimeter Goal is the number of unit squares of the goal color on the perimeter of the game board 
        (remember that corner squares count for 2 points).
        */
        int score = 0;
        int length = flattenedBoard.length;
        
        String letterColor = goalColor.substring(0, 1);
        //System.out.println("letterColor: " + letterColor);
        
        // for every unit cell on the perimeter, if the color matches the goalColor, increment score by 1
        // if the square is a corner sqaure, the score will end up being incremented twice because it will be counted twice
        // FIXED: instead of else if statements, use independent if statements so cells are not skipped
        
        for (int i = 0; i < length; i++) {
            // upper row
            if (flattenedBoard[0][i] != null && flattenedBoard[0][i].equals(letterColor)) {
                //System.out.println("+1 for cell in upper row at cell 0, " + i);
                score++;
            }
            // lower row
            if (flattenedBoard[length - 1][i] != null && flattenedBoard[length - 1][i].equals(letterColor)) {
                //System.out.println("+1 for cell in lower row at cell " + (length - 1) + ", " + i);
                score++;
            }
            // left column
            if (flattenedBoard[i][0] != null && flattenedBoard[i][0].equals(letterColor)) {
                //System.out.println("+1 for cell in left column at cell " + i + ", 0");
                score++;
            }
            // right column
            if (flattenedBoard[i][length - 1] != null && flattenedBoard[i][length - 1].equals(letterColor)) {
                //System.out.println("+1 for cell in right column at cell " + i + ", " + (length - 1));
                score ++;
            }               
        }
        
        System.out.println("Perimeter score: " + score);
        return score;
    }
    
    public int calculateBlob(String[][] flattenedBoard, String blobColor) {
        // start with initial score of 0
        int score = 0;
        // get the first letter of the blob color and use it to compare cell values later
        String blobLetter = blobColor.substring(0,1);
        /*
        iterating through the cells in the flattened tree, and finding out, for each cell, 
        what size of blob it is part of (if it is part of a blob of the target color). 
        The score is the biggest of these.  
        A "blob" is a group of contiguous "unit blocks" of the goal color.
        */  
        // initial thoughts and logic
        // for every cell on the board, if they have left right up and down directionals, check if they are the same color
        // if so, go to the one of the same color while keeping track of the other directions that also share the same color
        // repeat process for the next connecting cell of the same color while incrementing the score as you go to the next cells
        // when you've ran out of directions with alike colors
        // return to the direction of a previous cell with an alike color
        // once you've gone through all the possible paths with alike colors, the end score is kept
        // compare scores of other blobs, and the highest score of the target color blob wins and the score is returned. 
        // bfs/dfs
        int rows = flattenedBoard.length;
        // gives num of elements in first row, or the number of cols
        int cols = flattenedBoard[0].length;
        // create a 2d array to hold the coordinates of visited cells, to make sure we don't 'double dip'
        boolean[][] visited = new boolean[rows][cols];

        // go through all the rows and cols to find all blobs
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                //System.out.println("flattened board test " + flattenedBoard[i][j]);
                //System.out.println("Blob letter " + blobLetter);
                // if the cell is the blob color letter and is not visited yet
                if (flattenedBoard[i][j].equals(blobLetter) && !visited[i][j]) {
                    //System.out.println("test 1");
                    
                    // from the current cell we will calculate the size of the blob it is a part of 
                    int currentBlobSize = calculateBlobSize(flattenedBoard, blobLetter, visited, i, j);
                    // update score to current largest blob size
                    if (currentBlobSize > score) {
                        score = currentBlobSize;
                    }
                }
            }
        }
        
        System.out.println("Blob score: " + score);
        return score;
    }
    
    // from the starting cell, expand search for cells of the same color in each direction and update score accordingly
    private int calculateBlobSize(String[][] flattenedBoard, String blobColor, boolean[][] visited, int x, int y) {
        // get rows and cols
        int rows = flattenedBoard.length;
        int cols = flattenedBoard[0].length;
        String blobLetter = blobColor.substring(0,1);
        // get directions up, down, left, and right
        int[] directionsX = {-1, 1, 0, 0};
        int[] directionsY = {0, 0, -1, 1};

        // the current cell is marked as visited
        visited[x][y] = true;
        // the initial blob size is 1 because we start with the current cell
        int blobSize = 1;

        // for each cell in the 4 directions
        for (int i = 0; i < 4; i++) {
            int newX = x + directionsX[i];
            int newY = y + directionsY[i];

            // if the new cell is within bounds
            // is not visited
            // and is the correct color
            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && !visited[newX][newY] && flattenedBoard[newX][newY].equals(blobLetter)) {
                // recursively calculate blob size
                // therefore, the blobsize will account for when we find connecting cells of the correct color
                //System.out.println("visiting cell " + newX + ", " + newY);
                blobSize += calculateBlobSize(flattenedBoard, blobLetter, visited, newX, newY);
                //System.out.println("Updating blob size to" + blobSize);
            }
        }

        return blobSize;
    }
    
    public int calculateScore(int perimeterScore, int blobScore) {
        int totalScore = perimeterScore + blobScore;
        System.out.println("Total score: " + totalScore);
        return totalScore;
    }
    
    public String[][] flatten() {
        // calculate grid size based on MAX_DEPTH
        // for example, if the max depth is 2, the grid size will be 4
        int gridSize = (int) Math.pow(2, Block.MAX_DEPTH);
        String[][] flattened = new String[gridSize][gridSize];
        flattenHelper(this, flattened, 0, 0, gridSize);
        return flattened;
    }
    
    private void flattenHelper(Block block, String[][] flattened, int startX, int startY, int cellSize) {
        // base case: block is at the maximum depth, and therefore a unit cell
        if (block.getLevel() == Block.MAX_DEPTH) {
            // find color letter
            // with j being the default for testing purposes
            String colorLetter = "j";
            if (block.getColor() == REAL_RED) {
                colorLetter = "r";
            }
            else if (block.getColor() == OLD_OLIVE) {
                colorLetter = "g";
            }
            else if (block.getColor() == DAFFODIL_DELIGHT) {
                colorLetter = "y";
            }
            else if (block.getColor() == PACIFIC_POINT) {
                colorLetter = "b";
            }

            // mark this unit cell with the correct letter
            flattened[startY][startX] = colorLetter;
        } 
        // else if, block.getChildren().isEmpty()
        // basically if it is a large block comprised on unit cells of the same color
        else if (block.getChildren().isEmpty()) {
            // split it into unit cells
            String colorLetter = "j"; // Default
            if (block.getColor() == REAL_RED) {
                colorLetter = "r";
            }
            else if (block.getColor() == OLD_OLIVE) {
                colorLetter = "g";
            }
            else if (block.getColor() == DAFFODIL_DELIGHT) {
                colorLetter = "y";
            }
            else if (block.getColor() == PACIFIC_POINT) {
                colorLetter = "b";
            }

            // fill in the area of this block with the correct letter
            for (int i = startY; i < startY + cellSize; i++) {
                for (int j = startX; j < startX + cellSize; j++) {
                    flattened[i][j] = colorLetter;
                }
            }
        }
        // if the block has children
        else {
            // recursively process children
            // subdivide the array
            int halfCellSize = cellSize / 2;
            List<Block> children = block.getChildren();

            // check bounds, don't overwrite cells
            if (startX + halfCellSize <= flattened[0].length && startY + halfCellSize <= flattened.length) {
                //top left
                flattenHelper(children.get(0), flattened, startX, startY, halfCellSize); 
                // top right
                flattenHelper(children.get(1), flattened, startX + halfCellSize, startY, halfCellSize);
                // bottom left
                flattenHelper(children.get(2), flattened, startX, startY + halfCellSize, halfCellSize);
                // bottom right
                flattenHelper(children.get(3), flattened, startX + halfCellSize, startY + halfCellSize, halfCellSize);
            }
        }
    }


}

