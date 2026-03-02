package com.mycompany.pizzacalculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PizzaCalculator extends Application {

    private final TextArea receiptArea = new TextArea();

    @Override
    public void start(Stage stage) {

        // ===== Controls =====
        ComboBox<PizzaSize> sizeBox = new ComboBox<>();
        sizeBox.getItems().addAll(PizzaSize.values());
        sizeBox.setValue(PizzaSize.MEDIUM);
        sizeBox.setMaxWidth(Double.MAX_VALUE);

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 10, 1);
        quantitySpinner.setEditable(false);
        quantitySpinner.setPrefWidth(220);
        quantitySpinner.setMaxWidth(220);

        TextField taxField = new TextField("0.08");
        taxField.setMaxWidth(Double.MAX_VALUE);

        TextField couponField = new TextField();
        couponField.setMaxWidth(Double.MAX_VALUE);

        // ===== Toppings (2 columns x 3 rows) =====
        GridPane toppingGrid = new GridPane();
        toppingGrid.setHgap(18);
        toppingGrid.setVgap(8);

        List<CheckBox> toppingBoxes = new ArrayList<>();

        Topping[] toppings = Topping.values();
        for (int i = 0; i < toppings.length; i++) {
            Topping t = toppings[i];

            CheckBox cb = new CheckBox(t.getDisplayName());
            cb.setUserData(t); // store which topping this checkbox represents

            toppingBoxes.add(cb);

            int row = i % 3;     // 0,1,2 then repeats
            int col = i / 3;     // 0 for first 3, 1 for next 3
            toppingGrid.add(cb, col, row);
        }

        Button calculateBtn = new Button("Calculate");
        calculateBtn.getStyleClass().add("btn-primary");
        calculateBtn.setPrefWidth(160);

        Button exportBtn = new Button("Export Receipt");
        exportBtn.getStyleClass().add("btn-success");
        exportBtn.setPrefWidth(160);

        HBox buttonRow = new HBox(12, calculateBtn, exportBtn);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        receiptArea.setWrapText(false);
        receiptArea.setEditable(false);
        receiptArea.setPrefRowCount(6);

        // ===== Actions =====
        calculateBtn.setOnAction(e -> {

            List<Topping> selected = new ArrayList<>();
            for (CheckBox cb : toppingBoxes) {
                if (cb.isSelected()) {
                    selected.add((Topping) cb.getUserData());
                }
            }

            BigDecimal taxRate;
            try {
                taxRate = new BigDecimal(taxField.getText().trim());
            } catch (Exception ex) {
                showAlert("Invalid Tax Rate", "Please enter a valid tax rate (example: 0.08).");
                return;
            }

            PriceBreakdown breakdown = PriceCalculator.calculate(
                    sizeBox.getValue(),
                    selected,
                    quantitySpinner.getValue().intValue(),
                    taxRate,
                    couponField.getText()
            );

            receiptArea.clear();
            breakdown.receiptLines().forEach(line -> receiptArea.appendText(line + "\n"));
        });

        exportBtn.setOnAction(e -> exportReceipt(stage));

        // ===== Header (Title + Pizza Icon) =====
        Label title = new Label("Pizza Calculator");
        title.getStyleClass().add("app-title");

        Label pizzaIcon = new Label("🍕");
        pizzaIcon.getStyleClass().add("app-icon");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(10, title, spacer, pizzaIcon);
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);

        // ===== Two-column row for Tax + Coupon =====
        Label taxLabel = new Label("Tax Rate:");
        Label couponLabel = new Label("Coupon Code:");

        VBox taxCol = new VBox(6, taxLabel, taxField);
        VBox couponCol = new VBox(6, couponLabel, couponField);

        HBox row2 = new HBox(12, taxCol, couponCol);
        row2.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(taxCol, Priority.ALWAYS);
        HBox.setHgrow(couponCol, Priority.ALWAYS);

        // ===== Form "Card" =====
        VBox form = new VBox(12,
                new Label("Size:"), sizeBox,
                new Label("Toppings:"), toppingGrid,
                new Label("Quantity:"), quantitySpinner,
                row2,
                buttonRow,
                new Label("Receipt:"),
                receiptArea
        );
        form.getStyleClass().add("card");
        form.setPadding(new Insets(4));

        // ===== Root =====
        VBox root = new VBox(14, header, form);
        root.getStyleClass().add("root");

        Scene scene = new Scene(root, 520, 620);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Pizza Calculator");
        stage.setScene(scene);
        stage.show();
    }

    private void exportReceipt(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Receipt");
        chooser.setInitialFileName("receipt.txt");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = chooser.showSaveDialog(stage);
        if (file == null) return;

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print(receiptArea.getText());
        } catch (Exception ex) {
            showAlert("Export Failed", "Could not save receipt:\n" + ex.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}