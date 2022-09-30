import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Utils {

    public static void sortStrings(List<String> strings) {
        strings.sort(Comparator.reverseOrder());
    }
}