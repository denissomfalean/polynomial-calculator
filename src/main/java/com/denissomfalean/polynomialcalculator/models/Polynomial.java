package com.denissomfalean.polynomialcalculator.models;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Optional;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.*;

/**
 * Represents the operand of the calculator, compose of multiple {@link Monomial monomials}. Every
 * monomial that is added to the TreeSet will be ordered.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Polynomial {
  public static final String MONOMIAL_REGEX_PATTERN = "([+-]?\\d*)x\\^?(\\d*)|([+-]?\\d+)";
  public static final String SPACES_REGEX = "\\s+";

  private TreeSet<Monomial> monomials = new TreeSet<>();

  public Polynomial(Monomial... monomials) {
    for (Monomial monomial : monomials) {
      this.addMonomial(monomial);
    }
  }

  public static Polynomial fromString(String polynomialString) {
    Polynomial polynomial = new Polynomial();
    Pattern pattern = Pattern.compile(MONOMIAL_REGEX_PATTERN);
    Matcher matcher = pattern.matcher(polynomialString.replaceAll(SPACES_REGEX, EMPTY));

    while (matcher.find()) {
      if (matcher.group(3) != null) {
        addConstantTermToPolynomial(polynomial, matcher);
      } else {
        addTermWithDegreeToPolynomial(polynomial, matcher);
      }
    }

    return polynomial;
  }

  public Polynomial deepCopy() {
    TreeSet<Monomial> copiedSet = new TreeSet<>();

    for (Monomial monomial : this.monomials) {
      copiedSet.add(new Monomial(monomial.getCoefficient(), monomial.getDegree()));
    }

    Polynomial copy = new Polynomial();
    copy.monomials = copiedSet;
    return copy;
  }

  public void addMonomial(Monomial newMonomial) {
    Optional<Monomial> existingMonomial =
        monomials.stream()
            .filter(monomial -> monomial.getDegree() == newMonomial.getDegree())
            .findFirst();

    if (existingMonomial.isPresent()) {
      existingMonomial.get().addCoefficient(newMonomial.getCoefficient());
      if (existingMonomial.get().getCoefficient() == 0) {
        monomials.remove(existingMonomial.get());
      }
    } else {
      if (newMonomial.getCoefficient() != 0) {
        monomials.add(newMonomial);
      }
    }

    checkForZeroValue();
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (Monomial monomial : monomials) {
      stringBuilder.append(monomial.toString()).append(" + ");
    }

    return !stringBuilder.isEmpty()
        ? stringBuilder.substring(0, stringBuilder.length() - 3)
        : EMPTY;
  }

  private static void addConstantTermToPolynomial(Polynomial polynomial, Matcher matcher) {
    int coefficient = Integer.parseInt(matcher.group(3));
    polynomial.addMonomial(new Monomial(coefficient, 0));
  }

  private static void addTermWithDegreeToPolynomial(Polynomial polynomial, Matcher matcher) {
    int coefficient =
        matcher.group(1).isEmpty() || matcher.group(1).equals("+")
            ? 1
            : (matcher.group(1).equals("-") ? -1 : Integer.parseInt(matcher.group(1)));
    int degree = matcher.group(2).isEmpty() ? 1 : Integer.parseInt(matcher.group(2));
    polynomial.addMonomial(new Monomial(coefficient, degree));
  }

  private void checkForZeroValue() {
    if (this.monomials.isEmpty()) {
      this.monomials.add(new Monomial(0, 0));
    }
  }
}
