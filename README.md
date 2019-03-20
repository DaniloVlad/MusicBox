# MusicBox

Creates a sound depending on sample rate, sample size, frequency and duration.  Handles byte-order if the sample size is larger than 8 bits.

# Usage

1) Using Eclipse edit the run config and add the name of the file to the args. Then run it.
2) Using terminal:

    i) Set the priviledges for the scripts

    ```sh-session
    root@server:~$ chmod +x run compile
    ```

    ii) Compile the source file

    ```sh-session
    root@server:~$ ./compile
    ```

    iii) Run the program

    ```sh-session
    root@server:~$ ./run 
    Usage: ./run <file-name>
    Where <file-name> is present in /home/danilo/Documents/javaMusic/MusicBox
    root@server:~$ ./run mario
    Finished Generating Music: 7.8125 s
    Playing: Tone(Note, Frequency, Duration) :: (E, 659.2551138257398, 0.0625)
    Playing: Tone(Note, Frequency, Duration) :: (E, 659.2551138257398, 0.0625)
    ```

3) Enjoy the mario overlord theme
4) [Sample Output](sampleOutput)

# Writing your own note sheet

1) First letter is the note beat duration: Whole note: W, Half note: H, Quarter note: Q, Eigth note: E
2) This is followd by either a note [A-G] or an octive modifier [+/-][1-4] such that -1A means A3, A means A4, +1A means A5,
3) Rests are treated like notes without octive modifiers. Half Rest: HR, quarter rest: QR...
4) Put it together: Q+1E Q+1E QC QR

# Further Development:

1) Implement harmoics
2) Seperate tones and notes
3) Add beamed notes
4) Add note timing variations (On sheet music, this is done with a dot) 
5) Implement Time signatures and BPM