package Provider;

import ru.Calculator.Constants.SymbolsType;
import static ru.Calculator.Constants.Constants.*;
import java.util.ArrayList;
import java.util.List;


public class DataProvider {

    public static class Symbol {
        SymbolsType type;
        String value;

        public Symbol(SymbolsType type, String value) {
            this.type = type;
            this.value = value;

        }

        public Symbol(SymbolsType type, Character value) {
            this.type = type;
            this.value = value.toString();
        }

        public String toString() {
            return "Symbol(" +
                    "type=" + type +
                    ", value=" + value + '\'' +
                    '}';
        }
    }

    // Вспомогательный класс для упрощения прохода по символам и запоминания позиций.
    public static class SymbolBuffer {
        private int position;

        public List<Symbol> symbols;

        public SymbolBuffer(List<Symbol> symbols) {
            this.symbols = symbols;
        }

        //Получение символов и передвижение в право
        public Symbol next() {
            return symbols.get(position++);
        }

        public void back() {
            position--;
        }
        //Теущая позиция
        public int getPosition() {
            return position;
        }



    }

    //Функция лексического анализа
    public static List<Symbol> symbolAnalysis(String expText) {
        ArrayList<Symbol> symbols = new ArrayList<>();
        int position = 0;
        while (position < expText.length()) {
            char c = expText.charAt(position);
            switch (c) {
                case ADDITION:
                    symbols.add(new Symbol(SymbolsType.OP_PLUS, c));
                    position++;
                    continue;
                case SUBTRACTION:
                    symbols.add(new Symbol(SymbolsType.OP_MINUS, c));
                    position++;
                    continue;
                case MULTIPLICATION:
                    symbols.add(new Symbol(SymbolsType.OP_MUL, c));
                    position++;
                    continue;
                case DIVISION:
                    symbols.add(new Symbol(SymbolsType.OP_DIV, c));
                    position++;
                    continue;
                default:
                    if (c <= NINE && c >= NULL) {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            position++;
                            if (position >= expText.length()) {
                                break;
                            }
                            c = expText.charAt(position);
                        } while (c <= NINE && c >= NULL);
                        symbols.add(new Symbol(SymbolsType.NUMBER, sb.toString()));
                    } else {
                        if (c != ' ') {
                            throw new RuntimeException("Unexpected character: " + c);
                        }
                        position++;
                    }

            }




        }
        symbols.add(new Symbol(SymbolsType.EOF, END));
        return symbols;
    }

    public static int expr(SymbolBuffer symbols) {
        Symbol symbol = symbols.next();
        if (symbol.type == SymbolsType.EOF) {
            return 0;
        } else {
            symbols.back();
            return plusminus(symbols);
        }
    }

    public static int plusminus(SymbolBuffer symbols) {
        int value = multdiv(symbols);
        while (true) {
            Symbol symbol = symbols.next();
            switch (symbol.type) {
                case OP_PLUS -> value += multdiv(symbols);
                case OP_MINUS -> value -= multdiv(symbols);
                default -> {
                    symbols.back();
                    return value;
                }
            }
        }


    }
    public static int multdiv(SymbolBuffer symbols) {
        int value = factor(symbols);
        if (value > 0 && value <= 10) {
            while (true) {
                Symbol symbol = symbols.next();
                switch (symbol.type) {
                    case OP_MUL -> value *= factor(symbols);
                    case OP_DIV -> value /= factor(symbols);
                    default -> {
                        symbols.back();
                        return value;
                    }
                }
            }
        } else throw new RuntimeException("Symbol: " + value
                + " at position: " + symbols.getPosition()+ ", > 10 or 0 ");
    }

    public static int factor(SymbolBuffer symbols) {
        Symbol symbol = symbols.next();
        if (symbol.type == SymbolsType.NUMBER) {
            return Integer.parseInt(symbol.value);
        }
        throw new RuntimeException("1Unexpected symbol: " + symbol.value
                + "at position: " + symbols.getPosition());
    }

}

