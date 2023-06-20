package org.example.DAO;// Add proper import statements according to your eclipse project


public class RulesOfLife {

    public static byte applyingRules(int x, int y, byte[][] cells, boolean looping){
        int im = (x - 1 + cells.length) % cells.length;
        int ip = (x + 1) % cells.length;
        int jm = (y - 1 + cells[x].length) % cells[x].length;
        int jp = (y + 1) % cells[x].length;

        byte sum = 0;

        // Check top neighbors
        if (x > 0) {
            sum += cells[im][y]; // Top
            sum += cells[im][jm]; // Top Left
            sum += cells[im][jp]; // Top Right
        } else {
            // Apply closed border or looping border behavior
            if (looping) {
                sum += cells[cells.length - 1][y]; // Top (Looping)
                sum += cells[cells.length - 1][jm]; // Top Left (Looping)
                sum += cells[cells.length - 1][jp]; // Top Right (Looping)
            } else {
                sum += 0; // Top (Closed Border)
                sum += 0; // Top Left (Closed Border)
                sum += 0; // Top Right (Closed Border)
            }
        }

        // Check bottom neighbors
        if (x < cells.length - 1) {
            sum += cells[ip][y]; // Bottom
            sum += cells[ip][jm]; // Bottom Left
            sum += cells[ip][jp]; // Bottom Right
        } else {
            // Apply closed border or looping border behavior
            if (looping) {
                sum += cells[0][y]; // Bottom (Looping)
                sum += cells[0][jm]; // Bottom Left (Looping)
                sum += cells[0][jp]; // Bottom Right (Looping)
            } else {
                sum += 0; // Bottom (Closed Border)
                sum += 0; // Bottom Left (Closed Border)
                sum += 0; // Bottom Right (Closed Border)
            }
        }

        // Check left and right neighbors
        sum += cells[x][jm]; // Left
        sum += cells[x][jp]; // Right

        return sum;
    }
}
