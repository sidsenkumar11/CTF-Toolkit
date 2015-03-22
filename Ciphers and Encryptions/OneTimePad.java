import java.util.Scanner;

public class OneTimePad {

	/**
	 * Note that this program will only work with ciphertexts and plaintexts consisting of letters.
	 * Ex. String cipherText = "FFXRPQFURTJBSOGYTSRYFXINSIHEGOKZBEPICZWLN"
	 *     String plainText = "la flago estas turno en malnova problemo"
	 *++++++++++++++++++++++++++9
	 */
/*
		String e = "WXYTTIGRBCCTFAUKKRZPEGGNUDFENDEIORMEJIKSTZLGJJYWJRTBUORFTWQQOAGPXS";
		String h = "ZOTVYIZUZBEWAEUZAKKNYMQNYMZMOBLSSIPUOGVNMEVCVITGGAWIFPSZPZPOGUY";
		String f = "UONEAWYXXWMFETCYKSLUZJZLOOZQVUEOPZYDLCEOTPTYXSUQUVAVCXMAFTNSJT";
		String a = "WQJDYQBNJHYTFEQXIGCJQOUHYJFMPFOXBHILFRASZGFBROONSAUINJSEITFT";
		String d = "KIFCACEREUCTFIUHRASFXGODAVOMFJOXPAMAJYARSKVOSSXLHKXOLFJVSYK";
		String g = "SKYTYZRCFSQUMSVATENSOGVKWMZFGBOMTICGPRWJNIAICQRDBINBIO";
		String i = "RXEPYLNYICKFZAFUEKSFDSVSSHVXDLWJPGKUMWYNCAEFJRKNLFAQI";
		String c = "OPUTYIACFSQUMSNGPDDOOONUONOMBJPEJCRYSBSBHWAZJVRLFXEW";
		String b = "HHHSLAGJJOLHXANONYUFVUHHYNJOMFEXBSCLBBVZKHXIWZUH";
		String flag = "FFXRPQFURTJBSOGYTSRYFXINSIHEGOKZBEPICZWLN";
**/

	public static String findPlainText(String key, String cipherText) {
			
			String[] cipherTexts = {cipherText};
			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			String decipheredText = "";
			int keyIndex = 0;

			for (String x : cipherTexts) {
				decipheredText = "";
				for (int index = 0; index < x.toCharArray().length; index++) {
					if (keyIndex == key.length())
						keyIndex = 0;
					int num = alphabet.indexOf(x.toCharArray()[index]);
					int numToSubtract = alphabet.indexOf(key.substring(
							keyIndex, keyIndex + 1));
					int indexToAdd = Math.abs(num - numToSubtract) % 26;
					if (numToSubtract > num)
						indexToAdd = 26 - indexToAdd;
					decipheredText += alphabet.substring(indexToAdd,
							indexToAdd + 1);
					keyIndex++;
				}
			}
		return decipheredText;
	}

	public static String findKey(String plainText, String cipherText) {

			String[] cipherTexts = {cipherText};
			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			String key = "";
			int plainTextIndex = 0;

			for (String x : cipherTexts) {
				plainTextIndex = 0;
				key = "";
				for (int index = 0; index < x.toCharArray().length; index++) {
					if (plainTextIndex >= plainText.length())
						key += "-";
					else {
						String plainTextLetter = plainText.substring(
								plainTextIndex, plainTextIndex + 1);
						plainTextIndex++;
						String cipherLetter = "" + x.toCharArray()[index];
						int keyLetterIndex = -1;
						if (alphabet.indexOf(plainTextLetter) > alphabet
								.indexOf(cipherLetter))
							keyLetterIndex = 26
									- alphabet.indexOf(plainTextLetter)
									+ alphabet.indexOf(cipherLetter);
						else
							keyLetterIndex = alphabet.indexOf(cipherLetter)
									- alphabet.indexOf(plainTextLetter);
						key += alphabet.substring(keyLetterIndex, keyLetterIndex + 1);
					
					}
			
			}
		}
		return key;
	}

