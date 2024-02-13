package jp.co.amgakuin.teacher.kimura.examination.data;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExaminationQuestionData {
    private final Logger log = LoggerFactory.getLogger(ExaminationQuestionData.class);

    private String questionLine;
    private String question;
    private Map<String, String> answers;
    public String getQuestionLine() {
        return questionLine;
    }
    public void setQuestionLine(String questionLine) {
        this.questionLine = questionLine;
        String[] splitQuestions = this.questionLine.split(",");
        if (splitQuestions == null || splitQuestions.length < 2) {
            log.info("Can not load line:"+questionLine);
        } else {
            setQuestion(splitQuestions[0]);
            Map<String, String> answers = new HashMap<String, String>();
            for(int i=1; i<splitQuestions.length; i++) {
                answers.put(splitQuestions[i], "");
            }
            setAnswers(answers);
        }
    }
    public String getQuestion() {
        return question;
    }
    private void setQuestion(String question) {
        this.question = question;
    }
    public Map<String, String> getAnswers() {
        return answers;
    }
    private void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    public ExaminationQuestionData() {
       this.setAnswers(new HashMap<String, String>());
       this.setQuestion("");
       this.setQuestionLine("");
    }

    public ExaminationQuestionData(String q) {
        setQuestionLine(q);
    }

    public void setAnswerValues(String answerName, String answerValue) {
        answers.put(answerName, answerValue);
    }
}
