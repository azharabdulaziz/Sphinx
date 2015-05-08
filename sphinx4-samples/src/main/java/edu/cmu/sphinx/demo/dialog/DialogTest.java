package edu.cmu.sphinx.demo.dialog;

/*
 * Copyright 2013 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;


public class DialogTest {

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
	
	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.out.println("WWWWWWWWWWhat");
		}
	}


	public static void main(String[] args) throws Exception {
		//Configuration configuration = new Configuration();
		configuration.setAcousticModelPath(ACOUSTIC_MODEL);
		configuration.setDictionaryPath(DICTIONARY_PATH);
		configuration.setLanguageModelPath(LANGUAGE_MODEL);

		//		LiveSpeechRecognizer lmRecognizer =
		//				new LiveSpeechRecognizer(configuration);

		final Scanner s = new Scanner(System.in);

		Thread t1 = new Thread(){
			public void run() {
				try{
					LiveSpeechRecognizer lmRecognizer =
							new LiveSpeechRecognizer(configuration);
					lmRecognizer.startRecognition(true);
					while(true){
						String utterance = lmRecognizer.getResult().getHypothesis();
						if(utterance.equals("exit")) break;
						//System.out.println(utterance);
						what = utterance;
						System.out.println(what);
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
					System.out.println("woohoo");
				}
			}
		};

		//		lmRecognizer.startRecognition(true);
		//		while(true){
		//			String utterance = lmRecognizer.getResult().getHypothesis();
		//			if(utterance.equals("exit")) break;
		//			System.out.println(utterance);
		//		}
		//		lmRecognizer.stopRecognition();
		t1.start();
		t2.start();
	}
}

