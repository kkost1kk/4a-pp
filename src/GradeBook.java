import java.util.ArrayList;
import java.util.List;

public class GradeBook {
    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final int course;
    private final String group;
    private final List<Session> sessions;
    public GradeBook(String lastName, String firstName, String middleName, int course, String group) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.course = course;
        this.group = group;
        this.sessions = new ArrayList<>();
    }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public int getCourse() { return course; }
    public String getGroup() { return group; }
    public List<Session> getSessions() { return sessions; }
    public Session getOrCreateSession(int sessionNumber) {
        for (Session s : sessions) {
            if (s.getSessionNumber() == sessionNumber) {
                return s;
            }
        }
        Session newSession = new Session(sessionNumber);
        sessions.add(newSession);
        return newSession;
    }
    public boolean isExcellent() {
        if (sessions.isEmpty()) return false;
        for (Session s : sessions) {
            for (GradeItem item : s.getItems()) {
                if (item.isExam()) {
                    if (item.getGrade() < 9) return false;
                } else {
                    if (item.getGrade() == 0) return false;
                }
            }
        }
        return true;
    }
    public boolean isPassing() {
        for (Session s : sessions) {
            for (GradeItem item : s.getItems()) {
                if (item.isExam() && item.getGrade() < 4) return false;
                if (!item.isExam() && item.getGrade() == 0) return false;
            }
        }
        return true;
    }
    public static class Session {
        private final int sessionNumber;
        private final List<GradeItem> items;
        public Session(int sessionNumber) {
            this.sessionNumber = sessionNumber;
            this.items = new ArrayList<>();
        }
        public int getSessionNumber() { return sessionNumber; }
        public List<GradeItem> getItems() { return items; }
        public void addItem(String subject, boolean isExam, int grade) {
            items.add(new GradeItem(subject, isExam, grade));
        }
        public double getAverageGrade() {
            int count = 0;
            int sum = 0;
            for (GradeItem item : items) {
                if (item.isExam()) {
                    sum += item.getGrade();
                    count++;
                }
            }
            return count == 0 ? 0.0 : (double) sum / count;
        }
    }
    public static class GradeItem {
        private final String subject;
        private final boolean isExam;
        private final int grade;
        public GradeItem(String subject, boolean isExam, int grade) {
            this.subject = subject;
            this.isExam = isExam;
            this.grade = grade;
        }
        public String getSubject() { return subject; }
        public boolean isExam() { return isExam; }
        public int getGrade() { return grade; }
    }
}
