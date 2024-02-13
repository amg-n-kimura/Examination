package jp.co.amgakuin.teacher.kimura.examination.controller.form;

public class ExaminationAnswerForm {
    private String next;
    private String before;
    private String end;
    private int questionNumber;
    private String A;
    private String B;
    private String C;
    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }
    public String getNext() {
        return next;
    }
    public void setNext(String next) {
        this.next = next;
    }
    public String getBefore() {
        return before;
    }
    public void setBefore(String before) {
        this.before = before;
    }
    public int getQuestionNumber() {
        return questionNumber;
    }
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
    public String getA() {
        return A;
    }
    public void setA(String a) {
        A = a;
    }
    public String getB() {
        return B;
    }
    public void setB(String b) {
        B = b;
    }
    public String getC() {
        return C;
    }
    public void setC(String c) {
        C = c;
    }

    public String getAnswer(String name) {
        if (name.equals("A")) {
            return getA();
        } else if (name.equals("B")) {
            return getB();
        }

        return getC();
    }

    public void setAnswers(String answerLine) {
        String[] answers = answerLine.split(",");
        for(int i=0; i<answers.length; i+= 2) {
            if (answers[i].equals("A")) {
                setA(answers[i + 1]);
            } else if (answers[i].equals("B")) {
                setB(answers[i + 1]);
            } else {
                setC(answers[i + 1]);
            }
        }
    }
}
