import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by nishu on 3/1/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Proctor proctor = new Proctor();
        proctor.readQuestions();
    }
}
