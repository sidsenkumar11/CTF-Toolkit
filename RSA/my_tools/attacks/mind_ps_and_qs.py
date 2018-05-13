from utilities import gcd, decrypt

def solve_common_factors(ciphertexts, modulos, exponents):
	plaintexts = []
	factors_found = []
	for i in range(len(modulos)):
		for j in range(i + 1, len(modulos)):
			calc_gcd = gcd(modulos[i], modulos[j])
			if calc_gcd > 1:
				factors_found.append([i, j, calc_gcd])

	for mod_pair in factors_found:

		# Get factors of n1 and n2
		calc_gcd = mod_pair[2]
		q1 = modulos[mod_pair[0]] / calc_gcd
		q2 = modulos[mod_pair[1]] / calc_gcd

		# Decrypt c1
		c1 = ciphertexts[mod_pair[0]]
		e1 = exponents[mod_pair[0]]
		p1 = decrypt(c1, e1, modulos[mod_pair[0]], p=calc_gcd, q=q1)

		# Decrypt c2
		c2 = ciphertexts[mod_pair[1]]
		e2 = exponents[mod_pair[1]]
		p2 = decrypt(c2, e2, modulos[mod_pair[1]], p=calc_gcd, q=q2)

		plaintexts.append(p1)
		plaintexts.append(p2)
	return plaintexts
