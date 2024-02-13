package jp.co.amgakuin.teacher.kimura.examination.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.amgakuin.teacher.kimura.examination.data.ExaminationQuestion;

@Service
public class UserDataService {
    private Logger logger = LoggerFactory.getLogger(UserDataService.class);

    @Autowired
    private ExaminationDataService dataService;

    public void initializeSaveData(String saveDir, String filename) throws IOException {
        File file = new File(saveDir + filename);
        if (!file.exists()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < dataService.getQuestionCount(); i++) {
                ExaminationQuestion question = dataService.getQuestion(i);
                String str = "問" + (i + 1) + ",";
                Set<Entry<String, String>> set = question.getQuestionData().entrySet().stream()
                        .sorted(java.util.Map.Entry.comparingByKey())
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                for (Entry<String, String> entry : set) {
                    logger.info(entry.getKey() + "," + entry.getValue());
                    str += entry.getKey() + "," + entry.getValue() + ",";
                }
                writer.write(str);
                writer.newLine();
            }
            writer.close();
        }
    }

    public ArrayList<ExaminationQuestion> loadUserData(String saveDir, String filename) throws IOException {
        File file = new File(saveDir + filename);
        if (!file.exists()) {
            initializeSaveData(saveDir, filename);
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ArrayList<ExaminationQuestion> myAnswerList = new ArrayList<ExaminationQuestion>();
        for (int i = 0; i < dataService.getQuestionCount(); i++) {
            String[] data = reader.readLine().split(",");
            ExaminationQuestion q = new ExaminationQuestion();
            q.copy(dataService.getQuestion(i));
            for (int j = 1; j < data.length; j += 2) {
                q.updateAnswer(data[j], data[j + 1]);
            }
            myAnswerList.add(q);
        }
        reader.close();
        return myAnswerList;
    }

    public void saveUserData(String saveDir, ExaminationQuestion form, String filename) throws IOException {
        ArrayList<ExaminationQuestion> myAnswerList = loadUserData(saveDir, filename);
        form.getQuestionData().entrySet().forEach(entry -> {
            myAnswerList.get(form.getNumber()).updateAnswer(entry.getKey(), entry.getValue());
        });
        File file = new File(saveDir + filename);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < dataService.getQuestionCount(); i++) {
            ExaminationQuestion question = myAnswerList.get(i);
            String str = "問" + (i + 1) + ",";
            Set<Entry<String, String>> set = question.getQuestionData().entrySet().stream()
                    .sorted(java.util.Map.Entry.comparingByKey())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            for (Entry<String, String> entry : set) {
                str += entry.getKey() + "," + entry.getValue() + ",";
            }
            writer.write(str);
            writer.newLine();
        }
        writer.close();
    }
}
