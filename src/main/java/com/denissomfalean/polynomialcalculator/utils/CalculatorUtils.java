package com.denissomfalean.polynomialcalculator.utils;

import com.denissomfalean.polynomialcalculator.PolynomialCalculatorApplication;
import com.denissomfalean.polynomialcalculator.models.Monomial;
import com.denissomfalean.polynomialcalculator.models.Polynomial;
import com.denissomfalean.polynomialcalculator.service.CalculatorService;
import java.net.URL;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class CalculatorUtils {
  public static URL getResourceFileURL(String resourceFilePath) {
    return PolynomialCalculatorApplication.class.getResource(resourceFilePath);
  }

  public static String getLabelTextFromMouseEvent(MouseEvent mouseEvent) {
    Pane pane = ((Pane) mouseEvent.getSource());
    Optional<Node> children = pane.getChildren().stream().findFirst();
    if (children.isPresent()) {
      return ((Label) children.get()).getText();
    }

    return StringUtils.EMPTY;
  }

  public static boolean isDegreeBigger(Polynomial firstPolynomial, Polynomial secondPolynomial) {
    return firstPolynomial.getMonomials().first().getDegree()
            >= secondPolynomial.getMonomials().first().getDegree()
        && firstPolynomial.getMonomials().first().getDegree() != 0;
  }

  public static Polynomial getNewReminder(
      Polynomial firstPolynomial, Polynomial secondPolynomial, Monomial monomialFromDivision) {
    Polynomial multiplicationResult =
        CalculatorService.multiplication(secondPolynomial, new Polynomial(monomialFromDivision));
    return CalculatorService.subtraction(firstPolynomial, multiplicationResult);
  }

  public static Polynomial getNewReminder(Polynomial firstPolynomial, Polynomial secondPolynomial) {
    int degree = getDivisionDegree(firstPolynomial, secondPolynomial);
    int coefficient = getDivisionCoefficient(firstPolynomial, secondPolynomial);
    Polynomial multiplicationResult =
        CalculatorService.multiplication(
            secondPolynomial, new Polynomial(new Monomial(coefficient, degree)));

    return CalculatorService.subtraction(firstPolynomial, multiplicationResult);
  }

  public static int getDivisionCoefficient(
      Polynomial firstPolynomial, Polynomial secondPolynomial) {
    return firstPolynomial.getMonomials().first().getCoefficient()
        / secondPolynomial.getMonomials().first().getCoefficient();
  }

  public static int getDivisionDegree(Polynomial firstPolynomial, Polynomial secondPolynomial) {
    return firstPolynomial.getMonomials().first().getDegree()
        - secondPolynomial.getMonomials().first().getDegree();
  }
}
