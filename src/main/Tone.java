package main;

import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioInputStream;

public class Tone {
	//R is rest
	//Tuned to concert frequency
	public static enum StandardNote {R, C, C$, D, D$, E, F, F$, G, G$, A, A$, B}
	public static enum NoteType {W, H, Q, E}
	public Channel channel = null;
	public StandardNote noteMain = null;
	public NoteType noteType = null;
	public byte[] tone = null;
	public double freq;
	public double duration;
	public double period;
	public int volume;
	
	public Tone(Channel frm, double duration) {
		this.noteMain = StandardNote.R;
		this.channel = frm;
		this.freq = 20;
		this.duration = duration;
		this.period = 0;
		this.volume = 0;
		this.tone = new byte[(int) (2 * 2 * this.channel.sampleRate * this.duration)];
		for(int i = 0; i < this.tone.length; i++) 
			this.tone[i] = 0;
	}
	
	public Tone(String label, Channel frm, double barDuration, int volume) {
		// Note Types: Half-note: H, Quarter-note: Q, Whole-note: W, Eighth-note: E
		// Octive sign: +/- to indicate above or below concert pitch
		// Octive: [1, 4] prepended with + for up - for down
		// Concert pitch: no octave-sign or octive ie: 2-3 chars HA HA$
		// Note Label: The letter representing the note (WA:whole A, R: rest, HR: half-rest)
		// label[0] = <note-type>, label[1] = <octive-sign>, 
		// label[2] = <octive-value> label[3] = <note-label> [min, max] length = [1, 2] characters
		this.channel = frm;
		this.volume = this.channel.maxVolume * (volume / 100);
		this.noteType = NoteType.valueOf(Character.toString(label.charAt(0)));
		//Whole duration = bar/2^0, Half = bar/2^1 ...
		this.duration = barDuration / Math.pow(2, this.noteType.ordinal());
		if(label.length() < 4) {
			// Concert pitch
			this.noteMain = StandardNote.valueOf(label.substring(1));
			this.freq = noteFreq(this.noteMain, 1);
		}
		else {
			// Different octave
			this.noteMain = StandardNote.valueOf(label.substring(3));
			double octaveValue = Character.getNumericValue(label.charAt(2));
			octaveValue++;
			if(label.charAt(1) == '+') 
				this.freq = noteFreq(this.noteMain, octaveValue);
			else
				this.freq = noteFreq(this.noteMain, 1/octaveValue);
		}
		this.period = (this.channel.sampleRate) / this.freq;
		this.tone = new byte[(int) (2 * 2 * this.channel.sampleRate * this.duration)];
		this.generateTone();
	}
	//Constructor for moving to factory method
	public Tone(StandardNote note, int octive, Channel frm, double duration, int volume) {
		this.noteMain = note;
		this.channel = frm;
		this.freq = noteFreq(note, octive);
		this.volume = (int) (this.channel.maxVolume * ((double) volume / 100));
		this.duration = duration;
		this.tone = new byte[(int) (2 * this.channel.sampleRate * this.duration)]; //16-bit 2 Channels
		this.period = (double) (this.channel.sampleRate) / this.freq ;
		this.generateTone();
		
	}
	
	
	private void generateTone() {
		for(double i = 0; i < this.tone.length - 1; i++) {
			double angle = 2 * Math.PI * i / this.period;
			double lrgData = Math.sin(angle) * this.volume;
			if(this.channel.sampleSize / 8 > 1) {
				if(this.channel.format.isBigEndian()) {
					//big-end first byte
					this.tone[(int) i++] = (byte) (((short) lrgData & 0xff00) >> 8);	
					this.tone[(int) i] = (byte) ((short) lrgData & 0x00ff) ;
				}
				else {
					//little endian
					this.tone[(int) i++] = (byte) ((short) lrgData & 0x00ff) ;
					this.tone[(int) i] = (byte) (((short) lrgData & 0xff00) >> 8);	
				}
			}
			else {
				this.tone[(int) i] = (byte) lrgData;
			}
			
		
		}
		
	}

	public static double noteFreq(StandardNote note, double octive) {
		if(note.equals(StandardNote.R)) return 0d;
		double power = (double) (note.ordinal() - 10)/12d;
		return Math.pow(2, power) * 440d * (octive);
	}
	
	//returns the tone array
	public byte[] getToneArray() {
		return this.tone;
	}
	
	//returns audio input stream generated from the byte array
	public AudioInputStream getAudioInputStream() {
		return new AudioInputStream(
				new ByteArrayInputStream(this.tone),
				this.channel.format,
				this.tone.length / 2
				);
	}
	
	//to string
	public String toString() {
		return "Tone(Note, Frequency, Duration) :: (" + noteMain + ", "+freq+", " + this.duration + ")";
	}

}
