package jp.co.amgakuin.teacher.kimura.examination.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.amgakuin.teacher.kimura.examination.controller.form.ExaminationAnswerForm;
import jp.co.amgakuin.teacher.kimura.examination.data.ExaminationQuestionData;
import jp.co.amgakuin.teacher.kimura.examination.service.ExaminationResourceService;

/**
 * Examination Rootコントローラ
 * Examinationのトップページ及び
 * @author cm0002
 *
 */
@Controller
public class ExaminationController
{
    private static Logger logger = LoggerFactory.getLogger(ExaminationController.class);

    @Autowired
    ExaminationResourceService resource;

    @GetMapping("/")
    public String indexPage(Model model, HttpServletRequest request,
            HttpServletResponse response, @Value("${app.saveDir:./}") String saveDir) {
        ArrayList<ExaminationQuestionData> questions = resource.getQuestions();
        model.addAttribute("title", "試験");
        model.addAttribute("questionNumber", 0);
        model.addAttribute("questionMax", questions.size());
        model.addAttribute("question", questions.get(0));
        ArrayList<String> myAnswer = null;
        try {
            myAnswer = resource.loadUserData(saveDir, request.getRemoteHost());
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

        ExaminationAnswerForm form = new ExaminationAnswerForm();
        form.setAnswers(myAnswer.get(0));
        model.addAttribute("myAnswer", form);
        response.addCookie(new Cookie("examin", request.getRemoteHost()));
        return "questionAndAnswer";
    }

    @PostMapping("/")
    public String postPage(@ModelAttribute ExaminationAnswerForm form, Model model, HttpServletRequest request,
            HttpServletResponse response, @Value("${app.saveDir:./}") String saveDir) {
        logger.info(saveDir);
        Cookie[] cookies = request.getCookies();
        Cookie myCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("examin")) {
                myCookie = cookie;
                break;
            }
        }
        if (myCookie == null) {
            response.addCookie(new Cookie("examin", request.getRemoteHost()));
        }
        ArrayList<ExaminationQuestionData> questions = resource.getQuestions();
        model.addAttribute("title", "試験");
        if (form.getQuestionNumber() != questions.size()) {
            logger.info("Need Recording.");
            try {
                resource.saveUserData(saveDir, form, myCookie);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (form.getEnd() != null) {
            model.addAttribute("questionNumber", questions.size());
            model.addAttribute("questionMax", questions.size());
            return "examinationEnd";
        }
        int no = form.getQuestionNumber() + (form.getNext() == null ? -1 : 1);
        logger.info("next=" + no);
        model.addAttribute("questionNumber", no);
        model.addAttribute("questionMax", questions.size());
        model.addAttribute("question", questions.get(no));
        ArrayList<String> myAnswer = null;
        try {
            myAnswer = resource.loadUserData(saveDir, request.getRemoteHost());
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        logger.info(myAnswer.get(no));
        form.setAnswers(myAnswer.get(no));
        model.addAttribute("myAnswer", form);
        return "questionAndAnswer";
    }
}
