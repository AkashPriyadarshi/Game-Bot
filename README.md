# Game-Bot
                                                                    Game Bot

This	is	a	programming	assignment.	You	will	be	provided	sample	inputs	and	outputs	(see	below).	Please	understand	that	
the	goal	of	the	samples	is	to	check	that	you	can	correctly	parse	the	problem	definitions,	and	generate	a	correctly	formatted	output.
The	samples	are	very	simple	and	it	should	not	be	assumed	that	if	your	program	works	on	the	samples	it	will	work	on	all	test	
cases.	There	will	be	more	complex	test	cases	and	it	is	your	task	to	make	sure	that	your	program	will	work	correctly	on	
any	valid	input.	You	are	encouraged	to	try	your	own	test	cases	to	check	how	your	program	would	behave	in	some	complex	special	
case	that	you	might	think	of.	Since	each	homework	is	checked	via	an	automated	A.I.	script,	your	output	should	match	the	
specified	format	exactly.	Failure	to	do	so	will	most	certainly	cost	some	points.	The	output	format	is	simple	and	examples	
are	provided.	You	should	upload	and	test	your	code	on	vocareum.com,	and	you	will	submit	it	there.	You	may	use	any	of	
the	programming	languages	provided	by	vocareum.com.	


Project	description	
	
You	are	a	zookeeper	in	the	reptile	house.	One	of	your	rare	South	Pacific	Tufted	Wizzo	lizards	(Tufticus	Wizzocus)	has	just	
had	several	babies.	Your	job	is	to	find	a	place	to	put	each	baby	lizard	in	a	nursery.		
However,	there	is	a	catch,	the	baby	lizards	have	very	long	tongues.	A	baby	lizard	can	shoot	out	its	tongue	and	eat	any	other	
baby	lizard	before	you	have	time	to	save	it.	As	such,	you	want	to	make	sure	that	no	baby	lizard	can	eat	another	baby
lizard	in	the	nursery	(burp).			
	
For	each	baby	lizard,	you	can	place	them	in	one	spot	on	a	grid.	From	there,	they	can	shoot	out	their	tongue	up,	down,	left,
right	and	diagonally	as	well.	Their	tongues	are	very	long	and	can	reach	to	the	edge	of	the	nursery	from	any	location.	

In	addition	to	baby	lizards,	your	nursery	may	have	some	trees	planted	in	it.	Your	lizards	cannot	shoot	their	tongues	through	
the	trees	nor	can	you	move	a	lizard	into	the	same	place	as	a	tree.	As	such,	a	tree	will	block	any	lizard	from	eating	another
lizard	if	it	is	in	the	path.	Additionally,	the	tree	will	block	you	from	moving	the	lizard	to	that	location.	
	
	
You	will	write	a	program	that	will	take	an	input	file	that	has	an	arrangement	of	trees	and	will	output	a	new	arrangement	
of	lizards	(and	trees;	as	already	mentioned,	you	can’t	move	the	trees)	such	that	no	baby	lizard	can	eat	another	one.	
You	will	be	required	to	create	a	program	that	finds	the	solution.	To	find	the	solution	you	will	use	the	following	algorithms:	
	
- Breadth-first	search	(BFS)	
- Depth-first	search	(DFS)
- Simulated	annealing	(SA).		
	
 Input:	The	file	input.txt	in	the	current	directory	of	your	program	will	be	formatted	as	follows:	
First	line:		instruction	of	which	algorithm	to	use:	BFS,	DFS	or	SA	Second	line:		strictly	positive	32-bit	integer	n,	
the	width	and	height	of	the	square	nursery	Third	line:		strictly	positive	32-bit	integer	p,	the	number	of	baby	lizards	
Next	n	lines:		the	n	x	n	nursery,	one	file	line	per	nursery	row	(to	show	you	where	the	trees	are).	It	will	have	a	0	where	
there	is	nothing,	and	a	2	where	there	is	a	tree.		
	
So	for	instance,	an	input	file	arranged	like	figure	2B	(but	with	no	lizards	yet)	and	requesting	you	to	use	the	DFS	algorithm	to	place	8	lizards	in	the	8x8	nursery	would	look	like:	
	
DFS	8	8								
00000000	
00000000	
00000000	
00002000	
00000000	
00000200	
00000000	
00000000
