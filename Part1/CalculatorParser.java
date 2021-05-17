import java.io.InputStream;
import java.io.IOException;
import java.lang.Math;

class CalculatorParser {
    private final InputStream input;

    private int lookahead;


    public CalculatorParser(InputStream input) throws IOException {
	this.input = input;
	lookahead = input.read();
    }
    public void consume(int symbol) throws IOException, ParseError {
	if (lookahead == symbol)
	    lookahead = input.read();
	else
	    throw new ParseError();
    }
    private boolean isDigitStar(int c) {
	return '1' <= c && c <= '9';
    }
    private boolean isDigit(int c) {
	return '0' <= c && c <= '9';
    } 
    private int evalDigit(int c) {
	return c - '0';
    }
    private static int pow(int base, int exponent) {
	if (exponent < 0)
	    return 0;
	if (exponent == 0)
	    return 1;
	if (exponent == 1)
	    return base;    

	if (exponent % 2 == 0) //even exp -> b ^ exp = (b^2)^(exp/2)
	    return pow(base * base, exponent/2);
	else                   //odd exp -> b ^ exp = b * (b^2)^(exp/2)
	    return base * pow(base * base, exponent/2);
    }

    private int Pow(int current) throws IOException, ParseError {

	if (lookahead == '*') {
	    consume(lookahead);
	    if (lookahead == '*') {
		consume(lookahead);

		/*return (int)Math.pow((double)current, (double)Term());*/
		return pow(current, Term());
	    }

	    throw new ParseError();
	}

	switch (lookahead) {
	    case ')':
	    case '\n':
	    case -1:
	    case '+':
	    case '-':
		return current;

	}

	throw new ParseError();
    }

    private int NumTail(int current) throws IOException, ParseError {
	if (isDigit(lookahead)) {
	    int digit = evalDigit(lookahead);

	    consume(lookahead);
	    return NumTail(current*10 + digit);
	}
	switch (lookahead) {
	    case ')':
	    case '\n':
	    case -1:
	    case '+':
	    case '-':
	    case '*':
		return current;

	}

	throw new ParseError();
    }

    private int Num() throws IOException, ParseError {
	if (isDigitStar(lookahead)) {
	    int digit = evalDigit(lookahead);
	    consume(lookahead);
	    return NumTail(digit);
	}
	else if (lookahead == '0') {
	    int digit = evalDigit(lookahead);
	    consume(lookahead);
	    return digit;
	}

	throw new ParseError();
    }

    private int Term() throws IOException, ParseError {
	if (lookahead == '(') {
	    consume(lookahead);

	    int val = Expr();

	    if (lookahead == ')') {
		consume(lookahead);
		return Pow(val);
	    }

	    throw new ParseError();
	}
	else if (isDigit(lookahead)) {
	    return Pow(Num());
	}

	throw new ParseError();
    }

    private boolean isOper() throws IOException, ParseError {
	if (lookahead == '+' || lookahead == '-')
	    return true;
	return false;
    }

    private int ExprTail(int current) throws IOException, ParseError {
	if (isOper()) {
	    int oper = lookahead;
	    consume(lookahead);

	    if (oper == '+')
		return ExprTail(current + Term());
	    return ExprTail(current - Term());
	}

	switch (lookahead) {
	    case ')':
	    case -1:
	    case '\n':
		return current;
	}

	throw new ParseError();
    }

    private int Expr() throws IOException, ParseError {

	if (lookahead == '(' || isDigit(lookahead))
	    return ExprTail(Term());

	throw new ParseError();

    }

    public int eval() throws IOException, ParseError {
	int value = Expr();

	if (lookahead != -1 && lookahead != '\n')
	    throw new ParseError();

	return value;
    }
}
