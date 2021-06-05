package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class lexicalAnalyzer {
    private final String str;
    Map<String, TokenType> keywords = new HashMap<>();
    private int line;
    private int pos;
    private int position;
    private char chr;
    public static int counter = 0;

    lexicalAnalyzer(String source) {
        this.line = 1;
        this.pos = 1;
        this.position = 0;
        this.str = source;
        this.chr = this.str.charAt(0);

        this.keywords.put("Yesif", TokenType.Condition);
        this.keywords.put("Otherwise", TokenType.Condition);
        this.keywords.put("Omw", TokenType.Integer);
        this.keywords.put("SIMww", TokenType.SInteger);
        this.keywords.put("Chji", TokenType.Character);
        this.keywords.put("Seriestl", TokenType.String);
        this.keywords.put("IMwf", TokenType.Float);
        this.keywords.put("SIMwf", TokenType.SFloat);
        this.keywords.put("NOReturn", TokenType.Void);
        this.keywords.put("OutLoop", TokenType.Break);
        this.keywords.put("RepeatWhen", TokenType.Loop);
        this.keywords.put("Reiterate", TokenType.Loop);
        this.keywords.put("GetBack", TokenType.Return);
        this.keywords.put("Loli", TokenType.Struct);
        this.keywords.put("Include", TokenType.Inclusion);
        this.keywords.put("Start", TokenType.Start);
        this.keywords.put("Last", TokenType.End);

    }

    static void error() {
        
            counter++;
    }

    Token getToken() {
        int line, pos;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        line = this.line;
        pos = this.pos;

        switch (this.chr) {
            case '\u0000':
                return new Token(TokenType.EndOfInput, "", this.line, this.pos);
            case '/':
                    getNextChar();
                    if (this.chr == '@') {
                        getNextChar();
                        while (true) {
                            if (this.chr == '\u0000') {
                                error();
                            } else if (this.chr == '@') {
                                if (getNextChar() == '/') {
                                    getNextChar();
                                    return new Token(TokenType.Comment, "/@@/", line, pos);
                                }
                            } else {
                                getNextChar();
                            }
                        }
                    } else if (this.chr == '^') {
                        getNextChar();
                        while (true) {
                            if (this.chr == '\n') {
                                return new Token(TokenType.Comment, "/^", line, pos);
                            } else {
                                getNextChar();
                            }
                        }
                    } else {
                        return new Token(TokenType.Arithmetic_Operation, "/", line, pos);
                    }

            case '<':
                
                if (getNextChar() == '=') {
                    getNextChar();
                    return new Token(TokenType.Relational_Operators, "", line, pos);
                }
                if (TokenType.Op_less == TokenType.EndOfInput) {
                    error();
                }
                return new Token(TokenType.Op_less, "", line, pos);
            case '>':
                
                if (getNextChar() == '=') {
                    getNextChar();
                    return new Token(TokenType.Relational_Operators, "", line, pos);
                }
                if (TokenType.Op_greater == TokenType.EndOfInput) {
                    error();
                }
                return new Token(TokenType.Op_greater, "", line, pos);
            case '=':
                
                if (getNextChar() == '=') {
                    getNextChar();
                    return new Token(TokenType.Relational_Operators, "", line, pos);
                }
                if (TokenType.Assignment_operator == TokenType.EndOfInput) {
                    error();
                }
                return new Token(TokenType.Assignment_operator, "", line, pos);
            case '!':
                
                if (getNextChar() == '=') {
                    getNextChar();
                    return new Token(TokenType.Op_notequal, "", line, pos);
                }
                if (TokenType.EndOfInput == TokenType.EndOfInput) {
                    error();
                }
                return new Token(TokenType.EndOfInput, "", line, pos);
            case '&':
                if (getNextChar() == '&') {
                    getNextChar();
                    return new Token(TokenType.Logic_operators, "", line, pos);
                }
                if (TokenType.EndOfInput == TokenType.EndOfInput) {
                    error();
                }
                return new Token(TokenType.EndOfInput, "", line, pos);
            case '|':
                if (getNextChar() == '|') {
                    getNextChar();
                    return new Token(TokenType.Logic_operators, "", line, pos);
                }
                if (TokenType.EndOfInput == TokenType.EndOfInput) {
                    error();
                }
                return new Token(TokenType.EndOfInput, "", line, pos);
            case '~':
                getNextChar();
                return new Token(TokenType.Logic_operators, "~", line, pos);
            case '-':
                getNextChar();
                if (getNextChar() == '>') {
                    getNextChar();
                    return new Token(TokenType.Access_Operator, "", line, pos);
                }
                if (TokenType.Arithmetic_Operation == TokenType.EndOfInput) {
                    error();
                }
                return new Token(TokenType.Arithmetic_Operation, "", line, pos);
            case '"':
                StringBuilder result = new StringBuilder();
                while (getNextChar() != this.chr) {
                    if (this.chr == '\u0000') {
                        error();
                    }
                if (this.chr == '\n') {
                    error();
                }
                result.append(this.chr);
                }
        getNextChar();
        return new Token(TokenType.String, result.toString(), line, pos);
            case '{':
                getNextChar();
                return new Token(TokenType.LeftBrace, "{", line, pos);
            case '}':
                getNextChar();
                return new Token(TokenType.RightBrace, "}", line, pos);
            case '(':
                getNextChar();
                return new Token(TokenType.LeftParen, "(", line, pos);
            case ')':
                getNextChar();
                return new Token(TokenType.RightParen, ")", line, pos);
            case '[':
                getNextChar();
                return new Token(TokenType.LeftSquare, "(", line, pos);
            case ']':
                getNextChar();
                return new Token(TokenType.RightSquare, ")", line, pos);
            case '+':
                getNextChar();
                return new Token(TokenType.Arithmetic_Operation, "+", line, pos);
            case '*':
                getNextChar();
                return new Token(TokenType.Arithmetic_Operation, "*", line, pos);
            case '.':
                getNextChar();
                return new Token(TokenType.LineDelimiter, ".", line, pos);
            case '$':
                getNextChar();
                return new Token(TokenType.TokenDelimiter, "$", line, pos);
            case '\'':
                getNextChar();
                return new Token(TokenType.QuotationMark, "'", line, pos);

            default:
                boolean is_number = true;
        StringBuilder text = new StringBuilder();

        while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr) || this.chr == '_') {
            text.append(this.chr);
            if (!Character.isDigit(this.chr)) {
                is_number = false;
            }
            getNextChar();
            }

            if (text.toString().equals("")) {
                error();
            }

            if (Character.isDigit(text.charAt(0))) {
                if (!is_number) {
                    error();
                }
                return new Token(TokenType.Constant, text.toString(), line, pos);
            }

            if (this.keywords.containsKey(text.toString())) {
                return new Token(this.keywords.get(text.toString()), text.toString(), line, pos);
            }
            return new Token(TokenType.Identifier, text.toString(), line, pos);
        }
    }

    char getNextChar() {
        this.pos++;
        this.position++;
        if (this.position >= this.str.length()) {
            this.chr = '\u0000';
            return this.chr;
        }
        this.chr = this.str.charAt(this.position);
        if (this.chr == '\n') {
            this.line++;
            this.pos = 0;
        }
        return this.chr;
    }

    ArrayList<Token> printTokens() {
        Token t;
        ArrayList<Token> res = new ArrayList<>();
        while ((t = getToken()).tokentype != TokenType.EndOfInput) {
            res.add(t);
        }
        res.add(t);
        return res;
    }

    enum TokenType {
        EndOfInput, Arithmetic_Operation,
        Logic_operators, Relational_Operators, Op_less, Op_greater, Op_notequal, Assignment_operator,
        Condition, SInteger, Character, Float, SFloat, Void, Break, Loop, Return, Struct,
        Start, End, Inclusion, Integer, LeftParen, RightParen,
        LeftBrace, RightBrace, RightSquare, LeftSquare, TokenDelimiter, LineDelimiter, QuotationMark, Identifier, String, Access_Operator, Comment, Constant
    }

    static class Token {
        public TokenType tokentype;
        public String lexeme;
        public int line;
        public int pos;

        Token(TokenType token, String Lexeme, int line, int pos) {
            this.tokentype = token;
            this.lexeme = Lexeme;
            this.line = line;
            this.pos = pos;
        }

        @Override
        public String toString() {
            String result = String.format("%5d  %5d %-15s", this.line, this.pos, this.tokentype);
            if (this.tokentype == TokenType.String)
                result += String.format(" \"%s\"", lexeme);
            else
                result += String.format(" %s", lexeme);

            return result;
        }
    }

}