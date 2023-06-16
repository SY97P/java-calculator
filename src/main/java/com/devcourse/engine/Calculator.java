package com.devcourse.engine;

import com.devcourse.engine.computer.Computer;
import com.devcourse.engine.computer.SimpleComputer;
import com.devcourse.engine.model.computer.Computer;
import com.devcourse.engine.model.converter.Converter;
import com.devcourse.engine.model.exception.InvalidInputException;
import com.devcourse.engine.model.histories.Histories;
import com.devcourse.engine.model.validator.Validator;
import com.devcourse.engine.historian.Historian;
import com.devcourse.engine.validator.SimpleValidator;
import com.devcourse.engine.validator.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.devcourse.engine.model.exception.InvalidInputException.INVALID_MENU;
import static com.devcourse.engine.model.exception.InvalidInputException.NO_HISTORY;

public class Calculator implements Runnable {

    private final Input input;
    private final Output output;
    private final Histories histories;
    private final Validator validator;
    private final Converter converter;
    private final Computer computer;

    public Calculator(
            Input input,
            Output output,
            Histories histories,
            Validator validator,
            Converter converter,
            Computer computer
            ) {
        this.input = input;
        this.output = output;
        this.histories = histories;
        this.validator = validator;
        this.converter = converter;
        this.computer = computer;
    }

    @Override
    public void run() {
        label:
        while (true) {
            String menu = input.inputMenu();

            try {
                validMenuCheck(menu);

                switch (menu) {
                    case "0":
                        output.endGame();
                        break label;
                    case "1":
                        checkHasHistory();
                        printHistory();
                        break;
                    case "2":
                        calculate();
                        break;
                }
            } catch (InvalidInputException e) {
                output.printError(e.getMessage());
            }
        }
    }

    private void calculate() {
        String userInput = input.inputExpression();
        List<String> infixExpression = validator.validate(userInput);
        double result = computer.compute(
                converter.convert(infixExpression)
        );
        saveAndPrint(infixExpression, result);
    }

    private void saveAndPrint(List<String> infixExpression, double result) {
        histories.saveHistory(infixExpression, result);
        output.showResult(result);
    }

    private void printHistory() {
        IntStream.rangeClosed(1, histories.getLastIndex())
                .forEach(i -> output.showHistory(histories.getHistory(i)));
        output.showHistory("");
    }

    private void checkHasHistory() {
        if (histories.getLastIndex() < 1)
            throw new InvalidInputException(NO_HISTORY);
    }

    private void validMenuCheck(String menu) throws InvalidInputException {
        if (menu.length() > 1 || !Arrays.asList("0", "1", "2").contains(menu))
            throw new InvalidInputException(INVALID_MENU);
    }
}
