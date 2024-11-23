package com.denissomfalean.polynomialcalculator.models;

import lombok.*;

/**
 * Represents the base building block for Polynomials, holding the information regarding degree and
 * its coefficient.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Monomial implements Comparable<Monomial> {
  private int coefficient;
  private int degree;

  public void addCoefficient(int additionalCoefficient) {
    coefficient += additionalCoefficient;
  }

  @Override
  public String toString() {
    if (degree == 0) {
      return Integer.toString(coefficient);
    } else if (coefficient == 1) {
      return "x^" + degree;
    } else {
      return coefficient + "x^" + degree;
    }
  }

  @Override
  public int compareTo(Monomial monomial) {
    return Integer.compare(monomial.getDegree(), degree);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    Monomial monomial = (Monomial) object;
    return degree == monomial.degree;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(degree);
  }
}
