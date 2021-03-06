package main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

public class Main {
	//Add to file parser:
	//Time Sig = 2/2
	//BPM = H200
	private static boolean DEBUG = true;
	
	public static void main(String[] args) {
		Channel main;
		ArrayList<Tone> music;
		String file;
		BufferedReader buf;
		FileReader reader;
		String line;
		int count = 0;
		int timeSig = 2;
		int noteSig = 2;
		int bpm = 200;
		double totalDuration = 0;
		double totalBytes = 0;
		double barDuration = ((double)  60 / bpm) * (timeSig / noteSig);

		if(args.length >= 1) file = args[0];
		else {
			System.out.println("No file was supplied!");
			return;
		}
		try {
			main = new Channel();
			reader = new FileReader(file);
			buf = new BufferedReader(reader);
			music = new ArrayList<Tone>();
			

			while((line = buf.readLine()) != null) {
				if(line.isEmpty()) continue;
				String[] notes = line.split(" ");
				for(String note: notes) {
					Tone audio = new Tone(note, main, barDuration , 100);
					totalDuration += audio.duration;
					totalBytes += audio.tone.length;
					music.add(audio);
				}
			}
			if(DEBUG)
				System.out.println("Finished Generating Music: "+totalDuration+"s :: "+totalBytes+" bytes");
			
			count = 0;
			while(count < music.size()) {
				AudioInputStream inpStream = music.get(count).getAudioInputStream();
				if(DEBUG)
					System.out.println("Playing: " + music.get(count));
				main.playSound(inpStream);
				count++;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Error: Note file was not found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error: I/O error has occured!");
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			System.out.println("Error: Can't play sounds, Dataline not found!");
			e.printStackTrace();
		} 
		if(DEBUG)
			System.out.println("Turning off sounds...");
	}

}
