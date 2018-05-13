from attacks.utilities import *
from attacks.factordb import FactorDB
from attacks.fermat import fermat_solve
from attacks.wiener import Wiener
from attacks.mind_ps_and_qs import solve_common_factors
from attacks.hastads import Broadcast
from attacks.same_modulus import SharedMod

# Decrypt given the decryption information.
""" Given d """
def given_d(c, n, d):
	return decrypt(c, 0, n, d=d)

""" Given p and q """
def given_p_q(c, e, n, p, q):
	return decrypt(c, e, n, p=p, q=q)

""" Given totient """
def given_totient(c, e, n, totient):
	return totient_decrypt(c, e, n, totient)

# Single ciphertext attacks
""" Small N - just factor it! (on factordb) """
def factordb(n):
	factor_online = FactorDB()
	p, q = factor_online.query(n)
	return (p, q)

""" TODO: Small p or q - just factor it! (using YAFU) """

""" TODO: Multi-prime key """

"""
	Small C - just take nth root!
	- Used when c < n
"""
def c_smaller_than_n(c, e):
	return nth_root(c, e)

"""
	Fermat's Factorization
	- Used when p and q share half their leading bits
		- In other words, |p - q| < sqrt(p)
"""
def fermat(c, e, n):
	p, q = fermat_solve(e, n)
	return decrypt(c, e, n, p=p, q=q)

"""
	Wiener's Low Private Exponent
	- Used when d is small compared to n.
"""
def wiener(c, e, n):
	wiener_class = Wiener()
	d = wiener_class.solve(e, n)
	return decrypt(c, e, n, d=d)

# Multiple ciphertext attacks
"""
	Mind your P's and Q's
	- Many moduli with a weak PRNG that used duplicate P's and Q's.
	- Calculate the GCD between all pairs and try to factor one of them.
"""
def common_factors(ciphertexts, modulos, exponents):
	return solve_common_factors(ciphertexts, modulos, exponents)

"""
	Hastad's Broadcast Attack
	- Used when same plaintext encrypted multiple times with the same E but different Ns.
	- Need at least E ciphertexts.
"""
def broadcast(ciphertexts, modulos, e):
	hastads = Broadcast()
	return hastads.solve(ciphertexts, modulos, e)

"""
	Common Modulus Attack
	- Used when same plaintext encrypted multiple times with the same N but different Es.
	- E's must be coprime (gcd(e1, e2) == 1)
	- Only need 2 ciphertexts
"""
def common_modulos(c1, e1, c2, e2, n):
	shared_mod = SharedMod()
	return shared_mod.solve(n, c1, c2, e1, e2)

# Misc.
""" Print plaintext number as ASCII text """
def ascii(plaintext):
	return display_plain_as_ascii(plaintext)
