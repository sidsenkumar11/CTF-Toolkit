from fractions import Fraction
import gmpy

class Wiener():

	def f2cf(self, nu, de):
		'''
		Fraction nu/de to continued fraction
		'''
		cf = []
		while de:
			qu = nu // de
			cf.append(qu)
			nu, de = de, nu - de*qu
		return cf

	def cf2f(self, cf):
		'''
		Continued fraction to fraction
		'''
		f = Fraction(0, 1)
		for x in reversed(cf):
			try:
				f = 1 / (f+x)
			except ZeroDivisionError:
				return Fraction(0, 1)
		return 1/f

	def cf2cvg(self, cf):
		'''
		Continued faction to convergents
		'''
		for i in range(1,len(cf)+1):
			yield cf2f(cf[:i])

	def solve(self, e, n):
		for cvg in cf2cvg(f2cf(e, n)):
			k = cvg.numerator
			if k == 0:
				continue
			d = cvg.denominator
			phi = (e*d-1)//k
			nb = n - phi + 1
			squ = nb*nb-4*n
			if squ < 0:
				continue
			root = gmpy.sqrt(squ)
			if root*root == squ and not (nb+root)&1:
				p = (nb+root)>>1
				q = (nb-root)>>1
				d = d
				return d
		return -1
