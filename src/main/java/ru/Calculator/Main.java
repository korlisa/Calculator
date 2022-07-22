package ru.Calculator;


import Provider.DataProvider;

import java.util.List;
import java.util.Scanner;
import static Provider.DataProvider.symbolAnalysis;
import static ru.Calculator.Constants.Constants.*;


public class Main {

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println(INPUT);
        String expressionText= scanner.nextLine();
        List<DataProvider.Symbol> symbols = symbolAnalysis(expressionText);
//        System.out.println(symbols);
        DataProvider.SymbolBuffer symbolsBuffer = new DataProvider.SymbolBuffer(symbols);
        System.out.println(OUTPUT + new DataProvider().expr(symbolsBuffer));

    }
}
