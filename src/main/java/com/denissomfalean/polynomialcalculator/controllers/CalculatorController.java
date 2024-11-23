package com.denissomfalean.polynomialcalculator.controllers;

import static com.denissomfalean.polynomialcalculator.constants.CalculatorConstants.*;
import static com.denissomfalean.polynomialcalculator.models.Polynomial.fromString;
import static com.denissomfalean.polynomialcalculator.service.CalculatorService.*;
import static com.denissomfalean.polynomialcalculator.utils.CalculatorUtils.getLabelTextFromMouseEvent;

import com.denissomfalean.polynomialcalculator.constants.CalculatorConstants;
import com.denissomfalean.polynomialcalculator.models.Polynomial;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/** Includes all the functionalities for the elements of the calculator GUI. */
@Setter
@Slf4j
public class CalculatorController {
  private static final String START_EXECUTION_ADDITION_OPERATION =
      "Starting execution for ADDITION operation between {} and {}";
  private static final String START_EXECUTION_SUBTRACTION_OPERATION =
      "Starting execution for SUBTRACTION operation between {} and {}";
  private static final String START_EXECUTION_MULTIPLICATION_OPERATION =
      "Starting execution for MULTIPLICATION operation between {} and {}";
  private static final String START_EXECUTION_DIVISION_QUOTIENT_OPERATION =
      "Starting execution for DIVISION operation to find the QUOTIENT between {} and {}";
  private static final String START_EXECUTION_DIVISION_REST_OPERATION =
      "Starting execution for DIVISION operation to find the REST between {} and {}";

  @FXML private Pane titlePane;
  @FXML private ImageView closeButton;
  @FXML private ImageView minimizeButton;
  @FXML private Label inputTextField;
  @FXML private Label equationLabelField;
  private String selectedOperation;
  private ArrayList<String> userInputTextChunks;
  private Polynomial firstPolynomial;
  private double coordinateX, coordinateY;

