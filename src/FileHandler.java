import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FileHandler {
    public static List<GradeBook> readData(Path path) throws IOException {
        List<GradeBook> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (!st.hasMoreTokens()) continue;
                String lastName = st.nextToken();
                String firstName = st.nextToken();
                String middleName = st.nextToken();
                int course = Integer.parseInt(st.nextToken());
                String group = st.nextToken();
                int sessionNum = Integer.parseInt(st.nextToken());
                String subject = st.nextToken();
                String type = st.nextToken();
                int grade = Integer.parseInt(st.nextToken());
                GradeBook student = null;
                for (GradeBook gb : list) {
                    if (gb.getLastName().equals(lastName) && gb.getFirstName().equals(firstName) && gb.getMiddleName().equals(middleName)) {
                        student = gb;
                        break;
                    }
                }
                if (student == null) {
                    student = new GradeBook(lastName, firstName, middleName, course, group);
                    list.add(student);
                }
                boolean isExam = type.equalsIgnoreCase("экзамен") || type.equalsIgnoreCase("exam");
                student.getOrCreateSession(sessionNum).addItem(subject, isExam, grade);
            }
        }
        return list;
    }

    public static void writeExcellent(Path path, List<GradeBook> students) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (GradeBook student : students) {
                if (student.isExcellent()) {
                    for (GradeBook.Session s : student.getSessions()) {
                        for (GradeBook.GradeItem item : s.getItems()) {
                            writer.write(student.getLastName() + " " + student.getFirstName() + " " + student.getMiddleName() + ", ");
                            writer.write("Курс: " + student.getCourse() + ", Группа: " + student.getGroup() + ", ");
                            writer.write("Сессия: " + s.getSessionNumber() + ", Предмет: " + item.getSubject());
                            if (item.isExam()) {
                                writer.write(", Оценка: " + item.getGrade());
                            }
                            writer.newLine();
                        }
                    }
                }
            }
        }
    }
    public static void writePassing(Path path, List<GradeBook> students) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (GradeBook student : students) {
                if (student.isPassing()) {
                    writer.write(student.getLastName() + " " + student.getFirstName() + " " + student.getMiddleName() + ": ");
                    for (GradeBook.Session s : student.getSessions()) {
                        writer.write("Сессия " + s.getSessionNumber() + " Ср.балл: " + String.format("%.2f", s.getAverageGrade()) + "; ");
                    }
                    writer.newLine();
                }
            }
        }
    }
}
