import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by nishu on 3/1/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Proctor proctor = new Proctor(1, 2);
        proctor.readQuestions();
    }
}
