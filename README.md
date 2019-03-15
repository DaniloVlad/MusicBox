# MusicBox

Creates a sound depending on sample rate, sample size, frequency and duration.  Handles byte-order if the sample size is larger than 8 bits.

# Usage

1) Using Eclipse just run it.
2) Using terminal

```
root@server:~$ javac src/*/*.java -d bin/
root@server:~$ cp data bin/data
root@server:~$ cd bin && java main.Main
```

3) Enjoy the mario overlord theme
4) [Sample Output](sampleOutput)

# Further Development:

1) Implement harmoics 
2) Seperate tones and notes
3) Add beamed notes
4) Add note timing variations (On sheet music, this is done with a dot)