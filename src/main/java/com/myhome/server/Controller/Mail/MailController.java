package com.myhome.server.Controller.Mail;

import com.myhome.server.Entity.Member.MemberDetail;
import com.myhome.server.Repository.Member.MemberDetailRepository;
import com.myhome.server.Service.FileUpload;
import com.myhome.server.Service.MailService;
import com.myhome.server.Service.MemberService;
import com.myhome.server.Service.MyModel.MyModel;
import com.myhome.server.Service.MyModel.Templates;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/myApi")
public class MailController {


    private final JavaMailSender javaMailSender;
    private final MailService mailService;
    private final MemberService memberService;
    private final MemberDetailRepository memberDetailRepository;
    private final Templates templates = new Templates();
    public MailController(JavaMailSender javaMailSender, MailService mailService, MemberService memberService, MemberDetailRepository memberDetailRepository) {
        this.javaMailSender = javaMailSender;
        this.mailService = mailService;
        this.memberService = memberService;
        this.memberDetailRepository = memberDetailRepository;
    }
//    {UID}
    @GetMapping("/send/mail/{UID}")
    public void sendMail(
        @PathVariable(name = "UID") String UID,
        HttpServletResponse httpServletResponse
    ) throws Exception {
        MemberDetail member = memberService.LoginCheck(UID, httpServletResponse);
        String to = member.getEmail();
        String from = "MyDomus@mydomus.home";
        String subject = templates.MEMBER_REGISTRATION_AUTHENTICATION_TITLE;
        StringBuilder body = new StringBuilder();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
        MyModel myModel = new MyModel();
        myModel.setModel("uid",UID);
        String am = mailService.addTemplates(templates.MEMBER_REGISTRATION_AUTHENTICATION,myModel);
        body.append(am);
        mimeMessageHelper.setFrom(from,"MyDomus");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body.toString(), true);
        javaMailSender.send(message);
    }

    @GetMapping("/send/email/{email}")
    public String sendEmail(
            @PathVariable(name = "email") String email,
            HttpServletResponse httpServletResponse
    ) throws MessagingException, IOException {
        Optional<MemberDetail> findEmail = memberDetailRepository.findByEmail(email);
        if(findEmail.isPresent()) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "????????? ??? ?????? ??????????????????.");
            return "";
        }
        String to = email;
        String from = "MyDomus@mydomus.home";
        String subject = templates.MEMBER_REGISTRATION_AUTHENTICATION_TITLE;
        StringBuilder body = new StringBuilder();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
        MyModel myModel = new MyModel();
        FileUpload fileUpload = new FileUpload();
        String rs = fileUpload.RandomNumberString(10);
        myModel.setModel("auth_num",rs);
        String am = mailService.addTemplates(templates.MEMBER_REGISTRATION_AUTHENTICATION,myModel);
        body.append(am);
        mimeMessageHelper.setFrom(from,"MyDomus");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body.toString(), true);
        javaMailSender.send(message);
        return rs;
    }


}

// ?????? ?????? <img src="cid:flower.jpg"> <- ?????????
//        FileSystemResource file = new FileSystemResource(new File("C:/Users/HOME/Desktop/flower.jpg"));
//        mimeMessageHelper.addInline("flower.jpg", file);

// ?????? ??????
//        FileSystemResource fileSystemResource = new FileSystemResource(new File("C:/Users/HOME/Desktop/test.txt"));
//        mimeMessageHelper.addAttachment("?????????.txt", fileSystemResource);