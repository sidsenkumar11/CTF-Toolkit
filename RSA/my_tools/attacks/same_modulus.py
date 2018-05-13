from utilities import modinv

"""
Formula:
	- Given c1, c2, e1, e2, and n.

	Check gcd(e1, e2) = 1. Else can't continue.
	Find a and b such that (ax + by = gcd(a,b) = 1)
		- Extended Euclidean Algorithm
		- xgcd() in sage
		- Returns g,x,y such that ax+by=g=gcd(a,b)

	a = -xgcd[1] # Take the large negative value and invert to make it positive
	b = xgcd[2] # Take the second positive number
	c1_inv = inverse_mod(c1, n)
	c1a = Mod(c1_inv, n) ^ a
	c2b = Mod(c2_inv, n) ^ b
	plaintext = (c1a * c2b) % n
"""

class SharedMod:

	def modular_inverse(self, c1, c2, N):
		"""
		i is the modular multiplicative inverse of c2 and N.
		i^-b is equal to c2^b. So if the value of b is -ve, we
		have to find out i and then do i^-b.
		Final plain text is given by m = (c1^a) * (i^-b) %N
		:param c1: cipher text 1
		:param c2: cipher text 2
		:param N: Modulus
		"""
		i = modinv(c2, N)
		mx = pow(c1, self.a, N)
		my = pow(i, int(-self.b), N)
		self.m= mx * my % N

	def extended_euclidean(self, e1, e2):
		"""
		The value a is the modular multiplicative inverse of e1 and e2.
		b is calculated from the eqn: (e1*a) + (e2*b) = gcd(e1, e2)
		:param e1: exponent 1
		:param e2: exponent 2
		"""
		self.a = modinv(e1, e2)
		self.b = (float(gcd(e1, e2)-(self.a*e1)))/float(e2)

	def solve(self, N, c1, c2, e1, e2):
		if gcd(e1, e2) == 1:
			self.extended_euclidean(e1, e2)
			self.modular_inverse(c1, c2, N)
			return self.m
		else:
			raise Exception("e1 and e2 must be coprime for this to work")
