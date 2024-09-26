module com.denissomfalean.polynomialcalculator {
  requires javafx.controls;
  requires javafx.fxml;

  opens com.denissomfalean.polynomialcalculator to
      javafx.fxml;

  exports com.denissomfalean.polynomialcalculator;
}
