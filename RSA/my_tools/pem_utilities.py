from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from attacks.utilities import get_d
from base64 import b64decode

"""

PKCS = Public_Key Cryptography Standards

OAEP = padding scheme that adds element of randomness and prevents partial decryption of ciphertexts

"""

def read_key(fname):
	with open(fname, 'r') as f:
		return RSA.importKey(f.read())

def gen_private_key_p_q(n, e, p, q):
	d = get_d(p, q, e)
	return RSA.construct((n, e, d))

def gen_private_key(n, e, d):
	return RSA.construct((n, e, d))

def decrypt_file(fname, private_key):
	with open(fname, 'rb') as enc:
		with open("dec_" + fname, 'w') as dec:
			dec.write(private_key.decrypt(enc.read()))

# TODO: Write to file instead of printing string
def decrypt_b64(b64text, private_key):
	encrypted_bytes = b64decode(b64text.encode('utf_8'))
	decrypted_bytes = private_key.decrypt(encrypted_bytes)
	decrypted_str = decrypted_bytes.decode('utf_8')
	return decrypted_str
