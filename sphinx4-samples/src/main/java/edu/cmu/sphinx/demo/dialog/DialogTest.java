package edu.cmu.sphinx.demo.dialog;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class DialogTest extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private ArrayList<String> wordlist = new ArrayList<String>();
	private List<String> wordHistory = new ArrayList<String>();
	
	private static final String ACOUSTIC_MODEL =
			"resource:/edu/cmu/sphinx/models/en-us/en-us";
	//    private static final String DICTIONARY_PATH =
	//        "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
	private static final String DICTIONARY_PATH =
			"resource:/edu/cmu/sphinx/models/en-us/6073.dic";
	//private static final String GRAMMAR_PATH =
	//    "resource:/edu/cmu/sphinx/demo/dialog/";
	private static final String LANGUAGE_MODEL =
			"resource:/edu/cmu/sphinx/models/en-us/model.dmp";

	private static Configuration configuration = new Configuration();
	
	private static String what = "";
	private JList dictList;
	
	
	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.out.println("WWWWWWWWWWhat");
		}
	}
	
	private void readFile(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
	 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		while ((line = br.readLine()) != null) {
			//System.out.println(line);
			wordlist.add(line);
		}
	 
		dictList.setListData(wordlist.toArray());
		br.close();
	}
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DialogTest frame = new DialogTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DialogTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(271, 6, 134, 28);
		panel.add(textField);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 161, 256);
		panel.add(scrollPane);
		
		dictList = new JList();
		scrollPane.setViewportView(dictList);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(271, 53, 151, 199);
		panel.add(scrollPane_1);
		
		final JList spokenList = new JList();
		scrollPane_1.setViewportView(spokenList);
		try {
			readFile(new File("../sphinx4-data/src/main/resources/edu/cmu/sphinx/models/en-us/6073.dic"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
		//Configuration configuration = new Configuration();
		configuration.setAcousticModelPath(ACOUSTIC_MODEL);
		configuration.setDictionaryPath(DICTIONARY_PATH);
		configuration.setLanguageModelPath(LANGUAGE_MODEL);
		
		Thread t1 = new Thread(){
			public void run() {
				try{
					LiveSpeechRecognizer lmRecognizer =
							new LiveSpeechRecognizer(configuration);
					lmRecognizer.startRecognition(true);
					while(true){
						String utterance = lmRecognizer.getResult().getHypothesis();
						if(utterance.equals("exit")){
							System.out.println("EXITED");
							break;
						}
						//System.out.println(utterance);
						what = utterance;
						wordHistory.add(0, what);
						spokenList.setListData(wordHistory.toArray());
						//System.out.println(what);
						textField.setText(what);
					}
					lmRecognizer.stopRecognition();
				} catch(Exception e){}
			}
		};
		Thread t2 = new Thread(){
			public void run(){
				while(true){
					while(!what.equals("yes")){
						DialogTest.sleep(500);
					}
//					System.out.println("woohoo");
					textField.setText("woohoo" + what);
				}
			}
		};
		
		t1.start();
		t2.start();
	}
}
