package com.fraserHum.lox;

public class RpnPrinter implements Expr.Visitor<String> {
  public static void main(String[] args) {
    Expr expression = new Expr.Binary(
        new Expr.Unary(
            new Token(TokenType.MINUS, "-", null, 1),
            new Expr.Literal(123)),
        new Token(TokenType.STAR, "*", null, 1),
        new Expr.Grouping(
            new Expr.Literal(45.67)));

    Expr expression2 = new Expr.Binary(
        new Expr.Grouping(
            new Expr.Binary(new Expr.Literal(1), new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(2))),
        new Token(TokenType.STAR, "*", null, 1),
        new Expr.Grouping(
            new Expr.Binary(new Expr.Literal(4), new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(3))));

    System.out.println(new RpnPrinter().print(expression));

    System.out.println(new RpnPrinter().print(expression2));
  }

  String print(Expr expr) {
    return expr.accept(this);
  }

  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return reversePolishize(expr.left, expr.right) + expr.operator.lexeme;
  }

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {

    return expr.expression.accept(this);

  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null)
      return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return reversePolishize(expr.right) + expr.operator.lexeme;
  }

  private String reversePolishize(Expr... exprs) {
    StringBuilder builder = new StringBuilder();
    for (Expr expr : exprs) {
      builder.append(expr.accept(this));

      builder.append(" ");
    }

    return builder.toString();
  }

}
