import java.io.*;
import java.util.*;

public class RoundGenerator {
    private ArrayList<ArrayList<QuestionAndBonus>> questionSets = new ArrayList<>(7);
        // 0 = GNSC, 1 = MATH, 2 = ASTR, 3 = ERTH/ENRG, 4 = PHYS, 5 = BIOL, 6 = CHEM
    private Random random = new Random();

    RoundGenerator() throws IOException {
        //add empty sets in the list
        for (int i = 0; i < 7; i++) questionSets.add(i, new ArrayList<>());

        for (int i = 1; i <= 4; i++) for (int j = 1; j <= 17; j++) getAllByRound(i, j);
        for (int i = 1; i <= 8; i++) getAllByRound(5, i);

        for (int i = 0; i < 7; i++) {
            ArrayList<QuestionAndBonus> set = questionSets.get(i);
            Collections.shuffle(set);
        }

        //get random skew
        int randomNum = random.nextInt(10); //0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        if (randomNum <= 5) balancedRound();
        else if (randomNum <= 7) earthAndPhysSkewRound();
        else bioChemSkewRound();
    }

    private void getAllByRound(int set, int round) throws IOException {
        File f = new File("set " + set + "\\Set" + set + "Round" + round + ".txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        int numLines = countLines("set " + set + "\\Set" + set + "Round" + round + ".txt");
        int i = 0;
        while (i < numLines) {
            QuestionAndBonus qab;
            Question tossUp = new Question();
            Question bonus = new Question();

            //Get Toss Up question first
            try {
                String questionType = br.readLine().trim();
                String topic = br.readLine().trim();
                String answerType = br.readLine().trim();
                String question = br.readLine().trim();
                i += 4;
                tossUp = new Question(questionType, topic, answerType, question);
                i = checkQuestionMC(tossUp, br, i);
                try {
                    String[] answers = br.readLine().trim().split(" OR ");
                    i++;
                    tossUp.addAnswers(answers);

                } catch (NullPointerException e) {
                    System.out.println(question);
                }
            } catch (NullPointerException e) {
                System.out.println(i);
            }
            br.readLine();
            i++;
            //Get Bonus question next
            try {
                String questionType = br.readLine().trim();
                String topic = br.readLine().trim();
                String answerType = br.readLine().trim();
                String question = br.readLine().trim();
                i += 4;
                bonus = new Question(questionType, topic, answerType, question);
                i = checkQuestionMC(bonus, br, i);
                try {
                    String[] answers = br.readLine().trim().split(" OR ");
                    i++;
                    bonus.addAnswers(answers);
                } catch (NullPointerException e) {
                    System.out.println(question);
                }
            } catch (NullPointerException e) {
                System.out.println(i);
            }
            br.readLine();
            i++;

            qab = new QuestionAndBonus(tossUp);
            qab.addAnswers(tossUp.getAnswers());
            qab.addBonus(bonus);
            qab.getBonus().addAnswers(bonus.getAnswers());
            if (tossUp.getAnswerType().equalsIgnoreCase("MULTIPLE CHOICE")) {
                qab.setAnswerW(tossUp.getAnswerW());
                qab.setAnswerX(tossUp.getAnswerX());
                qab.setAnswerY(tossUp.getAnswerY());
                qab.setAnswerZ(tossUp.getAnswerZ());
            }
            if (bonus.getAnswerType().equalsIgnoreCase("MULTIPLE CHOICE")) {
                qab.getBonus().setAnswerW(bonus.getAnswerW());
                qab.getBonus().setAnswerX(bonus.getAnswerX());
                qab.getBonus().setAnswerY(bonus.getAnswerY());
                qab.getBonus().setAnswerZ(bonus.getAnswerZ());
            }

            switch (qab.getTopic()) {
                case "GENERAL SCIENCE":
                    questionSets.get(0).add(qab);
                    break;
                case "MATH":
                    questionSets.get(1).add(qab);
                    break;
                case "ASTRONOMY":
                    questionSets.get(2).add(qab);
                    break;
                case "EARTH SCIENCE":
                    questionSets.get(3).add(qab);
                    break;
                case "ENERGY": //grouped with earth science's hash set
                    questionSets.get(3).add(qab);
                    break;
                case "PHYSICS":
                    questionSets.get(4).add(qab);
                    break;
                case "BIOLOGY":
                    questionSets.get(5).add(qab);
                    break;
                case "CHEMISTRY":
                    questionSets.get(6).add(qab);
                    break;
            }
        }
    }
    private int countLines(String filename) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            is.close();
            return (count == 0 && !empty) ? 1 : count;
        }
    }
    private int checkQuestionMC(Question q, BufferedReader br, int j) throws IOException {
        if (q.getAnswerType().equals("MULTIPLE CHOICE")) {
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
        return j;
    }

    private void balancedRound() throws FileNotFoundException, UnsupportedEncodingException {
        int numQuestions = random.nextInt(4)+22; //22, 23, 24, or 25 questions
        QABLinkedList link;
        int subject = random.nextInt(100);
        //ArrayList<QuestionAndBonus> list = new ArrayList<>();
        if (subject <= 3) link = new QABLinkedList(questionSets.get(0).remove(0));
        else if (subject <= 15) link = new QABLinkedList(questionSets.get(1).remove(0));
        else if (subject <= 27) link = new QABLinkedList(questionSets.get(2).remove(0));
        else if (subject <= 45) link = new QABLinkedList(questionSets.get(3).remove(0));
        else if (subject <= 63) link = new QABLinkedList(questionSets.get(4).remove(0));
        else if (subject <= 81) link = new QABLinkedList(questionSets.get(5).remove(0));
        else link = new QABLinkedList(questionSets.get(6).remove(0));

        for (int i = 1; i < numQuestions; i++) {
            subject = random.nextInt(100);
            if (subject <= 3) link.add(questionSets.get(0).remove(0));
            else if (subject <= 15) link.add(questionSets.get(1).remove(0));
            else if (subject <= 27) link.add(questionSets.get(2).remove(0));
            else if (subject <= 45) link.add(questionSets.get(3).remove(0));
            else if (subject <= 63) link.add(questionSets.get(4).remove(0));
            else if (subject <= 81) link.add(questionSets.get(5).remove(0));
            else link.add(questionSets.get(6).remove(0));
        }

        PrintWriter pw = new PrintWriter("generatedset.txt", "UTF-8");
        for (int i = 0; i < numQuestions; i++) {
            QuestionAndBonus qab = link.get(i);
            pw.println(qab.getQuestionType());
            pw.println(qab.getTopic());
            pw.println(qab.getAnswerType());
            pw.println(qab.getQuestion());
            if (qab.getAnswerType().equalsIgnoreCase("MULTIPLE CHOICE")) {
                pw.println(qab.getAnswerW());
                pw.println(qab.getAnswerX());
                pw.println(qab.getAnswerY());
                pw.println(qab.getAnswerZ());
            }
            StringBuilder sb = new StringBuilder("");
            for (String s : qab.getAnswers()) sb.append(s).append(" OR ");
            pw.println(sb.subSequence(0, sb.length()-4).toString());
            pw.println();

            Question bonus = qab.getBonus();
            pw.println(bonus.getQuestionType());
            pw.println(bonus.getTopic());
            pw.println(bonus.getAnswerType());
            pw.println(bonus.getQuestion());
            if (bonus.getAnswerType().equalsIgnoreCase("MULTIPLE CHOICE")) {
                pw.println(bonus.getAnswerW());
                pw.println(bonus.getAnswerX());
                pw.println(bonus.getAnswerY());
                pw.println(bonus.getAnswerZ());
            }
            sb = new StringBuilder("");
            for (String s : bonus.getAnswers()) sb.append(s).append(" OR ");
            pw.println(sb.subSequence(0, sb.length()-4).toString());
            pw.println();
        }

        pw.close();
    }
    private void earthAndPhysSkewRound() throws FileNotFoundException, UnsupportedEncodingException {
        int numQuestions = random.nextInt(4)+22; //22, 23, 24, or 25 questions
        QABLinkedList link;
        int subject = random.nextInt(100);
        //ArrayList<QuestionAndBonus> list = new ArrayList<>();
        if (subject <= 3) link = new QABLinkedList(questionSets.get(0).remove(0));
        else if (subject <= 13) link = new QABLinkedList(questionSets.get(1).remove(0));
        else if (subject <= 27) link = new QABLinkedList(questionSets.get(2).remove(0));
        else if (subject <= 41) link = new QABLinkedList(questionSets.get(5).remove(0));
        else if (subject <= 55) link = new QABLinkedList(questionSets.get(6).remove(0));
        else if (subject <= 77) link = new QABLinkedList(questionSets.get(3).remove(0));
        else link = new QABLinkedList(questionSets.get(4).remove(0));

        for (int i = 1; i < numQuestions; i++) {
            subject = random.nextInt(100);
            if (subject <= 3) link.add(questionSets.get(0).remove(0));
            else if (subject <= 13) link.add(questionSets.get(1).remove(0));
            else if (subject <= 27) link.add(questionSets.get(2).remove(0));
            else if (subject <= 41) link.add(questionSets.get(5).remove(0));
            else if (subject <= 55) link.add(questionSets.get(6).remove(0));
            else if (subject <= 77) link.add(questionSets.get(3).remove(0));
            else link.add(questionSets.get(4).remove(0));
        }

        PrintWriter pw = new PrintWriter("generatedset.txt", "UTF-8");
        for (int i = 0; i < numQuestions; i++) {
            QuestionAndBonus qab = link.get(i);
            pw.println(qab.getQuestionType());
            pw.println(qab.getTopic());
            pw.println(qab.getAnswerType());
            pw.println(qab.getQuestion());
            if (qab.getAnswerType().equalsIgnoreCase("MULTIPLE CHOICE")) {
                pw.println(qab.getAnswerW());
                pw.println(qab.getAnswerX());
                pw.println(qab.getAnswerY());
                pw.println(qab.getAnswerZ());
            }
            StringBuilder sb = new StringBuilder("");
            for (String s : qab.getAnswers()) sb.append(s).append(" OR ");
            pw.println(sb.subSequence(0, sb.length()-4).toString());
            pw.println();

            Question bonus = qab.getBonus();
            pw.println(bonus.getQuestionType());
            pw.println(bonus.getTopic());
            pw.println(bonus.getAnswerType());
            pw.println(bonus.getQuestion());
            if (bonus.getAnswerType().equalsIgnoreCase("MULTIPLE CHOICE")) {
                pw.println(bonus.getAnswerW());
                pw.println(bonus.getAnswerX());
                pw.println(bonus.getAnswerY());
                pw.println(bonus.getAnswerZ());
            }
            sb = new StringBuilder("");
            for (String s : bonus.getAnswers()) sb.append(s).append(" OR ");
            pw.println(sb.subSequence(0, sb.length()-4).toString());
            pw.println();
        }

        pw.close();
    }
    private void bioChemSkewRound() throws FileNotFoundException, UnsupportedEncodingException {
        int numQuestions = random.nextInt(4)+22; //22, 23, 24, or 25 questions
        QABLinkedList link;
        int subject = random.nextInt(100);
        //ArrayList<QuestionAndBonus> list = new ArrayList<>();
        if (subject <= 7) link = new QABLinkedList(questionSets.get(0).remove(0));
        else if (subject <= 19) link = new QABLinkedList(questionSets.get(1).remove(0));
        else if (subject <= 27) link = new QABLinkedList(questionSets.get(2).remove(0));
        else if (subject <= 41) link = new QABLinkedList(questionSets.get(3).remove(0));
        else if (subject <= 55) link = new QABLinkedList(questionSets.get(4).remove(0));
        else if (subject <= 77) link = new QABLinkedList(questionSets.get(5).remove(0));
        else link = new QABLinkedList(questionSets.get(6).remove(0));

        for (int i = 1; i < numQuestions; i++) {
            subject = random.nextInt(100);
            if (subject <= 7) link.add(questionSets.get(0).remove(0));
            else if (subject <= 19) link.add(questionSets.get(1).remove(0));
            else if (subject <= 27) link.add(questionSets.get(2).remove(0));
            else if (subject <= 41) link.add(questionSets.get(3).remove(0));
            else if (subject <= 55) link.add(questionSets.get(4).remove(0));
            else if (subject <= 77) link.add(questionSets.get(5).remove(0));
            else link.add(questionSets.get(6).remove(0));
        }

        PrintWriter pw = new PrintWriter("generatedset.txt", "UTF-8");
        for (int i = 0; i < numQuestions; i++) {
            QuestionAndBonus qab = link.get(i);
            pw.println(qab.getQuestionType());
            pw.println(qab.getTopic());
            pw.println(qab.getAnswerType());
            pw.println(qab.getQuestion());
            if (qab.getAnswerType().equalsIgnoreCase("MULTIPLE CHOICE")) {
                pw.println(qab.getAnswerW());
                pw.println(qab.getAnswerX());
                pw.println(qab.getAnswerY());
                pw.println(qab.getAnswerZ());
            }
            StringBuilder sb = new StringBuilder("");
            for (String s : qab.getAnswers()) sb.append(s).append(" OR ");
            pw.println(sb.subSequence(0, sb.length()-4).toString());
            pw.println();

            Question bonus = qab.getBonus();
            pw.println(bonus.getQuestionType());
            pw.println(bonus.getTopic());
            pw.println(bonus.getAnswerType());
            pw.println(bonus.getQuestion());
            if (bonus.getAnswerType().equalsIgnoreCase("MULTIPLE CHOICE")) {
                pw.println(bonus.getAnswerW());
                pw.println(bonus.getAnswerX());
                pw.println(bonus.getAnswerY());
                pw.println(bonus.getAnswerZ());
            }
            sb = new StringBuilder("");
            for (String s : bonus.getAnswers()) sb.append(s).append(" OR ");
            pw.println(sb.subSequence(0, sb.length()-4).toString());
            pw.println();
        }

        pw.close();
    }
}
