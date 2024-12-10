import java.util.Scanner;

public class stressCalculator {
    public static void main(String[] args) {
        evaluateStress();
    }

    public static void evaluateStress() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Stress Calculation Program: Analyze Structural Stress");

            // Input and validate core values
            double pressure = getInput(scanner, "Enter pressure in N/mm^2: ");
            double diameterMm = getInput(scanner, "Enter structure diameter in mm: ");
            double thicknessMm = getInput(scanner, "Enter wall thickness in mm: ");

            // Optional axial force
            System.out.print("Enter axial force in N or press Enter if none: ");
            String axialForceInput = scanner.nextLine();
            double axialForce = 0;

            try {
                // Compute stresses
                if (!axialForceInput.isEmpty()) {
                    axialForce = Double.parseDouble(axialForceInput);
                }

                double axialStress;
                if (axialForce == 0) {
                    axialStress = computeLongitudinalStress(pressure, diameterMm, thicknessMm);
                } else {
                    axialStress = computeAxialStress(axialForce, diameterMm, thicknessMm) 
                                 + computeLongitudinalStress(pressure, diameterMm, thicknessMm);
                }

                double hoopStress = computeHoopStress(pressure, diameterMm, thicknessMm);
                double maxShear = (axialStress - hoopStress) / 2;

                // Display results
                System.out.printf("Longitudinal Stress: %.2f N/mm^2%n", axialStress);
                System.out.printf("Hoop (Circumferential) Stress: %.2f N/mm^2%n", hoopStress);
                System.out.printf("Maximum Shear Stress: %.2f N/mm^2%n", maxShear);

            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid input for axial force.");
                continue;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }

            // Ask if user wants to continue
            System.out.print("Would you like to perform another calculation? (yes/no): ");
            String repeat = scanner.nextLine();
            if (repeat.equalsIgnoreCase("no")) {
                System.out.println("Program ended. Thank you for using the stress calculator.");
                break;
            }
        }
        scanner.close();
    }

    private static double getInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                if (value > 0) {
                    return value;
                }
            } catch (Exception e) {
                scanner.nextLine(); // clear invalid input
            }
            System.out.println("Invalid input. Please enter a positive number.");
        }
    }

    private static double computeLongitudinalStress(double pressure, double diameter, double thickness) {
        return (pressure * diameter) / (4 * thickness);
    }

    private static double computeHoopStress(double pressure, double diameter, double thickness) {
        return (pressure * diameter) / (2 * thickness);
    }

    private static double computeAxialStress(double force, double diameter, double thickness) {
        return force / (Math.PI * diameter * thickness);
    }
}
