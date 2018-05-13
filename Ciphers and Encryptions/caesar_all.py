import sys

def all_shifts(text):
	shifts = []
	for i in range(1, 27):
		word = ""
		for j in range(0, len(text)):
			if text[j:j+1].isalpha() == True:
				newASCII = ord(text[j:j+1]) + i
				if newASCII >= 122:
					newASCII = 96 + newASCII - 122
				word = word + chr(newASCII)
			else:
				word = word + text[j:j+1]
		shifts.append(word)

	return shifts

def autosolve(shifts):
	#Automatically get Correct Shift
	words = []
	with open('dictionary.txt','r') as f:
		for line in f:
			for word in line.split():
				words.append(word)

	for i in range(0, len(shifts)):
		splitWords = shifts[i].split()
		count = 0
		for j in range(0, len(splitWords)):
			if splitWords[j] in words:
				count = count + 1
		# If 50% of text is actual words, then we likely have the correct shift
		if count > len(splitWords) / 2:
			print("Decoded Message: " + shifts[i])

text = sys.argv[1]
text = text.lower()
shifts = all_shifts(text)
for i in range(len(shifts)):
	print("{:02}".format(i) + ": " + shifts[i])

autosolve(shifts)
