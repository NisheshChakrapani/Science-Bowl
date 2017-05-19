import org.apache.commons.lang3.text.WordUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.awt.*;

/**
 * Created by nishu on 3/1/2017.
 */
public class Proctor {
    private boolean fastMode;
    private boolean visualMode;
    private final double OFFSET_MULTIPLIER = 30;
    private boolean correct = true;
    private Stopwatch stopwatch = new Stopwatch();
    private int set;
    private int round;
    private final int SLOW_MODE_TOSS_UP = 5000;
    private final int SLOW_MODE_BONUS = 20000;
    private final int FAST_MODE_TOSS_UP = 10000;
    private final int FAST_MODE_BONUS = 25000;
    private ArrayList<Question> unread = new ArrayList<>();

    public Proctor() throws IOException, InterruptedException {
        getSettings();
        readQuestions();
    }

    private void getSettings() throws IOException {
        Scanner scan = new Scanner(System.in);
        boolean modeFound = false;
        while (!modeFound) {
            System.out.print("Play casual mode or round mode? Type 'c' for casual or 'r' for round.\n> ");
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("c")) {
                modeFound = true;
                System.out.println();
                playCasualMode();
            } else if (input.equalsIgnoreCase("r")) {
                modeFound = true;
            } else {
                System.out.println("Not a valid answer.");
            }
        }

