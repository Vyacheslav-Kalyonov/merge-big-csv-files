import java.io.*;
import java.util.*;

public class BigFileSorter {

    private final String path;
    private final int sizeFileForSorting;
    private final Sort sort = new Sort();
    private String firstLineInFile;

    public BigFileSorter(String path, int sizeFileForSorting) {
        this.path = path;
        this.sizeFileForSorting = sizeFileForSorting;
    }

    public File getSortingFile() {
        Queue<File> files = getFilesForMerging();
        mergeFiles(files);

        try {
            // Код, который может привести к ошибке
            File file = files.remove();
            file.renameTo(new File("output.csv"));
            return file;

        } catch (NoSuchElementException e) {
            System.out.println("Файл не содержит строк.");
            return null;
        }
    }

    /**
     * Возвращает очередь файлов для слияния.
     *
     * @return очередь файлов для слияния
     */
    private Queue<File> getFilesForMerging() {
        Queue<File> files = new ArrayDeque<>();

        File file = new File(path);
        int countFiles = 0;
        int countLines = 0;

        try (Scanner scanner = new Scanner(file)) {
            List<String> arr = new ArrayList<>();
            this.firstLineInFile = scanner.nextLine();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                arr.add(line);
                countLines++;

                if (countLines >= sizeFileForSorting) {
                    addSortingData(arr, countFiles);
                    files.add(getProxyFile(countFiles));
                    arr = new ArrayList<>();
                    countFiles++;
                    countLines = 0;
                }
            }

            if (countLines != 0) {
                addSortingData(arr, countFiles);
                files.add(getProxyFile(countFiles));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    /**
     * Объединяет отсортированные файлы в один отсортированный файл.
     *
     * @param proxyFiles очередь с отсортированными файлами
     */
    private void mergeFiles(Queue<File> proxyFiles) {
        int countFiles = proxyFiles.size();

        while (proxyFiles.size() > 1) {
            File firstFileForMerging = proxyFiles.remove();
            File secondFileForMerging = proxyFiles.remove();
            firstFileForMerging.deleteOnExit();
            secondFileForMerging.deleteOnExit();

            mergeTwoFiles(firstFileForMerging, secondFileForMerging, countFiles, proxyFiles.isEmpty());

            proxyFiles.add(getProxyFile(countFiles));
            countFiles++;
        }
    }

    /**
     * Сортирует и добавляет данные в прокси файл
     *
     * @param arr        отсортированный список строк
     * @param countFiles номер файла-прокси
     */
    private void addSortingData(List<String> arr, int countFiles) {
        File proxyFile = getProxyFile(countFiles);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(proxyFile))) {
            sort.quickSort(arr, 0, arr.size() - 1);

            for (int i = 0; i < arr.size() - 1; i++) {
                writer.write(arr.get(i) + "\n");
            }
            writer.write(arr.get(arr.size() - 1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Объединяет два файла в один отсортированный файл.
     *
     * @param firstFile  первый файл
     * @param secondFile второй файл
     * @param countFiles счетчик файлов
     * @param checkSize  флаг длины очереди (для вноса первой строки csv)
     */
    private void mergeTwoFiles(File firstFile, File secondFile, int countFiles, boolean checkSize) {
        File proxyFile = getProxyFile(countFiles);

        try (Scanner scannerForFirst = new Scanner(firstFile);
             Scanner scannerForSecond = new Scanner(secondFile);
             BufferedWriter writer = new BufferedWriter(new FileWriter(proxyFile))) {

            if (checkSize) {
                writer.write(firstLineInFile + "\n");
            }

            int checkFileEnded = 0;
            String firstFilesLine = scannerForFirst.nextLine();
            String secondFilesLine = scannerForSecond.nextLine();
            while (scannerForFirst.hasNext() || scannerForSecond.hasNext()) {

                if (Utils.getNumFromLine(firstFilesLine) < Utils.getNumFromLine(secondFilesLine)) {
                    writer.write(firstFilesLine + "\n");

                    if (!scannerForFirst.hasNext()) {
                        checkFileEnded = 1;
                        break;
                    }
                    firstFilesLine = scannerForFirst.nextLine();

                } else {
                    writer.write(secondFilesLine + "\n");

                    if (!scannerForSecond.hasNext()) {

                        checkFileEnded = 2;
                        break;
                    }
                    secondFilesLine = scannerForSecond.nextLine();
                }
            }

            // ситуация, когда закончился первый файл
            if (checkFileEnded == 1) {
                inputDataInFile(scannerForSecond, writer, secondFilesLine);
                // ситуация, когда закончился второй файл
            } else if (checkFileEnded == 2) {
                inputDataInFile(scannerForFirst, writer, firstFilesLine);
                // ситуация, когда закончились оба файла во время цикла, но строки не были еще внесены
            } else {
                if (Utils.getNumFromLine(firstFilesLine) < Utils.getNumFromLine(secondFilesLine)) {
                    writer.write(firstFilesLine + "\n");
                    writer.write(secondFilesLine);
                } else {
                    writer.write(secondFilesLine + "\n");
                    writer.write(firstFilesLine);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает новый файл-прокси с указанным номером.
     *
     * @param countFiles номер файла-прокси
     * @return новый файл-прокси
     */
    private File getProxyFile(int countFiles) {
        return new File("number" + countFiles + ".csv");
    }

    /**
     * Записывает данные во входной файл.
     *
     * @param scannerFile сканер для чтения данных из входного файла
     * @param writer      буферизованный писатель для записи данных в выходной файл
     * @param line        текущая строка, которую нужно записать
     */
    private void inputDataInFile(Scanner scannerFile, BufferedWriter writer, String line) {
        try {
            if (!scannerFile.hasNext()) {
                writer.write(line);
                return;
            }
            writer.write(line + "\n");

            while (scannerFile.hasNext()) {
                line = scannerFile.nextLine();

                if (!scannerFile.hasNext()) {
                    writer.write(line);
                    break;
                }

                writer.write(line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



