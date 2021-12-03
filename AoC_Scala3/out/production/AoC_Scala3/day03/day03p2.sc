val s = "00100\n11110\n10110\n10111\n10101\n01111\n00111\n11100\n10000\n11001\n00010\n01010"
var bts = s.split('\n')
var r = '0'
var pos = 0
var l = bts.length
var rcnt = bts.map(_(pos)).count(_ == r) * 2
l
rcnt
var bsel = if rcnt > l then '1' else
  if rcnt < l then '0'
  else r
bsel
bts = bts.filter(_(pos) == bsel)
pos += 1
l = bts.length
rcnt = bts.map(_(pos)).count(_ == r) * 2
l
rcnt
bsel = if rcnt > l then '1' else
  if rcnt < l then '0'
  else r
bsel
bts = bts.filter(_(pos) == bsel)
pos += 1
l = bts.length
rcnt = bts.map(_(pos)).count(_ == r) * 2
l
rcnt
bsel = if rcnt > l then '1' else
  if rcnt < l then '0'
  else r
bsel
bts = bts.filter(_(pos) == bsel)
var res = Integer.parseInt(bts.head,2)
pos += 1
l = bts.length
rcnt = bts.map(_(pos)).count(_ == r) * 2
l
rcnt
bsel = if rcnt > l then '1' else
  if rcnt < l then '0'
  else r
bsel
bts = bts.filter(_(pos) == bsel)
pos += 1
l = bts.length
rcnt = bts.map(_(pos)).count(_ == r) * 2
l
rcnt
bsel = if rcnt > l then '1' else
  if rcnt < l then '0'
  else r
bsel
bts = bts.filter(_(pos) == bsel)
pos += 1
var res = Integer.parseInt(bts.head,2)