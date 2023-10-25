package com.fraserHum.lox;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.fraserHum.lox.TokenType.*;

class Parser {
  private final List<Token> tokens;
  private int current = 0;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  private Expr expression() {
    return equality();
  }

  private Expr equality() {
    Expr expr = comparison();

    while (match(BANG_EQUAL, EQUAL_EQUAL)) {
      Token operator = previous();
      Expr right = comparison();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private Expr comparison() {
    Expr expr = term();

    while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
      Token operator = previous();
      Expr right = term();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private Expr term() {
    parseLeftAssoiateBinary(comparison, LESS_EQUAL);
  }

  private Expr parseLeftAssoiateBinary(Supplier<Expr> operand, TokenType... tokenTypes) {
    Expr expr = operand.get();

    while (match(tokenTypes)) {
      Token operator = previous();
      Expr right = operand.get();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }

    return false;
  }

  private boolean check(TokenType type) {
    if (isAtEnd())
      return false;
    return peek().type == type;
  }

  private Token advance() {
    if (!isAtEnd())
      current++;
    return previous();
  }

  private boolean isAtEnd() {
    return peek().type == EOF;
  }

  private Token peek() {
    return this.tokens.get(current);
  }

  private Token previous() {
    return this.tokens.get(current - 1);
  }
}
