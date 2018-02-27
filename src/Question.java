import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;

public class Question {
    private String questionType;
    private String topic;
    private String answerType;
    private String question;
    private String[] answers;
    private String answerW;
    private String answerX;
    private String answerY;
    private String answerZ;
    private boolean hasError = false;

    Question(String questionType, String topic, String answerType, String question) {
        this.questionType = questionType;
        this.topic = topic;
        this.answerType = answerType;
        this.question = question;
    }
    Question() {
        this.questionType = "";
        this.topic = "";
        this.answerType = "";
        this.question = "";
    }
    void addAnswers(String[] answers) {
        this.answers = answers;
    }
    String[] getAnswers() { return this.answers; }

    void setAnswerW(String answerW) {
        this.answerW = answerW;
    }
    void setAnswerX(String answerX) {
        this.answerX = answerX;
    }
    void setAnswerY(String answerY) {
        this.answerY = answerY;
    }
    void setAnswerZ(String answerZ) {
        this.answerZ = answerZ;
    }
    public String getAnswerW() {
        return answerW;
    }
    public String getAnswerX() {
        return answerX;
    }
    public String getAnswerY() {
        return answerY;
    }
    public String getAnswerZ() {
        return answerZ;
    }

    String getQuestionType() { return this.questionType; }

    void printQuestion() {
        System.out.println(questionType);
        System.out.println(topic);
        System.out.println(answerType);
        System.out.println(wrapQuestion(question));
        if (answerW!=null)
            System.out.println(answerW);
        if (answerX!=null)
            System.out.println(answerX);
        if (answerY!=null)
            System.out.println(answerY);
        if (answerZ!=null)
            System.out.println(answerZ);
    }

    void setQuestion(String question) {
        this.question = question;
    }

    String getQuestion() {
        return this.question;
    }

    void printQuestionWithoutType() {
        System.out.println(topic);
        System.out.println(answerType);
        System.out.println(wrapQuestion(question));
        if (answerW!=null)
            System.out.println(answerW);
        if (answerX!=null)
            System.out.println(answerX);
        if (answerY!=null)
            System.out.println(answerY);
        if (answerZ!=null)
            System.out.println(answerZ);
    }

    boolean isCorrect(String answer) {
        for (String a : answers) {
            if (answer.equalsIgnoreCase(a.trim())) {
                return true;
            }
        }
        return false;
    }
    private String wrapQuestion(String q) {
        return WordUtils.wrap(q, 50);
    }

    void showErrors(int num) {
        if (!questionType.equals("TOSS UP") && !questionType.equals("BONUS") && num != 1) {
            System.out.println("Question Type error on question " + num + "(question type = " + questionType + ")");
            hasError = true;
        } else if (!topic.equals("BIOLOGY") && !topic.equals("CHEMISTRY") && !topic.equals("EARTH SCIENCE") && !topic.equals("PHYSICS") && !topic.equals("GENERAL SCIENCE") &&
                !topic.equals("MATH") && !topic.equals("ENERGY") && !topic.equals("ASTRONOMY") && !topic.equals("COMPUTER SCIENCE")) {
            System.out.println("Topic error on question " + num + "(topic = " + topic + ")");
            hasError = true;
        } else if (!answerType.equals("SHORT ANSWER") && !answerType.equals("MULTIPLE CHOICE")) {
            System.out.println("Answer type error on question " + num);
            hasError = true;
        } else if (question.trim().length()==0) {
            System.out.println("Question is blank error on question " + num);
            hasError = true;
        } else if (answers.length==0) {
            System.out.println("Answer is blank on question " + num);
            hasError = true;
        }

        if (answerType.equals("MULTIPLE CHOICE")) {
            if (answerW.trim().length()==0) {
                System.out.println("Choice W is blank error on question " + num);
                hasError = true;
            } else if (answerX.trim().length()==0) {
                System.out.println("Choice X is blank error on question " + num);
                hasError = true;
            } else if (answerY.trim().length()==0) {
                System.out.println("Choice Y is blank error on question " + num);
                hasError = true;
            } else if (answerZ.trim().length()==0) {
                System.out.println("Choice Z is blank error on question " + num);
                hasError = true;
            }
        }
    }

    String correctAnswers() {
        String line = Arrays.toString(this.answers);
        return line.substring(1, line.length()-1);
    }

    String getTopic() {
        return this.topic;
    }

    String getAnswerType() { return this.answerType; }

    boolean hasError() { return this.hasError; }

    public String[] getChoices() {
        if (answerType.equals("MULTIPLE CHOICE")) {
            return new String[]{answerW, answerX, answerY, answerZ};
        } else {
            return null;
        }
    }
}
