package helper;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class DataProvider {
    public static Stream<Arguments> checkingNameAndPriceProvider() {
        return Stream.of(Arguments.of("HP", "Hp", "Lenovo", "LENOVO", "ThinkPad", "Thinkpad", "lenovo", "Lenоvо", "Lenоvo", "Lenovо", "НР", "HР", "НP", "hp"));
    }
}
