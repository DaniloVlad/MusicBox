package main;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

public class Channel {
	public Clip clip = null;
	public AudioFormat format = null;
	public int channels;
	public int sampleSize;
	public int maxVolume;
	public double sampleRate;
	
	//Note the endianism only matters if sampleSize is larger than 1 byte
	public Channel() throws LineUnavailableException {
		channels = 2;
		sampleSize = 16; //16-bit samples
		sampleRate = 16 * 1024; //16 KHz sampling
		maxVolume = (int) Math.pow(2, sampleSize - 1) - 1;
		format = new AudioFormat( 
				(int) sampleRate,
				sampleSize,
				channels,
				true, //Big endian 
				true
				);
		DataLine.Info from = new DataLine.Info(Clip.class, format);
		clip = (Clip) AudioSystem.getLine(from);
		
	}
	
	public Channel(int sampleSize, double sampleRate, int channels, boolean bigEndian) throws LineUnavailableException {
		//We make signed values the default
		this.sampleSize = sampleSize;
		this.sampleRate = sampleRate;
		this.channels = channels;
		this.format = new AudioFormat((int) sampleRate, sampleSize, channels, true, bigEndian);
		DataLine.Info from = new DataLine.Info(Clip.class, this.format);
		this.clip = (Clip) AudioSystem.getLine(from);
	}
	
	
	public int playSound(AudioInputStream sound) {
		if(this.clip == null) return -1;
		try {
			//prepare stream
			this.clip.open(sound);
			//start stream
			this.clip.start();
			//make sure all bytes are read
			this.clip.drain();
			//close 
			this.clip.close();
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
}
