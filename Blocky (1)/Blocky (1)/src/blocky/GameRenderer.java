package blocky;

import static blocky.Block.DAFFODIL_DELIGHT;
import static blocky.Block.OLD_OLIVE;
import static blocky.Block.PACIFIC_POINT;
import static blocky.Block.REAL_RED;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

public class GameRenderer extends JComponent implements MouseListener, KeyListener
{
    private Image image;
    private JFrame frame;
    private Game game;
    
    public GameRenderer(Game inGame)
    {
        game = inGame;
        setUpRenderer();
    }
    
    public void display()
    {
        paintBlocks(game.getRoot(), 0, 0);
        paintHighlightedBlock();
        repaint();
        
        // flatten the board and print it
        String[][] flattenedBoard = game.getRoot().flatten();
        System.out.println("Flattened Board:");
        for (String[] row : flattenedBoard) {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        
        // print game color
        //String gameColor = selectGameColor();
        //System.out.println("Game color: " + gameColor);
        
        String gameColor = game.gameColor;
        
        String blobColor = game.blobColor;
        
        // calculate perimeter score
        game.getRoot().calculatePerimeter(flattenedBoard, gameColor);
        
        // calculate blob score
        game.getRoot().calculateBlob(flattenedBoard, blobColor);
        
        // calculate total score
        game.getRoot().calculateScore(game.getRoot().calculatePerimeter(flattenedBoard, gameColor), game.getRoot().calculateBlob(flattenedBoard, blobColor));
        
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
    
   
    
    private void setUpRenderer()
    {
        image = new BufferedImage(Block.MAX_SIZE, Block.MAX_SIZE, BufferedImage.TYPE_INT_RGB);

        frame = new JFrame("Blocky");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        frame.getContentPane().add(topPanel);
        
        // Add the key listener to this component
        addKeyListener(this);
        setFocusable(true); // Important to make this component focusable
        requestFocusInWindow(); // Request focus for this component

        setPreferredSize(new Dimension(Block.MAX_SIZE, Block.MAX_SIZE));
        addMouseListener(this);
        frame.addKeyListener(this);
        topPanel.add(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        topPanel.add(buttonPanel);
        
        frame.pack();
        frame.addWindowListener(new WindowAdapter() { 
          @Override
          public void windowClosing(WindowEvent e) {System.exit(0);} 
        });
        frame.setVisible(true);
    }
    
    private void paintHighlightedBlock()
    {
        Block highlightedBlock = game.getHighlightedBlock();
        
        if (highlightedBlock != null)
        {
            // Set up the graphics object and paint the border the highlight color.
            Graphics graphics = image.getGraphics();
            graphics.setColor(Block.HIGHLIGHT_COLOR);
            graphics.drawRect(highlightedBlock.getXCoordinate(), highlightedBlock.getYCoordinate(), 
                    highlightedBlock.getSize(), highlightedBlock.getSize());
            graphics.drawRect(highlightedBlock.getXCoordinate() + 1, highlightedBlock.getYCoordinate() + 1, 
                    highlightedBlock.getSize() - 2, highlightedBlock.getSize() - 2);
        }
    }
    
    private void paintBlocks(Block block, int xCoordinate, int yCoordinate)
    {
        // Set up the graphics object and paint the solid rectangle the appropriate color.
        Graphics graphics = image.getGraphics();
        graphics.setColor(block.getColor());
        graphics.fillRect(xCoordinate, yCoordinate, block.getSize(), block.getSize());
        
        // Set the border color.
        if (block.isHighlighted())
        {
            
            game.getHighlightedBlock().setXCoordinate(xCoordinate);
            game.getHighlightedBlock().setYCoordinate(yCoordinate);
        }
        else
        {
            graphics.setColor(Color.BLACK);
            graphics.drawRect(xCoordinate, yCoordinate, block.getSize(), block.getSize());
            graphics.drawRect(xCoordinate + 1, yCoordinate + 1, block.getSize() - 2, block.getSize() - 2);
        }
        
        List<Block> children = block.getChildren();
        
        if (!children.isEmpty())
        {
            Block childBlock;
            
            // Upper left.
            childBlock = children.get(0);
            paintBlocks(childBlock, xCoordinate, yCoordinate);
            
            // Upper right.
            childBlock = children.get(1);
            paintBlocks(childBlock, xCoordinate + childBlock.getSize(), yCoordinate);
            
            // Lower left.
            childBlock = children.get(2);
            paintBlocks(childBlock, xCoordinate, yCoordinate + childBlock.getSize());
            
            // Lower right.
            childBlock = children.get(3);
            paintBlocks(childBlock, xCoordinate + childBlock.getSize(), yCoordinate + childBlock.getSize());
        }
    }
    /**
     * Takes in a Block, initially the root Block.
     * Determines which child Block (child, grandchild, etc.) the user clicked
     * @param block
     * @param row
     * @param column 
     */
    
    private void highlightBlock(Block block, int row, int column) {
        // base case, if the block does not have children
        if (block.getChildren().isEmpty()) {
            // check if block contains click
            if (clickInBlock(block, row, column)) {
                // if it does...
                // reset previously highlighted block
                if (game.getHighlightedBlock() != null) {
                    game.getHighlightedBlock().setHighlighted(false);
                }
                // highlight the current block and set it as the highlighted block
                block.setHighlighted(true);
                game.setHighlightedBlock(block);
            }
            // exit
            return; 
        }

        // if the block has children, explore those child blocks for the click
        for (Block child : block.getChildren()) {
            if (clickInBlock(child, row, column)) {
                // if child block contains click
                // highlight child block
                highlightBlock(child, row, column);
                // exit once child block is highlighted
                return; 
            }
        }
    }

    // Helper method to check if block contains click 
    private boolean clickInBlock(Block block, int row, int column) {
        int x = block.getXCoordinate();
        int y = block.getYCoordinate();
        int size = block.getSize();

        return row >= y && row < y + size && column >= x && column < x + size;
    }

    private void highlightBlock(MouseEvent e)
    {
        int row = e.getY();
        int column = e.getX();
        highlightBlock(game.getRoot(), row, column);
    }
  
    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, null);
    }
  
    @Override 
    public void keyPressed(KeyEvent e) {
        
        // check if the up arrow was pressed
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            // get highlighted block
            Block highlightedBlock = game.getHighlightedBlock();

            // Get the parent of the current highlighted block
            Block parentBlock = highlightedBlock.getParent();

            // if parent is not null
            if (parentBlock != null) {

                // reset the highlight on the highlightedBlock
                highlightedBlock.setHighlighted(false);

                // set highlight block to parent
                parentBlock.setHighlighted(true);
                game.setHighlightedBlock(parentBlock);

                display();
            }
            
        }
        
        // check if the H key is pressed
        else if (e.getKeyCode() == KeyEvent.VK_H) {
            // get highlighted block
            Block highlightedBlock = game.getHighlightedBlock();
            // set horizontal to true so swap is horizontal
            highlightedBlock.swap(true);
            
            display();      
        }
        
        // check if the V key is pressed
        else if (e.getKeyCode() == KeyEvent.VK_V) {
            // get highlighted block
            Block highlightedBlock = game.getHighlightedBlock();
            // set horizontal to false so swap is vertical
            highlightedBlock.swap(false);
            
            display();  
        }
        
        // check if the S key is pressed
        else if (e.getKeyCode() == KeyEvent.VK_S) {
            // get the highlighted block
            Block highlightedBlock = game.getHighlightedBlock();
            if (highlightedBlock.getLevel() > 0) {
                // call smash to clear existing children
                highlightedBlock.smash();
                // generate new children for the highlighted block
                game.createRandomChildren(highlightedBlock);
            }
            
            else if (highlightedBlock.getLevel() == 0) {
                System.out.println("Smashing at level 0 will result in a new game board.");
            }
                        
            display();
        }
        
        // check if the Left Arrow key is pressed
        // didn't work for me, so I changed it to the L key
        else if (e.getKeyCode() == KeyEvent.VK_L) {
            // get the highlighted block
            Block highlightedBlock = game.getHighlightedBlock();
            // call rotate and enter false to rotate counterclockwise
            highlightedBlock.rotate(false);
            
            display();
        }
        
        // check if the Right Arrow key is pressed
        // didn't work for me, so I changed it to the R key
        else if (e.getKeyCode() == KeyEvent.VK_R) {
            // get the highlighted block
            Block highlightedBlock = game.getHighlightedBlock();
            // call rotate and enter true to rotate clockwise
            highlightedBlock.rotate(true);
            
            display();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        //highlightBlock(e);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        //highlightBlock(e);
        //display();
        highlightBlock(e);
        display();
        Block highlightedBlock = game.getHighlightedBlock();
        if (highlightedBlock != null) {
            System.out.println("Highlighted Block: " + highlightedBlock.getXCoordinate() + ", " + highlightedBlock.getYCoordinate());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        
    }
}
