ILIAS KONTONIS - 1115201700055


------------------------------ PART 1 --------------------------------

The LL(1) grammar is written below:

Expr ::= Term ExprTail
ExprTail ::= Op Term ExprTail
	| e

Term ::= Num Expo
	| ( Expr ) Expo

Expo ::= ** Term
	| e

Op ::= + | -

Num ::= DStar NumTail
NumTail ::= D NumTail
	| e

D ::= 0 | 1 | 2 | ... | 9
DStar ::= 1 | 2 | ... | 9



To run:
	Project/Part1 javac *.java
	Project/Part1 java Main
----------------------------------------------------------------------


------------------------------ PART 2 --------------------------------

-> The only thing that is important about the scanner is that ")" followed by
"{" makes the token **DECL**. This helps the parser detect a function declaration
instead of a function call.

-> The parser seems to work properly and produce corrent java programs.
A static global variable is used to hold in a string the whole body of main method
produced by the rules. This one is needed because of the logic behind the grammar,
that was needed for correct results. More specifically the first "main" rule is
declaration_or_call that decides what its name says (plus none of the 2, so literally
anything). If a declaration is given, it continues with the same rule. If not, the body
of the main method is parsed. So we have something like :
	program -> declarations -> main body
This prevents from printing main body inside the braces of the main method, so the
main body is concatenated piece by piece, and in the end it is printed in the write
place.

-> The parser throws error in the case of an identifier in the main method, but for that
a silly duplication of most of the rules is done.

To run:
	Project1/Part2 make (or make compile)
	Project1/Part2 make execute

* The java program must have the name 'Main.java'

* Sometimes (depends on the number of the newline's character given in the input) we
  must give more than one times EOF (CTRL+d)!
