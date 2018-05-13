import requests
from bs4 import BeautifulSoup

# To use this class, factor_online = FactorDB(); p, q = factor_online.query(n)
class FactorDB():

	def __init__(self):
		self.baseUrl = "http://factordb.com/"

	def query(self, n):
		s, p, q = self.interact(n)[0]
		if p == q:
			P = self.get_prime(p)
			return (int(P), int(P))
		else :
			P = self.get_prime(p)
			Q = self.get_prime(q)
			return (int(P), int(Q))

	def interact(self, n):
		# Query database, get factors and information
		myVars = []
		r = requests.get(self.baseUrl+"index.php?query="+str(n))
		html = r.content
		mylist =[]
		soup = BeautifulSoup(html,"lxml")
		tables = soup.findAll("table")
		for table in tables:
			if table.findParent("table") is None:
				for row in table.findAll("tr"):
					cells = row.findAll("td")
					mylist.append(str(cells))
		data = mylist[3]
		data = data.split(",")
		soup2 = BeautifulSoup(data[0],"lxml")
		for row in soup2.findAll("td"):
			status = row.text # Get the status

		links = []
		soupLinks = BeautifulSoup(data[2],"lxml")
		for row in soupLinks.findAll("a"):
			links.append(row["href"])
		l = len(links)
		myVars.append([status,links[l-2],links[l-1]])
		return myVars

	def get_prime(self, p):
		r = requests.get(self.baseUrl+p)
		html = r.content
		soup = BeautifulSoup(html,"lxml")
		try:
			value = soup.find('input', {'name': 'query'}).get('value')
			return value
		except:
			print("Fail")
