package jp.co.amgakuin.teacher.kimura.examination.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import jp.co.amgakuin.teacher.kimura.examination.controller.form.ExaminationAnswerForm;
import jp.co.amgakuin.teacher.kimura.examination.data.ExaminationQuestionData;

@Service
public class ExaminationResourceService
{
    private final Logger log = LoggerFactory.getLogger(ExaminationResourceService.class);

    @Autowired
    private ResourceLoader loader;

    private ArrayList<ExaminationQuestionData> questions = new ArrayList<ExaminationQuestionData>();

    private Resource loadExamination() {
        return loader.getResource("classpath:data/question.txt");
    }

    public ArrayList<ExaminationQuestionData> getQuestions() {
        return questions;
    }

    @PostConstruct
    public void getExaminations() {
        log.info("getExaminations started.");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(loadExamination().getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                ExaminationQuestionData q = new ExaminationQuestionData(line);
                questions.add(q);
            }
            reader.close();
            log.info("getExaminations finished.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> loadUserData(String saveDir, String fileName) throws IOException {
        File file = new File(saveDir + fileName);
        if (!file.exists()) {
            //log.info("Make new file " + file.getPath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < questions.size(); i++) {
                ExaminationQuestionData question = questions.get(i);
                String str = "問" + (i + 1) + ":";
                for (Map.Entry<String, String> answer : question.getAnswers().entrySet()) {
                    str += answer.getKey() + "," + (answer.getValue().equals("") ? " " : answer.getValue()) + ",";
                }
                writer.write(str);
                //log.info(str);
                writer.newLine();
            }
            writer.close();
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        ArrayList<String> myAnswerList = new ArrayList<String>();
        for (int i = 0; i < questions.size(); i++) {
            String line = reader.readLine();
            String[] answers = line.split(":");
            myAnswerList.add(answers[1]);
        }
        reader.close();

        return myAnswerList;
    }

    public void saveUserData(String saveDir, ExaminationAnswerForm form, Cookie cookie) throws IOException {
        String address = cookie.getValue();
        int questionNo = form.getQuestionNumber();
        ArrayList<String> myAnswerList = loadUserData(saveDir, address);
        String answer = "";
        for (Map.Entry<String, String> e : questions.get(questionNo).getAnswers().entrySet()) {
            String v = form.getAnswer(e.getKey());
            answer += e.getKey() + "," + (v.equals("") ? " " : v) + ",";
        }

        myAnswerList.set(questionNo, answer);
        File file = new File(saveDir + address);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        int n = 1;
        for (String myAnswer : myAnswerList) {
            String str = "問" + n + ":" + myAnswer;
            writer.write(str);
            writer.newLine();
            n++;
        }
        writer.close();
    }
}
