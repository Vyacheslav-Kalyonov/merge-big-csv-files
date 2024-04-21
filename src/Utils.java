public class Utils {

    public static char SEPARATOR = ',';

    /**
     * Извлекает число из строки.
     *
     * @param line строка, содержащая число
     * @return число
     */
    public static int getNumFromLine(String line) {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == SEPARATOR) {
                return Integer.parseInt(String.valueOf(number));
            }
            number.append(line.charAt(i));
        }
        return 0;
    }
}
