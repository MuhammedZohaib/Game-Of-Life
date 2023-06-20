package org.example.View;// Add proper import statements according to your eclipse project


import org.example.DAO.Simulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class MyInterface extends JFrame {

	private static final long serialVersionUID = -6840815447618468846L;
	private JPanel contentPane;
	private JLabel stepLabel;
	private JLabel borderLabel;
	private JLabel speedLabel;
	private JPanelDraw panelDraw;
	private Simulator mySimu = null;
	private JSlider randSlider;
	private JSlider speedSlider;


	public MyInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelTop = new JPanel();
		contentPane.add(panelTop, BorderLayout.NORTH);

		JPanel panelRight = new JPanel();
		panelRight.setLayout(new GridLayout(10, 1));
		contentPane.add(panelRight, BorderLayout.EAST);

		JButton btnGo = new JButton("Start/Pause");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clicButtonGo();
			}
		});
		panelTop.add(btnGo);

		stepLabel = new JLabel("Step : X");
		panelTop.add(stepLabel);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clicButtonStop();
			}
		});
		panelTop.add(btnStop);

		speedLabel = new JLabel("speed slider : ");
		panelTop.add(speedLabel);

		speedSlider = new JSlider();
		speedSlider.setValue(3);
		speedSlider.setMinimum(0);
		speedSlider.setMaximum(10);
		speedSlider.setOrientation(SwingConstants.HORIZONTAL);
		speedSlider.setPreferredSize(new Dimension(100, 30));
		speedSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				changeSpeed();
			}
		});
		panelTop.add(speedSlider);

		JButton btnLoad = new JButton("Load File");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clicLoadFileButton();
			}
		});
		panelRight.add(btnLoad);

		JButton btnSave = new JButton("Save To File");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clicSaveToFileButton();
			}
		});
		panelRight.add(btnSave);

		JButton btnRandGen = new JButton("Random");
		btnRandGen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generateRandomBoard();
			}
		});
		panelRight.add(btnRandGen);

		JLabel randLabel = new JLabel("random density slider :");
		panelRight.add(randLabel);

		randSlider = new JSlider();
		randSlider.setValue(50);
		randSlider.setMinimum(0);
		randSlider.setMaximum(100);
		randSlider.setPreferredSize(new Dimension(30, 200));
		panelRight.add(randSlider);

		JButton btnBorder = new JButton("Toggle Border");
		btnBorder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clicButtonBorder();
			}
		});
		panelRight.add(btnBorder);

		borderLabel = new JLabel("border : X");
		panelRight.add(borderLabel);

		panelDraw = new JPanelDraw(this);
		contentPane.add(panelDraw, BorderLayout.CENTER);
	}

	public void setStepBanner(String s) {
		stepLabel.setText(s);
	}

	public void setBorderBanner(String s) {
		borderLabel.setText(s);
	}

	public void instantiateSimu() {
		if (mySimu == null) {
			mySimu = new Simulator(this);
			panelDraw.setSimu(mySimu);
		}
	}

	public void clicButtonGo() {
		this.instantiateSimu();
		if (!mySimu.isAlive()) {
				mySimu.start();
		} else {
			mySimu.togglePause();
		}
	}

	public void clicButtonStop() {
		if (mySimu != null) {
			panelDraw.setSimu(null);
			mySimu.stopSimu();
			mySimu = null;
			this.eraseLabels();
			panelDraw.repaint();
		}
	}

	public void clicButtonBorder() {
		if (mySimu != null) {
			mySimu.toggleLoopingBorder();
			borderLabel.setText("border : " + (mySimu.isLoopingBorder() ? "loop" : "closed"));
		}
	}

	public void generateRandomBoard() {
		this.instantiateSimu();
		float chanceOfLife = ((float) randSlider.getValue()) / ((float) randSlider.getMaximum());
		mySimu.generateRandom(chanceOfLife);
		panelDraw.repaint();
	}

	public void changeSpeed() {
		if (mySimu != null) {
			int delay = (int) Math.pow(2, 10 - speedSlider.getValue());
			mySimu.setLoopDelay(delay);
		} else {
			speedSlider.setValue(3);
		}
	}

	public void clicLoadFileButton() {
		Simulator loadedSim = new Simulator(this);
		String fileName = SelectFile();
		if (fileName.length() > 0) {
			try {
				BufferedReader fileContent = new BufferedReader(new FileReader(fileName));
				String line = fileContent.readLine();
				int y = 0;
				while (line != null) {
					loadedSim.populateLine(y, line);

					y++;
					line = fileContent.readLine();
				}
				fileContent.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (mySimu != null) {
				mySimu.stopSimu();
				this.eraseLabels();
			}
			mySimu = loadedSim;
			panelDraw.setSimu(mySimu);
			this.repaint();
		}
	}

	public void clicSaveToFileButton() {
		String fileName = SelectFile();
		if (fileName.length() > 0) {
			String[] content = mySimu.getFileRepresentation();
			writeFile(fileName, content);
		}
	}

	public String SelectFile() {
		String s;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Choose a file");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setAcceptAllFileFilterUsed(true);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			s = chooser.getSelectedFile().toString();
		} else {
			System.out.println("No Selection ");
			s = "";
		}
		return s;
	}

	public void writeFile(String fileName, String[] content) {
		FileWriter csvWriter;
		try {
			csvWriter = new FileWriter(fileName);
			for (String row : content) {
				csvWriter.append(row);
				csvWriter.append("\n");
			}
			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(int stepCount) {
		this.setStepBanner("Step : " + stepCount);
		this.repaint();
	}

	public void eraseLabels() {
		this.setStepBanner("Step : X");
		this.setBorderBanner("border : X");
		speedSlider.setValue(3);
	}

}
