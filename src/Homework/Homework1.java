package Homework;

//Joseph Barrow 1-22-2025
//A class that uses the Trapezoid class to create custom trapezoids and mutate them
public class Homework1 {
    public static void main(String[] args) {
        Trapezoid testTrap1 = new Trapezoid();
        System.out.println("Default area: " + testTrap1.getArea());

        Trapezoid testTrap2 = new Trapezoid(2,3,6);
        System.out.println("Custom longer base: " + testTrap2.getLongerBase());
        System.out.println("Custom area: " + testTrap2.getArea());

        testTrap2.setHeight(4);
        System.out.println("Custom area after adjustment: " + testTrap2.getArea());

    }
}
class Trapezoid{

    private double height = 0;//The height of the trapezoid
    private double[] bases;//An array with lenghts of both bases

    /**
     * default constructor
     * starts all values at 1
     */
    Trapezoid(){
        height = 1;
        bases = new double[] {1, 1};
    }

    /**
     * @param Height Custom height of trapezoid
     * @param BaseFirst Custom base of trapezoid
     * @param BaseSecond Custom base of trapezoid
     */
    Trapezoid(double Height, double BaseFirst, double BaseSecond){
        if (Height <= 0 || BaseFirst <= 0 || BaseSecond <= 0){
            throw new IllegalArgumentException("Number entered is less than or equal to 0");
        }

        height = Height;
        bases = new double[] {BaseFirst, BaseSecond};
    }

    /**
     * @param input New height of trapezoid
     */
    public void setHeight(double input){
        height = input;
    }

    /**
     * @return Longer of the two bases
     */
    public double getLongerBase(){
        double longSide;

        if (bases[0] > bases[1]){
            longSide = bases[0];
        }
        else {
            longSide = bases[1];
        }

        return longSide;
    }

    /**
     * @return Area of the trapezoid
     */
    public double getArea(){
        double area = (bases[0] + bases[1]) * height * .5;
        return area;
    }

}