  /** The initialization of values and setup for screen movement by mouse event. */
  public void init(Stage stage) {
    initializeWindowMovementByMouseEvent(stage);
    initializeWindowButtons(stage);
    selectedOperation = PLUS_OPERATION_SYMBOL;
    firstPolynomial = new Polynomial();
    userInputTextChunks = new ArrayList<>();

    stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);
  }

  /**
   * Prints the text of the clicked label on the {@link CalculatorController#inputTextField }. It
   * includes the symbols: 0,1,2,3,4,5,6,7,8,9,x.
   *
   * @param mouseEvent contains the details of the element that was clicked on
   */
  @FXML
  public void onMonomialSymbolClicked(MouseEvent mouseEvent) {
    String labelText = getLabelTextFromMouseEvent(mouseEvent);
    addUserInput(labelText);
  }

  /**
   * Delivers to the {@link CalculatorController#equationLabelField } the result of the operation
   * based on the {@link CalculatorController#selectedOperation}, which was previously set. The
   * result obtained can later be reused for another operation as it is saved in the {@link
   * CalculatorController#firstPolynomial} , while the {@link CalculatorController#inputTextField }
   * is reset.
   */
  @FXML
  public void onEqualSymbolClicked() {
    executePolynomialOperation();
  }

  /**
   * Includes the functionality of special symbols. For the {@link
   * CalculatorConstants#SAVE_POLYNOMIAL_SYMBOL P}, there are 2 special cases. The first one
   * involves pressing the symbol when the input text only contains the operation characters:
   * addition - {@link CalculatorConstants#PLUS_OPERATION_SYMBOL +}, subtraction - {@link
   * CalculatorConstants#MINUS_OPERATION_SYMBOL -}, multiplication - {@link
   * CalculatorConstants#MULTIPLY_OPERATION_SYMBOL *}, division with quotient determination - {@link
   * CalculatorConstants#DIVIDE_OPERATION_SYMBOL div}, division with rest determination - {@link
   * CalculatorConstants#REST_OPERATION_SYMBOL mod}. This involves the selection of the desired
   * operation for the calculation process. The second one is used for saving into memory the first
   * polynomial described by the user in the {@link CalculatorController#inputTextField}, which will
   * be reinitialized. {@link CalculatorConstants#CLEAR_OPERATION_SYMBOL C} is used for clearing the
   * user input in case of mistakes.
   *
   * @param mouseEvent contains the details of the element that was clicked on
   */
  @FXML
  public void onOperationSymbolClicked(MouseEvent mouseEvent) {
    String labelText = getLabelTextFromMouseEvent(mouseEvent);
    switch (labelText) {
      case SAVE_POLYNOMIAL_SYMBOL:
        {
          savePolynomialToMemory();
          break;
        }
      case CLEAR_OPERATION_SYMBOL:
        {
          clearUserInput();
          break;
        }
    }
  }

  /**
   * Includes the functionalities of the calculator by using the keyboard keys. Keyboard key {@link
   * KeyCode#ENTER ENTER} will represent the functionality of calculator symbol {@link
   * CalculatorConstants#SAVE_POLYNOMIAL_SYMBOL P}.Keyboard key {@link KeyCode#DELETE DELETE} will
   * represent the functionality of calculator symbol {@link
   * CalculatorConstants#CLEAR_OPERATION_SYMBOL C}. Keyboard key {@link KeyCode#EQUALS EQUALS} will
   * trigger the chosen operation on the polynomials. For the rest of the used keyboard keys:
   * 0,1,2,3,4,5,6,7,8,9,X, the combination SHIFT + '6' for the symbol '^', the combination SHIFT +
   * '=' for the symbol '+', they will be printed on the display as elements of the polynomial, or
   * as selected operation for the operation symbols if {@link
   * CalculatorConstants#SAVE_POLYNOMIAL_SYMBOL P}/{@link KeyCode#ENTER ENTER} is pressed.
   *
   * @param keyEvent contains the details of the keyboard key that was clicked on
   */
  public void onKeyPressed(KeyEvent keyEvent) {
    if (keyEvent.isShiftDown() && !keyEvent.getCode().toString().equals("SHIFT")) {
      KeyCodeCombination kc =
          new KeyCodeCombination(keyEvent.getCode(), KeyCombination.CONTROL_ANY);
      switch (kc.getCode()) {
        case DIGIT6 -> addUserInput("^");
        case EQUALS -> addUserInput(PLUS_OPERATION_SYMBOL);
      }
    } else {
      switch (keyEvent.getCode()) {
        case ENTER -> savePolynomialToMemory();
        case DELETE -> clearUserInput();
        case EQUALS -> executePolynomialOperation();
        default -> addUserInput(keyEvent.getText().toLowerCase());
      }
    }
  }

  private void executePolynomialOperation() {
    userInputTextChunks.add(inputTextField.getText());
    Polynomial secondPolynomial = fromString(String.join("", userInputTextChunks));

    switch (selectedOperation) {
      case PLUS_OPERATION_SYMBOL:
        log.info(START_EXECUTION_ADDITION_OPERATION, firstPolynomial, secondPolynomial);
        equationLabelField.setText(addition(firstPolynomial, secondPolynomial).toString());
        break;
      case MINUS_OPERATION_SYMBOL:
        log.info(START_EXECUTION_SUBTRACTION_OPERATION, firstPolynomial, secondPolynomial);
        equationLabelField.setText(subtraction(firstPolynomial, secondPolynomial).toString());
        break;
      case MULTIPLY_OPERATION_SYMBOL:
        log.info(START_EXECUTION_MULTIPLICATION_OPERATION, firstPolynomial, secondPolynomial);
        equationLabelField.setText(multiplication(firstPolynomial, secondPolynomial).toString());
        break;
      case DIVIDE_OPERATION_SYMBOL:
        log.info(START_EXECUTION_DIVISION_QUOTIENT_OPERATION, firstPolynomial, secondPolynomial);
        equationLabelField.setText(divisionQuotient(firstPolynomial, secondPolynomial).toString());
        break;
      case REST_OPERATION_SYMBOL:
        log.info(START_EXECUTION_DIVISION_REST_OPERATION, firstPolynomial, secondPolynomial);
        equationLabelField.setText(divisionRest(firstPolynomial, secondPolynomial).toString());
        break;
    }
    firstPolynomial = fromString(equationLabelField.getText());
    clearUserInput();
  }

  private void addUserInput(String labelText) {
    resizeUserInputIfNeeded();
    if (inputTextField.getText().equals(INITIAL_CALCULATOR_VALUE)) {
      inputTextField.setText(labelText);
    } else {
      inputTextField.setText(inputTextField.getText() + labelText);
    }
  }

  private void savePolynomialToMemory() {
    if (isOperationSymbol(inputTextField.getText())) {
      setSelectedOperation(inputTextField.getText());
    } else {
      userInputTextChunks.add(inputTextField.getText());
      firstPolynomial = fromString(String.join("", userInputTextChunks));
      equationLabelField.setText("(" + firstPolynomial + ")");
    }
    clearUserInput();
  }

  private void resizeUserInputIfNeeded() {
    if (inputTextField.getText().length() > 15) {
      userInputTextChunks.add(inputTextField.getText());
      inputTextField.setText(INITIAL_CALCULATOR_VALUE);
      log.info(userInputTextChunks.toString());
    }
  }

  private boolean isOperationSymbol(String inputText) {
    switch (inputText) {
      case PLUS_OPERATION_SYMBOL,
          MINUS_OPERATION_SYMBOL,
          MULTIPLY_OPERATION_SYMBOL,
          DIVIDE_OPERATION_SYMBOL,
          REST_OPERATION_SYMBOL -> {
        return true;
      }
      default -> {
        return false;
      }
    }
  }

  private void setSelectedOperation(String labelText) {
    selectedOperation = labelText;
    if (equationLabelField.getText().equals(INITIAL_CALCULATOR_VALUE)) {
      equationLabelField.setText("( ) " + labelText + " ( )");
    } else {
      equationLabelField.setText(equationLabelField.getText() + " " + labelText + " ");
    }
  }

  private void initializeWindowMovementByMouseEvent(Stage stage) {
    titlePane.setOnMousePressed(
        mouseEvent -> {
          coordinateX = mouseEvent.getSceneX();
          coordinateY = mouseEvent.getSceneY();
        });
    titlePane.setOnMouseDragged(
        mouseEvent -> {
          stage.setX(mouseEvent.getScreenX() - coordinateX);
          stage.setY(mouseEvent.getScreenY() - coordinateY);
        });
  }

  private void initializeWindowButtons(Stage stage) {
    closeButton.setOnMouseClicked(mouseEvent -> stage.close());
    minimizeButton.setOnMouseClicked(mouseEvent -> stage.setIconified(true));
  }

  private void clearUserInput() {
    userInputTextChunks.clear();
    inputTextField.setText(INITIAL_CALCULATOR_VALUE);
  }
}
