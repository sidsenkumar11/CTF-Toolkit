import numpy

def root3rd(x):
    y, y1 = None, 2
    while y!=y1:
        y = y1
        y3 = y**3
        d = (2*y3+x)
        y1 = (y*(y3+2*x)+d//2)//d
    return y

def find_invpow(x,n):
    """Finds the integer component of the n'th root of x,
    an integer such that y ** n <= x < (y + 1) ** n.
    """
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
            return mid
    return mid + 1

# def root(n, r=4):
#      return roots([1]+[0]*(r-1)+[-n])

def iroot(k, n):
    hi = 1
    while pow(hi, k) < n:
        hi *= 2
    lo = hi / 2
    while hi - lo > 1:
        mid = (lo + hi) // 2
        midToK = pow(mid, k)
        if midToK < n:
            lo = mid
        elif n < midToK:
            hi = mid
        else:
            return mid
    if pow(hi, k) == n:
        return hi
    else:
        return lo

original = 1554920717086938018030122553400203178274577912173811879320124401718690525384693409190685372073213959662381568662629275644371743268399171302352580415961906930159340507274375871834291249967681828878009814439059868315477366003988597329780162442632004680943429629071362718384778382221223275017224078480939367350429386840240764962719835271008892730636645956210198474861619921115852536921361781588817324520560381924007614060384039536648157362936639618647470169199456731

supposed1 = root3rd(original)
supposed2 = find_invpow(original, 3)
# supposed3 = root(original, r=3)
supposed3 = iroot(9, original)

print original
print "----------------------------------------------------------------------"
print supposed3 ** 9
