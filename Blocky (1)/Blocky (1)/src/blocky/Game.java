package blocky;
import static blocky.Block.DAFFODIL_DELIGHT;
import static blocky.Block.OLD_OLIVE;
import static blocky.Block.PACIFIC_POINT;
import static blocky.Block.REAL_RED;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game
{
    //=== Public Attributes ===
    //root:
    //    The Blocky board on which this game will be played.
    private Block root;
    
    //highlightedBlock:
    //    The block the user has selected.  This will be colored specially when rendered.
    private Block highlightedBlock;
    
    public String gameColor;
    
    public String blobColor;
    
    public Game()
    {
        //root = createTestBoard();
        root = new Block();
        createRandomChildren(root);
        
        highlightedBlock = null;
        
        // print game color
        gameColor = selectGameColor();
        System.out.println("Perimeter game color: " + gameColor);
        
        //print blob color
        blobColor = selectBlobColor();
        System.out.println("Blob game color: " + blobColor);
        
        
    }
    
    public String selectGameColor() {
        Random random = new Random();
        Color[] colors = {REAL_RED, OLD_OLIVE, DAFFODIL_DELIGHT, PACIFIC_POINT};
        Color gameColor = colors[random.nextInt(colors.length)];
        String goalColor = "";
        if (gameColor == REAL_RED) {
            goalColor = "red";
        }
        else if (gameColor == OLD_OLIVE) {
            goalColor = "green";
        }
        else if (gameColor == DAFFODIL_DELIGHT) {
            goalColor = "yellow";
        }
        else if (gameColor == PACIFIC_POINT) {
            goalColor = "blue";
        }
        return goalColor;
    }
    
    public String selectBlobColor() {
        Random random = new Random();
        Color[] colors = {REAL_RED, OLD_OLIVE, DAFFODIL_DELIGHT, PACIFIC_POINT};
        Color blob = colors[random.nextInt(colors.length)];
        String blobColor = "";
        if (blob == REAL_RED) {
            blobColor = "red";
        }
        else if (blob == OLD_OLIVE) {
            blobColor = "green";
        }
        else if (blob == DAFFODIL_DELIGHT) {
            blobColor = "yellow";
        }
        else if (blob == PACIFIC_POINT) {
            blobColor = "blue";
        }
        return blobColor;
    }
    

    
    public void createRandomChildren(Block parent)
    {
        // TODO - complete this method.
        // Determine what level the children are at.  Hint: their level is one greater than the parent's level.
        int childLevel = parent.getLevel() + 1;
        
        // Base case, if we have hit max depth
        if (childLevel > Block.MAX_DEPTH) {
            return;
        }
        
        //Determine the size of the children (number of pixels tall and wide).  Hint: their size is half of the parent's size.
        int childSize = parent.getSize() / 2;
        int parentX = parent.getXCoordinate();
        int parentY = parent.getYCoordinate();
        
        // Create a List to store four children.
        List<Block> fourChildren = new ArrayList();
        // Create four Blocks.  Each Block should have a random color (see the Block.COLORS array).  Add each Block to your List.
        Block firstBlock = new Block();
        firstBlock.setLevel(childLevel);
        firstBlock.setColor(Block.getRandomColor());
        firstBlock.setSize(childSize);
        firstBlock.setXCoordinate(parentX);
        firstBlock.setYCoordinate(parentY);
        firstBlock.setParent(parent);
        fourChildren.add(firstBlock);
        
        Block secondBlock = new Block();
        secondBlock.setLevel(childLevel);
        secondBlock.setColor(Block.getRandomColor());
        secondBlock.setSize(childSize);
        secondBlock.setXCoordinate(parentX + childSize);
        secondBlock.setYCoordinate(parentY);
        secondBlock.setParent(parent);
        fourChildren.add(secondBlock);
        
        Block thirdBlock = new Block();
        thirdBlock.setLevel(childLevel);
        thirdBlock.setColor(Block.getRandomColor());
        thirdBlock.setSize(childSize);
        thirdBlock.setXCoordinate(parentX);
        thirdBlock.setYCoordinate(parentY + childSize);
        thirdBlock.setParent(parent);
        fourChildren.add(thirdBlock);
        
        Block fourthBlock = new Block();
        fourthBlock.setLevel(childLevel);
        fourthBlock.setColor(Block.getRandomColor());
        fourthBlock.setSize(childSize);
        fourthBlock.setXCoordinate(parentX + childSize);
        fourthBlock.setYCoordinate(parentY + childSize);
        fourthBlock.setParent(parent);
        fourChildren.add(fourthBlock);
        
        // Send the List to the parent via the setChildren method.
        parent.setChildren(fourChildren);
        
        // If we haven't hit the maximum depth of the gameboard (Block.MAX_DEPTH), we need to randomly add children to the children.
        if (childLevel <= Block.MAX_DEPTH) {
            // For each child, randomly determine if it will have children.  
            // Do this by checking to see if a random number between 0 and 1 (including 0 but not including 1) 
            // is less than e raised to the (-1/4 * level) power.  In case that is confusing :), 
            // here is the if statement you need: if (Math.random() < Math.exp(-0.25 * level))
            // If you have determined that a child should have children, generate four random children for it.  
            // How could you do this?  Do you happen to have a method that generates random children for a Block?
            for (Block child: fourChildren) {
                if (Math.random() < Math.exp(-0.25 * childLevel)) {
                    createRandomChildren(child);  
                }                
            }
        }
                
    }

    
    public Block createTestBoard()
    {
        Block root = new Block();
        root.setSize(Block.MAX_SIZE);
        
        List<Block> rootChildren = new ArrayList<>();
        rootChildren.add(new Block(Block.DAFFODIL_DELIGHT, 1, 320, root));
        rootChildren.add(new Block(Block.OLD_OLIVE, 1, 320, root));
        rootChildren.add(new Block(Block.PACIFIC_POINT, 1, 320, root));
        rootChildren.add(new Block(Block.REAL_RED, 1, 320, root));
        
        root.setChildren(rootChildren);
        Block child = root.getChildren().get(0);
        
        rootChildren = new ArrayList<>();
        rootChildren.add(new Block(Block.DAFFODIL_DELIGHT, 2, 160, child));
        rootChildren.add(new Block(Block.OLD_OLIVE, 2, 160, child));
        rootChildren.add(new Block(Block.PACIFIC_POINT, 2, 160, child));
        rootChildren.add(new Block(Block.REAL_RED, 2, 160, child));
        
        child.setChildren(rootChildren);
        
        return root;
    }

    public Block getRoot()
    {
        return root;
    }

    public Block getHighlightedBlock()
    {
        return highlightedBlock;
    }

    public void setRoot(Block root)
    {
        this.root = root;
    }

    public void setHighlightedBlock(Block highlightedBlock)
    {
        this.highlightedBlock = highlightedBlock;
    }
}
