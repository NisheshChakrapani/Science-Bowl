public class QuestionAndBonus extends Question {
    private Question bonus;

    public QuestionAndBonus(String questionType, String topic, String answerType, String question) {
        super(questionType, topic, answerType, question);
    }
    public QuestionAndBonus(Question tossUp) {
        super(tossUp.getQuestionType(), tossUp.getTopic(), tossUp.getAnswerType(), tossUp.getQuestion());
    }

    public void addBonus(Question bonus) {
        this.bonus = bonus;
    }

    public Question getBonus() {
        return this.bonus;
    }
}
