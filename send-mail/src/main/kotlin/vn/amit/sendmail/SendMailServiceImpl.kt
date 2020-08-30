package vn.amit.sendmail

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import vn.amit.common.language.LocaleService
import javax.mail.internet.MimeMessage


@Service
class SendMailServiceImpl @Autowired constructor(
        private val mailSender: JavaMailSender,
        private val templateEngine: TemplateEngine,
        private val localeService: LocaleService
) : SendMailService {
    @Value("\$spring.mail.username")
    private lateinit var sendFrom: String

    override fun sendMailMessage(to: String, sub: String, text: String) {
        val message = SimpleMailMessage()
        message.setFrom(sendFrom)
        message.setTo(to)
        message.setSubject(sub)
        message.setText(text)
        mailSender.send(message)
    }

    override fun sendMailMessage(to: String, sub: String, arguments: Map<String, Any?>, template: String) {
        val ctx = Context()
        ctx.setVariables(arguments)
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, true, "UTF-8") // true = multipart

        message.setSubject(sub)
        message.setFrom(sendFrom)
        message.setTo(to)
        message.setText(templateEngine.process(template, ctx), true) // true = isHtml
        mailSender.send(mimeMessage)
    }

    override fun sendMailMessage(to: String, sub: String, arguments: Map<String, Any?>, template: MailTemplate) {
        sendMailMessage(to, sub, arguments, template.path)
    }
}