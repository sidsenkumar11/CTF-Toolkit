from Crypto.Cipher import PKCS1_OAEP
from Crypto.PublicKey import RSA
from fractions import gcd
import glob
import gmpy

# Get all file names
key_files = glob.glob("./*.key")
cipher_files = glob.glob("./*.enc")

# Extract n and e from all key files
keys = {}
for key_file in key_files:
	key = RSA.importKey(open(key_file).read())
	keys[key_file] = { "n" : key.n, "e" : key.e}

# Find all pairs of n with common factors
private_keys = {}

for key_file in key_files:
	key_one = keys[key_file]

	for other_key_file in key_files:
		key_two = keys[other_key_file]

		# Don't find GCD between the same exact keys
		if key_one == key_two:
			continue

		# Check if key found
		if gcd(key_one["n"], key_two["n"]) != 1:
			key_one_p = gcd(key_one["n"], key_two["n"])
			key_one_q = key_one["n"] / key_one_p
			key_one_e = key_one["e"]

			key_two_p = key_one_p
			key_two_q = key_two["n"] / key_two_p
			key_two_e = key_two["e"]

			private_keys[key_file] = {"p":key_one_p, "q":key_one_q, "e":key_one_e}
			private_keys[other_key_file] = {"p":key_two_p, "q":key_two_q,"e":key_two_e}

# Construct PKCS1_OAEP Private Keys
pkeys = {}
for key_file in private_keys:

	e = private_keys[key_file]["e"]
	p = private_keys[key_file]["p"]
	q = private_keys[key_file]["q"]
	n = p * q
	d = long(gmpy.invert(e, (p-1)*(q-1)))

	private_key = RSA.construct((n, e, d))
	encname = key_file[:key_file.index(".key")] + ".enc"
	pkeys[encname] = private_key

# Decrypt appropriate ciphertexts
flag_text = {}
for cipher_file in pkeys:
	with open(cipher_file, "r") as f:
		cipher = PKCS1_OAEP.new(pkeys[cipher_file])
		plaintext = cipher.decrypt(f.read())

		flag_text[cipher_file[2:cipher_file.index(".enc")]] = plaintext

# Sort the ciphertexts by numerical order of filename
final_plaintext = ""
keylist = flag_text.keys()
keylist = [int(x) for x in keylist]
keylist.sort()

# Print out the plaintexts in order
for key in keylist:
	final_plaintext = final_plaintext + flag_text[str(key)]
print(final_plaintext)
