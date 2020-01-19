package com.example.kotlinBeta.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailServiceImpl(private val emailSender: JavaMailSender): MailService {

    @Value("\${spring.mail.username}")
    lateinit var sourceEmailAddress: String

    override fun sendSuccessRegistrationMessage(): String {

        val message = this.emailSender.createMimeMessage()
        val mimeMessageHelper = MimeMessageHelper(message, true)
        mimeMessageHelper.setTo("")
        mimeMessageHelper.setSubject("meow")
        mimeMessageHelper.setText("testMeow")
        mimeMessageHelper.setFrom(this.sourceEmailAddress)
        mimeMessageHelper.addAttachment("murr.txt") {"mrrrrrrrrrrrrrrr".byteInputStream()}
        this.emailSender.send(message)
        return ""
    }

}
