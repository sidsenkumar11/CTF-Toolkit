from math import sqrt

def fermat_solve(e, n, limit=10000):

	a = sqrt(n)
	max = a + limit

	while a < max:
		b2 = a*a - n
		if b2 >= 0:
			b = sqrt(b2)
			if b*b == b2:
				break
		a += 1

	if a < max:
		p = a+b
		q = a-b
		return (p, q)
	return -1
