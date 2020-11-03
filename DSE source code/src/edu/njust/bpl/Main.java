package edu.njust.bpl;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import edu.njust.bpl.algorithms.DiscoverDeadlocks;
import edu.njust.bpl.algorithms.DiscoverEventRelations;
import edu.njust.bpl.algorithms.DiscoverSynchronization;
import edu.njust.bpl.algorithms.PreprocessEventLog;
import edu.njust.bpl.log.Dot;
import edu.njust.bpl.log.PnmlUtil;
import edu.njust.bpl.log.ReadLog;
import edu.njust.bpl.structure.EventFlow;
import edu.njust.bpl.structure.PetriArc;
import edu.njust.bpl.structure.PetriNet;
import edu.njust.bpl.structure.PetriPlace;
import edu.njust.bpl.structure.PetriTransition;

public class Main extends JPanel implements ActionListener,
		PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	private JTextField filenameTextField;
	private JButton btnButton, disButton;
	private JFileChooser fileChooser = new JFileChooser();
	private JTextArea resultArea;
	private JProgressBar progressBar;
	private Task task;

	String filePath = null;

	private static List<EventFlow> L;
	private static List<EventFlow> L1;
	private static List<EventFlow> L2;
	private static List<PetriNet> deadlockPetriNets;
	private static List<PetriNet> synchronizationPetriNets;

	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			Random random = new Random();

			// Initialize progress property.
			int progress = 0;
			setProgress(0);

			File file = new File(filePath);
			L = ReadLog.getTraces(file);

			progress += random.nextInt(30);
			setProgress(Math.min(progress, 100));

			PreprocessEventLog preprocessEventLog = new PreprocessEventLog(L,
					"te");
			L1 = preprocessEventLog.getL1();
			L2 = preprocessEventLog.getL2();

			progress += random.nextInt(15);
			setProgress(Math.min(progress, 100));

			DiscoverEventRelations dis1 = new DiscoverEventRelations(L1);
			DiscoverEventRelations dis2 = new DiscoverEventRelations(L2);

			progress += random.nextInt(15);
			setProgress(Math.min(progress, 100));

			DiscoverDeadlocks deadlocks = new DiscoverDeadlocks(dis1);
			deadlocks.mainBody();
			deadlockPetriNets = deadlocks.getPetriNets();

			progress += random.nextInt(15);
			setProgress(Math.min(progress, 100));

			DiscoverSynchronization synchronization = new DiscoverSynchronization(
					dis2);
			synchronization.mainBody();
			synchronizationPetriNets = synchronization.getPetriNets();

			progress += random.nextInt(10);
			setProgress(Math.min(progress, 100));

			write2pnml();

			setProgress(100);
			String result = showResult();
			resultArea.setText(result);

			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			disButton.setEnabled(true);
			setCursor(null); // turn off the wait cursor
			resultArea.append("Done!\n");
		}
	}

	public Main() {
		super(new BorderLayout());

		// Create the demo's UI.
		filenameTextField = new JTextField(20);

		btnButton = new JButton("Choose File");
		btnButton.addActionListener(this);

		disButton = new JButton("Discovery");
		disButton.setActionCommand("start");
		disButton.addActionListener(this);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		resultArea = new JTextArea(20, 50);
		resultArea.setMargin(new Insets(5, 5, 5, 5));
		resultArea.setEditable(false);

		JPanel panel = new JPanel();
		panel.add(filenameTextField);
		panel.add(btnButton);
		panel.add(disButton);
		panel.add(progressBar);

		add(panel, BorderLayout.PAGE_START);
		add(new JScrollPane(resultArea), BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	}

	private static void write2pnml() {

		int number = 0;

		for (PetriNet petriNet : deadlockPetriNets) {
			File file = new File("deadlock" + number + ".pnml");
			ArrayList<Dot> dots = new ArrayList<Dot>();
			for (PetriPlace place : petriNet.getPlaces()) {
				Dot dot = new Dot(1);
				dot.setId(place.getName());
				dots.add(dot);
			}
			for (PetriTransition transition : petriNet.getTransitions()) {
				Dot dot = new Dot(2);
				dot.setId(transition.getName());
				dots.add(dot);
			}
			for (PetriArc arc : petriNet.getEdges()) {
				Dot dot = new Dot(3);
				dot.setId(arc.getName());
				dot.setSource(arc.getSource().getName());
				dot.setTarget(arc.getTarget().getName());
				dots.add(dot);
			}
			PnmlUtil.write(dots, file, number);
			number++;
		}

		number = 0;

		for (PetriNet petriNet : synchronizationPetriNets) {
			File file = new File("synchronization" + number + ".pnml");
			ArrayList<Dot> dots = new ArrayList<Dot>();
			for (PetriPlace place : petriNet.getPlaces()) {
				Dot dot = new Dot(1);
				dot.setId(place.getName());
				dots.add(dot);
			}
			for (PetriTransition transition : petriNet.getTransitions()) {
				Dot dot = new Dot(2);
				dot.setId(transition.getName());
				dots.add(dot);
			}
			for (PetriArc arc : petriNet.getEdges()) {
				Dot dot = new Dot(3);
				dot.setId(arc.getName());
				dot.setSource(arc.getSource().getName());
				dot.setTarget(arc.getTarget().getName());
				dots.add(dot);
			}
			PnmlUtil.write(dots, file, number);
			number++;
		}

	}

	public static String showResult() {

		StringBuilder show = new StringBuilder();

		show.append("Deadlock:\n");
		if (deadlockPetriNets.size() == 0)
			show.append("Do not find Deadlock in log!\n");
		else {
			for (PetriNet petriNet : deadlockPetriNets)
				show.append(petriNet + "\n");
		}

		show.append("Synchronization:\n");
		if (synchronizationPetriNets.size() == 0)
			show.append("Do not find lack of Synchronization in log!\n");
		else {
			for (PetriNet petriNet : synchronizationPetriNets)
				show.append(petriNet + "\n");
		}

		return show.toString();

	}

	/**
	 * Invoked when the user presses the start button.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnButton) {
			resultArea.setText("");
			int intRetVal = fileChooser.showOpenDialog(this);
			if (intRetVal == JFileChooser.APPROVE_OPTION) {
				filenameTextField.setText(fileChooser.getSelectedFile()
						.getPath());
				filePath = fileChooser.getSelectedFile().getPath();
				if (!filePath.endsWith(".xes"))
					try {
						throw new Exception("Don't end with .xes");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,
								"Select wrong file!");
					}
			}
		}
		if (e.getSource() == disButton) {

			if (filePath == null) {
				JOptionPane.showMessageDialog(null, "Please select file!");
			} else {
				disButton.setEnabled(false);
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				// Instances of javax.swing.SwingWorker are not reusuable, so
				// we create new instances as needed.
				task = new Task();
				task.addPropertyChangeListener(this);
				task.execute();

			}
		}

	}

	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			resultArea.append(String.format("Completed %d%% of task.\n",
					task.getProgress()));
		}
	}

	/**
	 * Create the GUI and show it. As with all GUI code, this must run on the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("DSE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new Main();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}