import java.util.List;

public class Sort {

    /**
     * Сортирует список строк с помощью быстрой сортировки.
     *
     * @param arr список строк
     * @param low индекс начала сортируемой части списка
     * @param high индекс конца сортируемой части списка
     */
    public void quickSort(List<String> arr, int low, int high) {
        if (low < high) {
            // Разбиение массива
            int partitionIndex = partition(arr, low, high);

            // Рекурсивно сортировка левой и правой частей
            quickSort(arr, low, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, high);
        }
    }

    /**
     * Разбивает список строк на две части: элементы, меньшие опорного элемента, и элементы, большие или равные опорному элементу.
     *
     * @param arr список строк
     * @param low индекс начала сортируемой части списка
     * @param high индекс конца сортируемой части списка
     * @return индекс опорного элемента
     */
    public int partition(List<String> arr, int low, int high) {
        // Первый элемент в качестве опорного элемента
        int pivot = Utils.getNumFromLine(arr.get(high));
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            // Если первый элемент текущей строки меньше опорного элемента
            if (Utils.getNumFromLine(arr.get(j)) < pivot) {
                i++;

                // Обмен строк
                String temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }

        // Обмен строк
        String temp = arr.get(i + 1);
        arr.set(i + 1, arr.get(high));
        arr.set(high, temp);

        return (i + 1);
    }
}
