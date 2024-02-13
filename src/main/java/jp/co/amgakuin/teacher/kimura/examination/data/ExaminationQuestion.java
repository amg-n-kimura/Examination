package jp.co.amgakuin.teacher.kimura.examination.data;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 試験問題データクラス
 * 解答用フォームデータも兼用
 * @author kimura_nobuyuki
 *
 */
public class ExaminationQuestion implements Cloneable {
    Logger logger = LoggerFactory.getLogger(ExaminationQuestion.class);

    //問題文
    private String questionText;
    //nameとvalue
    private Map<String, String> questionData;
    //問題番号(0〜
    private int number;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Map<String, String> getQuestionData() {
        return questionData;
    }

    public void setQuestionData(Map<String, String> questionData) {
        this.questionData = questionData;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * コンストラクタ
     */
    public ExaminationQuestion() {
        questionText = "";
        questionData = new HashMap<String, String>();
        number = 0;
    }

    /**
     * 解答欄を埋める
     * @param name
     * @param value
     */
    public void updateAnswer(String name, String value) {
        questionData.put(name, value);
    }

    /**
     * 問題文の解析
     * @param csvQuestionString
     */
    public void setupQuestion(String csvQuestionString) {
        //,区切りを分解
        String[] data = csvQuestionString.split(",");
        if (data.length < 2) {
            logger.info("'"+csvQuestionString+"' is too short.");
        }
        questionText = data[0];
        for(int i=1; i<data.length; i++) {
            questionData.put(data[i], " ");
        }
    }

    public void copy(ExaminationQuestion original) {
        setNumber(original.getNumber());
        setQuestionText(original.getQuestionText());
        setQuestionData(original.getQuestionData());
    }

    @Override
    public ExaminationQuestion clone() {
        ExaminationQuestion c = new ExaminationQuestion();
        c.setNumber(this.number);
        c.setQuestionText(this.questionText);
        c.setQuestionData(this.questionData);

        return c;
    }
}
