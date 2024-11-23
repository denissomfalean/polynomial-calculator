module com.denissomfalean.polynomialcalculator {
  requires javafx.controls;
  requires javafx.fxml;
  requires static lombok;
  requires org.slf4j;
  requires org.apache.commons.lang3;

  opens com.denissomfalean.polynomialcalculator.views to
      javafx.graphics;
  opens com.denissomfalean.polynomialcalculator.controllers to
      javafx.fxml,
      org.slf4j;

  exports com.denissomfalean.polynomialcalculator.views to
      javafx.graphics;
  exports com.denissomfalean.polynomialcalculator.controllers to
      javafx.fxml,
      org.slf4j;
}
