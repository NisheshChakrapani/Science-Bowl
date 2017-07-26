import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;

/**
 * Created by nishu on 5/14/2017.
 */
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
    public Question(String questionType, String topic, String answerType, String question) {
        this.questionType = questionType;
        this.topic = topic;
        this.answerType = answerType;
        this.question = question;
    }

    public void addAnswers(String[] answers) {
        this.answers = answers;
    }

    public void setAnswerW(String answerW) {
        this.answerW = answerW;
    }
    public void setAnswerX(String answerX) {
        this.answerX = answerX;
    }
    public void setAnswerY(String answerY) {
        this.answerY = answerY;
    }
    public void setAnswerZ(String answerZ) {
        this.answerZ = answerZ;
    }

    public void printQuestion() {
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

    public void printQuestionWithoutType() {
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

    public boolean isCorrect(String answer) {
        for (String a : answers) {
            if (answer.equalsIgnoreCase(a.trim())) {
                return true;
            }
        }
        return false;
    }
    private String wrapQuestion(String q) {
        return WordUtils.wrap(q, 120);
    }

    public void showErrors(int num) {
        /*if (this.questionType.trim().length()==0 || this.topic.trim().length()==0 || this.answerType.trim().length()==0
                || this.question.trim().length()==0) {
            return true;
        } else if (this.answerType.trim().equalsIgnoreCase("MULTIPLE CHOICE")) {
            if (this.answerW.trim().length()==0 || this.answerX.trim().length()==0 || this.answerY.trim().length()==0 || this.answerZ.trim().length()==0) {
                return true;
            }
        }
        return false;*/
        if (!questionType.equals("TOSS UP") && !questionType.equals("BONUS")) {
            System.out.println("Question Type error on question " + num + "(question type = " + questionType + ")");
        } else if (!topic.equals("BIOLOGY") && !topic.equals("CHEMISTRY") && !topic.equals("EARTH SCIENCE") && !topic.equals("PHYSICS") && !topic.equals("GENERAL SCIENCE") &&
                !topic.equals("MATH") && !topic.equals("ENERGY") && !topic.equals("ASTRONOMY") && !topic.equals("COMPUTER SCIENCE")) {
            System.out.println("Topic error on question " + num + "(topic = " + topic + ")");
        } else if (!answerType.equals("SHORT ANSWER") && !answerType.equals("MULTIPLE CHOICE")) {
            System.out.println("Answer type error on question " + num);
        } else if (question.trim().length()==0) {
            System.out.println("Question is blank error on question " + num);
        } else if (answers.length==0) {
            System.out.println("Answer is blank on question " + num);
        }

        if (answerType.equals("MULTIPLE CHOICE")) {
            if (answerW.trim().length()==0) {
                System.out.println("Choice W is blank error on question " + num);
            } else if (answerX.trim().length()==0) {
                System.out.println("Choice X is blank error on question " + num);
            } else if (answerY.trim().length()==0) {
                System.out.println("Choice Y is blank error on question " + num);
            } else if (answerZ.trim().length()==0) {
                System.out.println("Choice Z is blank error on question " + num);
            }
        }
    }

    public String correctAnswers() {
        String line = Arrays.toString(this.answers);
        return line.substring(1, line.length()-1);
    }

    public String getTopic() {
        return this.topic;
    }

    public String getAnswerType() { return this.answerType; }
}
