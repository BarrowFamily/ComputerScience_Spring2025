package Homework.Homework2;
//You will need to modify and turn in this file for HW2.
/**
 * A class that manages a 2-D array of pixels for the GrowCut algorithm.
 * @author Starter code by Jon Juett.
 * @version Starter code updated 8/28/2024 - moved graphics update to GrowCut
 * @version Starter code for HW2 first written 1/29/2024
 *
 * @author Joseph Barrow
 * Uses the methods provided and an array of pixels to decide if a pixel is defeated or not.
 * @version 1-24-2025
 */
public class GrowCutGrid{
    //Add a private instance variable "pixels"
    //this variable should be able to refer to a row-major
    //2-D array of GrowCutPixel elements
    private GrowCutPixel[][] pixels;

    /**
     * A custom constructor for GrowCutGrid. Takes in a height and width
     * based on the image to be segmented, and uses them to initialize
     * the pixels grid to have these dimensions.
     * Also default constructs each GrowCutPixel object in the new 2-D array.
     * @param height - the number of rows in the pixels array, > 0
     * @param width - the number of columns in the pixels array, > 0
     * NOTE: You may assume these preconditions are always met for HW2.
     */
    public GrowCutGrid(int height, int width){
        pixels = new GrowCutPixel[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j] = new GrowCutPixel();
            }
        }
    }

    /**
     * An accessor that returns the GrowCutPixel at row, col in the grid.
     * @param row - the first index (row) of the pixels array of the desired pixel
     * @param col - the second index (column) of the pixels array of the desired pixel
     * @return - a non-null GrowCutPixel located at the specified row and column.
     */
    public GrowCutPixel getPixel(int row, int col){
        return pixels[row][col];
    }

    /**
     * The core of the GrowCut algorithm, where one pixel "attacks" another
     * pixel, which will be one of its neighbors, attempting to "conquer" it,
     * changing the label and/or strength of the defending pixel.
     *      Note: the possible labels indicate
     *              0 - not labeled yet
     *              1 - foreground
     *              2 - background
     *            and the strength values range from 0.0 to 1.0, with
     *            strengths of 1.0 only being possible for the clicked
     *            "seed" pixels or identical pixels neighboring them.
     *            All non-seed pixels start with a strength of 0.0
     *            to ensure they are labeled, no matter how dissimilar
     *            they are to their neighbors.
     *
     * Steps for this method:
     *
     *      0. If the attacker's label according to getLabel() is 0,
     *         return false and skip all following steps.
     *         (to prevent pixels from attacking before they're labeled)
     *
     *      1. Determine the similarity factor g as 1.0 minus the distance
     *         between the attacker and defender's RGB values. GrowCutPixel
     *         contains a static distance method to help find this distance.
     *
     *      2. Determine the strength of the attack as g times the stored
     *         strength of the attacking pixel, found with getStrength().
     *         (The algorithm uses these factors since its confidence that the
     *         defending pixel should be "conquered" and take on the attacker's
     *         label depends on both how similar the pixels are and on how
     *         confident it was that the attacker was correctly labeled.)
     *
     *      3. If the strength of the attack (g * attacker's strength) is greater
     *         than the defender's stored strength, the defender is "conquered".
     *         Have the attacker call the conquer method with the defender
     *         and the strength of the attack as input arguments, then return true.
     *         (Avoid using the setStrength() or setLabel() methods here - these
     *         are implemented in a way that assumes they are only used by the
     *         provided graphics code. You should only need conquer() here.)
     *
     *      4. If the strength of the attack was not greater than the defender's
     *         stored strength, neither the attacker or defender is conquered.
     *         Return false without making any changes to either pixel.
     *
     *  @param - attacker: a non-null reference to a GrowCutPixel that will
     *                     attempt to apply its label to the defender if its
     *                     label is nonzero.
     *  @param - defender: a non-null reference to a GrowCutPixel that will
     *                     be "attacked" and possibly take on a new label
     *                     or strength based on the attacker.
     *  @return - true if the defender was "conquered" because the strength
     *            of the attack was higher than the defender's strength,
     *            and false otherwise.
     */
    public static boolean attack(GrowCutPixel attacker, GrowCutPixel defender){

        if (attacker.getLabel() == 0){
            return false;
        }
        else{
            double g = 1 - GrowCutPixel.distance(attacker, defender);
            double attackStrength = attacker.getStrength() * g;
            if (attackStrength > defender.getStrength()){
                attacker.conquer(defender, attackStrength);
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Causes every pixel with a neighbor to its right (that is, every
     * GrowCutPixel not in the last column of the pixels array) to attack that
     * right neighbor, the pixel with a col index one higher than its own.
     * NOTE: Pixels in the last col will be attacked, but should not attack.
     *       (Be careful not to attack right of the last col - out of bounds!)
     * Returns whether any pixels were changed due to being
     * conquered by these attacks.
     *
     * @return - true if at least one call of attack(attacker, defender)
     *           returns true, indicating the defender was conquered in
     *           at least that one case, and false otherwise.
     */
    public boolean attackRight(){
        boolean retBool = false;

        for (int i = 0; i < pixels.length - 2; i++) {
            for (int j = 0; j < pixels[i].length - 1; j++) {
                boolean t = attack(pixels[i][j], pixels[i][j + 1]);
                if (t){
                    retBool = true;
                }
            }
        }

        return retBool;
    }

    /**
     * Causes every pixel with a neighbor to its left (that is, every
     * GrowCutPixel not in column 0 of the pixels array) to attack that
     * left neighbor, the pixel with a col index one lower than its own.
     * NOTE: Pixels in the col 0 will be attacked, but should not attack.
     *       (Be careful not to attack left of col 0 - out of bounds!)
     * Returns whether any pixels were changed due to being
     * conquered by these attacks.
     *
     * @return - true if at least one call of attack(attacker, defender)
     *           returns true, indicating the defender was conquered in
     *           at least that one case, and false otherwise.
     */
    public boolean attackLeft(){
        boolean retBool = false;

        for (int i = 0; i < pixels.length - 1; i++) {
            for (int j = 1; j < pixels[i].length - 1; j++) {
                boolean t = attack(pixels[i][j], pixels[i][j - 1]);
                if (t){
                    retBool = true;
                }
            }
        }

        return retBool;
    }

    /**
     * Causes every pixel with a neighbor below it (that is, every
     * GrowCutPixel not in the last row of the pixels array) to attack that
     * below neighbor, the pixel with a row index one higher than its own.
     * NOTE: Pixels in the last row will be attacked, but should not attack.
     *       (Be careful not to attack below the last row - out of bounds!)
     * Returns whether any pixels were changed due to being
     * conquered by these attacks.
     *
     * @return - true if at least one call of attack(attacker, defender)
     *           returns true, indicating the defender was conquered in
     *           at least that one case, and false otherwise.
     */
    public boolean attackDown(){
        boolean retBool = false;

        for (int i = 0; i < pixels.length - 2; i++) {
            for (int j = 0; j < pixels[i].length - 1; j++) {
                boolean t = attack(pixels[i][j], pixels[i + 1][j]);
                if (t){
                    retBool = true;
                }
            }
        }

        return retBool;
    }

    /**
     * Causes every pixel with a neighbor above it (that is, every
     * GrowCutPixel not in row 0 of the pixels array) to attack that above
     * neighbor, the pixel with a row index one lower than its own.
     * NOTE: Pixels in row 0 will be attacked, but should not attack.
     *       (Be careful not to attack above row 0 - out of bounds!)
     * Returns whether any pixels were changed due to being
     * conquered by these attacks.
     *
     * @return - true if at least one call of attack(attacker, defender)
     *           returns true, indicating the defender was conquered in
     *           at least that one case, and false otherwise.
     */
    public boolean attackUp(){
        boolean retBool = false;

        for (int i = 1; i < pixels.length - 1; i++) {
            for (int j = 0; j < pixels[i].length - 1; j++) {
                boolean t = attack(pixels[i][j], pixels[i - 1][j]);
                if (t){
                    retBool = true;
                }
            }
        }

        return retBool;
    }

    /**
     * Causes attackRight(), attackLeft(), attackUp(), and attackDown()
     * to be called exactly once, in that order, and returns whether any
     * pixels were changed due to being conquered by these attacks.
     *
     *
     * @return - true if at least one of the four directional attack
     *           methods called returns true, and false otherwise.
     */
    public boolean attackAll(){
        boolean retBool = false;

        boolean t = attackRight();
        if (t){
            retBool = true;
        }

        t = attackLeft();
        if (t){
            retBool = true;
        }

        t = attackUp();
        if (t){
            retBool = true;
        }

        t = attackDown();
        if (t){
            retBool = true;
        }


        return retBool;
    }
}