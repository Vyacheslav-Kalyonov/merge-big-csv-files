import java.io.File;

public class Main {

    public static void main(String[] args) {
        BigFileSorter bigFileMerger = new BigFileSorter("input.csv", 6);
        File resultFile = bigFileMerger.getSortingFile();

        System.out.println("Имя отсортированного файла : " + resultFile.getName());
    }
}