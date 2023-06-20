package org.example.DAO;// Add proper import statements according to your eclipse project

public class FileHandler {
    public static void stringToFile(int y, String fileLine, byte[][] cells){
        String[] values = fileLine.split(";");
        for (int x = 0; x < values.length; x++) {
            cells[x][y] = Byte.parseByte(values[x]);
        }
    }
    public static String[] fileToString(int size, byte[][] cells){
        String[] fileRepresentation = new String[size];
        for (int i = 0; i < cells.length; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < cells[i].length; j++) {
                line.append(cells[i][j]).append(";");
            }
            fileRepresentation[i] = line.toString();
        }
        return fileRepresentation;
    }
}
