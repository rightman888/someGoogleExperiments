package com.example.kotlinBeta.controllers

import com.example.kotlinBeta.services.GoogleApiService
import com.example.kotlinBeta.services.MailService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration

@RestController
class RestBetaController(private val googleService: GoogleApiService, private val mailService: MailService) {

    @GetMapping("/single")
    fun getSingleMeow(): String = "meow"

    @GetMapping("/stream")
    fun getStreamMeow(): Flux<String> = Flux.interval(Duration.ofSeconds(1)).map { "meow" }

    @GetMapping("/doc")
    fun getDocTitle(): String = this.googleService.getDoc()

    @GetMapping("/filelist")
    fun getFileList(): String = this.googleService.getFiles()

    @GetMapping("/sharedoc")
    fun sharedocument(): String = this.googleService.shareDoc()

    @GetMapping("/emailer")
    fun emailer(): String = this.mailService.sendSuccessRegistrationMessage()

}
