import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<GradeBook> students = FileHandler.readData(Paths.get("students.txt"));
            FileHandler.writeExcellent(Paths.get("output1.txt"), students);
            FileHandler.writePassing(Paths.get("output2.txt"), students);
            System.out.println("записались данные по студентам в файлы");
        } catch (IOException e) {
            System.err.println("input/output error");
        }
    }
}
