package jp.co.amgakuin.teacher.kimura.examination.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import jp.co.amgakuin.teacher.kimura.examination.data.ExaminationQuestion;

@Service
public class ExaminationDataService {
    Logger logger = LoggerFactory.getLogger(ExaminationDataService.class);

    @Autowired
    ResourceLoader loader;

    ArrayList<ExaminationQuestion> questions = new ArrayList<ExaminationQuestion>();

    private Resource loadExamination() {
        return loader.getResource("classpath:data/question.txt");
    }

    @PostConstruct
    public void getExaminations() {
        logger.info("getExaminations started.");
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(loadExamination().getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                ExaminationQuestion q = new ExaminationQuestion();
                q.setupQuestion(line);
                questions.add(q);
            }
            reader.close();
            logger.info("getExaminations finished.");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ExaminationQuestion getQuestion(int no) {
        return questions.get(no);
    }

    public int getQuestionCount() {
        return questions.size();
    }

}