	public static String specialXOR(String one, String two) {
		String shorter = "";
		String longer = "";
		if (one.length() < two.length()) {
			shorter = one;
			longer = two;
		} else {
			shorter = two;
			longer = one;
		}

		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String XORD = "";
		for (int i = 0; i < shorter.length(); i++) {
			int shorterIndex = alphabet.indexOf(shorter.charAt(i));
			int longerIndex = alphabet.indexOf(longer.charAt(i));
			int newIndex = (shorterIndex + longerIndex) % alphabet.length();
			XORD += alphabet.charAt(newIndex);
		}
		return XORD;
	}

	public static void main(String[] args) {

		// Use if you think you know the Key and want to see what the Plaintext
		// is
		// findPlainText();

		// Use if you think you know the Plaintext and want to see what the Key
		// is
		// findKey();
		Scanner scan = new Scanner(System.in);
		System.out.println("-------------------------------------");
		System.out.println("Welcome to my Crib Dragging Software!");
		System.out.println("-------------------------------------");
		System.out.println("Enter cipherText one");
		String cipherTextOne = scan.nextLine().toUpperCase();
		System.out.println("Enter cipherText two");
		String cipherTextTwo = scan.nextLine().toUpperCase();

		String xordCipherText = specialXOR(cipherTextOne, cipherTextTwo);

		String[] figuredPlaintext = new String[xordCipherText.length()];
		for (int i = 0; i < figuredPlaintext.length; i++) {
			figuredPlaintext[i] = "_";
		}

		String[] figuredKey = new String[xordCipherText.length()];
		for (int i = 0; i < figuredKey.length; i++) {
			figuredKey[i] = "_";
		}

		String input = "";
		while (!input.equalsIgnoreCase("end")) {
			if (!input.equalsIgnoreCase("end")) {
				System.out.println("Your message is currently:");
				String figuredPlain = "";
				for (String x : figuredPlaintext) {
					figuredPlain += x;
				}
				String figuredK = "";
				for (String x : figuredKey) {
					figuredK += x;
				}
				System.out.println(figuredPlain);
				System.out.println("Your key is currently:");
				System.out.println(figuredK);
				System.out.print("Please enter your crib: ");
				String crib = scan.next();
				System.out.println("Is this crib part of the message or the key?");
				input = scan.next();
				while (!(input.equalsIgnoreCase("message") || input.equalsIgnoreCase("key"))) {
					System.out.println("Please enter either \"message\" or \"key\".");
					input = scan.nextLine();
				}

				boolean message = false;
				if (input.equalsIgnoreCase("message")) {
					for (int i = 0; i < figuredPlaintext.length - crib.length(); i++) {
						System.out.println(i + ": " + findKey(crib, xordCipherText.substring(i, i + crib.length())));
						message = true;
					}
				} else if (input.equalsIgnoreCase("key")) {
					for (int i = 0; i < figuredKey.length - crib.length(); i++) {
						System.out.println(i + ": " + findPlainText(crib, xordCipherText.substring(i, i + crib.length())));
					}
				}

				System.out.print("Enter the correct position, \"none\" for no match, or \"end\" to quit: ");
				input = scan.next();
				int index = -1;
				try {
					index = Integer.parseInt(input);
					String figuredPortion = "";
					if (message) {
						figuredPortion = findKey(crib, xordCipherText.substring(index, index + crib.length()));
					} else {
						figuredPortion = findPlainText(crib, xordCipherText.substring(index, index + crib.length()));
					}
					for (int i = index; i < crib.length(); i++) {
						if (message) {
							figuredPlaintext[i] = figuredPortion.substring(i - index, i - index + 1);
							figuredKey[i] = crib.substring(i - index, i - index + 1);
						} else {
							figuredKey[i] = figuredPortion.substring(i - index, i - index + 1);
							figuredPlaintext[i] = crib.substring(i - index, i - index + 1);
						}
					}
				} catch (Exception e) { }
			}
		}
	}
}
