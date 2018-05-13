from utilities import *

class Broadcast():

	def solve(self, ciphertexts, moduli, exponent):

		plain_nth_power = crt(moduli, ciphertexts)
		return nth_root(plain_nth_power, exponent)
