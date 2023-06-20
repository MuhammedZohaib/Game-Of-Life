package org.example.DAO;// Add proper import statements according to your eclipse project


import org.example.View.MyInterface;

import java.util.Random;


public class Simulator extends Thread {

	private MyInterface mjf;
	private boolean stopFlag;
	private boolean pauseFlag;
	private int loopDelay;
	private long tick;
	private int size = 600;
	private byte cells[][];
	private byte newCells[][];
	private boolean loopingBorder;

	public Simulator(MyInterface mjfParam) {
		mjf = mjfParam;
		stopFlag = false;
		pauseFlag = false;
		loopDelay = 150;
		cells = new byte[size][size];
		newCells = new byte[size][size];
	}

	public int getWidth() {
		return mjf.getWidth();
	}

	public int getHeight() {
		return mjf.getHeight();
	}

	public void run() {
		int stepCount = 0;
		while (!stopFlag) {
			stepCount++;
			makeStep();
			mjf.update(stepCount);
			try {
				Thread.sleep(loopDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (pauseFlag && !stopFlag) {
				try {
					Thread.sleep(loopDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void makeStep() {
		tick++;
		newCells = new byte[size][size];
		int totalSum = 0;
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				byte sum = calculateNeighborSum(i, j);
				totalSum += sum;
				if (cells[i][j] == 1) {
					if (sum < 2) {
						newCells[i][j] = 0;
					} else if (sum > 3) {
						newCells[i][j] = 0;
					} else {
						newCells[i][j] = 1;
					}
				} else if (sum == 3) {
					newCells[i][j] = 1;
				} else {
					newCells[i][j] = 0;
				}
			}
		}
		if (totalSum == 0) {
			System.out.println("All cells are dead, stopping!");
			mjf.clicButtonStop();
		}
		cells = newCells;
		mjf.repaint();
	}

	private byte calculateNeighborSum(int x, int y) {
		boolean loopingBorder = this.loopingBorder;
		return RulesOfLife.applyingRules(x,y,this.cells,loopingBorder);

	}

	public void stopSimu() {
		stopFlag = !stopFlag;
	}

	public void togglePause() {
		pauseFlag = !pauseFlag;
	}

	public void toggleCell(int x, int y) {
		if (cells[x][y] == 1) {
			cells[x][y] = 0;
		} else {
			cells[x][y] = 1;
		}
	}
	public int getCell(int x, int y) {
		return cells[x][y];
	}
	public void setCell(int x, int y, int val) {
		cells = new byte[x][y];
		cells[x][y] = (byte) val;
	}

	public String[] getFileRepresentation() {
		return FileHandler.fileToString(this.size,this.cells);
	}

	public void populateLine(int y, String fileLine) {
		FileHandler.stringToFile(y,fileLine,this.cells);
	}
	public void generateRandom(float chanceOfLife) {
		Random r = new Random();
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				if (r.nextFloat() < chanceOfLife) {
					cells[i][j] = 1;
				} else {
					cells[i][j] = 0;
				}
			}
		}
	}

	public boolean isLoopingBorder() {
		if (loopingBorder) {
			return true;
		}
		return false;
	}

	public void toggleLoopingBorder() {
		if (isLoopingBorder()) {
			loopingBorder = false;
		} else {
			loopingBorder = true;
		}
	}

	public void setLoopDelay(int delay) {
		loopDelay = delay;
	}

}