        boolean setFound = false;
        while (!setFound) {
            System.out.print("Enter set number from 1 to 8\n> ");
            String input = scan.nextLine();
            try {
                set = Integer.parseInt(input);
                if (set < 1 || set > 8) {
                    System.out.println("Sorry, that set of science bowl rounds does not exist.");
                } else {
                    System.out.println("Set " + set + " chosen.");
                    setFound = true;
                }
            } catch (NumberFormatException | NoSuchElementException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        boolean roundFound = false;
        int maxRound;
        while (!roundFound) {
            if (set == 5 || set == 6) {
                System.out.print("This set contains 15 science bowl rounds. Enter a number from 1 to 15\n> ");
                maxRound = 15;
            } else {
                System.out.print("This set contains 17 science bowl rounds. Enter a number from 1 to 17\n> ");
                maxRound = 17;
            }
            String input = scan.nextLine();
            try {
                round = Integer.parseInt(input);
                if (round > maxRound || round <= 0) {
                    System.out.println("That is not a valid round number.");
                } else {
                    System.out.println("Round " + round + " chosen.");
                    roundFound = true;
                }
            } catch (NumberFormatException | NoSuchElementException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        boolean speedFound = false;
        while (!speedFound) {
            System.out.print("Fast or slow question speed? Type 'fast' for fast or 'slow' for slow.\n> ");
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("fast")) {
                fastMode = true;
                speedFound = true;
            } else if (input.equalsIgnoreCase("slow")) {
                fastMode = false;
                speedFound = true;
            } else {
                System.out.println("Not a valid answer.");
            }
        }
    }

    private void readQuestions() throws IOException, InterruptedException {
        readRules();
        String filepath = ("set " + set + "\\Set" + set + "Round" + round + ".txt");
        File file = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        Scanner user = new Scanner(System.in);
        boolean done = false;
        int score = 0;
        int qCount = 0;
        boolean asked = false;
        while (!done) {
            boolean interrupt = false;
            stopwatch.reset();
            String questionType = br.readLine().trim();
            try {
                questionType.isEmpty();
            } catch (NullPointerException e) {
                done = true;
            }
            if (!correct && !questionType.contains("TOSS UP")) {
                skipQuestion(br);
            } else if (!done) {
                int totalChars = 0;
                String topic = br.readLine();
                String answerType = br.readLine();
                String question = br.readLine();
                totalChars = question.length();
                Question q = new Question(questionType, topic, answerType, question);
                //System.out.println(questionType);
                //System.out.println(topic);
                //System.out.println(answerType);
                //System.out.println(question);
                if (answerType.equals("MULTIPLE CHOICE")) {
                    String w = br.readLine();
                    String x = br.readLine();
                    String y = br.readLine();
                    String z = br.readLine();
                    q.setAnswerW(w);
                    q.setAnswerY(y);
                    q.setAnswerX(x);
                    q.setAnswerZ(z);
                    //System.out.println(w);
                    //System.out.println(x);
                    //System.out.println(y);
                    //System.out.println(z);
                    totalChars+=w.length();
                    totalChars+=x.length();
                    totalChars+=y.length();
                    totalChars+=z.length();
                }
                q.printQuestion();
                asked = false;
                if (!fastMode) {
                    Thread.sleep((long) (totalChars * OFFSET_MULTIPLIER));
                    System.out.println("TIMER START");
                }
                stopwatch.start();
                String userAnswer = user.nextLine();
                stopwatch.stop();
                boolean outOfTime = false;
                if (questionType.contains("TOSS UP")) {
                    qCount++;
                    long elapsed = stopwatch.getElapsedTimeMillis();
                    if (fastMode && elapsed>FAST_MODE_TOSS_UP) {
                        outOfTime = true;
                    } else if (!fastMode && elapsed>SLOW_MODE_TOSS_UP) {
                        outOfTime = true;
                    }
                    if (!fastMode && elapsed == 0) {
                        interrupt = true;
                    }
                } else  {
                    long elapsed = stopwatch.getElapsedTimeMillis();
                    if (fastMode && elapsed>FAST_MODE_BONUS) {
                        outOfTime = true;
                    } else if (!fastMode && elapsed>SLOW_MODE_BONUS) {
                        outOfTime = true;
                    }
                }
                String answers = br.readLine();
                String[] correctAnswers = answers.split(" OR ");
                if (outOfTime) {
                    System.out.println("Your response was out of time.");
                    System.out.println("Accepted answer(s): " + answers);
                    correct = false;
                } else {
                    int wrongCount = 0;
                    for (String correctAnswer : correctAnswers) {
                        if (userAnswer.equalsIgnoreCase(correctAnswer.trim())) {
                            System.out.println("Correct.");
                            correct = true;
                            if (questionType.contains("TOSS UP")) {
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
                        if (interrupt && questionType.contains("TOSS UP")) {
                            System.out.println("You interrupted and got it wrong, so you lose 4 points.");
                            score-=4;
                        }
                        System.out.println("Accepted answer(s): " + answers);
                    }
                }
                System.out.println("Current score: " + score);
                System.out.println("--------------------------------------");
            }
            String skip = br.readLine();
            if (qCount == 25 && !correct) {
                done = true;
            } else if (qCount == 25 && correct && questionType.equals("BONUS")) {
                done = true;
            }
            if (!fastMode && !asked) {
                System.out.print("Next question? Type 'y' for yes or 'n' for no\n> ");
                String input = user.nextLine();
                while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                    System.out.print("Type 'y' for yes or 'n' for no\n> ");
                    input = user.nextLine();
                }
                if (input.equalsIgnoreCase("n")) {
                    done = true;
                }
                System.out.println();
                asked = true;
            }
        }
        System.out.print("Final score: " + score + ". ");
        analyzePerformance(score);
        System.out.println("Thank you for using the Science Bowl Simulator!");
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
        if (score < 0) {
            System.out.println("Who are you, Amol?");
        } else if (score == 0) {
            System.out.println("Were you actually playing or did you seriously drop a 0?");
        } else if (score < 20) {
            System.out.println("A Kennewick-esque performance.");
        } else if (score < 40) {
            System.out.println("You know, it could have been worse.");
        } else if (score < 70) {
            System.out.println("Hey, that's pretty good.");
        } else if (score < 100) {
            System.out.println("Alright, you would probably win most games.");
        } else if (score < 350) {
            System.out.println("Nationals-bound, I presume.");
        } else {
            System.out.println("Dang, you're Skyline-Team-1-level good.");
        }
    }

    private void readRules() {
        System.out.println();
        System.out.println("Science bowl rules: 4 points for every correct toss-up, 10 for every correct bonus. You get 5 seconds to answer a toss-up and 20 for a bonus.");
        if (fastMode) {
            System.out.println("In fast mode, questions will keep coming at you at a very fast pace. Be ready!");
        } else {
            System.out.println("In slow mode, the reading of the question is simulated. When the question is \"fully read\", the words \"TIMER START\" will appear." +
                    "\nIf you answer the question before this pop-up, it is treated as an interrupt. On toss-ups, if you interrupt and get the answer right, you earn points as usual. \nHowever, " +
                    "if you interrupt and get it wrong, you lose points. Interrupts do not matter in bonuses.");
        }
        System.out.println();
        Scanner scan = new Scanner(System.in);
        System.out.print("Type 'start' to begin the game\n> ");
        String input = scan.nextLine();
        while (!input.equalsIgnoreCase("start")) {
            System.out.print("Type 'start' to begin the game\n> ");
            input = scan.nextLine();
        }
        System.out.println();
    }

    private void getAllQuestions() throws IOException {
        for (int i = 1; i <= 2; i++) {
            File folder = new File("set " + i);
            File[] files = folder.listFiles();
            for (File f : files) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                int numLines = countLines(f.getAbsolutePath());
                int j = 0;
                while (j < numLines) {
                    String questionType = br.readLine().trim();
                    String topic = br.readLine().trim();
                    String answerType = br.readLine().trim();
                    String question = br.readLine().trim();
                    j+=4;
                    Question q = new Question(questionType, topic, answerType, question);
                    if (answerType.equals("MULTIPLE CHOICE")) {
                        String w = br.readLine().trim();
                        String x = br.readLine().trim();
                        String y = br.readLine().trim();
                        String z = br.readLine().trim();
                        j+=4;
                        q.setAnswerW(w);
                        q.setAnswerX(x);
                        q.setAnswerY(y);
                        q.setAnswerZ(z);
                    }
                    String[] answers = br.readLine().trim().split(" OR ");
                    j++;
                    q.addAnswers(answers);
                    unread.add(q);
                    br.readLine();
                    j++;
                }
            }
        }
        for (int i = 1; i <= 6; i++) {
            File f = new File("set 3\\Set3Round" + i + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(f));
            int numLines = countLines(f.getAbsolutePath());
            int j = 0;
            while (j < numLines) {
                String questionType = br.readLine().trim();
                String topic = br.readLine().trim();
                String answerType = br.readLine().trim();
                String question = br.readLine().trim();
                j+=4;
                Question q = new Question(questionType, topic, answerType, question);
                if (answerType.equals("MULTIPLE CHOICE")) {
                    String w = br.readLine().trim();
                    String x = br.readLine().trim();
                    String y = br.readLine().trim();
                    String z = br.readLine().trim();
                    j+=4;
                    q.setAnswerW(w);
                    q.setAnswerX(x);
                    q.setAnswerY(y);
                    q.setAnswerZ(z);
                }
                String[] answers = br.readLine().trim().split(" OR ");
                j++;
                q.addAnswers(answers);
                unread.add(q);
                br.readLine();
                j++;
            }
        }
    }

    private int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    private void getAllByRound(int set, int round) throws IOException {
        File f = new File("set " + set + "\\Set" + set + "Round" + round + ".txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        int numLines = countLines("set " + set + "\\Set" + set + "Round" + round + ".txt");
        int i = 0;
        while (i < numLines) {
            try {
                String questionType = br.readLine().trim();
                String topic = br.readLine().trim();
                String answerType = br.readLine().trim();
                String question = br.readLine().trim();
                i += 4;
                Question q = new Question(questionType, topic, answerType, question);
                if (answerType.equals("MULTIPLE CHOICE")) {
                    String w = br.readLine().trim();
                    String x = br.readLine().trim();
                    String y = br.readLine().trim();
                    String z = br.readLine().trim();
                    i += 4;
                    q.setAnswerW(w);
                    q.setAnswerX(x);
                    q.setAnswerY(y);
                    q.setAnswerZ(z);
                }
                try {
                    String[] answers = br.readLine().trim().split(" OR ");
                    i++;
                    q.addAnswers(answers);
                } catch (NullPointerException e) {
                    System.out.println(question);
                }
                unread.add(q);
            } catch (NullPointerException e) {
                System.out.println(i);
            }
            br.readLine();
            i++;
        }
    }

    public void playCasualMode() throws IOException {
        getAllQuestions();
        //test(3, 10);
        boolean done = false;
        Random random = new Random();
        Scanner user = new Scanner(System.in);
        while (!done) {
            int randQ = random.nextInt(unread.size());
            Question q = unread.get(randQ);
            q.printQuestionWithoutType();
            String response = user.nextLine();
            if (q.isCorrect(response)) {
                System.out.println("Correct!");
            } else {
                System.out.println("Incorrect.");
                System.out.print("Correct answers are: " + q.correctAnswers()+"\n");
            }
            System.out.print("Next question? Type 'y' for yes or 'n' for no\n> ");
            String input = user.nextLine();
            while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                System.out.print("Type 'y' for yes or 'n' for no\n> ");
                input = user.nextLine();
            }
            if (input.equalsIgnoreCase("n")) {
                done = true;
            }
            System.out.println();
            unread.remove(unread.get(randQ));
            if (unread.size()==0) {
                done = true;
                System.out.print("Ran out of questions. ");
            }
        }
        System.out.println("Thanks for playing!");
        System.exit(0);
    }

    private void test(int set, int round) throws IOException {
        getAllByRound(set, round);
        for (int i = 0; i < unread.size(); i++) {
            Question q = unread.get(i);
            q.showErrors((i+1));
        }
        System.exit(0);
    }
}
