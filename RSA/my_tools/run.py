from pem_utilities import *
from attack_functions import *
import glob

# Get all file names that end with .key or .enc (if necessary)
# key_files = glob.glob("./*.key")
# cipher_files = glob.glob("./*.enc")

ciphertext_fname = "flag.enc"


# Crack the key, get factors
n = 0x5f750e1b6fea81d8fe21acbcfdde68ae205d033ba1000d5f5564272bf49df1c6a41dc66f9f1ceb2cb749dc76aaab73e752ad0c4f85028403cfb79d004a063ca9042b9561fdddb8214ee2222013fe65e7705e9c146fa9d6bc9e3fd9ec39e0d10a69d2dbc4fca1ece87fe790fbfd9841e77f76971b7a0664e18daddc11b2df2a27
phi_n = (n-1)

e = 16
print modinv(e, phi_n)
# priv_key = gen_private_key_p_q(n, long(e), p, q)

# Decrypt the ciphertext file
# decrypt_file(ciphertext_fname, priv_key)

# If you desire more accuracy, write the private key to a file then decrypt using openssl
# openssl rsautl -decrypt -inkey private.pem < ctfexample-text.txt
# Write PEM to file
with open ("private.pem", "w") as prv_file:
    prv_file.write("{}".format(priv_key.exportKey()))
