"""
	==================================================================================================
	MATH UTILITIES
	==================================================================================================
"""

"""
	Extended Euclidean Algorithm (also called Extended GCD)
	@param a Positive integer
	@param b Positive integer
	@return (g, x, y) such that ax + by = g = gcd(a, b)
"""
def egcd(a, b):
	u, u1 = 1, 0
	v, v1 = 0, 1
	while b:
		q = a // b
		u, u1 = u1, u - q * u1
		v, v1 = v1, v - q * v1
		a, b = b, a - q * b
	return (a, u, v)

"""
	Modular Inverse
	@return x such that (a * x) % n = 1
"""
def modinv(a, n):
	g, x, y = egcd(a, n)
	if g != 1:
		raise Exception('Modular inverse does not exist!')
	else:
		return x % n

"""
	GCD
	@return Largest x such that a % x = 0 and b % x = 0
"""
def gcd(a, b):
	return egcd(a, b)[0]

"""
	Finds the integer component y of the nth root of x,
	an integer such that y ** n <= x < (y + 1) ** n.
"""
def nth_root(x,n):
	high = 1
	while high ** n <= x:
		high *= 2
	low = high/2
	while low < high:
		mid = (low + high) // 2
		if low < mid and mid**n < x:
			low = mid
		elif high > mid and mid**n > x:
			high = mid
		else:
			if (mid + 1) ** n != x:
				raise ValueError("Problem finding exact nth root. Found value:\n{}".format(mid))
			return mid

	if (mid + 1) ** n != x:
		raise ValueError("Problem finding exact nth root. Try using Sage!")
	return mid + 1

"""
	Chinese Remainder Theorem:
	ms = list of pairwise relatively prime integers
	as = remainders when x is divided by ms
	(ai is 'each in as', mi 'each in ms')

	The solution for x modulo M (M = product of ms) will be:
	x = a1*M1*y1 + a2*M2*y2 + ... + ar*Mr*yr (mod M),
	where Mi = M/mi and yi = (Mi)^-1 (mod mi) for 1 <= i <= r.
"""
def crt(ml,al):
	M  = reduce(lambda x, y: x*y,ml)        # multiply ml together
	Ms = [M/mi for mi in ml]   # list of all M/mi
	ys = [modinv(Mi, mi) for Mi,mi in zip(Ms,ml)]
	return reduce(lambda x, y: x+y,[ai*Mi*yi for ai,Mi,yi in zip(al,Ms,ys)]) % M

def get_d(p, q, e):
	totientN = (p - 1) * (q - 1)
	d = modinv(e, totientN)
	return d

"""
	==================================================================================================
	BASIC RSA DECRYPTION
	==================================================================================================
"""

"""
	Decrypt an RSA ciphertext c given either (p and q) or d
"""
def decrypt(c, e, n, d=0, p=0, q=0):
	if (d == 0):
		if (p == 0 or q == 0):
			raise Exception("You must give either d or p and q to decrypt!")
		d = get_d(p, q, e)

	plaintext = pow(c, d, n)
	return plaintext

"""
	Decrypt an RSA ciphertext c given totient value.
"""
def totient_decrypt(c, e, n, totientN):
	d = modinv(e, totientN)
	plaintext = pow(c, d, n)
	return plaintext

"""
	Convert integer plaintext into ASCII characters.
"""
def display_plain_as_ascii(plaintext):
	plaintext = hex(plaintext)[2:len(hex(plaintext)) - 1]
	plaintext = plaintext.decode('hex')
	return plaintext
