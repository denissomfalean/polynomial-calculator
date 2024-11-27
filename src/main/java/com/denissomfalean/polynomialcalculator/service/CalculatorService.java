package com.denissomfalean.polynomialcalculator.service;

import static com.denissomfalean.polynomialcalculator.utils.CalculatorUtils.*;

import com.denissomfalean.polynomialcalculator.models.Monomial;
import com.denissomfalean.polynomialcalculator.models.Polynomial;
import lombok.extern.slf4j.Slf4j;

/**
 * Includes all the operations functionalities: addition, subtraction, division for finding the
 * Quotient, division for finding the REST, multiplication. It is used by {@link
 * com.denissomfalean.polynomialcalculator.controllers.CalculatorController CalculatorController}
 * for implementing the operations.
 */
@Slf4j
public class CalculatorService {

  public static final Polynomial ZERO_POLYNOMIAL = new Polynomial(new Monomial(0, 0));

  public static Polynomial addition(Polynomial firstPolynomial, Polynomial secondPolynomial) {
    Polynomial result = new Polynomial();
    for (Monomial monomial : firstPolynomial.getMonomials()) {
      result.addMonomial(monomial);
    }

    for (Monomial monomial : secondPolynomial.getMonomials()) {
      result.addMonomial(monomial);
    }

    log.info("Result for ADDITION is {}", result);
    return result;
  }

  public static Polynomial subtraction(Polynomial firstPolynomial, Polynomial secondPolynomial) {
    Polynomial result = new Polynomial();
    for (Monomial monomial : firstPolynomial.getMonomials()) {
      result.addMonomial(monomial);
    }

    for (Monomial monomial : secondPolynomial.getMonomials()) {
      monomial.setCoefficient((-1) * monomial.getCoefficient());
      result.addMonomial(monomial);
    }

    log.info("Result for SUBTRACTION is {}", result);
    return result;
  }

  public static Polynomial multiplication(Polynomial firstPolynomial, Polynomial secondPolynomial) {
    Polynomial result = new Polynomial();
    for (Monomial monomialFromFirst : firstPolynomial.getMonomials()) {
      for (Monomial monomialFromSecond : secondPolynomial.getMonomials()) {
        int multiplicationCoefficient =
            monomialFromFirst.getCoefficient() * monomialFromSecond.getCoefficient();
        int multiplicationDegree = monomialFromFirst.getDegree() + monomialFromSecond.getDegree();
        result.addMonomial(new Monomial(multiplicationCoefficient, multiplicationDegree));
      }
    }

    log.info("Result for MULTIPLICATION is {}", result);
    return result;
  }

  public static Polynomial divisionQuotient(
      Polynomial firstPolynomial, Polynomial secondPolynomial) {
    Polynomial result = new Polynomial();
    Polynomial reminder = firstPolynomial.deepCopy();

    do {
      int degree = getDivisionDegree(reminder, secondPolynomial);
      int coefficient = getDivisionCoefficient(reminder, secondPolynomial);
      result.addMonomial(new Monomial(coefficient, degree));

      reminder = getNewReminder(reminder, secondPolynomial, new Monomial(coefficient, degree));
    } while (reminder.equals(ZERO_POLYNOMIAL) && (isDegreeBigger(reminder, secondPolynomial)));

    log.info("Result for DIVISION QUOTIENT is {}", result);
    return result;
  }

  public static Polynomial divisionRest(Polynomial firstPolynomial, Polynomial secondPolynomial) {
    Polynomial reminder = firstPolynomial.deepCopy();

    do {
      reminder = getNewReminder(reminder, secondPolynomial);
    } while (reminder.equals(ZERO_POLYNOMIAL) && (isDegreeBigger(reminder, secondPolynomial)));

    log.info("Result for DIVISION REST is {}", reminder);
    return reminder;
  }
}
