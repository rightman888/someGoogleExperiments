package com.example.kotlinBeta.controllers

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.time.Duration

@Controller
class RSockerBetaController() {

    @MessageMapping("meower")
    fun getStreamMeow(): Flux<String> = Flux.interval(Duration.ofSeconds(1)).map { "meow" }

}
