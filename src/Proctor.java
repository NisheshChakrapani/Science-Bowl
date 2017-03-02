import java.io.*;
import java.util.Scanner;

/**
 * Created by nishu on 3/1/2017.
 */
public class Proctor {
    private boolean correct = true;
    private Stopwatch stopwatch = new Stopwatch();
    private int set;
    private int round;
    public Proctor(int set, int round) {
        this.set = set;
        this.round = round;
    }

    public void readQuestions() throws IOException {
        String filepath = "Set" + set + "Round" + round + ".txt";
        File file = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        Scanner user = new Scanner(System.in);
        boolean done = false;
        int score = 0;
        int qCount = 0;
        while (!done) {
            stopwatch.reset();
            String questionType = br.readLine();
            try {
                questionType.isEmpty();
            } catch (NullPointerException e) {
                skipQuestion(br);
                done = true;
            }
            if (!correct && questionType.equals("BONUS")) {
                skipQuestion(br);
            } else if (!done && qCount<25) {
                String topic = br.readLine();
                String answerType = br.readLine();
                System.out.println(questionType + ": " + topic + " " + answerType);
                String question = br.readLine();
                System.out.println(question);
                if (answerType.equals("MULTIPLE CHOICE")) {
                    String w = br.readLine();
                    String x = br.readLine();
                    String y = br.readLine();
                    String z = br.readLine();
                    System.out.println(w);
                    System.out.println(x);
                    System.out.println(y);
                    System.out.println(z);
                }
                stopwatch.giveBuffer(1000);
                stopwatch.start();
                String userAnswer = user.nextLine();
                stopwatch.stop();
                boolean outOfTime = false;
                if (questionType.equals("TOSS UP")) {
                    if (stopwatch.getElapsedTimeMillis()>5000) {
                        outOfTime = true;
                    }
                } else if (questionType.equals("BONUS")) {
                    if (stopwatch.getElapsedTimeMillis()>20000) {
                        outOfTime = true;
                    }
                }
                String[] correctAnswers = br.readLine().split(" OR ");
                if (outOfTime) {
                    System.out.println("Your response was out of time.");
                    correct = false;
                } else {
                    int wrongCount = 0;
                    for (String correctAnswer : correctAnswers) {
                        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                            System.out.println("Correct.");
                            correct = true;
                            if (questionType.equals("TOSS UP")) {
                                score += 4;
                            } else {
                                score += 10;
                            }
                        } else {
                            wrongCount++;
                        }
                    }
                    if (wrongCount == correctAnswers.length) {
                        System.out.println("Incorrect.");
                        correct = false;
                    }
                }
                System.out.println("Current score: " + score);
                System.out.println("--------------------------------------");
            }
            String skip = br.readLine();
            qCount++;
        }
        System.out.println("Thanks for booling.");
        System.out.print("Final score: " + score + ". ");

    }

    private void skipQuestion(BufferedReader br) throws IOException {
        String s1 = br.readLine();
        String s2 = br.readLine();
        String s3 = br.readLine();
        String s4 = br.readLine();
        if (s2.equals("MULTIPLE CHOICE")) {
            String s5 = br.readLine();
            String s6 = br.readLine();
            String s7 = br.readLine();
            String s8 = br.readLine();
        }
        correct = true;
    }

    private void analyzePerformance(int score) {
        if (score < 20) {
            System.out.println("Who are you, Kennewick?.");
        } else if (score < 40) {
            System.out.println("Could have been worse.");
        } else if (score < 70) {
            System.out.println("Hey, that's pretty good.");
        } else if (score < 100) {
            System.out.println("You would probably win most games.");
        } else if (score < 350) {
            System.out.println("Nationals-bound, I presume.");
        } else {
            System.out.println("Dang, you're Skyline-Team-1-level good.");
        }
    }
}
