import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.UIManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JTextPane;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class Med extends JFrame {
            // needed variable
	static boolean empty = true;
	static boolean emptyPart2 = true;
	private JPanel contentPane;
	static JPanel panel = new JPanel();
	static JPanel panel_1 = new JPanel();
	static JLabel lblNewLabel_2_time = new JLabel();
	static DefaultListModel<String> dl = new DefaultListModel<>();
	static JList list = new JList(dl);
	static JLabel run = new JLabel();
	static JLabel runPart2 = new JLabel();
	static JTextField txtAdas = new JTextField();
	static ArrayList<String> dict = new ArrayList<String>();
	static final JLabel exitLabel = new JLabel();
	static JTextField word1TextField;
	static JTextField word2TextField;
	static JTextPane textPane = new JTextPane();
	static final JTextPane textPaneInfo = new JTextPane();
	static final JLabel lblNewLabel_2 = new JLabel("TIME : ");
	static final JLabel lblNewLabelTimePart2 = new JLabel();
	

	public static class Part2 {

		private StringBuilder steps;
		private int row;
		private int col;
		private int M[][];
		private int m = 0;
		private int n = 0;
		private String[][] table;
		private String word1;
		private String word2;
		private String tb;
		private ArrayList<Integer> indexes = new ArrayList<Integer>();
		private StringBuilder operations = new StringBuilder();

		public String getTbl() {
			return tb;
		}

		public int min(int x, int y, int z) {
			if (x <= y && x <= z) {
				return x;// insert
			}
			if (y <= x && y <= z) {
				return y;// remove
			} else {
				return z;// replace
			}
		}

		public int  Steps(int value, int x, int y, int z) {
			if (row > 0 && col > 0 && word1.charAt(row - 1) == word2.charAt(col - 1)) {
				col--;
				row--;
				// No operation
				indexes.add(0, col);
				indexes.add(0, row);
				operations.insert(0, " ");
				return M[row][col];

			} else {
				if (x <= y && x <= z) {
					if (x != value) {
						steps.insert(0, "1 Insert\n");
						operations.insert(0, " ");
						indexes.add(0, col - 1);
						indexes.add(0, row);
					}
					col--;
					return x;
				}
				if (y <= x && y <= z) {
					if (y != value) {
						steps.insert(0, "1 Delete\n");
						operations.insert(0, " ");
						indexes.add(0, col);
						indexes.add(0, row - 1);
					}
					row--;

					return y;
				} else {
					if (z != value) {
						steps.insert(0, "1 Substitution\n");
						operations.insert(0, " ");
						indexes.add(0, col - 1);
						indexes.add(0, row - 1);
					}
					col--;
					row--;

					return z;
				}
			}

		}

		public int editDistDP(String str1, String str2, int m, int n) {
			steps = new StringBuilder();
			this.m = m;
			this.n = n;
			table = new String[m + 2][n + 2];
			word1 = str1;
			word2 = str2;
			// Create a table to insert result.
			M = new int[m + 1][n + 1];
			for (int i = 0; i <= m; i++) {
				for (int j = 0; j <= n; j++) {
					// If the first string is empty, add all the characters of the second string
					if (i == 0)
						M[i][j] = j;

					// If the second string is empty, remove all its characters
					else if (j == 0)
						M[i][j] = i;

					// If the last characters are the same, ignore it and repeat for the remaining
					// string
					else if (str1.charAt(i - 1) == str2.charAt(j - 1))
						M[i][j] = M[i - 1][j - 1];

					// If the last character is different, find the minimum of all possibilities.
					else
						M[i][j] = 1 + min(M[i][j - 1], // Add
								M[i - 1][j], // Delete
								M[i - 1][j - 1]); // Delete

				}

			}
			return M[m][n];
		}

		public void FindOp() {
			steps = new StringBuilder();
			int value = M[m][n];
			row = m;
			col = n;

			while (row != 0 || col != 0) {
				if (row > 0 && col > 0) {
					value = Steps(value,M[row][col - 1], // Insert
							M[row - 1][col], // Remove
							M[row - 1][col - 1]);// Replace
				} else if (row > 0 && col == 0) {
					value = Steps(value, 100, // Insert
							M[row - 1][col], // Remove
							100);// Replace
				} else if (row == 0 && col > 0) {
					value = Steps(value, M[row][col - 1], // Insert
							100, // Remove
							100);// Replace
				}

			}

		}

		public String getSteps() {

			return steps.toString();
		}

		public void TableForScr() {

			table[0][0] = "-";
			table[0][1] = "-";
			table[1][0] = "-";

			// for word 1
			for (int i = 0; i < word1.length(); i++) {
				table[i + 2][0] = Character.toString(word1.charAt(i));
			}

			// for word 2
			for (int i = 0; i < word2.length(); i++) {
				table[0][i + 2] = Character.toString(word2.charAt(i));
			}
			int count = indexes.size();
			int count2 = operations.length();
			for (int i = word1.length() + 1; i >= 1; i--) {

				for (int j = word2.length() + 1; j >= 1; j--) {

					if (i - 1 == indexes.get(count - 2) && j - 1 == indexes.get(count - 1)) {
						table[i][j] = Integer.toString(M[i - 1][j - 1]) + " " + operations.charAt(count2 - 1);
						count -= 2;
						count2--;
					} else {
						table[i][j] = Integer.toString(M[i - 1][j - 1]);
					}

				}

			}

			StringBuilder str = new StringBuilder();
			for (int i = 0; i < word1.length() + 2; i++) {

				for (int j = 0; j < word2.length() + 2; j++) {

					if (j != word2.length() + 1) {
						str.append(table[i][j] + "\t");
					} else {
						str.append(table[i][j]);
					}

				}

				str.append("\n\n");

			}
			tb = str.toString();

		}

	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Med frame = new Med();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		txtAdas.setFont(new Font("Serif", Font.CENTER_BASELINE, 19));
		ImageIcon iconRun3 = new ImageIcon("img/icn6.png");
		ImageIcon iconRun4 = new ImageIcon("img/icn7.png");
		run.setIcon(iconRun3);
		run.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

				run.setIcon(iconRun3);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

				run.setIcon(iconRun4);
			}

			@Override
			public void mouseClicked(MouseEvent e) {

				if (txtAdas.getText().toLowerCase().equals("")) {
					txtAdas.setBackground(Color.MAGENTA);
					txtAdas.setBackground(Color.RED);

					JOptionPane.showMessageDialog(panel, "It can not be empty !", "Warning",
							JOptionPane.WARNING_MESSAGE);
					txtAdas.setBackground(Color.white);
					txtAdas.setFont(new Font("Serif", Font.ITALIC, 19));
					empty = false;
				}
				if (empty) {

					txtAdas.setText(txtAdas.getText().toLowerCase());
					HashMap<String, Integer> words = new HashMap<String, Integer>();

					Part2 ed = new Part2();
					long startTime = System.nanoTime();

					for (int i = 0; i < dict.size(); i++) {
						int distance = ed.editDistDP(txtAdas.getText().toLowerCase(), dict.get(i),
								txtAdas.getText().length(), dict.get(i).length());
						words.put(dict.get(i), distance);
					}

					Set<Entry<String, Integer>> entrySet = words.entrySet();
					ArrayList<Entry<String, Integer>> listOfEntry = new ArrayList<>(entrySet);

					listOfEntry.sort(new Comparator<Entry<String, Integer>>() {
						@Override
						public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
							return o1.getValue() - o2.getValue();
						}
					});

					long endTime = System.nanoTime();
					lblNewLabel_2_time.setText((endTime - startTime) / 1000000 + " MS");

					// record texts to sgow on frame
					dl.addElement(listOfEntry.get(0).getValue().toString() + " " + listOfEntry.get(0).getKey());
					dl.addElement(listOfEntry.get(1).getValue().toString() + " " + listOfEntry.get(1).getKey());
					dl.addElement(listOfEntry.get(2).getValue().toString() + " " + listOfEntry.get(2).getKey());
					dl.addElement(listOfEntry.get(3).getValue().toString() + " " + listOfEntry.get(3).getKey());
					dl.addElement(listOfEntry.get(4).getValue().toString() + " " + listOfEntry.get(4).getKey());
					dl.addElement("---------" + " " + "----------");
					

				}
			}
		});

		ImageIcon ArrowIcon = new ImageIcon("img/rightArrow.png");
		ImageIcon ArrowIcon2 = new ImageIcon("img/rightArrow2.png");
		runPart2.setIcon(ArrowIcon);
		runPart2.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				runPart2.setIcon(ArrowIcon);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				runPart2.setIcon(ArrowIcon2);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

				String word1 = word1TextField.getText().toLowerCase();
				String word2 = word2TextField.getText().toLowerCase();
				int lengtWord1 = word1.length();
				int lengtWord2 = word2.length();

				if (word1.equals("") || word2.equals("")) {

					word1TextField.setBackground(Color.MAGENTA);
					word1TextField.setBackground(Color.RED);
					word2TextField.setBackground(Color.MAGENTA);
					word2TextField.setBackground(Color.RED);

					JOptionPane.showMessageDialog(panel, "It can not be empty !", "Warning",
							JOptionPane.WARNING_MESSAGE);
					word1TextField.setBackground(Color.white);
					word1TextField.setBackground(Color.white);
					word2TextField.setBackground(Color.white);
					word2TextField.setBackground(Color.white);
					txtAdas.setFont(new Font("Serif", Font.ITALIC, 19));
					emptyPart2 = false;
				}
				if (emptyPart2) {
					word1TextField.setText(word1TextField.getText().toLowerCase());
					word2TextField.setText(word2TextField.getText().toLowerCase());
					Part2 ed = new Part2();
					long startTimePart2 = System.nanoTime();
					int runPart2 = ed.editDistDP(word1, word2, lengtWord1, lengtWord2);

					ed.FindOp();
					ed.TableForScr();

					textPane.setText(ed.getTbl());
					textPaneInfo.setText(ed.getSteps() + "\nEdit Distance: " + runPart2);

					long endTimePart2 = System.nanoTime();
					lblNewLabelTimePart2.setText((endTimePart2 - startTimePart2) / 1000000 + " MS");
				}

			}
		});

		ImageIcon ex1 = new ImageIcon("img/exit00.png");
		ImageIcon ex2 = new ImageIcon("img/exit1.png");
		exitLabel.setIcon(ex1);
		exitLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				exitLabel.setIcon(ex1);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				exitLabel.setIcon(ex2);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

				int answer = JOptionPane.showConfirmDialog(null, "Are Sure To Exit?", "WARNING",
						JOptionPane.YES_NO_OPTION);

				if (answer == 0) {

					JOptionPane.showMessageDialog(null, "SYSTEM WILL SHUT DOWN.");

					System.exit(0);

				} else {
					JOptionPane.showMessageDialog(null, "SHUTDOWN CANCELED");
				}

			}

		});

		File f = new File("vocabulary_tr.txt"); // read file
		Scanner scanner;
		try {
			scanner = new Scanner(f, "ISO-8859-9");
			while (scanner.hasNextLine()) {
				dict.add(scanner.nextLine());

			}

			scanner.close();

		} catch (FileNotFoundException e1) {
			System.out.println("File Not Found!!!");
			// e1.printStackTrace();
		}

	}

	public Med() {// Create needed frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("NLP 2 - Hattap Tan 2016510102");
		setBounds(100, 100, 900, 651);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 0, 153));
		contentPane.setBorder(new EmptyBorder(8, 5, 5, 9));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);

		panel.setForeground(Color.WHITE);
		panel.setBackground(new Color(153, 0, 153));
		panel.setBounds(0, 0, 457, 610);

		contentPane.add(panel);
		TitledBorder titledBorder = BorderFactory.createTitledBorder("PART1");
		titledBorder.setTitleColor(Color.WHITE);
		panel.setBorder(
				new TitledBorder(null, "P A R T - 2", TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE));
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("PLEASE ENTER A WORD");
		lblNewLabel.setBounds(96, 56, 243, 16);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBackground(Color.WHITE);
		exitLabel.setBounds(388, 512, 105, 92);

		panel.add(exitLabel);

		JLabel lblNewLabel_1 = new JLabel("TIME : ");
		lblNewLabel_1.setBounds(96, 498, 92, 30);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_1.setForeground(Color.WHITE);
		list.setBounds(97, 215, 214, 227);
		panel.add(list);
		list.setForeground(Color.WHITE);
		list.setBackground(new Color(255, 102, 51));
		txtAdas.setBounds(97, 102, 208, 38);
		panel.add(txtAdas);
		txtAdas.setColumns(10);
		run.setBounds(87, 152, 224, 51);
		panel.add(run);
		run.setForeground(Color.WHITE);
		run.setBackground(Color.RED);
		run.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2_time.setBounds(167, 498, 172, 30);
		panel.add(lblNewLabel_2_time);

		lblNewLabel_2_time.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2_time.setForeground(Color.WHITE);
		
		 

		
		 
	      
	      
		panel_1.setBackground(new Color(153, 0, 153));
		panel_1.setBounds(455, 0, 427, 610);
		contentPane.add(panel_1);
		TitledBorder titledBorder1 = BorderFactory.createTitledBorder("PART2");
		titledBorder.setTitleColor(Color.WHITE);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "P A R T 2", TitledBorder.CENTER,
				TitledBorder.TOP, null, new Color(255, 255, 255)));
		panel_1.setLayout(null);

		word1TextField = new JTextField();
		word1TextField.setFont(new Font("SansSerif", Font.BOLD, 16));
		word1TextField.setBounds(19, 48, 143, 38);
		panel_1.add(word1TextField);
		word1TextField.setColumns(10);

		word2TextField = new JTextField();
		word2TextField.setFont(new Font("SansSerif", Font.BOLD, 16));
		word2TextField.setBounds(251, 48, 143, 38);
		panel_1.add(word2TextField);
		word2TextField.setColumns(10);

		runPart2.setBounds(177, 31, 73, 71);
		panel_1.add(runPart2);

		JLabel lblNewLabel_3 = new JLabel("WORD - 1");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setBounds(44, 20, 106, 16);
		panel_1.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("WORD - 2");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_4.setForeground(Color.WHITE);
		lblNewLabel_4.setBounds(275, 20, 92, 16);
		panel_1.add(lblNewLabel_4);

		textPane.setForeground(new Color(255, 255, 255));
		textPane.setBackground(new Color(255, 102, 0));
		textPane.setBounds(19, 249, 402, 299);
		panel_1.add(textPane);
		textPaneInfo.setBounds(19, 114, 402, 126);

		panel_1.add(textPaneInfo);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setBounds(130, 572, 92, 20);

		panel_1.add(lblNewLabel_2);
		lblNewLabelTimePart2.setForeground(new Color(255, 255, 255));
		lblNewLabelTimePart2.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabelTimePart2.setBounds(196, 572, 159, 20);

		panel_1.add(lblNewLabelTimePart2);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
