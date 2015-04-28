package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class MainFrame extends JFrame {

	private static JTextField commandLine;
	private static JTextArea outputArea;
	private static JPanel mainPanel;
	private static JSplitPane splitPane;
	private static JScrollPane outputAreaScroll;

	public MainFrame() {
		super("Chess engine");

		initialiseComponents();
		addListener();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 800);
		setResizable(false);
		setVisible(true);
	}

	public void initialiseComponents() {

		commandLine = new JTextField();
		outputArea = new JTextArea();
		outputAreaScroll = new JScrollPane(outputArea);

		mainPanel = new JPanel();

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, outputAreaScroll,
				commandLine);
		splitPane.setDividerLocation(435);
		splitPane.setEnabled(false);
		outputArea.setEditable(false);
		outputArea.setFont(new Font("monospaced", Font.PLAIN, 12));
		mainPanel.add(splitPane);

		DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		add(splitPane);
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				commandLine.requestFocus();
			}
		});

	}

	public void addListener() {
		commandLine.addKeyListener(new customKeyListener());
	}

	public static void clearCommandLine() {
		commandLine.setText("");
	}

	public static void clearOutputArea() {
		outputArea.setText("");
	}

	public static String getCommandLineText() {
		return commandLine.getText();
	}

	public static void appendText(String input) {
		outputArea.append("\n" + input);
		outputArea.revalidate();
	}

	public static void toggleCommandLine() {

	}

	public static void disableCmd() {
		commandLine.setEnabled(false);
	}

	public static void enableCmd() {
		commandLine.setEnabled(true);
	}

}
