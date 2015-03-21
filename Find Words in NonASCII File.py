#Read in only letters from non-UTF-8 character file
nonascii = bytearray(range(0x80, 0x100))
possibleStrings = []
consecutive = 0
with open('randomGenProb1.txt','rb') as infile:
    for line in infile: # b'\n'-separated lines (Linux, OSX, Windows)
    	line = line.translate(None, nonascii)
    	data = line
        for i in range(len(data)):
			c = data[i]
			# The possible characters in the file you are searching that may compose words
			if c.lower() in 'abcdefghijklmnopqrstuvwxyz ':
				consecutive += 1
			else:
				if consecutive > 11: #Chose 11 to be number of consecutive letters that is deemed 'significant'
					possibleStrings.append(data[i - consecutive:i])
				consecutive = 0

#Find which string has actual words
words = []
with open('dictionary.txt','r') as f:
    for line in f:
        for word in line.split():
           words.append(word)

for i in range(0, len(possibleStrings)):
	splitWords = possibleStrings[i].split()
	count = 0
	for j in range(0, len(splitWords)):
		if splitWords[j] in words:
			count = count + 1
	# If 50% of text is actual words, then we likely have the correct shift
	if count > len(splitWords) / 2:
		print(possibleStrings[i])