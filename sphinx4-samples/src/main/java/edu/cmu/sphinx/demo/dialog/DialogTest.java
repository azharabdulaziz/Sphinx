package edu.cmu.sphinx.demo.dialog;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

import javax.swing.JList;

public class DialogTest extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private ArrayList<String> wordlist = new ArrayList<String>();
	
	private static final String ACOUSTIC_MODEL =
			"resource:/edu/cmu/sphinx/models/en-us/en-us";
	//    private static final String DICTIONARY_PATH =
	//        "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
	private static final String DICTIONARY_PATH =
			"resource:/edu/cmu/sphinx/models/en-us/test.dict";
	//private static final String GRAMMAR_PATH =
	//    "resource:/edu/cmu/sphinx/demo/dialog/";
	private static final String LANGUAGE_MODEL =
			"resource:/edu/cmu/sphinx/models/en-us/en-us.lm.dmp";

	private static Configuration configuration = new Configuration();
	
	private static String what = "";
	private JList list;
	
	
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
	 
		list.setListData(wordlist.toArray());
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
		
		list = new JList();
		list.setBounds(6, 6, 161, 256);
		panel.add(list);
		try {
			readFile(new File("/Users/joefiala/Documents/workspace/sphinx4-5prealpha-src/sphinx4-data/src/main/resources/edu/cmu/sphinx/models/en-us/test.dict"));
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
