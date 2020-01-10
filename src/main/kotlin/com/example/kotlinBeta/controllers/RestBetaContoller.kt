package com.example.kotlinBeta.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration

@RestController
class RestBetaContoller() {

    @GetMapping("/single")
    fun getSingleMeow(): String = "meow"

    @GetMapping("/stream")
    fun getStreamMeow(): Flux<String> = Flux.interval(Duration.ofSeconds(1)).map { "meow" }

}
